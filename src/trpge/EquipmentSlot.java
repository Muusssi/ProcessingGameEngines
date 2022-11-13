package trpge;

import processing.data.JSONObject;

public class EquipmentSlot extends TRPGEObject {

  public Item item;
  public GameCharacter character;
  public String name;
  public String item_category;
  public String item_type;

  public EquipmentSlot(GameCharacter character, String name, String item_category, String item_type) {
    this.character = character;
    this.name = name;
    this.item_category = item_category;
    this.item_type = item_type;
  }

  public void unequip() {
    if (this.item != null) {
      this.character.inventory.add(this.item);
      this.item = null;
    }
  }

  @Override
  public JSONObject save_object() {
    JSONObject save_object = super.save_object();
    save_object.put("name", this.name);
    save_object.put("item_category", this.name);
    save_object.put("item_type", this.name);
    save_object.put("item", this.item.save_object());
    return save_object;
  }





}
