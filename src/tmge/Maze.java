package tmge;

import processing.core.PGraphics;
import processing.data.JSONArray;
import processing.data.JSONObject;
import tge.SerializableObject;

public class Maze extends SerializableObject {

  public int width = 0;
  public int height = 0;

  public MazeCell[][] cells;
  public MazeCell highlighted_cell;

  protected boolean maze_layer_dirty = true;
  public PGraphics maze_layer;

  public Maze(int width, int height) {
    if (TMGE.active_maze == null) {
      TMGE.active_maze = this;
    }
    TMGE.mazes.add(this);
    this.width = width;
    this.height = height;
    reset_cells();
  }

  public Maze(JSONObject json) {
    if (json.getBoolean("active")) {TMGE.active_maze = this;}
    TMGE.mazes.add(this);
    this.width = json.getInt("width");
    this.height = json.getInt("height");
    cells = new MazeCell[this.width][this.height];

    JSONArray rows = json.getJSONArray("cells");
    for (int i = 0; i < rows.size(); i++) {
      JSONArray row = rows.getJSONArray(i);
      for (int j = 0; j < this.height; j++) {
        MazeCell cell = (MazeCell)SerializableObject.load_instance(row.getJSONObject(j));
        cell.maze = this;
        cell.x = i;
        cell.y = j;
        cells[i][j] = cell;
      }
    }
    maze_layer_dirty = true;
    update_maze_layer();
  }

  @Override
  public JSONObject save_object() {
    JSONObject json = super.save_object();
    json.put("width", this.width);
    json.put("height", this.height);
    json.put("active", this == TMGE.active_maze);
    JSONArray cells = new JSONArray();
    for (int i = 0; i < this.width; i++) {
      JSONArray row = new JSONArray();
      for (int j = 0; j < this.width; j++) {
        row.append(this.cells[i][j].save_object());
      }
      cells.append(row);
    }
    json.put("cells", cells);
    return json;
  }

  public void cell_init(MazeCell cell) {}

  public void reset_cells() {
    cells = new MazeCell[this.width][this.height];
    for (int i = 0; i < this.width; i++) {
      for (int j = 0; j < this.height; j++) {
        MazeCell cell = new MazeCell(this, i, j);
        cells[i][j] = cell;
        cell_init(cell);
      }
    }
    maze_layer_dirty = true;
  }

  public void update_maze_layer() {
    if (maze_layer == null) {
      maze_layer = TMGE.papplet().createGraphics(this.width*TMGE.maze_scale,
                                                 this.height*TMGE.maze_scale);
    }
    if (maze_layer_dirty) {
      maze_layer.beginDraw();
      maze_layer.background(100);
      maze_layer.line(0, 0, this.width*TMGE.x_offset, 0);
      maze_layer.line(0, 0, 0, this.height*TMGE.x_offset);
      for (int i = 0; i < this.width; i++) {
        for (int j = 0; j < this.height; j++) {
          cells[i][j].draw(maze_layer);
        }
      }
      maze_layer.endDraw();
    }
    maze_layer_dirty = false;
  }

  public void draw() {
    update_maze_layer();
    TMGE.papplet().pushStyle();
    TMGE.papplet().pushMatrix();
    TMGE.papplet().translate(TMGE.papplet().width/2 - TMGE.x_offset,
                             TMGE.papplet().height/2 - TMGE.y_offset);
    TMGE.papplet().image(maze_layer, 0, 0);
    TMGE.papplet().ellipse((TMGE.player_x + 0.5f)*TMGE.maze_scale, (TMGE.player_y + 0.5f)*TMGE.maze_scale, TMGE.maze_scale*0.8f, TMGE.maze_scale*0.8f);
    TMGE.papplet().popMatrix();
    TMGE.papplet().popStyle();
  }

  public void highlight_cell(MazeCell cell) {
    if (cell != highlighted_cell) {
      highlighted_cell = cell;
      maze_layer_dirty = true;
    }
  }

  public int pointed_x() {
    return TMGE.papplet().mouseX - TMGE.papplet().width/2 + TMGE.x_offset;
  }

  public int pointed_y() {
    return TMGE.papplet().mouseY - TMGE.papplet().height/2 + TMGE.y_offset;
  }

  public MazeCell pointed_cell() {
    int x_index = pointed_x()/TMGE.maze_scale;
    int y_index = pointed_y()/TMGE.maze_scale;
    if (x_index >= 0 && x_index < this.width && y_index >= 0 && y_index < this.height) {
      return cells[x_index][y_index];
    }
    return null;
  }



}
