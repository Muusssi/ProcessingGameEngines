package trpge;

import java.util.ArrayList;

import processing.core.PImage;
import tge.TGE;
import tge.ui.Button;
import tge.ui.TGEUI;

public class InventoryView extends TGEUI {

  GameCharacter character;

  ArrayList<Button> all_buttons = new ArrayList<Button>();
  ArrayList<DropButton> drop_buttons = new ArrayList<DropButton>();
  ArrayList<InventoryButton> inventory_buttons = new ArrayList<InventoryButton>();
  ArrayList<EquipmentSlotButton> equipment_slot_buttons = new ArrayList<EquipmentSlotButton>();
  ArrayList<InventoryImage> images = new ArrayList<InventoryImage>();

  public InventoryView(GameCharacter character) {
    this.character = character;
    for (Item item : character.inventory) {
      all_buttons.add(new DropButton(item));
      all_buttons.add(new InventoryButton(item));
    }

    for (EquipmentSlot slot : character.equipment_slots) {
      all_buttons.add(new EquipmentSlotButton(slot));
    }
  }

  void reset_inventory_view() {
    TRPGE.view = new InventoryView(this.character);
  }

  @Override
  public void draw() {
    TGE.draw_buttons(all_buttons);
    for (InventoryImage image : images) {
      TRPGE.papplet().image(image.image, image.x, image.y, Button.default_button_height, Button.default_button_height);
    }
  }

  class DropButton extends Button {
    Item item;

    public DropButton(Item item) {
      super("Drop", 50, 120 + Button.default_button_height*drop_buttons.size());
      this.item = item;
      drop_buttons.add(this);
      this.rounding = 0;
    }

    @Override
    public void action() {
      character.drop(this.item);
      reset_inventory_view();
    }
  }

  class InventoryButton extends Button {
    Item item;

    public InventoryButton(Item item) {
      super(item.name, 100 + Button.default_button_height, 120 + Button.default_button_height*inventory_buttons.size());
      this.item = item;
      if (this.item.image != null) {
        new InventoryImage(this.item.image, 100, 120 + Button.default_button_height*inventory_buttons.size());
      }
      inventory_buttons.add(this);
    }

    @Override
    public void action() {
      character.equip(this.item);
      reset_inventory_view();
    }
  }

  class InventoryImage {
    PImage image;
    int x, y;

    public InventoryImage(PImage image, int x, int y) {
      this.image = image;
      this.x = x;
      this.y = y;
      images.add(this);
    }
  }

  class EquipmentSlotButton extends Button {
    EquipmentSlot slot;

    public EquipmentSlotButton(EquipmentSlot slot) {
      super("<" + slot.item_category + "/" + slot.item_type + ">", TGE.papplet().width/2, 120 + Button.default_button_height*equipment_slot_buttons.size());
      this.slot = slot;
      if (this.slot.item != null) {
        this.text = this.slot.item.name;
        if (this.slot.item.image != null) {
          new InventoryImage(this.slot.item.image, TGE.papplet().width/2 - Button.default_button_height, 120 + Button.default_button_height*equipment_slot_buttons.size());
        }
      }
      equipment_slot_buttons.add(this);
    }

    @Override
    public void action() {
      if (this.slot.item != null) {
        this.slot.unequip();
        reset_inventory_view();
      }
    }
  }

  @Override
  public void child_button_pressed() {}

}
