package tge.ui;

import java.util.ArrayList;

import processing.core.PConstants;
import tge.TGE;

public class PlayerDialog extends TGEUI {

  public static final int MARGIN = 100;

  public String title, message;
  public ArrayList<Button> options = new ArrayList<Button>();

  public int background_r = 255;
  public int background_g = 255;
  public int background_b = 255;
  public int text_r = 0;
  public int text_g = 0;
  public int text_b = 0;

  public boolean done = false;

  public PlayerDialog(String title, String message) {
    this.title = title;
    this.message = message;
  }

  public void add_option(Button option) {
    option.parent = this;
    this.options.add(option);
    option.x = TGE.papplet().width/2 - option.width/2;
    option.y = 3*MARGIN;
    if (!options.isEmpty()) {
      option.y = options.get(options.size() - 1).y + 50;
    }
  }

  public void draw() {
    if (options.isEmpty()) {
      this.add_option(new DefaultDialogButton());
    }
    TGE.papplet().pushStyle();
    TGE.papplet().textAlign(PConstants.CENTER);
    TGE.papplet().fill(background_r, background_g, background_b);
    TGE.papplet().rect(MARGIN, MARGIN,
                       TGE.papplet().width - 2*MARGIN,
                       TGE.papplet().height - 2*MARGIN);
    TGE.papplet().stroke(text_r, text_g, text_b);
    TGE.papplet().fill(text_r, text_g, text_b);
    if (title != null) {
      TGE.papplet().text(title, TGE.papplet().width/2, 2*MARGIN);
    }
    TGE.papplet().text(message, TGE.papplet().width/2, 3*MARGIN);
    TGE.draw_buttons(options);
    TGE.papplet().popStyle();
  }

  @Override
  public void child_button_pressed() {
    done = true;
  }

}
