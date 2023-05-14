package ttbsge;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import ttbsge.tiles.Tile;

public class TownCenter extends Building {

  public static final String[] TOWN_NAMES = {""};

  ArrayList<Building> buildings = new ArrayList<Building>();

  String name;

  public TownCenter(Faction faction, Tile tile, String name) {
    super(faction, tile, null);
    this.town = this;
    faction.towns.add(this);
    this.name = name;
    if (faction.capital == null) {
      faction.capital = this;
    }
  }

  @Override
  public String screen_type() {
    return "Town of " + this.faction.name;
  }

  @Override
  public String screen_name() {
    return name;
  }

  @Override
  public void draw(PGraphics layer) {
    layer.push();
    layer.fill(255);
    layer.stroke(0);
    layer.rectMode(PApplet.CENTER);
    layer.rect(this.x, this.y, TTBSGE.TILE_SIZE/2, TTBSGE.TILE_SIZE/2);
    layer.pop();
    this.draw_flag(layer);
  }

  @Override
  public void draw_flag(PGraphics layer) {
    layer.push();
    layer.stroke(0);
    layer.line(this.x, this.y, this.x, this.y - TTBSGE.TILE_SIZE);
    layer.fill(this.faction.r, this.faction.g, this.faction.b);
    layer.noStroke();
    layer.rect(this.x, this.y - TTBSGE.TILE_SIZE, 2*TTBSGE.TILE_SIZE/3, TTBSGE.TILE_SIZE/2);
    layer.pop();
  }

  @Override
  public void draw_highlight(PGraphics layer) {
    // TODO Auto-generated method stub

  }

}
