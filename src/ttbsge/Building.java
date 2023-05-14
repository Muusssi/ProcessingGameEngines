package ttbsge;

import processing.core.PApplet;
import processing.core.PGraphics;
import ttbsge.tiles.Tile;

public class Building extends CampaignObject {

  TownCenter town;

  public Building(Faction faction, Tile tile, TownCenter town) {
    super(faction, tile);
    TTBSGE.map_dirty = true;
    faction.buildings.add(this);
    set_on_tile(tile);
    this.town = town;
    if (this.town != null) {
      town.buildings.add(this);
    }
  }

  @Override
  public void turn_ended() {}

  @Override
  public void set_on_tile(Tile tile) {
    if (this.tile != null) {
      this.tile.building = null;
    }
    tile.building = this;
    super.set_on_tile(tile);
  }

  @Override
  public void draw(PGraphics layer) {
    layer.push();
    layer.fill(0);
    layer.stroke(0);
    layer.rectMode(PApplet.CENTER);
    layer.rect(this.x, this.y, TTBSGE.TILE_SIZE/3, TTBSGE.TILE_SIZE/3);
    layer.pop();
  }

  @Override
  public void draw_highlight(PGraphics layer) {
    // TODO Auto-generated method stub

  }

  @Override
  public void act(CampaignObject other_obejct) {
    // TODO Auto-generated method stub

  }

  @Override
  public String screen_type() {
    return "Building of " + this.faction.name;
  }

  @Override
  public String screen_name() {
    return "";
  }

}
