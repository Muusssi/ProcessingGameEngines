package ttbsge.tiles;

import java.util.ArrayList;
import java.util.HashSet;

import processing.core.PGraphics;
import ttbsge.Building;
import ttbsge.CampaignObject;
import ttbsge.CampaignUnit;
import ttbsge.Faction;
import ttbsge.TTBSGE;

public abstract class Tile extends CampaignObject {

  public int row, col;
  public boolean odd_row = false;

  int r = 250;
  int g = 250;
  int b = 250;

  float movement_cost = 1;

  public CampaignUnit unit;
  public Building building;

  public HashSet<Faction> discovered_by = new HashSet<Faction>();
  public HashSet<Faction> visible_by = new HashSet<Faction>();

  public Tile(int col, int row) {
    super(null, null);
    this.col = col;
    this.row = row;
    if (this.row % 2 == 1) odd_row = true;
    int off_set = (odd_row ? TTBSGE.TILE_SIZE/2 : 0);
    this.x = TTBSGE.TILE_SIZE/2 + TTBSGE.TILE_SIZE*this.col + off_set;
    this.y = TTBSGE.TILE_SIZE/2 + TTBSGE.TILE_SIZE*0.866f*this.row;
  }

  @Override
  public void turn_ended() {}

  @Override
  public void act(CampaignObject other_obejct) {}

  public boolean navigable() {
    return false;
  }

  public float movement_cost() {
    return movement_cost;
  }

  public ArrayList<Tile> neighbours() {
    ArrayList<Tile> neighbours = new ArrayList<Tile>();
    int[][] even_directions = {{-1, -1},{0, -1},{-1, 0},{+1, 0},{-1, 1},{0, 1}};
    int[][] odd_directions = {{0, -1},{1, -1},{-1, 0},{+1, 0},{0, 1},{1, 1}};
    int[][] directions;
    if (this.odd_row) directions = odd_directions;
    else directions = even_directions;
    for (int[] dir : directions) {
      Tile neighbour = TTBSGE.get_tile_at(col + dir[0], row + dir[1]);
      if (neighbour != null) {
        neighbours.add(neighbour);
      }
    }
    return neighbours;
  }

  public ArrayList<Tile> neighbours(int radius) {
    HashSet<Tile> neighbours = new HashSet<Tile>();
    ArrayList<Tile> new_neighbours = new ArrayList<Tile>();
    new_neighbours.add(this);
    for (int round=0; round < radius; radius++) {
      ArrayList<Tile> new_new_neighbours = new ArrayList<Tile>();
      for (Tile tile : new_neighbours) {
        for (Tile new_neighbour : tile.neighbours()) {
          if (!neighbours.contains(new_neighbour)) {
            neighbours.add(new_neighbour);
            new_new_neighbours.add(new_neighbour);
          }
        }
        new_neighbours = new_new_neighbours;
      }
    }
    return new ArrayList<Tile>(neighbours);
  }

  @Override
  public void draw(PGraphics layer) {
    TTBSGE.papplet().pushStyle();
    layer.noStroke();
    layer.fill(this.r, this.g, this.b);
    layer.ellipse(x, y, TTBSGE.TILE_SIZE, TTBSGE.TILE_SIZE);
    TTBSGE.papplet().popStyle();
  }

  @Override
  public void draw_highlight(PGraphics layer) {
    TTBSGE.papplet().pushStyle();
    layer.stroke(0);
    layer.noFill();
    layer.ellipse(x, y, TTBSGE.TILE_SIZE*1.1f, TTBSGE.TILE_SIZE*1.1f);
    TTBSGE.papplet().popStyle();
  }
}
