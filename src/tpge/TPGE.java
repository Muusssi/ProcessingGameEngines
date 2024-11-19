package tpge;

import java.util.ArrayList;

import processing.core.PApplet;
import tge.TGE;

public class TPGE extends TGE {

  public static ArrayList<World> worlds = new ArrayList<World>();
  public static World active_world;

  public static float camera_smoothing_factor = 0.8f;

  public static void init(PApplet papplet) {
    TGE.init(papplet);
  }

  public static void draw() {
    if (active_world != null) {
      active_world.draw();
    }
  }

  public static float pointed_x( ) {
    if (active_world != null) {
      return active_world.pointed_x();
    }
    return 0;
  }

  public static float pointed_y( ) {
    if (active_world != null) {
      return active_world.pointed_y();
    }
    return 0;
  }

}
