package tge.ui.selforganizing;

import java.util.ArrayList;
import java.util.Collections;

import tge.TGE;

public abstract class SelfOrganisingCollection {

  public ArrayList<SelfOrganisingItem> items = new ArrayList<SelfOrganisingItem>();

  public int movement_steps = 100;
  public int x = 100;
  public int y = 100;

  public boolean show_values = true;
  public boolean show_value_changes = true;
  public SelfOrganisingItem title_item;

  public SelfOrganisingItem set_title(String title) {
    title_item = new SelfOrganisingItem(this, title);
    title_item.hide_value = true;
    title_item.hide_value_change = true;
    return title_item;
  }

  public void draw() {
    if (title_item != null) {
      title_item.animate();
      title_item.draw();
    }
    for (int i = items.size() - 1; i >= 0; i--) {
      SelfOrganisingItem item = items.get(i);
      item.animate();
      item.draw();
    }
  }

  public void shuffle() {
    Collections.shuffle(this.items);
    this.reorganize();
  }

  public void reverse() {
    Collections.reverse(this.items);
    this.reorganize();
  }

  public void rotate(int distance) {
    Collections.rotate(this.items, distance);
    this.reorganize();
  }

  public void rotate() {
    this.rotate(1);
  }

  public void sort() {
    Collections.sort(this.items);
    this.reorganize();
  }

  public SelfOrganisingItem add_item(String name) {
    SelfOrganisingItem item = new SelfOrganisingItem(this, name);
    this.items.add(item);
    this.reorganize();
    return item;
  }

  public void add_item(SelfOrganisingItem item) {
    if (item == null) return;
    item.collection = this;
    this.items.add(item);
    this.reorganize();
  }

  public SelfOrganisingItem pop_first() {
    if (this.items.size() > 0) {
      return this.items.remove(0);
    }
    return null;
  }

  public SelfOrganisingItem pop_last() {
    if (this.items.size() > 0) {
      return this.items.remove(this.items.size() - 1);
    }
    return null;
  }

  public void empty_to(SelfOrganisingCollection new_collection) {
    while (this.items.size() > 0) {
      new_collection.add_item(this.pop_first());
    }
  }

  public void randomize_values(int scale) {
    for (SelfOrganisingItem item : items) {
      item.value = (int)(TGE.papplet().random(0, scale));
    }
  }

  public void update_values() {
    for (SelfOrganisingItem item : items) {
      item.update_value();
    }
  }

  public abstract void reorganize();

}
