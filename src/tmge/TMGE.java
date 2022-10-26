package tmge;

import processing.core.PApplet;
import tge.TGE;

public class TMGE extends TGE {
  /**
   * Tommi's Maze Game Engine
   */

  public static int maze_scale = 40;

  public static int x_offset = 0;
  public static int y_offset = 0;
  public static float camera_smoothing_factor = 0.1f;


  public static int player_x = 0;
  public static int player_y = 0;

  public static Maze active_maze;

  public static void log_version() {
    System.out.println("TMGE version: " + VERSION);
  }

  public static void init(PApplet papplet) {
    TGE.init(papplet);
  }

  public static void follow_player() {
    x_offset += ((TMGE.player_x + 0.5f)*maze_scale - x_offset)*camera_smoothing_factor;
    y_offset += ((TMGE.player_y + 0.5f)*maze_scale - y_offset)*camera_smoothing_factor;
  }

  public static MazeCell current_cell() {
    if (active_maze != null) {
      return active_maze.cells[player_x][player_y];
    }
    return null;
  }

  public static boolean player_up() {
    if (active_maze == null) return false;
    MazeCell neighbor = current_cell().neighbor_up();
    if (neighbor != null && !neighbor.wall_down) {
      player_y--;
      return true;
    }
    return false;
  }

  public static boolean player_down() {
    if (active_maze == null) return false;
    if (current_cell().neighbor_down() != null && !current_cell().wall_down) {
      player_y++;
      return true;
    }
    return false;
  }

  public static boolean player_left() {
    if (active_maze == null) return false;
    MazeCell neighbor = current_cell().neighbor_left();
    if (neighbor != null && !neighbor.wall_right) {
      player_x--;
      return true;
    }
    return false;
  }

  public static boolean player_right() {
    if (active_maze == null) return false;
    if (current_cell().neighbor_right() != null && !current_cell().wall_right) {
      player_x++;
      return true;
    }
    return false;
  }


  public static void player_all_up() {
    while (player_up());
  }

  public static void player_all_down() {
    while (player_down());
  }

  public static void player_all_left() {
    while (player_left());
  }

  public static void player_all_right() {
    while (player_right());
  }

}
