package ttge;

import processing.core.PApplet;

public class Tank {

  public static final int TANK_WIDTH = 30;
  public static final int TANK_HEIGHT = 15;
  public static final int TANK_TOWER_RADIUS = 20;
  public static final int TANK_BARREL_LENGTH = 30;
  public static final float HEALTH_BAR_HEIGHT = 30;
  public static final int HEALTH_BAR_OFFSET = 20;
  public static final int DEFAULT_HIT_X = -12345;

  public static float TANK_MAX_POWER = 20;
  public static float TANK_MIN_POWER = 3;
  public static float TANK_MAX_HEALTH = 100;

  public float x;
  public int direction = 1;
  public float speed = 2;
  public float barrel_angle = 0;
  public float health = TANK_MAX_HEALTH;
  public float original_health = TANK_MAX_HEALTH;
  public float power = 10;
  public float fuel = 300;
  public float fuel_consumption = 0;
  public int shot_cooldown = 20;
  public float shot_damage = 20;

  public int r = 0;
  public int g = 200;
  public int b = 0;

  public int previus_shot_frame = 0;
  public int previus_slide_frame = 0;

  public Projectile previous_projectile;
  public int previous_hit_x = DEFAULT_HIT_X;
  public Tank target_tank;

  public Tank() {
    this.x = 100;
    this.health = TANK_MAX_HEALTH;
    this.original_health = TANK_MAX_HEALTH;
    TTGE.tanks.add(this);
  }

  void draw() {
    TTGE.papplet().pushStyle();
    TTGE.papplet().pushMatrix();
    TTGE.papplet().translate(TTGE.x_offset, 0);
    TTGE.papplet().fill(r, g, b);
    TTGE.papplet().line(this.x, TTGE.papplet().height - y() - TANK_HEIGHT,
         aim_x()*TANK_BARREL_LENGTH + this.x,
         TTGE.papplet().height - aim_y()*TANK_BARREL_LENGTH - y() - TANK_HEIGHT);
    TTGE.papplet().ellipse(this.x, TTGE.papplet().height - y() - TANK_HEIGHT, TANK_TOWER_RADIUS, TANK_TOWER_RADIUS);
    TTGE.papplet().rect(this.x - TANK_WIDTH/2, TTGE.papplet().height - y() - TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);
    TTGE.papplet().popMatrix();
    TTGE.papplet().popStyle();
    if (TTGE.show_health_bars) {
      this.draw_health_bar();
    }
  }

  public void draw_health_bar() {
    TTGE.papplet().pushStyle();
    TTGE.papplet().pushMatrix();
    TTGE.papplet().translate(TTGE.x_offset, 0);
    TTGE.papplet().fill(255, 0, 0);
    TTGE.papplet().rect(x - HEALTH_BAR_OFFSET, TTGE.papplet().height - y() - TANK_HEIGHT*2 - HEALTH_BAR_HEIGHT, 5, HEALTH_BAR_HEIGHT);
    TTGE.papplet().fill(100, 0, 0);
    TTGE.papplet().rect(x - HEALTH_BAR_OFFSET, TTGE.papplet().height - y() - TANK_HEIGHT*2 - HEALTH_BAR_HEIGHT, 5, HEALTH_BAR_HEIGHT*(1 - health/original_health));
    TTGE.papplet().popMatrix();
    TTGE.papplet().popStyle();
  }


  public void move_left() {
    if (this.fuel >= this.fuel_consumption) {
      x -= speed;
      if (x < 0) {
        x = 0;
      }
      this.fuel -= this.fuel_consumption;
    }
    direction = -1;
  }

  public void move_right() {
    if (this.fuel >= this.fuel_consumption) {
      x += speed;
      if (x >= TTGE.ground.length) {
        x = TTGE.ground.length - 1;
      }
      this.fuel -= this.fuel_consumption;
    }
    direction = 1;
  }

  public float aim_x() {
    return direction * PApplet.cos(PApplet.radians(this.barrel_angle));
  }

  public float aim_y() {
    return PApplet.sin(PApplet.radians(this.barrel_angle));
  }

  public float y() {
    return TTGE.ground[(int)this.x];
  }

  public void lift_barrel() {
    if (barrel_angle < 85) {
      barrel_angle += 0.5;
    }
  }

  public void lower_barrel() {
    if (barrel_angle > 0) {
      barrel_angle -= 0.5;
    }
  }

  public void increase_power() {
    if (power < TANK_MAX_POWER) {
      power += 0.1;
    }
  }

  public void decrease_power() {
    if (power > TANK_MIN_POWER) {
      power -= 0.1;
    }
  }

  public void draw_aim_trace() {
    TTGE.papplet().pushStyle();
    TTGE.papplet().stroke(100);
    float x = this.x + aim_x()*TANK_BARREL_LENGTH;
    float y = y() + TANK_HEIGHT + aim_y()*TANK_BARREL_LENGTH;
    float vx = aim_x()*power;
    float vy = aim_y()*power;
    while (x > 0 && x < TTGE.ground.length && y > TTGE.ground[(int)x]) {
      TTGE.papplet().line(x + TTGE.x_offset, TTGE.papplet().height - y,
                          x + vx + TTGE.x_offset, TTGE.papplet().height - y - vy);
      x += vx;
      y += vy;
      vy += TTGE.gravity;
    }
    TTGE.papplet().popStyle();
  }

  public void draw_power() {
    TTGE.papplet().pushStyle();
    TTGE.papplet().fill(255);
    TTGE.papplet().rect(50, 50, (TANK_MAX_POWER - TANK_MIN_POWER)/TANK_MAX_POWER*100, 30);
    TTGE.papplet().fill(250, 0, 0);
    TTGE.papplet().rect(50, 50, (power - TANK_MIN_POWER)/TANK_MAX_POWER*100, 30);
    TTGE.papplet().popStyle();
  }

  public boolean can_shoot() {
    if (this.previus_shot_frame + this.shot_cooldown > TTGE.papplet().frameCount) {
      return false;
    }
    return true;
  }

  public Projectile shoot() {
    return shoot(shot_damage);
  }

  public Projectile shoot(float damage) {
    if (this.can_shoot()) {
      previous_projectile = new Projectile(
          this.x + aim_x()*TANK_BARREL_LENGTH,
          y() + TANK_HEIGHT + aim_y()*TANK_BARREL_LENGTH,
          damage, aim_x()*power, aim_y()*power);
      previous_projectile.shooting_tank = this;
      this.previus_shot_frame = TTGE.papplet().frameCount;
      return previous_projectile;
    }
    return null;
  }

  public void follow_with_camera() {
    if (TTGE.papplet().width/2 - TTGE.x_offset - this.x > TTGE.camera_speed) {
      TTGE.move_camera_left();
    }
    else if (TTGE.papplet().width/2 - TTGE.x_offset - this.x < -TTGE.camera_speed) {
      TTGE.move_camera_right();
    }
  }

  /**
   * These methods can be overridden for game mechanics and AI
   */
  public void on_destruction() {}

  public void projectile_hit(Projectile projectile) {}

  public void explosion_hit(Explosion explosion) {}

  public void ai_act() {
    // This has base line implementation for AI to control the aiming and power
    if (target_tank != null && target_tank.health > 0) {
      if (target_tank.x < this.x && this.direction > 0) {
        this.move_left();
      }
      else if (target_tank.x > this.x && this.direction < 0) {
        this.move_right();
      }
      if (this.barrel_angle < 45) {
        this.lift_barrel();
      }
      else if (this.barrel_angle > 45) {
        this.lower_barrel();
      }
      if (can_shoot() && previous_hit_x != DEFAULT_HIT_X) {
        // Adjust based on previous missed shot
        float delta = PApplet.abs(previous_hit_x - target_tank.x);
        float adjustment = 0;
        if (delta > TANK_WIDTH*10) {
          adjustment = 0.2f;
        }
        else if (delta > TANK_WIDTH*5) {
          adjustment = 0.1f;
        }
        else if (delta > TANK_WIDTH) {
          adjustment = 0.05f;
        }
        else if (delta > 1) {
          adjustment = 0.01f;
        }

        if ((x < target_tank.x && previous_hit_x < target_tank.x) ||
            (x > target_tank.x && previous_hit_x > target_tank.x)) {
          this.power *= 1 + adjustment;
        }
        else {
          this.power *= 1 - adjustment;
        }
      }
      shoot();
    }
    else {
      target_tank = null;
    }
  }


}
