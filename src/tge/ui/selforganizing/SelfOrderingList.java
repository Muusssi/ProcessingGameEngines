package tge.ui.selforganizing;

public class SelfOrderingList extends SelfOrganisingCollection {

  public int list_offset = 0;

  public SelfOrderingList () {}

  @Override
  public void reorganize() {
    if (this.title_item != null) {
      this.title_item.set_target(this.x, this.y - SelfOrganisingItem.BOX_HEIGHT - SelfOrganisingItem.MARGIN);
    }
    list_offset = 0;
    for (SelfOrganisingItem item : items) {
      item.set_target(this.x, this.y + list_offset);
      list_offset += SelfOrganisingItem.BOX_HEIGHT + SelfOrganisingItem.MARGIN;
    }
  }

}
