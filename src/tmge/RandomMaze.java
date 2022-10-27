package tmge;

public class RandomMaze extends Maze {

  public float wall_factor;

  public RandomMaze(int width, int height, float wall_factor) {
    super(width, height);
    this.wall_factor = wall_factor;
    this.reset_cells();
  }

  @Override
  public void cell_init(MazeCell cell) {
    if (TMGE.papplet().random(1) < wall_factor) {
      cell.wall_right = false;
    }
    if (TMGE.papplet().random(1) < wall_factor) {
      cell.wall_down = false;
    }
  }

}
