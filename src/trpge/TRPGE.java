package trpge;

import java.util.ArrayList;
import java.util.PriorityQueue;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.data.JSONArray;
import processing.data.JSONObject;
import tge.TGE;
import tge.ui.TGEUI;

public class TRPGE extends TGE {

  public static final String VERSION = "0.0.1";

  public static final int MINUTE = 60;
  public static final int HOUR = 3600;
  public static final int DAY = 86400;
  public static final String[] DAYS = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

  public static TGEUI view;

  public static int time = 0;
  public static int seconds_per_frame = 1;
  public static PriorityQueue<Event> events = new PriorityQueue<Event>();
  public static ArrayList<Rule> rules = new ArrayList<Rule>();

  public static int clock_x = 50;
  public static int clock_y = 50;
  public static int clock_r = 25;
  public static boolean stop_time_uis = true;

  public static ArrayList<Room> rooms = new ArrayList<Room>();

  public static GameCharacter active_player;

  public static float camera_offset_x = 0;
  public static float camera_offset_y = 0;
  public static float camera_smoothing_factor = 0.1f;


  public static void init(PApplet papplet) {
    TGE.init(papplet);
  }

  public static void message(String message) {
    System.out.println(message);
  }

  public static void draw() {
    if (view != null) {
      view.draw();
      if (stop_time_uis) return;
    }
    else {
      draw_active_room();
    }
    move_clock(seconds_per_frame);
  }

  public static void toggle_inventory_view() {
    if (view instanceof InventoryView) view = null;
    else view = new InventoryView(active_player);
  }

  public static void draw_active_room() {
    papplet().background(200);
    papplet().pushMatrix();
    papplet().translate(papplet().width/2, papplet().height/2);
    if (active_player != null) {
      camera_offset_x -= (camera_offset_x - active_player.x)*camera_smoothing_factor;
      camera_offset_y -= (camera_offset_y - active_player.y)*camera_smoothing_factor;
      active_player.player_draw();
    }
    papplet().popMatrix();
  }

  public static void focus_camera() {
    if (active_player != null) {
      camera_offset_x = active_player.x;
      camera_offset_y = active_player.y;
    }
  }

  public static JSONObject save_object() {
    JSONObject save_object = new JSONObject();
    save_object.put("VERSION", VERSION);

    JSONArray room_array = new JSONArray();
    for (Room room : rooms) {
      room_array.append(room.save_object());
    }
    save_object.put("rooms", room_array);

    return save_object;
  }


  /**
   * Time functions
   */

  public static void move_clock(int seconds) {
    int target_time = time + seconds;
    Event next_event = events.peek();
    while (next_event != null && next_event.time <= target_time) {
      time = next_event.time;
      events.poll().trigger();
      next_event = events.peek();
    }
    time = target_time;

    check_rules();
  }

  public static void check_rules() {
    for (int i = rules.size() - 1; i >= 0; i--) {
      Rule rule = rules.get(i);
      if (rule.check()) {
        rule.consequence();
      }
    }
  }

  public static void draw_clock() {
    draw_clock(time);
  }

  public static void draw_clock(int time) {
    papplet().pushStyle();
    papplet().fill(100);
    papplet().rect(0, 0, clock_x + 2*clock_r, clock_y + 2*clock_r);
    papplet().textAlign(PConstants.CENTER);
    String time_of_day = "AM: ";
    if (time%DAY > DAY/2) {time_of_day = "PM: ";}
    papplet().fill(255);
    papplet().text("Day: " + day() + " (" + DAYS[day()%7] + ") " + time_of_day, clock_x, clock_y + clock_r*2);
    papplet().ellipse(clock_x, clock_y, 2*clock_r, 2*clock_r);

    papplet().line(clock_x, clock_y,
                   clock_x + PApplet.sin(time/86400.0f*2*PConstants.TWO_PI)*2*clock_r/3,
                   clock_y - PApplet.cos(time/86400.0f*2*PConstants.TWO_PI)*2*clock_r/3);

    papplet().line(clock_x, clock_y,
                   clock_x + PApplet.sin(time/3600.0f*PConstants.TWO_PI)*clock_r,
                   clock_y - PApplet.cos(time/3600.0f*PConstants.TWO_PI)*clock_r);
    papplet().popStyle();
  }

  public static int day() {
    return time/DAY;
  }

  public static int day_of_week() {
    return day()%7;
  }

  public static int time_of_day() {
    return time%DAY;
  }

}
