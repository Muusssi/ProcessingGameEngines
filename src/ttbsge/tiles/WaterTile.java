package ttbsge.tiles;

public class WaterTile extends Tile {

  public WaterTile(int x, int y) {
    super(x, y);
    this.r = 32;
    this.g = 73;
    this.b = 175;
  }

  @Override
  public boolean navigable() {
    return true;
  }

  @Override
  public String screen_type() {
    return "Sea";
  }

  @Override
  public String screen_name() {
    return "";
  }

}
