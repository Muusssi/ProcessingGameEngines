package tpge;

import java.util.ArrayList;

import processing.core.PConstants;
import tge.SerializableObject;

public class TPGEObject extends SerializableObject {

  public World world;
  public boolean static_object = true;
  public float ground_resistance = 0.8f;

  public boolean touching_ground = true;

  public float x = 0;
  public float y = 0;
  public float speed_x = 0;
  public float speed_y = 0;

  public ArrayList<CollisionCorner> corners = new ArrayList<CollisionCorner>();

  public TPGEObject(World world) {
    this.world = world;
    this.world.objects.add(this);
    this.clear_hitbox();
  }

  public CollisionCorner add_corner(float x, float y) {
    CollisionCorner corner = new CollisionCorner(x, y);
    this.corners.add(corner);
    return corner;
  }

  public void clear_hitbox() {
    this.corners.clear();
    this.add_corner(0, 0);
  }

  public void draw() {
    animate();
    draw_corners();
  }

  public void follow_with_camera() {
    this.world.camera_x += TPGE.camera_smoothing_factor*(this.x - this.world.camera_x);
    this.world.camera_y += TPGE.camera_smoothing_factor*(this.y - this.world.camera_y);
  }

  public TPGEObject animate_x_step(int steps) {
    float new_x = this.x + this.speed_x/steps;
    TPGEObject collided_object = this.collides_in_position(new_x, this.y);
    if (collided_object != null) {
      return collided_object;
    }
    this.x = new_x;
    return null;
  }

  public TPGEObject animate_y_step(int steps) {
    this.speed_y -= this.world.gravity/steps;
    float new_y = this.y + this.speed_y/steps;
    TPGEObject collided_object = this.collides_in_position(this.x, new_y);
    if (collided_object != null) {
      this.speed_y = 0;
      return collided_object;
    }
    this.y = new_y;
    return null;
  }

  public void when_hits_ground() {
    this.speed_y = 0;
  }

  public void animate() {
    if (this.static_object) {
      return;
    }

    int steps = Math.max((int)(Math.max(Math.abs(speed_x), Math.abs(speed_y))), 1);
    TPGEObject x_collides = null;
    TPGEObject y_collides = null;
    for (int i=0; i<steps; i++) {
      x_collides = animate_x_step(steps);
      y_collides = animate_y_step(steps);
      if (x_collides != null && y_collides != null) {
        break;
      }
    }

    if (x_collides != null) {
      this.speed_x = 0;
    }

    if (y_collides != null) {
      if (this.speed_y <= 0) { // falling
        if (!this.touching_ground) {
          this.when_hits_ground();
        }
        this.touching_ground = true;
        this.speed_x *= ground_resistance;
      }
      else {
        this.speed_y = 0;
      }
    }
    else {
      this.touching_ground = false;
    }
  }

  public TPGEObject collides_in_position(float potential_x, float potential_y) {
    // Check if any corner hits the world boundaries
    for (CollisionCorner corner : corners) {
      if (potential_x + corner.x < 0
          || potential_y + corner.y < 0
          || potential_x + corner.x > this.world.width
          || potential_y + corner.y > this.world.height) {
        return this;
      }
    }
    for (TPGEObject other_object : this.world.objects) {
      if (this == other_object) {
        continue;
      }
      // Check no corner would be inside another object
      for (CollisionCorner corner : this.corners) {
        if (other_object.point_inside(potential_x + corner.x, potential_y + corner.y)) {
          return other_object;
        }
      }
      // If this object has less than 3 corners no other object can me inside of it
      if (this.corners.size() < 3) continue;
      // Check no other objects corner is inside this object
      for (CollisionCorner corner : other_object.corners) {
        // The collision check is adjusted with the potential location
        if (this.point_inside(other_object.x + corner.x - (potential_x - this.x),
            other_object.y + corner.y - (potential_y - this.y))) {
          return other_object;
        }
      }
    }
    return null;
  }

  public boolean point_inside(float x, float y) {
    if (this.corners.size() < 3) {
      return false;
    }
    x -= this.x;
    y -= this.y;
    boolean inside = true;
    CollisionCorner point1 = this.corners.get(this.corners.size() - 1);
    CollisionCorner point2;

    for (CollisionCorner corner : this.corners) {
      point2 = corner;
      if (point2.x - point1.x == 0) {
        if (point2.y > point1.y) {
          inside = inside && x < point1.x;
        }
        else if (point2.y < point1.y) {
          inside = inside && x > point1.x;
        }
      }
      else if (point2.x > point1.x) {
        inside = inside && (y > (point2.y - point1.y)/(point2.x - point1.x)*(x - point1.x) + point1.y);
      }
      else if (point2.x < point1.x) {
        inside = inside && (y < (point2.y - point1.y)/(point2.x - point1.x)*(x - point1.x) + point1.y);
      }
      point1 = point2;
    }
    return inside;
  }

  public void draw_corners() {
    TPGE.papplet().push();
    TPGE.papplet().fill(100, 200);
    TPGE.papplet().ellipse(this.x, -this.y, 10, 10);
    TPGE.papplet().beginShape();
    for (CollisionCorner corner : corners) {
      TPGE.papplet().vertex(this.x + corner.x, -y - corner.y);
    }
    TPGE.papplet().endShape(PConstants.CLOSE);
    TPGE.papplet().pop();
  }

  public class CollisionCorner {
    public float x, y;

    public CollisionCorner(float x, float y) {
      this.x = x;
      this.y = y;
    }
  }


}
