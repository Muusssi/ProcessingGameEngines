
boolean editing_walls = false;
int slide_speed = 10;

void setup() {
  size(1000, 800, P2D);
  TMGE.init(this);
  new RandomMaze(50, 50, 0.8);
}

void draw(){
  background(200);
  if (!editing_walls) {
    TMGE.follow_player();
  }
  TMGE.draw();

  if (TMGE.is_key_pressed(UP)) {
    TMGE.y_offset -= slide_speed;
  }
  if (TMGE.is_key_pressed(DOWN)) {
    TMGE.y_offset += slide_speed;
  }
  if (TMGE.is_key_pressed(LEFT)) {
    TMGE.x_offset -= slide_speed;
  }
  if (TMGE.is_key_pressed(RIGHT)) {
    TMGE.x_offset += slide_speed;
  }
}

void keyPressed() {
  TMGE.register_key_press();
  // Regenerate the maze
  if (keyCode == ' ') {
    TMGE.active_maze.reset_cells();
  }
  if (keyCode == 'E') {
    // Toggle if player is followed
    editing_walls = !editing_walls;
    TMGE.active_maze.highlight_cell(null);
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

  if (!editing_walls) {
    if (keyCode == 'W') {
      TMGE.player_all_up();
    }
    if (keyCode == 'A') {
      TMGE.player_all_left();
    }
    if (keyCode == 'S') {
      TMGE.player_all_down();
    }
    if (keyCode == 'D') {
      TMGE.player_all_right();
    }
  }
  else if (TMGE.active_maze.highlighted_cell != null) {
    if (keyCode == 'W') {
      TMGE.active_maze.highlighted_cell.toggle_up_wall();
    }
    if (keyCode == 'A') {
      TMGE.active_maze.highlighted_cell.toggle_left_wall();
    }
    if (keyCode == 'S') {
      TMGE.active_maze.highlighted_cell.toggle_down_wall();
    }
    if (keyCode == 'D') {
      TMGE.active_maze.highlighted_cell.toggle_right_wall();
    }
  }
}

void keyReleased() {
  TMGE.register_key_release();
}

void mousePressed() {
  TMGE.active_maze.highlight_cell(TMGE.active_maze.pointed_cell());
}
