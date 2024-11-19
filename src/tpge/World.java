package tpge;

import java.util.ArrayList;

import processing.core.PImage;

public class World {

  public ArrayList<TPGEObject> objects = new ArrayList<TPGEObject>();

  public float gravity = 0.4f;

  public float camera_x = 0;
  public float camera_y = 0;

  public float width = 2000;
  public float height = 2000;

  public PImage background_image;


  public World() {
    TPGE.worlds.add(this);
    if (TPGE.active_world == null) {
      TPGE.active_world = this;
    }
  }

  public TPGEObject add_platform(float x, float y, float width) {
    TPGEObject rect = new TPGEObject(this);
    rect.add_corner(width, 0);
    rect.add_corner(width, 10);
    rect.add_corner(0, 10);
    rect.x = x;
    rect.y = y;
    return rect;
  }

  public TPGEObject add_platform(float x, float y, float width, float thickness) {
    TPGEObject rect = new TPGEObject(this);
    rect.add_corner(width, 0);
    rect.add_corner(width, thickness);
    rect.add_corner(0, thickness);
    rect.x = x;
    rect.y = y;
    return rect;
  }

  public void draw() {
    TPGE.papplet().push();
    TPGE.papplet().translate(TPGE.papplet().width/2, -TPGE.papplet().height/2);
    TPGE.papplet().translate(-camera_x, camera_y);
    TPGE.papplet().translate(0, TPGE.papplet().height);

    if (this.background_image == null) {
      TPGE.papplet().rect(0, -this.height, this.width, this.height);
    }
    else {
      TPGE.papplet().image(this.background_image, 0, -this.height, this.width, this.height);
    }

    for (TPGEObject object : objects) {
      object.draw();
    }
    TPGE.papplet().pop();
  }

  public float pointed_x() {
    return TPGE.papplet().mouseX + camera_x - TPGE.papplet().width/2;
  }

  public float pointed_y() {
    return -TPGE.papplet().mouseY + camera_y - TPGE.papplet().height/2 + TPGE.papplet().height;
  }

}
