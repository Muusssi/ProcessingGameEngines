
import ttge.*;

// Example of two tanks fighting
Tank tank, tank2;

public void setup() {
  size(1000, 800, P2D);
  // World needs to be initialized
  TTGE.init(this, 2000);

  // Set various options about the world
  // These are the default values
  TTGE.camera_speed = 5;
  TTGE.sliding_ground = true; // Wheather the ground slides or not
  TTGE.sliding_tanks = true; // Wheather the tanks slide with the ground or not
  TTGE.show_health_bars = true; // Wheather the health bars of tanks are shown or not
  TTGE.destructiveness = 1; // Value between 0-1 stating how much ground is destroyed by explosions
  // RGB for the ground color
  TTGE.ground_r = 165;
  TTGE.ground_g = 136;
  TTGE.ground_b = 77;
  // RGB for the ground color
  TTGE.sky_r = 162;
  TTGE.sky_g = 210;
  TTGE.sky_b = 247;

  // Create tanks and set them to target eachother
  tank = new Tank();
  tank2 = new Tank();
  tank.target_tank = tank2; // Comment out this line to disable first tank AI
  tank.shot_cooldown = 100;
  tank2.target_tank = tank;
  tank2.x = 900;
  tank2.shot_cooldown = 100;

  // You can override the basic tank AI (ai_act()) when extending Tank class
}

public void draw(){
  background(200);
  // Draw the world
  TTGE.draw_world();

  // Camera controls
  if (TTGE.is_key_pressed('A')) {
    TTGE.move_camera_left();
  }
  if (TTGE.is_key_pressed('D')) {
    TTGE.move_camera_right();
  }

  // Controls for the first tank
  if (TTGE.is_key_pressed(PConstants.LEFT)) {
    tank.move_left();
  }
  if (TTGE.is_key_pressed(PConstants.RIGHT)) {
    tank.move_right();
  }

  if (TTGE.is_key_pressed(PConstants.UP)) {
    tank.lift_barrel();
  }
  if (TTGE.is_key_pressed(PConstants.DOWN)) {
    tank.lower_barrel();
  }

  if (TTGE.is_key_pressed('N')) {
    tank.increase_power();
  }
  if (TTGE.is_key_pressed('M')) {
    tank.decrease_power();
  }
}

public void keyPressed() {
  TTGE.register_key_press();
  if (keyCode == ' ') {
    tank.shoot();
  }
}

public void keyReleased() {
  TTGE.register_key_release();
}

public void mousePressed() {
  // Clicking in the world creates big bombs
  new Projectile(
      TTGE.mouse_x(),
      TTGE.mouse_y(),
      100, 0, 0);
}
