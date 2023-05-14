package ttbsge.buttons;

import ttbsge.TTBSGE;

public class EndTurnButton extends tge.ui.Button {

  public EndTurnButton() {
    super("End turn XXXX", 0, 0);
    this.x = TTBSGE.papplet().width - this.width;
    this.y = TTBSGE.papplet().height - this.height;
  }

  @Override
  public String button_text() {
    return "End turn "+ TTBSGE.round;
  }

  @Override
  public void action() {
    TTBSGE.end_turn();
  }

}
