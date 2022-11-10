package tge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import tge.ui.Button;

public class TGE {

  public static final String VERSION = "0.6.0";

  public static void log_version() {
    System.out.println("TGE version: " + VERSION);
  }

  private static PApplet papplet;

  public static void init(PApplet papplet) {
    TGE.papplet = papplet;
    log_version();
  }

  public static PApplet papplet() {
    if (TGE.papplet == null) {
      System.err.println("ERROR: The game engine was not initialized.");
      System.err.println("Execute: 'TGE.init(this);' in the begining setup().");
      System.exit(1);
    }
    return TGE.papplet;
  }

  public static PGraphics pgraphics() {
    return TGE.papplet().g;
  }

  // =============================================
  // Image loaders
  // =============================================
  protected static HashMap<String, PImage> image_cache = new HashMap<String, PImage>();

  public static PImage load_image(String image_path) {
    if (image_path == null) return null;
    if (!image_cache.containsKey(image_path)) {
      PImage image = papplet().loadImage(image_path);
      image_cache.put(image_path, image);
    }
    return image_cache.get(image_path);
  }



  // =============================================
  // UI Helpers
  // =============================================
  public static boolean quit_on_esc = true;
  public static ArrayList<Button> buttons = new ArrayList<Button>();
  public static HashMap<Integer, Boolean> pressed_keys = new HashMap<Integer, Boolean>();
  public static int previous_mouse_press = -100;
  public static int clicked_x = -1;
  public static int clicked_y = -1;
  public static int pclicked_x = -1;
  public static int pclicked_y = -1;

  // Key follower
  public static boolean is_key_pressed(int key) {
    if (pressed_keys.containsKey(key) ) {
      return pressed_keys.get(key);
    }
    return false;
  }

  public static void register_key_press() {
    pressed_keys.put(papplet().keyCode, true);
    pressed_keys.put((int)TGE.papplet().key, true);
    if (TGE.papplet().key == PConstants.ESC && !quit_on_esc) {
      TGE.papplet().key = 0;
    }
  }

  public static void register_key_release() {
    pressed_keys.put(papplet().keyCode, false);
    pressed_keys.put((int)TGE.papplet().key, false);
  }

  public static Button register_mouse_press() {
    if (papplet().frameCount > previous_mouse_press) {
      pclicked_x = clicked_x;
      pclicked_y = clicked_y;
      clicked_x = papplet().mouseX;
      clicked_y = papplet().mouseY;
    }
    previous_mouse_press = papplet().frameCount;
    // Check buttons
    Iterator<Button> itr = buttons.iterator();
    while (itr.hasNext()) {
      Button btn = itr.next();
      if (btn.cursor_points()) {
        btn.press();
        return btn;
      }
    }
    return null;
  }

  public static void draw_buttons() {
    Iterator<Button> itr = buttons.iterator();
    while (itr.hasNext()) {
      itr.next().draw();
    }
  }

  public static void draw_buttons(List<Button> buttons) {
    Iterator<Button> itr = buttons.iterator();
    while (itr.hasNext()) {
      itr.next().draw();
    }
  }

}
