package tmge;

import java.util.ArrayList;
import java.util.HashMap;

import processing.data.JSONObject;
import tge.SerializableObject;

public class MazeCharacter extends SerializableObject {

  public Maze maze;
  public int x = 0;
  public int y = 0;

  public MazeCell target_cell;
  protected ArrayList<Integer> path;

  public int slowness = 5;
  protected int last_acted = 0;

  public int r, b, g;

  // For debugging:
  public boolean draw_path = false;

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

  public void set_target(MazeCell target) {
    this.target_cell = target;
    this.path = this.path_to(this.target_cell);
  }

  public ArrayList<Integer> path_to(MazeCell target) {
    if (target == null || this.current_cell() == target) return null;
    HashMap<MazeCell,ArrayList<Integer>> paths = new HashMap<MazeCell,ArrayList<Integer>>();
    ArrayList<MazeCell> stack = new ArrayList<MazeCell>();
    paths.put(this.current_cell(), new ArrayList<Integer>());
    stack.add(this.current_cell());
    while (!stack.isEmpty()) {
      MazeCell current = stack.remove(0);
      if (current == target) {
        return paths.get(current);
      }

      for (int direction = 1; direction <= 4; direction++) {
        MazeCell neightbor = current.neighbor_in_direction(direction);
        if (neightbor != null && !current.wall_in_direction(direction)) {
          if (!paths.containsKey(neightbor)) {
            ArrayList<Integer> path = new ArrayList<Integer>(paths.get(current));
            path.add(direction);
            paths.put(neightbor, path);
            stack.add(neightbor);
          }
        }
      }
    }
    return null;
  }

  protected void draw() {
    TMGE.papplet().pushStyle();
    TMGE.papplet().fill(this.r, this.g, this.b);
    TMGE.papplet().ellipse((this.x + 0.5f)*TMGE.maze_scale, (this.y + 0.5f)*TMGE.maze_scale, TMGE.maze_scale*0.8f, TMGE.maze_scale*0.8f);
    TMGE.papplet().popStyle();
    this.draw_path();
  }

  protected void draw_path() {
    if (!this.draw_path || this.path == null) return;
    TMGE.papplet().pushStyle();
    TMGE.papplet().stroke(0, 0, 200);
    MazeCell previous_cell = this.current_cell();
    for (int direction : this.path) {
      MazeCell next_cell = previous_cell.neighbor_in_direction(direction);
      TMGE.papplet().line((previous_cell.x + 0.5f)*TMGE.maze_scale,
                          (previous_cell.y + 0.5f)*TMGE.maze_scale,
                          (next_cell.x + 0.5f)*TMGE.maze_scale,
                          (next_cell.y + 0.5f)*TMGE.maze_scale);
      previous_cell = next_cell;
    }
    TMGE.papplet().popStyle();
  }

  public void act() {
    // This is to slow the movement of the character
    if (TMGE.papplet().frameCount - slowness < last_acted) return;

    if (this.path == null || this.path.isEmpty() || this.maze.maze_layer_dirty) {
      this.path = this.path_to(this.target_cell);
    }
    if (this.path != null && !this.path.isEmpty()) {
      this.move_in_direction(this.path.remove(0));
    }
    last_acted = TMGE.papplet().frameCount;
  }

  public MazeCell current_cell() {
    if (maze != null) {
      return maze.cells[this.x][this.y];
    }
    return null;
  }

  public boolean move_in_direction(int direction) {
    if (direction == TMGE.UP) return move_up();
    if (direction == TMGE.DOWN) return move_down();
    if (direction == TMGE.LEFT) return move_left();
    if (direction == TMGE.RIGHT) return move_right();
    return false;
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
