package test_games;

import processing.core.PApplet;
import ttbsge.CampaignUnit;
import ttbsge.Faction;
import ttbsge.TTBSGE;
import ttbsge.TownCenter;

public class TTBSGEtest extends PApplet {

  public static void main(String[] args) {
    PApplet.main("test_games.TTBSGEtest");
  }

  @Override
  public void settings(){
    size(1200, 800);
  }

  @Override
  public void setup() {
    TTBSGE.init(this);
    TTBSGE.init_world(40, 40);
    Faction faction = new Faction("Foobarians");
    new CampaignUnit(faction, TTBSGE.get_tile_at(1, 1));
    new CampaignUnit(faction, TTBSGE.get_tile_at(11, 12));
    new TownCenter(faction, TTBSGE.get_tile_at(10, 10), "Foobria");

    Faction faction2 = new Faction("Narnians");
    new CampaignUnit(faction2, TTBSGE.get_tile_at(15, 3));
    new TownCenter(faction2, TTBSGE.get_tile_at(15, 5), "Narnia");
  }

  @Override
  public void draw(){
    TTBSGE.handle_wasd();
    TTBSGE.draw_campaign_map();
    //TTBSGE.focus_smoothly_on_active_object();
    TTBSGE.draw_game_controls();
  }

  @Override
  public void keyPressed(){
    TTBSGE.register_key_press();
    if (keyCode == ENTER) {

    }
  }

  @Override
  public void keyReleased(){
    TTBSGE.register_key_release();
  }

  @Override
  public void mousePressed() {
    TTBSGE.handle_mouse_press();
  }

}
