package tmge;

import processing.data.JSONObject;

public class FullMaze extends Maze {

  public FullMaze(int width, int height) {
    super(width, height);
    this.reset_cells();
  }

  public FullMaze(JSONObject json) {
    super(json);
  }

  @Override
  public void cell_init(MazeCell cell) {
    cell.wall_right = true;
    cell.wall_down = true;
  }
}
