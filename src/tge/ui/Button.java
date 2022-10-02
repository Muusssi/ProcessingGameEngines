package tge.ui;

import processing.core.PConstants;
import tge.TGE;

public abstract class Button {

  public int x, y;
  public int width;
  public int height = 30;
  public String text;

  public int background_r = 255;
  public int background_g = 255;
  public int background_b = 255;

  public int active_background_r = 230;
  public int active_background_g = 230;
  public int active_background_b = 230;

  public int text_r = 0;
  public int text_g = 0;
  public int text_b = 0;

  public boolean stay_down = false;
  public boolean pressed = false;
  public int pressed_on_frame = -1;
  public int drawn_on_frame = -1;
  public TGEUI parent;


  public Button(String text, int x, int y) {
      this.text = text;
      this.x = x;
      this.y = y;
      if (text.length() < 4) {
          this.width = 40;
      }
      else {
          this.width = text.length()*10;
      }
      TGE.buttons.add(this);
  }

  public String button_text() {
    return this.text;
  }

  public boolean cursor_points() {
      if (TGE.papplet().frameCount == drawn_on_frame &&
          TGE.papplet().mouseX >= x && TGE.papplet().mouseX <= x + width &&
          TGE.papplet().mouseY >= y && TGE.papplet().mouseY <= y + height) {
          return true;
      }
      else {
          return false;
      }
  }

  public void draw() {
    drawn_on_frame = TGE.papplet().frameCount;
    TGE.papplet().pushStyle();
    TGE.papplet().textSize(12);
    if (pressed || cursor_points()){
        if (pressed || TGE.papplet().mousePressed) {
          TGE.papplet().fill(text_r, text_g, text_b);
        }
        else {
          TGE.papplet().fill(active_background_r, active_background_g, active_background_b);
        }
    }
    else {
      TGE.papplet().fill(background_r, background_g, background_b);
    }
    TGE.papplet().stroke(text_r, text_g, text_b);
    TGE.papplet().rect(x, y, width, height, 10);

    if (pressed || (cursor_points() && TGE.papplet().mousePressed)) {
      TGE.papplet().stroke(active_background_r, active_background_g, active_background_b);
      TGE.papplet().fill(active_background_r, active_background_g, active_background_b);
    }
    else {
      TGE.papplet().stroke(text_r, text_g, text_b);
      TGE.papplet().fill(text_r, text_g, text_b);
    }

    TGE.papplet().textAlign(PConstants.CENTER, PConstants.CENTER);
    TGE.papplet().text(button_text(), x + width/2, y + height/2);
    TGE.papplet().popStyle();
  }

  public void background_color(int r, int g, int b) {
      this.background_r = r;
      this.background_g = g;
      this.background_b = b;
  }

  public void background_color(int c) {
    background_color(c, c, c);
  }

  public void active_background_color(int r, int g, int b) {
      this.active_background_r = r;
      this.active_background_g = g;
      this.active_background_b = b;
  }

  public void text_color(int r, int g, int b) {
      this.text_r = r;
      this.text_g = g;
      this.text_b = b;
  }

  public void text_color(int c) {
    text_color(c, c, c);
  }

  public void press() {
    if (pressed_on_frame != TGE.papplet().frameCount) {
      if (stay_down) {
        if (pressed) {
          pressed = false;
          release_action();
        }
        else {
          pressed = true;
          if (parent != null) {
            parent.child_button_pressed();
          }
          action();
        }
      }
      else {
        if (parent != null) {
          parent.child_button_pressed();
        }
        action();
      }
    }
    pressed_on_frame = TGE.papplet().frameCount;
  }

  /**
   * This is the method that should be implemented
   */
  public abstract void action();

  public void release_action() {}

}



