package ttbsge;

import processing.core.PGraphics;
import ttbsge.tiles.Tile;

public abstract class CampaignObject {

  public Tile tile;
  public float x, y;
  Faction faction;

  public CampaignObject(Faction faction, Tile tile) {
    this.faction = faction;
    if (tile != null) {
      this.set_on_tile(tile);
    }
  }

  public void set_on_tile(Tile tile) {
    this.tile = tile;
    this.x = tile.x;
    this.y = tile.y;
  }

  public void select() {
    TTBSGE.active_object = this;
  }

  public boolean is_hostile(CampaignObject other_object) {
    if (other_object.faction != null && this.faction != null && other_object.faction != this.faction) {
      return true;
    }
    return false;
  }

  public abstract void act(CampaignObject other_obejct);

  public abstract void draw(PGraphics layer);

  public abstract void draw_highlight(PGraphics layer);

  public abstract void turn_ended();

  public void focus() {
    TTBSGE.focus(this.x, this.y);
  }

  public void focus_smoothly() {
    TTBSGE.focus_smoothly(this.x, this.y);
  }

  public abstract String screen_type();
  public abstract String screen_name();

  public void draw_info_card() {
    PGraphics screen = TTBSGE.pgraphics();
    screen.push();
    screen.fill(255);
    screen.rect(screen.width - 200, 0, 200, 400);

    if (this.faction != null) {
      screen.fill(this.faction.r, this.faction.g, this.faction.b);
      screen.rect(screen.width - 200, 0, 200, 20);
    }

    screen.fill(0);
    screen.text(this.screen_type(), screen.width - 200 + 20, 40);
    screen.text(this.screen_name(), screen.width - 200 + 20, 60);
    screen.pop();
  }

  public void draw_flag(PGraphics layer) {
    layer.push();
    layer.stroke(0);
    layer.line(this.x, this.y, this.x, this.y - TTBSGE.TILE_SIZE);
    layer.fill(this.faction.r, this.faction.g, this.faction.b);
    layer.noStroke();
    layer.triangle(this.x, this.y - TTBSGE.TILE_SIZE,
                   this.x + TTBSGE.TILE_SIZE/2, this.y - TTBSGE.TILE_SIZE + TTBSGE.TILE_SIZE/3,
                   this.x, this.y - TTBSGE.TILE_SIZE/2);
    layer.pop();
  }

}
