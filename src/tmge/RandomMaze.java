package tmge;

import processing.data.JSONObject;

public class RandomMaze extends Maze {

  public float wall_factor;

  public RandomMaze(int width, int height, float wall_factor) {
    super(width, height);
    this.wall_factor = wall_factor;
    this.reset_cells();
  }

  public RandomMaze(JSONObject json) {
    super(json);
    this.wall_factor = json.getFloat("wall_factor");
  }

  @Override
  public JSONObject save_object() {
    JSONObject json = super.save_object();
    json.put("wall_factor", wall_factor);
    return json;
  }

  @Override
  protected boolean maze_ok() {
    for (MazeCharacter character : this.characters) {
      if (character.path_to(TMGE.current_cell()) == null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void cell_init(MazeCell cell) {
    if (TMGE.papplet().random(1) < wall_factor) {
      cell.wall_right = false;
    }
    else {
      cell.wall_right = true;
    }
    if (TMGE.papplet().random(1) < wall_factor) {
      cell.wall_down = false;
    }
    else {
      cell.wall_down = true;
    }
  }

}
