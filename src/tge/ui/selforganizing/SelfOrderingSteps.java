package tge.ui.selforganizing;

public class SelfOrderingSteps extends SelfOrganisingCollection {

  public int list_offset = 0;
  public int step_offset = 0;

  public SelfOrderingSteps () {}

  @Override
  public void reorganize() {
    list_offset = 0;
    step_offset = 0;
    for (SelfOrganisingItem item : items) {
      item.set_target(this.x + step_offset, this.y + list_offset);
      list_offset += SelfOrganisingItem.BOX_HEIGHT + SelfOrganisingItem.MARGIN;
      step_offset += SelfOrganisingItem.BOX_HEIGHT;
    }
  }

}
