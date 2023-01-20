package trpge;

import processing.core.PConstants;
import processing.core.PImage;
import processing.data.JSONObject;

public class WorldObject extends TRPGEObject {

  public Room location;
  public int width = 50;
  public int height = 50;
  public int x, y;

  public String name;
  public String type;
  public String category;

  public String image_file;

  public WorldObject(String name, String type, String category, String image) {
    this.name = name;
    this.type = type;
    this.category = category;
    this.image_file = image;
  }

  public PImage image() {
    return TRPGE.load_image(this.image_file);
  }

  @Override
  public JSONObject save_object() {
    JSONObject save_object = super.save_object();
    save_object.put("type", "object");
    save_object.put("width", this.width);
    save_object.put("height", this.height);
    save_object.put("x", this.x);
    save_object.put("y", this.y);

    save_object.put("name", this.name);
    save_object.put("type", this.type);
    save_object.put("category", this.category);
    save_object.put("image_file", this.image_file);
    return save_object;
  }


  public void draw() {
    TRPGE.papplet().pushStyle();
    if (TRPGE.active_player != null && this.touches(TRPGE.active_player)) {
      TRPGE.papplet().fill(0);
      if (this.name != null) {
        TRPGE.papplet().textAlign(PConstants.CENTER);
        TRPGE.papplet().text(this.name, this.x, this.y - this.height);
      }
    }
    TRPGE.papplet().rectMode(PConstants.CENTER);
    TRPGE.papplet().imageMode(PConstants.CENTER);
    if (this.image() != null) {
      TRPGE.papplet().image(this.image(), x, y, width, height);
    }
    else {
      TRPGE.papplet().rect(x, y, width, height);
    }
    TRPGE.papplet().popStyle();
  }

  public void default_interaction(GameCharacter character) {
    TRPGE.message("It is a " + this.name);
  }

  public boolean touches(GameCharacter character) {
    if (character.x + character.radius < this.x - this.width/2) {
      return false;
    }
    if (character.x - character.radius > this.x + this.width/2) {
      return false;
    }
    if (character.y + character.radius < this.y - this.height/2) {
      return false;
    }
    if (character.y - character.radius > this.y + this.height/2) {
      return false;
    }
    return true;
  }

}
