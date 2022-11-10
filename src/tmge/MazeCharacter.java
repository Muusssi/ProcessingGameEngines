package tmge;

import processing.data.JSONObject;
import tge.SerializableObject;

public class MazeCharacter extends SerializableObject {


  public Maze maze;
  public int x = 0;
  public int y = 0;

  public int slowness = 5;
  protected int last_acted = 0;

  public int r, b, g;

  public MazeCharacter(Maze maze) {
    this.maze = maze;
    this.maze.characters.add(this);
  }

  public MazeCharacter(JSONObject json) {
   this.x = json.getInt("x");
   this.y = json.getInt("y");
   this.r = json.getInt("r");
   this.g = json.getInt("g");
   this.b = json.getInt("b");
   this.slowness = json.getInt("slowness");
  }

  @Override
  public JSONObject save_object() {
    JSONObject json = super.save_object();
    json.put("x", this.x);
    json.put("y", this.y);
    json.put("r", this.r);
    json.put("g", this.g);
    json.put("b", this.b);
    json.put("slowness", this.slowness);
    return json;
  }

  public void act() {
    if (TMGE.papplet().frameCount - slowness < last_acted) return;
    float rand = TMGE.papplet().random(4);
    if (rand < 1) move_up();
    else if (rand < 2) move_left();
    else if (rand < 3) move_down();
    else if (rand < 4) move_right();
    last_acted = TMGE.papplet().frameCount;
  }

  public MazeCell current_cell() {
    if (maze != null) {
      return maze.cells[this.x][this.y];
    }
    return null;
  }

  public boolean move_up() {
    if (this.maze == null) return false;
    if (!current_cell().wall_up()) {
      y--;
      return true;
    }
    return false;
  }

  public boolean move_down() {
    if (maze == null) return false;
    if (!current_cell().wall_down()) {
      y++;
      return true;
    }
    return false;
  }

  public boolean move_left() {
    if (maze == null) return false;
    if (!current_cell().wall_left()) {
      x--;
      return true;
    }
    return false;
  }

  public boolean move_right() {
    if (maze == null) return false;
    if (!current_cell().wall_right()) {
      x++;
      return true;
    }
    return false;
  }

}
