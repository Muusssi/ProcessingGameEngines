package trpge;

import java.util.ArrayList;

import processing.data.JSONObject;

public class GameCharacter extends TRPGEObject {

  public String name;
  public int x, y;
  public int speed = 5;
  public int radius = 25;

  public Room location;

  public ArrayList<Item> inventory = new ArrayList<Item>();
  public ArrayList<EquipmentSlot> equipment_slots = new ArrayList<EquipmentSlot>();

  public GameCharacter(String name) {
    this.name = name;
    initial_equipment_slots();
  }

  protected void initial_equipment_slots() {
    this.add_equipment_slot("Weapon", "Weapon", null);
    this.add_equipment_slot("Armour", "Armour", null);
  }

  public EquipmentSlot add_equipment_slot(String name, String item_category, String item_type) {
    EquipmentSlot slot = new EquipmentSlot(this, name, item_category, item_type);
    this.equipment_slots.add(slot);
    return slot;
  }

  public EquipmentSlot free_equiment_slot(String item_category, String item_type) {
    return equiment_slot(item_category, item_type, true);
  }

  public EquipmentSlot any_equiment_slot(String item_category, String item_type) {
    return equiment_slot(item_category, item_type, false);
  }

  public EquipmentSlot equiment_slot(String item_category, String item_type, boolean free) {
    for (EquipmentSlot slot : equipment_slots) {
      if (item_category != null && slot.item_category != null && !item_category.equals(slot.item_category)) {
        continue;
      }
      if (item_type != null && slot.item_type != null && !item_type.equals(slot.item_type)) {
        continue;
      }
      if (free && slot.item != null) {
        continue;
      }
      return slot;
    }
    return null;
  }

  public boolean equip(Item item) {
    if (this.inventory.contains(item)) {
      EquipmentSlot slot = this.free_equiment_slot(item.category, item.type);
      if (slot == null) {
        slot = any_equiment_slot(item.category, item.type);
      }
      if (slot != null) {
        slot.unequip();
        slot.item = item;
        inventory.remove(item);
        return true;
      }
    }
    return false;
  }

  public float total_effect(String effect_name) {
    float effect = 0;
    for (EquipmentSlot slot : equipment_slots) {
      if (slot.item != null) {
        effect += slot.item.effects.getOrDefault(effect_name, 0f);
      }
    }
    return effect;
  }

  @Override
  public JSONObject save_object() {
    JSONObject save_object = super.save_object();
    save_object.put("name", this.name);
    save_object.put("x", this.x);
    save_object.put("y", this.y);
    save_object.put("radius", this.radius);
    save_object.put("speed", this.speed);
    save_object.put("active_player", TRPGE.active_player == this);
    return save_object;
  }

  public void move_up() {
    y -= speed;
    if (y < radius) {
      y = radius;
    }
  }

  public void move_down() {
    y += speed;
    if (y > location.height - radius) {
      y = location.height - radius;
    }

  }

  public void move_left() {
    x -= speed;
    if (x < radius) {
      x = radius;
    }
  }

  public void move_right() {
    x += speed;
    if (x > location.width - radius) {
      x = location.width - radius;
    }
  }

  public void player_draw() {
    TRPGE.papplet().pushMatrix();
    TRPGE.papplet().translate(-TRPGE.camera_offset_x, -TRPGE.camera_offset_y);
    if (location != null) {
      location.draw();
    }
    TRPGE.papplet().popMatrix();
  }

  public void draw() {
    TRPGE.papplet().pushStyle();
    TRPGE.papplet().ellipse(x, y, 2*radius, 2*radius);
    TRPGE.papplet().popStyle();
  }

  public WorldObject interactable_object() {
    if (this.location == null) return null;
    for (WorldObject object : this.location.objects) {
      if (object.touches(this)) {
        return object;
      }
    }
    return null;
  }

  public void interact() {
    WorldObject object = interactable_object();
    if (object != null) {
      object.default_interaction(this);
    }
  }

  public Item pickItem() {
    WorldObject object = interactable_object();
    if (object != null) {
      if (object instanceof Item) {
        location.remove(object);
        inventory.add((Item)object);
        TRPGE.message("Picked up " + object.name);
      }
      else {
        TRPGE.message("That can not be picked up");
      }
    }
    return null;
  }

  public void drop(Item item) {
    if (inventory.contains(item)) {
      inventory.remove(item);
      this.location.put(item, this.x, this.y);
    }
  }

}
