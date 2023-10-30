package ttdrge;

import processing.core.PConstants;
import processing.core.PVector;
import processing.data.JSONObject;
import tge.SerializableObject;
import tge.TGE;

public class Vehicle extends SerializableObject {

    public float frame_length = 40;
    public float frame_width = 20;

    public int r = 200;
    public int g = 200;
    public int b = 0;

    public PVector position = new PVector(0, 0);
    public PVector speed = new PVector(0, 0);
    public float direction = 0;

    public float forward_drag_coefficient = 0.99f;
    public float slide_drag_coefficient = 0.80f;
    public float turn_speed = 0.05f;
    public float acceleration_speed = 0.15f;
    public float deceleration_speed = 0.2f;

    public Vehicle() {
        TTDRGE.vehicles.add(this);
    }

    public Vehicle(JSONObject json) {
        TTDRGE.vehicles.add(this);
        this.update_from_json(json);
    }

    public void update_from_json(JSONObject json) {
        this.frame_length = json.getFloat("frame_length", this.frame_length);
        this.frame_width = json.getFloat("frame_width", this.frame_width);

        this.r = json.getInt("r", this.r);
        this.g = json.getInt("g", this.g);
        this.b = json.getInt("b", this.b);

        this.position = new PVector(json.getFloat("position_x", this.position.x),
                json.getFloat("position_y", this.position.y));
        this.speed = new PVector(json.getFloat("speed_x", this.speed.x),
                json.getFloat("speed_y", this.speed.y));
        this.direction = json.getFloat("direction", this.direction);

        this.forward_drag_coefficient = json.getFloat("forward_drag_coefficient", this.forward_drag_coefficient);
        this.slide_drag_coefficient = json.getFloat("slide_drag_coefficient", this.slide_drag_coefficient);
        this.turn_speed = json.getFloat("turn_speed", this.turn_speed);
        this.acceleration_speed = json.getFloat("acceleration_speed", this.acceleration_speed);
        this.deceleration_speed = json.getFloat("deceleration_speed", this.deceleration_speed);
    }

    @Override
    public JSONObject save_object() {
        JSONObject json = super.save_object();
        json.put("frame_length", this.frame_length);
        json.put("frame_width", this.frame_width);

        json.put("r", this.r);
        json.put("g", this.g);
        json.put("b", this.b);

        json.put("position_x", this.position.x);
        json.put("position_y", this.position.y);
        json.put("speed_x", this.speed.x);
        json.put("speed_y", this.speed.y);
        json.put("direction", this.direction);

        json.put("forward_drag_coefficient", this.forward_drag_coefficient);
        json.put("slide_drag_coefficient", this.slide_drag_coefficient);
        json.put("turn_speed", this.turn_speed);
        json.put("acceleration_speed", this.acceleration_speed);
        json.put("deceleration_speed", this.deceleration_speed);
        return json;
    }

    @Override
    public void update(JSONObject json) {
        super.update(json);
        this.update_from_json(json);
    }

    public void animate() {
        position.add(speed);
        apply_drag();
    }

    public void control(int accelerate_key, int break_key, int left_turn_key, int right_turn_key) {
        if (TGE.is_key_pressed(accelerate_key)) {
            this.accelerate();
        }
        if (TGE.is_key_pressed(break_key)) {
            this.decelerate();
        }
        if (TGE.is_key_pressed(left_turn_key)) {
            this.turn_left();
        }
        if (TGE.is_key_pressed(right_turn_key)) {
            this.turn_right();
        }
    }

    public void accelerate() {
        this.speed.add(this.frame_vector().mult(acceleration_speed));
    }

    public void decelerate() {
        PVector deceleration = this.frame_vector().mult(-deceleration_speed);
        if (PVector.add(this.forward_projection(speed), deceleration).mag() > deceleration_speed) {
            this.speed.add(deceleration);
        }
        else {
            this.speed.mult(0.01f);
        }
    }

    public void turn_left() {
        if (this.speed.mag() > 1) {
            this.direction -= turn_speed;
        }
    }

    public void turn_right() {
        if (this.speed.mag() > 1) {
            this.direction += turn_speed;
        }
    }

    public void fundamental_draw() {
        TTDRGE.papplet().fill(r, g, b);
        TTDRGE.papplet().rectMode(PConstants.CENTER);
        TTDRGE.papplet().rect(0, 0, frame_length, frame_width);
    }

    public void draw() {
        TTDRGE.papplet().push();
        TTDRGE.papplet().translate(this.position.x, this.position.y);
        TTDRGE.papplet().rotate(direction);
        this.fundamental_draw();
        TTDRGE.papplet().pop();
    }

    public void draw_on_minimap() {
        TTDRGE.papplet().push();
        TTDRGE.papplet().fill(this.r, this.g, this.b);
        TTDRGE.papplet().noStroke();
        TTDRGE.papplet().ellipse(this.position.x, this.position.y, 20*TTDRGE.track_image_scale, 20*TTDRGE.track_image_scale);
        TTDRGE.papplet().pop();
    }

    public void follow_with_camera() {
        if (TTDRGE.view_offset.dist(position) != 0) {
            TTDRGE.view_offset = this.position;
        }
        TTDRGE.camera_direction = (TTDRGE.camera_direction - (
                TTDRGE.camera_direction - (this.direction + PConstants.HALF_PI))*TTDRGE.camera_smoothing_factor);
    }

    public void follow_with_camera_horizontal() {
        TTDRGE.camera_direction -= TTDRGE.camera_direction*TTDRGE.camera_smoothing_factor;
        if (TTDRGE.view_offset.dist(position) != 0) {
            TTDRGE.view_offset.add(position.copy().sub(TTDRGE.view_offset).mult(TTDRGE.camera_smoothing_factor));
        }
    }

    public void apply_drag() {
        speed = forward_projection(speed).mult(forward_drag_coefficient)
                .add(side_projection(speed).mult(slide_drag_coefficient));
    }

    public PVector frame_vector() {
        return PVector.fromAngle(direction);
    }

    public PVector forward_projection(PVector vector) {
        PVector frame = frame_vector();
        return PVector.mult(frame, vector.dot(frame) / frame.dot(frame));
    }

    public PVector side_projection(PVector vector) {
        return vector.copy().sub(forward_projection(vector));
    }

}
