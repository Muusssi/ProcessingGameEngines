package tge.ui.selforganizing;

import processing.core.PApplet;
import processing.core.PConstants;
import tge.TGE;

public class SelfOrganisingItem implements Comparable<SelfOrganisingItem> {

  public static int BOX_WIDTH = 100;
  public static int BOX_HEIGHT = 30;
  public static int POINT_BOX_WIDTH = 60;
  public static int CORNERS = 10;
  public static int MARGIN = 5;

  public String name;
  public int value = 0;
  public int value_change = 0;

  public SelfOrganisingCollection collection;

  public float x = 100;
  public float y = 100;

  public boolean moving = false;

  public int target_x = 0;
  public int target_y = 0;
  public float speed_x = 0;
  public float speed_y = 0;

  public SelfOrganisingItem (SelfOrganisingCollection collection, String name) {
    this.name = name;
    this.collection = collection;
    this.x = this.collection.x;
    this.y = this.collection.y;
  }

  public boolean is_moving() {
    if (this.moving) {
      this.moving = PApplet.abs(this.x - this.target_x) >= 1 || PApplet.abs(this.y - this.target_y) >= 1;
    }
    return this.moving;
  }

  public void animate() {
    if (this.is_moving()) {
      if (PApplet.abs(this.x - this.target_x) >= 1) this.x += this.speed_x;
      if (PApplet.abs(this.y - this.target_y) >= 1) this.y += this.speed_y;
    }
  }

  public void set_target(int x, int y) {
    this.target_x = x;
    this.target_y = y;
    if (PApplet.abs(this.target_x - this.x) >= 1) {
      this.speed_x = (this.target_x - this.x)/this.collection.movement_steps;
    }
    else {
      this.speed_x = 0;
    }

    if (PApplet.abs(this.target_y - this.y) >= 1) {
      this.speed_y = (this.target_y - this.y)/this.collection.movement_steps;
    }
    else {
      this.speed_y = 0;
    }

    this.moving = true;
  }

  public void draw() {
    this.animate();
    TGE.papplet().push();
    TGE.papplet().rectMode(PConstants.CENTER);
    TGE.papplet().textSize(BOX_HEIGHT/2);
    TGE.papplet().textAlign(PConstants.CENTER, PConstants.CENTER);

    TGE.papplet().rect(this.x, this.y, BOX_WIDTH, BOX_HEIGHT, CORNERS);
    if (this.collection.show_values) {
      TGE.papplet().rect(this.x + BOX_WIDTH/2 + POINT_BOX_WIDTH/2 + 3, this.y, POINT_BOX_WIDTH, BOX_HEIGHT, CORNERS);
      if (this.collection.show_value_changes && this.value_change != 0) {
        TGE.papplet().rect(this.x + BOX_WIDTH/2 + 1.25f*POINT_BOX_WIDTH + 6, this.y, POINT_BOX_WIDTH/2, BOX_HEIGHT, CORNERS);
      }
    }
    TGE.papplet().fill(0);
    TGE.papplet().text(this.name, this.x, this.y);
    if (this.collection.show_values) {
      TGE.papplet().text(this.value, this.x + BOX_WIDTH/2 + POINT_BOX_WIDTH/2 + 3, this.y);
      if (this.collection.show_value_changes && this.value_change != 0) {
        String sign = "";
        if (this.value_change > 0) sign = "+";
        TGE.papplet().text(sign + this.value_change, this.x + BOX_WIDTH/2 + 1.25f*POINT_BOX_WIDTH + 6, this.y);
      }
    }
    TGE.papplet().pop();
  }

  public void update_value() {
    this.value += this.value_change;
    this.value_change = 0;
  }

  @Override
  public int compareTo(SelfOrganisingItem other) {
    if (other instanceof SelfOrganisingItem) {
      SelfOrganisingItem other_list_item = other;
      return other_list_item.value - this.value;
    }
    return 0;
  }

}
