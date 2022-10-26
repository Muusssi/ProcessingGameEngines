package tmge;

public class FullMaze extends Maze {

  public FullMaze(int width, int height) {
    super(width, height);
    this.reset_cells();
  }

  @Override
  public void cell_init(MazeCell cell) {
    cell.wall_right = true;
    cell.wall_down = true;
  }
}
