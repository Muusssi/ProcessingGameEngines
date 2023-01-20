package trpge;

import java.util.HashMap;

import processing.data.JSONObject;

public class Item extends WorldObject {

  public HashMap<String,Float> effects = new HashMap<String,Float>();

  public Item(String name, String type, String category, String image) {
    super(name, type, category, image);
    this.width = 30;
    this.height = 30;
  }

  @Override
  public JSONObject save_object() {
    JSONObject save_object = super.save_object();
    return save_object;
  }


}
