package ttge;

import processing.core.PApplet;

public class Explosion extends Projectile {

  public int ttl = 5;
  public float explosion_radius;

  public Explosion(float x, float y, float damage) {
    super(x, y, 0, 0, 0);
    explosion_radius = damage;
    this.damage = damage;
    this.explode();
  }

  @Override
  public void draw() {
    TTGE.papplet().pushStyle();
    TTGE.papplet().fill(255, 0, 0);
    TTGE.papplet().ellipse(x + TTGE.x_offset, TTGE.papplet().height - y, 2*explosion_radius, 2*explosion_radius);
    TTGE.papplet().popStyle();
    if (ttl <= 0) {
      dead = true;
    }
    ttl--;
  }

  public void explode() {
    for (int i = 0; i < TTGE.tanks.size(); ++i) {
      Tank tank = TTGE.tanks.get(i);
      float dist = PApplet.dist(tank.x, tank.y() + Tank.TANK_HEIGHT/2, x, y);
      if (dist < explosion_radius/2) {
        tank.health -= damage;
        tank.explosion_hit(this);
      }
      else if (dist < explosion_radius) {
        tank.health -= damage/2;
        tank.explosion_hit(this);
      }
    }
    for (int i = -(int)explosion_radius; i < (int)explosion_radius; ++i) {
      int pos = ((int)x) + i;
      if (pos >= 0 && pos < TTGE.ground.length) {
        float diff = PApplet.sqrt(PApplet.sq(explosion_radius) - PApplet.sq(i));
        if (TTGE.ground[pos] > y + diff) {
          TTGE.ground[pos] -= 2*diff;
        }
        else if (TTGE.ground[pos] > y - diff) {
          TTGE.ground[pos] -= TTGE.ground[pos] - (y - diff);
        }
      }
    }
    TTGE.unstability_start = (int)(x - explosion_radius) - 1;
    TTGE.unstability_end = (int)(x + explosion_radius) + 1;
    TTGE.update_ground_layer();
  }

}
