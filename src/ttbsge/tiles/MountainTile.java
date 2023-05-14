package ttbsge.tiles;

public class MountainTile extends Tile {

  public MountainTile(int x, int y) {
    super(x, y);
    this.r = 142;
    this.g = 94;
    this.b = 4;
  }

  @Override
  public String screen_type() {
    return "Mountains";
  }

  @Override
  public String screen_name() {
    return "";
  }

}
