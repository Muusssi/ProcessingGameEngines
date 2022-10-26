package tmge;

import processing.core.PGraphics;

public class Maze {

  public int width = 0;
  public int height = 0;

  public MazeCell[][] cells;

  protected boolean maze_layer_dirty = true;
  protected PGraphics maze_layer;

  public Maze(int width, int height) {
    if (TMGE.active_maze == null) {
      TMGE.active_maze = this;
    }
    this.width = width;
    this.height = height;
    reset_cells();
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
    //this.main_path.draw();
    TMGE.papplet().popMatrix();
    TMGE.papplet().popStyle();
  }



}
