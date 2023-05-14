package ttbsge;

import java.util.ArrayList;

public class Faction {

  public ArrayList<CampaignUnit> units = new ArrayList<CampaignUnit>();
  public ArrayList<TownCenter> towns = new ArrayList<TownCenter>();
  public ArrayList<Building> buildings = new ArrayList<Building>();

  public TownCenter capital;

  public int r = 250;
  public int g = 250;
  public int b = 250;

  public String name;

  public Faction(String name) {
    this.name = name;
    TTBSGE.factions.add(this);
    this.r = (int) (Math.random()*255);
    this.g = (int) (Math.random()*255);
    this.b = (int) (Math.random()*255);
  }

}
