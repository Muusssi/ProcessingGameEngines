
Maze maze;

public void setup() {
  size(1000, 800);
  TMGE.init(this);
  maze = new ClassicMaze(30, 40);
}

public void draw(){
  background(200);
  TMGE.follow_player();
  maze.draw();
}

public void keyPressed() {
  if (keyCode == ' ') {
    TMGE.active_maze.reset_cells();
  }

  if (keyCode == 'W') {
    TMGE.player_up();
  }
  if (keyCode == 'A') {
    TMGE.player_left();
  }
  if (keyCode == 'S') {
    TMGE.player_down();
  }
  if (keyCode == 'D') {
    TMGE.player_right();
  }
}
