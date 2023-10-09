
Car racing_car;
boolean horizontal = true;

void setup() {
  size(1000, 800, P2D);
  TTDRGE.init(this);
  TTDRGE.set_track_image(loadImage("your_race_track.png"), 2000);
  racing_car = new Car();
  racing_car.acceleration_speed = 0.3;
}


void draw() {
  rect(0, 0, 100, 100);
  TTDRGE.draw();
  if (horizontal) {
    racing_car.follow_with_camera_horizontal();
  }
  else {
    racing_car.follow_with_camera();
  }
  racing_car.control('W','S','A','D');
}

void keyPressed() {
    TTDRGE.register_key_press();
    if (key == ' ') horizontal = !horizontal;
}

public void keyReleased() {
    TTDRGE.register_key_release();
}
