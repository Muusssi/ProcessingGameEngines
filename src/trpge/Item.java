package trpge;

import processing.data.JSONObject;

public class Item extends WorldObject {



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
