package ttdrge;

import java.util.ArrayList;

import processing.core.PImage;
import processing.core.PVector;
import tge.TGE;

public class TTDRGE extends TGE {

    // Tommi's Top-Down Racing Game Engine

    public static PVector view_offset = new PVector();
    public static float scale = 1;
    public static float camera_direction = 0;
    public static float camera_smoothing_factor = 0.1f;
    public static float zoom_factor = 1.2f;

    public static PImage track_image;
    public static float track_image_scale = 1;
    public static float minimap_proportion = 0.3f;

    public static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    public static ArrayList<CheckPoint> check_points = new ArrayList<CheckPoint>();

    public static void set_track_image(PImage track_image) {
        TTDRGE.track_image = track_image;
        track_image_scale = 1;
    }

    public static void set_track_image(PImage track_image, float track_width) {
        TTDRGE.track_image = track_image;
        track_image_scale = track_width/track_image.width;
    }

    public static void zoom_in() {
        TTDRGE.scale *= zoom_factor;
    }

    public static void zoom_out() {
        TTDRGE.scale /= zoom_factor;
    }

    public static void reset_zoom() {
        TTDRGE.scale = 1;
    }

    public static PVector cursor_points_on_track() {
        return new PVector(papplet().mouseX, papplet().mouseY)
                .sub(new PVector(papplet().width/2, papplet().height/2))
                .div(scale)
                .rotate(camera_direction)
                .add(new PVector(view_offset.x, view_offset.y));
    }

    public static PVector cursor_points_on_minimap() {
        return new PVector(papplet().mouseX, papplet().mouseY)
                .sub(new PVector((1 - minimap_proportion)*papplet().width, 0))
                .div(minimap_scale())
                .mult(track_image_scale);
    }

    public static void draw() {
        papplet().background(200);
        papplet().push();
        papplet().translate(papplet().width/2, papplet().height/2);
        papplet().scale(scale);
        papplet().rotate(-camera_direction);
        papplet().translate(-view_offset.x, -view_offset.y);
        if (track_image != null) {
            papplet().image(track_image, 0, 0,
                    track_image.width*track_image_scale,
                    track_image.height*track_image_scale);
        }
        draw_vehicles();
        draw_check_points();
        papplet().pop();
    }

    public static void draw_vehicles() {
        for (Vehicle vehicle : vehicles) {
            vehicle.draw();
            vehicle.animate();
        }
    }

    public static void draw_check_points() {
        for (CheckPoint check_point : check_points) {
            check_point.draw();
        }
    }

    public static float minimap_scale() {
        return minimap_proportion*papplet().width/track_image.width;
    }

    public static void draw_mini_map() {
        if (track_image != null) {
            papplet().push();
            papplet().translate((1 - minimap_proportion)*papplet().width, 0);
            papplet().scale(minimap_scale());
            papplet().image(track_image, 0, 0);
            papplet().scale(1/track_image_scale);
            for (Vehicle vehicle : vehicles) {
                vehicle.draw_on_minimap();
            }
            for (CheckPoint check_point : check_points) {
                check_point.draw_on_minimap();
            }
            papplet().pop();
        }
    }

}
