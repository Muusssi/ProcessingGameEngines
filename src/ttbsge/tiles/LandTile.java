package ttbsge.tiles;

public class LandTile extends Tile {

  public LandTile(int x, int y) {
    super(x, y);
    this.r = 108;
    this.g = 175;
    this.b = 32;
  }

  @Override
  public String screen_name() {
    return "";
  }

  @Override
  public String screen_type() {
    return "Land area";
  }

}
