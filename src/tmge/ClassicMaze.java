package tmge;

import java.util.ArrayList;
import java.util.Collections;

public class ClassicMaze extends FullMaze {

  public ClassicMaze(int width, int height) {
    super(width, height);
    this.reset_cells();
  }

  @Override
  public void reset_cells() {
    super.reset_cells();
    generate_paths();
  }

  public void generate_paths() {
    ArrayList<MazeCell> stack = new ArrayList<MazeCell>();
    stack.add(cells[0][0]);
    while (!stack.isEmpty()) {
      Collections.shuffle(stack);
      MazeCell cell = stack.remove(0);
      cell.generator_visited = true;

      MazeCell neighbor = cell.neighbor_up();
      if (neighbor != null && !neighbor.generator_visited) {
        stack.add(neighbor);
        neighbor.path_previous = cell;
      }
      neighbor = cell.neighbor_down();
      if (neighbor != null && !neighbor.generator_visited) {
        stack.add(neighbor);
        neighbor.path_previous = cell;
      }
      neighbor = cell.neighbor_left();
      if (neighbor != null && !neighbor.generator_visited) {
        stack.add(neighbor);
        neighbor.path_previous = cell;
      }
      neighbor = cell.neighbor_right();
      if (neighbor != null && !neighbor.generator_visited) {
        stack.add(neighbor);
        neighbor.path_previous = cell;
      }

      if (cell.path_previous != null) {
        if (cell.path_previous == cell.neighbor_up()) {
          cell.neighbor_up().wall_down = false;
        }
        if (cell.path_previous == cell.neighbor_down()) {
          cell.wall_down = false;
        }
        if (cell.path_previous == cell.neighbor_left()) {
          cell.neighbor_left().wall_right = false;
        }
        if (cell.path_previous == cell.neighbor_right()) {
          cell.wall_right = false;
        }
      }
    }
  }

}
