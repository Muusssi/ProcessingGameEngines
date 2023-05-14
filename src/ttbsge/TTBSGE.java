package ttbsge;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import tge.TGE;
import tge.ui.Button;
import ttbsge.CampaignUnit.Route;
import ttbsge.buttons.EndTurnButton;
import ttbsge.tiles.LandTile;
import ttbsge.tiles.MountainTile;
import ttbsge.tiles.Tile;
import ttbsge.tiles.WaterTile;

public class TTBSGE extends TGE {

  public static final float SLIDE_SPEED = 10;
  public static final float ZOOMIMG_SPEED = 0.03f;
  public static final float ZOOM_IN = 1 + ZOOMIMG_SPEED;
  public static final float ZOOM_OUT = 1 - ZOOMIMG_SPEED;

  public static final float MAX_ZOOM = 3;
  public static final float MIN_ZOOM = 0.3f;

  public static final int TILE_SIZE = 20;

  protected static Tile[][] board;
  protected static int columns = 0;
  protected static int rows = 0;

  public static PGraphics map_layer;
  protected static boolean map_dirty = true;
  public static PGraphics object_layer;
  public static boolean object_layer_dirty = true;

  public static float x_offset = 0;
  public static float y_offset = 0;
  public static float scale = 1;

  public static int round = 1;
  public static EndTurnButton end_turn_button;

  public static Faction active_faction;
  public static CampaignObject active_object;
  public static ArrayList<Faction> factions = new ArrayList<Faction>();
  public static ArrayList<CampaignUnit> units = new ArrayList<CampaignUnit>();

  public static CampaignUnit moving_unit;


  public static void init_world(int columns, int rows) {
    board = new Tile[columns][rows];
    TTBSGE.columns = columns;
    TTBSGE.rows = rows;
    generate_map();
    end_turn_button = new EndTurnButton();
  }

  private static void generate_map() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        Tile tile = generate_tile_for(col, row);
        board[row][col] = tile;
      }
    }
  }

  private static Tile generate_tile_for(int col, int row) {
    float num = TTBSGE.papplet().noise(col/4f, row/4f);
    if (num < 0.3) {
      return new MountainTile(col, row);
    }
    else if (num < 0.6) {
      return new LandTile(col, row);
    }
    else {
      return new WaterTile(col, row);
    }
  }

  private static void update_map_layer() {
    if (map_dirty) {
      map_layer = papplet().createGraphics(TILE_SIZE*board[0].length + TILE_SIZE/2, TILE_SIZE*board.length);
      map_layer.beginDraw();
      map_layer.background(200);
      for (int row = 0; row < board.length; row++) {
        for (int col = 0; col < board[row].length; col++) {
          Tile tile = board[row][col];
          tile.draw(map_layer);
          if (tile.building != null) {
            tile.building.draw(map_layer);
          }
        }
      }
      map_layer.endDraw();
      map_dirty = false;
    }
  }

  private static void update_object_layer() {
    if (object_layer_dirty) {
      object_layer = papplet().createGraphics(TILE_SIZE*board[0].length + TILE_SIZE/2, TILE_SIZE*board.length);
      object_layer.beginDraw();
      for (CampaignObject campaign_unit : units) {
        campaign_unit.draw(object_layer);
      }
      if (active_object != null) {
        active_object.draw_highlight(object_layer);
      }
      object_layer.endDraw();
      object_layer_dirty = false;
    }
  }

  public static Tile get_tile_at(int col, int row) {
    if (col < 0 || col >= board.length || row < 0 || row >= board[0].length) {
      return null;
    }
    return board[row][col];
  }

  public static void draw_campaign_map() {
    papplet().background(200);
    update_map_layer();
    update_object_layer();

    papplet().pushMatrix();
    set_world_coordinates();
    papplet().background(200);
    papplet().image(map_layer, 0, 0);
    papplet().image(object_layer, 0, 0);
    if (active_object instanceof CampaignUnit) {
      draw_unit_path((CampaignUnit) active_object);
    }
    move_units();
    papplet().popMatrix();

    if (active_object != null) {
      active_object.draw_info_card();
    }
  }

  public static void draw_game_controls() {
    papplet().pushStyle();
    end_turn_button.draw();
    papplet().popStyle();
  }

  public static CampaignObject handle_mouse_press() {
    Button pressed_button = TGE.register_mouse_press();
    if (pressed_button == null) {
      if (papplet().mouseButton == PApplet.LEFT) {
        CampaignObject object = TTBSGE.select_pointed_object();
        return object;
      }
      else if (papplet().mouseButton == PApplet.RIGHT) {
        CampaignObject object = TTBSGE.pointed_object();
        if (TTBSGE.active_object instanceof CampaignUnit) {
          TTBSGE.active_object.act(object);
        }
        return object;
      }
    }
    return null;
  }

  protected static void move_units() {
    if (moving_unit != null) {
      moving_unit.move();
      if (moving_unit.route == null) {
        moving_unit = null;
      }
    }
  }

  public static void draw_unit_path(CampaignUnit unit) {
    papplet().push();
    HashMap<Tile, Route> reachable = unit.reachable_tiles();
    for (Route route : reachable.values()) {
      route.draw(TTBSGE.pgraphics());
    }
//    for (Route route : reachable.values()) {
//      route.draw_path(TTBSGE.pgraphics());
//    }
    Tile pointed_tile = pointed_tile();
    if (reachable.containsKey(pointed_tile)) {
      reachable.get(pointed_tile).draw_path(TTBSGE.pgraphics());

    }
    papplet().pop();
  }

  public static void end_turn() {
    for (CampaignUnit unit : units) {
      unit.turn_ended();
    }
    round++;
  }

  public static void set_world_coordinates() {
    papplet().translate(TTBSGE.papplet().width/2, TTBSGE.papplet().height/2);
    papplet().scale(scale);
    papplet().translate(-TTBSGE.papplet().width/2, -TTBSGE.papplet().height/2);
    papplet().translate(x_offset, y_offset);
  }

  public static float pointed_x() {
    return (TTBSGE.papplet().mouseX - TTBSGE.papplet().width/2)/scale + TTBSGE.papplet().width/2 - x_offset;
  }

  public static float pointed_y() {
    return (TTBSGE.papplet().mouseY - TTBSGE.papplet().height/2)/scale + TTBSGE.papplet().height/2 - y_offset;
  }

  public static Tile pointed_tile() {
    int row = (int)((pointed_y() - 0.134f*TILE_SIZE)/0.866f/TILE_SIZE);
    int offset = (row%2 == 1 ? TTBSGE.TILE_SIZE/2 : 0);
    int col = (int)(pointed_x() - offset)/TILE_SIZE;
    if (row < 0 || col < 0 || row >= rows || col >= columns) {
      return null;
    }
    return board[row][col];
  }

  public static void set_active_object(CampaignObject object) {
    if (object != null && moving_unit == null) {
      object.select();
    }
    object_layer_dirty = true;
  }

  public static CampaignObject pointed_object() {
    Tile tile = pointed_tile();
    if (tile != null) {
      if (tile.unit != null) {
        return tile.unit;
      }
      else if (tile.building != null) {
        return tile.building;
      }
      else {
        return tile;
      }
    }
    return null;
  }

  public static CampaignObject select_pointed_object() {
    CampaignObject object = pointed_object();
    set_active_object(object);
    return object;
  }


  public static void focus(float x, float y) {
    x_offset = -x + papplet().width/2;
    y_offset = -y + papplet().height/2;
  }

  public static void focus_smoothly(float x, float y) {
    x_offset += (-x + papplet().width/2 - x_offset)/5;
    y_offset += (-y + papplet().height/2 - y_offset)/5;
  }

  public static void focus_on_active_object() {
    if (active_object != null) {
      active_object.focus();
    }
  }

  public static void focus_smoothly_on_active_object() {
    if (active_object != null) {
      active_object.focus_smoothly();
    }
  }

  public static void zoom_in() {
    if (scale < MAX_ZOOM) {
      scale *= ZOOM_IN;
    }
  }

  public static void zoom_out() {
    if (scale > MIN_ZOOM) {
      scale *= ZOOM_OUT;
    }
  }

  public static void reset_zoom() {
    scale = 1;
  }

  public static void handle_wasd() {
    if (is_key_pressed('W')) {
      y_offset += SLIDE_SPEED/scale;
      if (y_offset > TTBSGE.papplet().height/2) {
        y_offset = TTBSGE.papplet().height/2;
      }
    }
    else if (is_key_pressed('S')) {
      y_offset -= SLIDE_SPEED/scale;
      if (y_offset < -TTBSGE.papplet().width*TILE_SIZE + TTBSGE.papplet().height/2) {
        y_offset = -TTBSGE.papplet().width*TILE_SIZE + TTBSGE.papplet().height/2;
      }

    }
    if (is_key_pressed('A')) {
      x_offset += SLIDE_SPEED/scale;
      if (x_offset > TTBSGE.papplet().width/2) {
        x_offset = TTBSGE.papplet().width/2;
      }
    }
    else if (is_key_pressed('D')) {
      x_offset -= SLIDE_SPEED/scale;
      if (x_offset < -TTBSGE.papplet().width*TILE_SIZE + TTBSGE.papplet().width/2) {
        x_offset = -TTBSGE.papplet().width*TILE_SIZE + TTBSGE.papplet().width/2;
      }
    }
    if (is_key_pressed('Q')) {
      zoom_in();
    }
    else if (is_key_pressed('E')) {
      zoom_out();
    }
  }


}

