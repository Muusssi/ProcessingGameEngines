package tge;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.Table;
import processing.data.TableRow;
import tge.ui.Button;

public class TGE {

  public static final String VERSION = "0.18.0";

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

  public static PImage load_image_with_transparent_background(String image_path) {
    return load_image_with_transparent_background(image_path, 240);
  }

  public static PImage load_image_with_transparent_background(String image_path, float threshold) {
    PImage original_image = papplet().loadImage(image_path);
    PGraphics base = papplet().createGraphics(original_image.width, original_image.height);
    base.beginDraw();
    base.image(original_image, 0, 0);
    base.endDraw();
    base.loadPixels();
    for (int i = 0; i < (base.width*base.height); i++) {
      int pixel = base.pixels[i];
      int r = (pixel >> 16) & 0xFF;
      int g = (pixel >> 8) & 0xFF;
      int b = pixel & 0xFF;
      if (r > threshold && g > threshold && b > threshold) {
        base.pixels[i] = papplet().color(0, 0);
      }
    }
    base.updatePixels();
    return base;
  }

  // =============================================
  // Table loaders
  // =============================================

  public static Table load_table(String filename, String separator) {
    return load_table(filename, false, separator);
  }

  public static Table load_table(String filename, boolean header) {
    return load_table(filename, header, ";");
  }

  public static Table load_table(String filename) {
    return load_table(filename, false, ";");
  }

  public static Table load_table(String filename, boolean header, String separator) {
    Table table = new Table();
    boolean header_read = false;
    BufferedReader reader = papplet().createReader(filename);
    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        String[] pieces = PApplet.split(line, separator);

        if (header && header_read) {
          for (String piece : pieces) {
            table.addColumn(piece);
          }
          header_read = true;
        }
        else {
          TableRow new_row = table.addRow();
          new_row.setString("name", "Lion");
          for (int i = 0; i < pieces.length; i++) {
            new_row.setString(i, pieces[i]);
          }
        }
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return table;
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

  public static String prompt_user_input(String text) {
    return JOptionPane.showInputDialog(text);
  }

  // =============================================
  // Draw special shapes
  // =============================================

  public static void heart(float x, float y, float height) {
    papplet().beginShape();
    papplet().vertex(x, y - height*5/7);
    papplet().bezierVertex(x, y - height*9/7, x + height*8/7, y - height, x, y);
    papplet().endShape();
    papplet().beginShape();
    papplet().vertex(x, y - height*5/7);
    papplet().bezierVertex(x, y - height*9/7, x - height*8/7, y - height, x, y);
    papplet().endShape();
  }

}
