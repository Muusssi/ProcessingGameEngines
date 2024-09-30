package tge.ui.selforganizing;

public class SelfOrderingList extends SelfOrganisingCollection {

  public int list_offset = 0;

  public SelfOrderingList () {}

  @Override
  public void reorganize() {
    list_offset = 0;
    for (SelfOrganisingItem item : items) {
      item.set_target(this.x, this.y + list_offset);
      list_offset += SelfOrganisingItem.BOX_HEIGHT + SelfOrganisingItem.MARGIN;
    }
  }

}
