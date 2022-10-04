
import tge.TGE;
// Buttons are imported from TGEs ui package
import tge.ui.*;

void setup() {
  size(800, 600);
  // TGE needs to be initialized
  TGE.init(this);
  // Adds a simple button that calls draw_circle() function when clicked
  new SimpleButton("Click to draw a circle", 200, 200, "draw_circle");
}

void draw() {
  // Draw all the buttons
  TGE.draw_buttons();
}

void draw_circle() {
  // Draws a cirle to a random position
  ellipse(random(0, width), random(0, height), 30, 30);
}

void mousePressed() {
  // TGE needs to know when mouse is pressed to know when button is pressed
  TGE.register_mouse_press();
}
