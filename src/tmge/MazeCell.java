package tmge;

import processing.core.PGraphics;
import processing.data.JSONObject;
import tge.SerializableObject;

public class MazeCell extends SerializableObject {

  public boolean wall_right = false;
  public boolean wall_down = false;

  public boolean generator_visited = false;
  public MazeCell path_previous = null;

  public Maze maze;
  public int x, y;

  public MazeCell(Maze maze, int x, int y) {
    this.maze = maze;
    this.x = x;
    this.y = y;
  }

  public MazeCell(JSONObject json) {
    this.wall_right = json.getBoolean("wall_right");
    this.wall_down = json.getBoolean("wall_down");
  }

  @Override
  public JSONObject save_object() {
    JSONObject json = super.save_object();
    json.put("wall_right", this.wall_right);
    json.put("wall_down", this.wall_down);
    return json;
  }

  public void draw(PGraphics layer) {
    layer.pushStyle();
    if (this == maze.highlighted_cell) {
      layer.fill(200);
      layer.noStroke();
      layer.rect(x*TMGE.maze_scale + 1, y*TMGE.maze_scale + 1, TMGE.maze_scale, TMGE.maze_scale);
    }
    layer.popStyle();
    layer.stroke(0);
    if (wall_right) {
      layer.pushStyle();
      if (maze.highlighted_cell ==  this || (neighbor_right() != null && maze.highlighted_cell == neighbor_right())) {
        layer.stroke(255, 0, 0);
      }
      layer.line((x + 1)*TMGE.maze_scale, y*TMGE.maze_scale, (x + 1)*TMGE.maze_scale, (y + 1)*TMGE.maze_scale);
      layer.popStyle();
    }
    if (wall_down) {
      layer.pushStyle();
      if (maze.highlighted_cell ==  this || (neighbor_down() != null && maze.highlighted_cell == neighbor_down())) {
        layer.stroke(255, 0, 0);
      }
      layer.line(x*TMGE.maze_scale, (y + 1)*TMGE.maze_scale, (x + 1)*TMGE.maze_scale, (y + 1)*TMGE.maze_scale);
      layer.popStyle();
    }
  }

  public void set_right_wall() {
    this.wall_right = true;
    this.maze.maze_layer_dirty = true;
  }

  public void set_left_wall() {
    if (this.neighbor_left() == null) return;
    this.neighbor_left().wall_right = true;
    this.maze.maze_layer_dirty = true;
  }

  public void set_down_wall() {
    this.wall_down = true;
    this.maze.maze_layer_dirty = true;
  }

  public void set_up_wall() {
    if (this.neighbor_up() == null) return;
    this.neighbor_up().wall_down = true;
    this.maze.maze_layer_dirty = true;
  }

  public void remove_right_wall() {
    this.wall_right = false;
    this.maze.maze_layer_dirty = true;
  }

  public void remove_left_wall() {
    if (this.neighbor_left() == null) return;
    this.neighbor_left().wall_right = false;
    this.maze.maze_layer_dirty = true;
  }

  public void remove_down_wall() {
    this.wall_down = false;
    this.maze.maze_layer_dirty = true;
  }

  public void remove_up_wall() {
    if (this.neighbor_up() == null) return;
    this.neighbor_up().wall_down = false;
    this.maze.maze_layer_dirty = true;
  }


  public void toggle_right_wall() {
    this.wall_right = !this.wall_right;
    this.maze.maze_layer_dirty = true;
  }

  public void toggle_left_wall() {
    if (this.neighbor_left() == null) return;
    this.neighbor_left().wall_right = !this.neighbor_left().wall_right;
    this.maze.maze_layer_dirty = true;
  }

  public void toggle_down_wall() {
    this.wall_down = !this.wall_down;
    this.maze.maze_layer_dirty = true;
  }

  public void toggle_up_wall() {
    if (this.neighbor_up() == null) return;
    this.neighbor_up().wall_down = !this.neighbor_up().wall_down;
    this.maze.maze_layer_dirty = true;
  }

  public boolean wall_up() {
    if (this.neighbor_up() != null && !this.neighbor_up().wall_down) {
      return false;
    }
    return true;
  }

  public boolean wall_down() {
    if (this.y == this.maze.height - 1) {
      return true;
    }
    return this.wall_down;
  }

  public boolean wall_left() {
    if (this.neighbor_left() != null && !this.neighbor_left().wall_right) {
      return false;
    }
    return true;
  }

  public boolean wall_right() {
    if (this.x == this.maze.width - 1) {
      return true;
    }
    return this.wall_right;
  }


  public MazeCell neighbor_up() {
    if (y > 0) {
      return maze.cells[x][y - 1];
    }
    return null;
  }

  public MazeCell neighbor_down() {
    if (y < maze.height - 1) {
      return maze.cells[x][y + 1];
    }
    return null;
  }

  public MazeCell neighbor_left() {
    if (x > 0) {
      return maze.cells[x - 1][y];
    }
    return null;
  }

  public MazeCell neighbor_right() {
    if (x < maze.width - 1) {
      return maze.cells[x + 1][y];
    }
    return null;
  }


}
