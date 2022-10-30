
void setup() {
  size(1000, 800);
  TMGE.init(this);
  new ClassicMaze(50, 50);
}

void draw(){
  background(200);
  TMGE.follow_player();
  TMGE.draw();
}

void keyPressed() {
  TMGE.register_key_press();
  // Regenerate the maze
  if (keyCode == ' ') {
    TMGE.active_maze.reset_cells();
  }

  // Save and load games
  if (TMGE.is_key_pressed(CONTROL)) {
    if (keyCode == 'S') {
      TMGE.save();
    }
    if (keyCode == 'L') {
      TMGE.load();
    }
    return;
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

void keyReleased() {
  TMGE.register_key_release();
}

void mousePressed() {
  // Toggle left and right walls
  if (TMGE.active_maze.pointed_cell() != null) {
    if (mouseButton == PConstants.LEFT) {
      TMGE.active_maze.pointed_cell().toggle_left_wall();
    }
    if (mouseButton == PConstants.RIGHT) {
      TMGE.active_maze.pointed_cell().toggle_right_wall();
    }
  }
}
