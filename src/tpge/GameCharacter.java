package tpge;

import processing.core.PImage;

public class GameCharacter extends TPGEObject {

  public static float DEFAULT_SPEED = 4;
  public static float DEFAULT_JUMP_SPEED = 10;

  public float speed = DEFAULT_SPEED;
  public float jump_speed = DEFAULT_JUMP_SPEED;

  public boolean unused_double_jump = false;

  public PImage image;
  public float image_scale = 1;
  public boolean show_hitbox = false;


  public GameCharacter(World world) {
    super(world);
    this.static_object = false;
    this.add_corner(40, 0);
    this.add_corner(20, 80);
  }

  public void set_image(PImage image) {
    this.set_image(image, true, image.height);
  }

  public void set_image(PImage image, float character_height) {
    this.set_image(image, true, character_height);
  }

  public void set_image(PImage image, boolean use_image_hit_box) {
    this.set_image(image, use_image_hit_box, image.height);
  }

  public void set_image(PImage image, boolean use_image_hit_box, float character_height) {
    this.clear_hitbox();
    this.image = image;
    image_scale = character_height / image.height;
    if (use_image_hit_box) {
      this.add_corner(image.width*image_scale, 0);
      this.add_corner(image.width*image_scale, image.height*image_scale);
      this.add_corner(0, image.height*image_scale);
    }
  }

  @Override
  public void draw() {
    animate();
    if (image != null) {
      TPGE.papplet().image(image, this.x, -this.y - this.image.height*image_scale,
          this.image.width*image_scale, this.image.height*image_scale);
    }
    if (image == null || show_hitbox) draw_corners();
  }

  public boolean can_jump() {
    if (this.touching_ground) {
      unused_double_jump = true;
      return true;
    }
    if (unused_double_jump) {
      unused_double_jump = false;
      return true;
    }
    return false;
  }


  public void jump() {
    if (this.can_jump()) {
      this.speed_y = jump_speed;
    }
  }

  public void move_left() {
    if (this.touching_ground) {
      this.speed_x = -this.speed/this.ground_resistance;
    }
    else {
      this.speed_x = Math.min(-this.speed, this.speed_x);
    }
  }

  public void move_right() {
    if (this.touching_ground) {
      this.speed_x = this.speed/this.ground_resistance;
    }
    else {
      this.speed_x = Math.max(this.speed, this.speed_x);
    }
  }

}
