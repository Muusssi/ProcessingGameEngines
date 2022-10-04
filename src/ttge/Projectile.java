package ttge;

public class Projectile {

  public static float room_in_the_sky = 1000;

  public float radius = 5;
  public float x, y, damage;
  public float x_speed = 0;
  public float y_speed = 0;
  public boolean dead = false;
  public Tank shooting_tank;

  public Projectile(float x, float y, float damage, float x_speed, float y_speed) {
    this.x = x;
    this.y = y;
    this.damage = damage;
    this.x_speed = x_speed;
    this.y_speed = y_speed;
    TTGE.projectiles.add(this);
  }

  public void draw() {
    x += x_speed;
    y += y_speed;
    y_speed += TTGE.gravity;
    TTGE.papplet().ellipse(x + TTGE.x_offset, TTGE.papplet().height - y, 10, 10);
    if (x > 0 && x < TTGE.ground.length && (y < TTGE.ground[(int)x] || hits_tank())) {
      new Explosion(x, y, damage);
      dead = true;
      if (shooting_tank != null) {
        shooting_tank.previous_hit_x = (int)x;
      }
    }
    else if (y > TTGE.papplet().height + room_in_the_sky) {
      dead = true;
    }
    else if (x < 0 || x > TTGE.ground.length) {
      dead = true;
      shooting_tank.previous_hit_x = (int)x;
    }
  }

  public boolean hits_tank() {
    for (int i = TTGE.tanks.size() - 1; i >= 0 ; i--) {
      Tank tank = TTGE.tanks.get(i);
      if (x > tank.x - Tank.TANK_WIDTH/2 && x < tank.x + Tank.TANK_WIDTH/2) {
        if (y < tank.y() + Tank.TANK_HEIGHT && y > tank.y()) {
          return true;
        }
      }
    }
    return false;
  }

}
