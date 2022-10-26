package tmge;

import processing.core.PGraphics;

public class MazeCell {

  public boolean wall_right = true;
  public boolean wall_down = true;

  public boolean generator_visited = false;
  public MazeCell path_previous = null;

  public Maze maze;
  public int x, y;

  public MazeCell(Maze maze, int x, int y) {
    this.maze = maze;
    this.x = x;
    this.y = y;
  }

  public void draw(PGraphics layer) {
    layer.pushStyle();
    if (wall_right) {
      layer.line((x + 1)*TMGE.maze_scale, y*TMGE.maze_scale, (x + 1)*TMGE.maze_scale, (y + 1)*TMGE.maze_scale);
    }
    if (wall_down) {
      layer.line(x*TMGE.maze_scale, (y + 1)*TMGE.maze_scale, (x + 1)*TMGE.maze_scale, (y + 1)*TMGE.maze_scale);
    }
    layer.popStyle();
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
