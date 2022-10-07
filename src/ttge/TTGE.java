package ttge;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import tge.TGE;

public class TTGE extends TGE {
  /**
   * Tommi's Tank Game Engine
   */

  public static float gravity = -0.3f;
  public static float[] ground;
  public static PGraphics ground_layer;
  public static int x_offset = 0;

  protected static int unstability_start = 0;
  protected static int unstability_end = 0;

  public static int camera_speed = 5;
  public static float maximum_steepness = 2;
  public static float initial_smoothness = 150.0f;
  public static boolean sliding_ground = true;
  public static boolean sliding_tanks = true;
  public static boolean ground_unstable = true;
  public static boolean show_health_bars = true;
  public static float destructiveness = 1;

  public static int ground_r = 165;
  public static int ground_g = 136;
  public static int ground_b = 77;

  public static int sky_r = 162;
  public static int sky_g = 210;
  public static int sky_b = 247;

  public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
  public static ArrayList<Tank> tanks = new ArrayList<Tank>();

  public static void init(PApplet papplet, int width) {
    TGE.init(papplet);
    init_ground(width);
  }

  public static void log_version() {
    System.out.println("TTGE version: " + VERSION);
  }

  public static void init_ground(int width) {
    ground = new float[width];
    for (int x = 0; x < width; x++) {
      ground[x] = (int) (papplet().noise(x/initial_smoothness)*papplet().height/2 + papplet().height/3);
    }
    ground_unstable = true;
    while (ground_unstable) {
      slide();
    }
    unstability_end = width;
    update_ground_layer();
  }

  public static void slide() {
    ground_unstable = false;
    if (!sliding_ground) {
      return;
    }
    for (int x = 1; x < ground.length - 1; x++) {
      float diff = ground[x - 1] - ground[x];
      if (diff > maximum_steepness || -diff > maximum_steepness) {
        ground[x - 1] -= 0.5*diff;
        ground[x] += 0.5*diff;
        ground_unstable = true;
        if (unstability_start > x - 1) {
          unstability_start = x - 1;
        }
        if (unstability_end < x) {
          unstability_end = x;
        }
        if (sliding_tanks) {
          slide_tanks(x - 1, diff);
        }
      }
    }
  }

  protected static void slide_tanks(int x, float diff) {
    for (Tank tank : tanks) {
      if (tank.x == x && tank.previus_slide_frame != papplet().frameCount) {
        if (diff > 0) {
          tank.x++;
        }
        else {
          tank.x--;
        }
        tank.previus_slide_frame = papplet().frameCount;
      }
    }
  }

  public static void update_ground_layer() {
    slide();
    if (ground_layer == null) {
      ground_layer = papplet().createGraphics(ground.length, papplet().height);
      ground_unstable = true;
    }
    int start = unstability_start>0 ? unstability_start : 0;
    int end = unstability_end>=ground.length ? ground.length : unstability_end;
    if (ground_unstable) {
      ground_layer.beginDraw();
      ground_layer.rectMode(PConstants.CORNERS);
      ground_layer.stroke(sky_r, sky_g, sky_b);
      ground_layer.fill(sky_r, sky_g, sky_b);
      ground_layer.rect(start, 0, end - 1, ground_layer.height);
      //for (int x = 0; x < ground.length; x++) {
      for (int x = start; x < end; x++) {
        ground_layer.stroke(ground_r, ground_g, ground_b);
        ground_layer.line(x, ground_layer.height, x, ground_layer.height - ground[x]);
      }
      ground_layer.endDraw();
    }
    unstability_start = ground.length;
    unstability_end = 0;
  }

  public static void draw_world() {
    draw_ground_and_sky();
    draw_tanks();
    draw_projectiles();
  }

  public static void draw_ground_and_sky() {
    if (ground_unstable) {
      update_ground_layer();
    }
    papplet().image(ground_layer, x_offset, 0);
  }

  public static boolean draw_projectiles() {
    for (int i = projectiles.size() - 1; i >= 0; --i) {
      Projectile p = projectiles.get(i);
      p.draw();
      if (p.dead) {
        projectiles.remove(i);
        if (p.shooting_tank != null) {
          p.shooting_tank.projectile_hit(p);
          if (p.shooting_tank.previous_projectile == p) {
            p.shooting_tank.previous_projectile = null;
          }
        }
      }
    }
    return false;
  }

  public static void draw_tanks() {
    for (int i = tanks.size() - 1; i >= 0 ; i--) {
      Tank tank = tanks.get(i);
      tank.ai_act();
      tank.draw();
      if (tank.health <= 0) {
        tanks.remove(i);
        tank.on_destruction();
      }
    }
  }

  public static void move_camera_left() {
    x_offset += camera_speed;
    if (x_offset > 0) {
      x_offset = 0;
    }
  }

  public static void move_camera_right() {
    x_offset -= camera_speed;
    if (papplet().width - x_offset > ground.length) {
      x_offset = papplet().width - ground.length ;
    }
  }

  public static float mouse_x() {
    return papplet().mouseX - x_offset;
  }

  public static float mouse_y() {
    return papplet().height - papplet().mouseY;
  }


}
