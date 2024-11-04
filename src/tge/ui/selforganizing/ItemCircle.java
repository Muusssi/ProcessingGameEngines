package tge.ui.selforganizing;

import processing.core.PApplet;
import processing.core.PConstants;
import tge.TGE;

public class ItemCircle extends SelfOrganisingCollection {

  public float angle_offset = 0;
  public int radius;

  public ItemCircle (int radius) {
    this.radius = radius;
    this.x = TGE.papplet().width/2;
    this.y = TGE.papplet().height/2;
  }

  @Override
  public void reorganize() {
    if (this.title_item != null) {
      this.title_item.set_target(this.x, this.y);
    }
    angle_offset = 0;
    int center_offset = 0;
    if (show_values) center_offset = SelfOrganisingItem.POINT_BOX_WIDTH/2;
    for (SelfOrganisingItem item : items) {
      item.set_target((int)(this.x + PApplet.sin(angle_offset)*radius) - center_offset, (int)(this.y + PApplet.cos(angle_offset)*radius));
      angle_offset += 2*PConstants.PI/items.size();
    }
  }

}
