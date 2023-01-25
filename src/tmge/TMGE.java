package tmge;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import tge.SerializableObject;
import tge.TGE;

public class TMGE extends TGE {
  /**
   * Tommi's Maze Game Engine
   */

  public static final int UP = 1;
  public static final int LEFT = 2;
  public static final int DOWN = 3;
  public static final int RIGHT = 4;

  public static String save_game_location = "saved_games/";
  public static String default_save_name = "tmge_save.json";

  public static int maze_scale = 40;

  public static int x_offset = 0;
  public static int y_offset = 0;
  public static float camera_smoothing_factor = 0.1f;

  public static int player_x = 0;
  public static int player_y = 0;
  public static PImage player_image;

  public static ArrayList<Maze> mazes = new ArrayList<Maze>();
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

  public static void save() {
    save(save_game_location, default_save_name);
  }

  public static void save(String name) {
    save(save_game_location, name);
  }

  public static void save(String location, String name) {
    JSONObject json = save_object();
    papplet().saveJSONObject(json, location + name);
    System.out.println("Saved mazes to: " + location + name);
  }

  public static void load() {
    load(save_game_location, default_save_name);
  }

  public static void load(String name) {
    load(save_game_location, name);
  }

  public static void load(String location, String name) {
    TMGE.mazes.clear();
    JSONObject json = papplet().loadJSONObject(location + name);
    player_x = json.getInt("player_x");
    player_y = json.getInt("player_y");
    JSONArray maze_objects = json.getJSONArray("mazes");
    for (int index = 0; index < maze_objects.size(); index++) {
      SerializableObject.load_instance(maze_objects.getJSONObject(index));
    }
    System.out.println("Loaded mazes from: " + location + name);
  }

  public static JSONObject save_object() {
    JSONObject json = new JSONObject();
    json.put("player_x", player_x);
    json.put("player_y", player_y);
    JSONArray maze_objects = new JSONArray();
    for (Maze maze : mazes) {
      maze_objects.append(maze.save_object());
    }
    json.put("mazes", maze_objects);
    return json;
  }

  public static void draw() {
    if (active_maze != null) {
      active_maze.draw();
    }
  }

  public static void draw_player() {
    TMGE.papplet().pushStyle();
    if (player_image == null) {
      TMGE.papplet().fill(255);
      TMGE.papplet().ellipse((TMGE.player_x + 0.5f)*TMGE.maze_scale, (TMGE.player_y + 0.5f)*TMGE.maze_scale, TMGE.maze_scale*0.8f, TMGE.maze_scale*0.8f);
    }
    else {
      TMGE.papplet().image(player_image, TMGE.player_x*TMGE.maze_scale, TMGE.player_y*TMGE.maze_scale, TMGE.maze_scale, TMGE.maze_scale);
    }
    TMGE.papplet().popStyle();
  }

  public static boolean player_up() {
    if (active_maze == null) return false;
    if (!current_cell().wall_up()) {
      player_y--;
      return true;
    }
    return false;
  }

  public static boolean player_down() {
    if (active_maze == null) return false;
    if (!current_cell().wall_down()) {
      player_y++;
      return true;
    }
    return false;
  }

  public static boolean player_left() {
    if (active_maze == null) return false;
    if (!current_cell().wall_left()) {
      player_x--;
      return true;
    }
    return false;
  }

  public static boolean player_right() {
    if (active_maze == null) return false;
    if (!current_cell().wall_right()) {
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
