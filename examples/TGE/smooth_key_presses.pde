
import tge.TGE;

float x = 200;
float y = 200;

void setup() {
  size(800, 600);
  // TGE needs to be initialized
  TGE.init(this);
}

void draw() {
  background(200);
  ellipse(x, y, 30, 30);

  // TGE can keep account of which keyboard keys are currently pressed.
  // This allows smooth movent with WASD keys like this:
  if (TGE.is_key_pressed('A')) {
    x -= 3;
  }
  else if (TGE.is_key_pressed('D')) {
    x += 3;
  }
  if (TGE.is_key_pressed('W')) {
    y -= 3;
  }
  else if (TGE.is_key_pressed('S')) {
    y += 3;
  }
}


// TGE needs to register key presses and releases
// to allow smooth movement using keyboard
void keyPressed() {
  TGE.register_key_press();
}

void keyReleased() {
  TGE.register_key_release();
}
