package ttbsge;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import ttbsge.tiles.LandTile;
import ttbsge.tiles.Tile;

public class CampaignUnit extends CampaignObject {

  public float base_movement_points = 10;
  public float movement_points = base_movement_points;
  private HashMap<Tile, Route> reachable;
  Route route;
  float movement_speed = 4;

  public CampaignUnit(Faction faction, Tile tile) {
    super(faction, tile);
    this.faction = faction;
    faction.units.add(this);
    TTBSGE.units.add(this);
  }

  @Override
  public void act(CampaignObject other_object) {
    if (other_object instanceof Tile) {
      go_to((Tile) other_object);
    }
    else if (other_object instanceof Building) {
      Building building = (Building) other_object;
      go_to(building.tile);
    }
    else if (other_object instanceof CampaignUnit) {
      CampaignUnit unit = (CampaignUnit) other_object;
      go_to(unit.tile);
    }

  }

  @Override
  public void turn_ended() {
    movement_points = base_movement_points;
    reachable = null;
  }

  @Override
  public void select() {
    super.select();
    this.reachable = null;
  }

  public void go_to(Tile tile) {
    this.route = this.reachable_tiles().get(tile);
    if (this.route != null && TTBSGE.moving_unit == null) {
      TTBSGE.moving_unit = this;
    }
  }

  protected Route next_route() {
    if (this.route == null) {
      return null;
    }
    Route route = this.route;
    while (route.previous_route.tile != this.tile) {
      route = route.previous_route;
    }
    return route;
  }

  protected void move() {
    if (route != null && this.tile == route.tile) {
      route = null;
    }
    else {
      Route next_route = next_route();
      if (next_route != null) {
        float distance = PApplet.dist(x, y, next_route.tile.x, next_route.tile.y);
        if (distance < movement_speed) {
          this.movement_points -= next_route.movement_cost;
          if (next_route.action_target != null) {
            System.out.println("Action!!!!");
            this.set_on_tile(next_route.previous_route.tile);
            this.route = null;
          }
          else {
            this.set_on_tile(next_route.tile);
          }

        }
        else {
          x += movement_speed*(next_route.tile.x - x)/distance;
          y += movement_speed*(next_route.tile.y - y)/distance;
        }
        TTBSGE.object_layer_dirty = true;
      }
    }
  }

  @Override
  public void set_on_tile(Tile tile) {
    if (this.tile != null) {
      this.tile.unit = null;
    }
    tile.unit = this;
    super.set_on_tile(tile);
  }

  public boolean has_neighbouring_enemies(Tile tile) {
    for (Tile neighbour : tile.neighbours()) {
      if (neighbour.unit != null && neighbour.unit.is_hostile(this)) {
        return true;
      }
    }
    return false;
  }

  public boolean can_traverse(Tile tile) {
    if (tile instanceof LandTile) {
      if (tile.building != null) {
        if (!this.is_hostile(tile.building)) {
          return true;
        }
        else {
          return false;
        }
      }
      if (tile.unit == null) {
        return true;
      }

    }
    return false;
  }

  public CampaignObject action_target_in(Tile tile) {
    if (tile instanceof LandTile) {
      if (tile.unit != null && this.is_hostile(tile.unit)) {
        return tile.unit;
      }
      if (tile.building != null && this.is_hostile(tile.building)) {
        return tile.building;
      }
    }
    return null;
  }

  public HashMap<Tile,Route> reachable_tiles() {
    if (reachable == null || reachable.get(this.tile).total_movement_cost != 0) {
      reachable = new HashMap<Tile,Route>();
      reachable.put(this.tile, new Route(this.tile, null, 0, has_neighbouring_enemies(this.tile)));
      ArrayList<Tile> uninvestigated_tiles = new ArrayList<Tile>();
      uninvestigated_tiles.add(this.tile);

      while (!uninvestigated_tiles.isEmpty()) {
        Tile current_tile = uninvestigated_tiles.remove(0);
        Route current_route = reachable.get(current_tile);
        for (Tile neighbour : current_tile.neighbours()) {
          float movement_cost = neighbour.movement_cost()/2 + current_tile.movement_cost()/2;
          if (movement_cost <= this.movement_points - current_route.total_movement_cost) {
            if (!reachable.containsKey(neighbour) || reachable.get(neighbour).total_movement_cost > current_route.total_movement_cost + movement_cost) {
              if (can_traverse(neighbour)) {
                uninvestigated_tiles.add(neighbour);
                reachable.put(neighbour, new Route(neighbour, current_route, movement_cost, has_neighbouring_enemies(neighbour)));
              }
              else {
                CampaignObject target = action_target_in(neighbour);
                if (target != null) {
                  reachable.put(neighbour, new Route(neighbour, current_route, movement_cost, target));
                }
              }
            }

          }
        }
      }
    }
    return reachable;
  }

  @Override
  public void draw(PGraphics layer) {
    layer.pushStyle();
    layer.noStroke();
    layer.fill(0);
    layer.ellipse(x, y, TTBSGE.TILE_SIZE/3, TTBSGE.TILE_SIZE/3);
    layer.popStyle();
    this.draw_flag(layer);
  }

  @Override
  public void draw_highlight(PGraphics layer) {
    TTBSGE.papplet().pushStyle();
    layer.noStroke();
    layer.fill(255, 0, 0);
    layer.triangle(x, y - TTBSGE.TILE_SIZE/3,
                   x - TTBSGE.TILE_SIZE/3, y - 2*TTBSGE.TILE_SIZE/3,
                   x + TTBSGE.TILE_SIZE/3, y - 2*TTBSGE.TILE_SIZE/3);
    TTBSGE.papplet().popStyle();
  }

  public class Route {

    public float movement_cost;
    public float total_movement_cost;
    public Tile tile;
    public Route previous_route;
    public CampaignObject action_target;
    public boolean hostile_area;

    public void basic_initialization(Tile tile, Route previous_route, float cost, boolean hostile_area) {
      this.movement_cost = cost;
      if (previous_route != null) {
        this.total_movement_cost = previous_route.total_movement_cost + cost;
      }
      else {
        this.total_movement_cost = cost;
      }
      this.tile = tile;
      this.previous_route = previous_route;
      this.hostile_area = hostile_area;
    }

    public Route(Tile tile, Route previous_route, float cost, boolean hostile_area) {
      basic_initialization(tile, previous_route, cost, hostile_area);
    }

    public Route(Tile tile, Route previous_route, float cost, CampaignObject action_target) {
      basic_initialization(tile, previous_route, cost, true);
      this.action_target = action_target;
    }

    public void draw(PGraphics layer) {
      layer.push();
      if (this.action_target != null || this.hostile_area) {
        layer.fill(255, 0, 0, 150);
      }
      else {
        layer.fill(255, 255, 0, 100);

      }

      layer.noStroke();
      layer.ellipse(this.tile.x, this.tile.y, TTBSGE.TILE_SIZE, TTBSGE.TILE_SIZE);
      layer.pop();
    }

    public void draw_path(PGraphics layer) {
      Route current_route = this;
      while (current_route.previous_route != null) {
        layer.line(current_route.tile.x, current_route.tile.y,
                   current_route.previous_route.tile.x, current_route.previous_route.tile.y);
        current_route = current_route.previous_route;
      }
    }
  }

  @Override
  public String screen_type() {
    return "An army of " + this.faction.name;
  }

  @Override
  public String screen_name() {
    return "";
  }


}
