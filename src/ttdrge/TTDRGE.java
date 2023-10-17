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

    public static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

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
        papplet().pop();
    }

    public static void draw_vehicles() {
        for (Vehicle vehicle : vehicles) {
            vehicle.draw();
            vehicle.animate();
        }
    }

    public static void draw_mini_map() {
        if (track_image != null) {
            papplet().push();
            float scale = 0.3f*papplet().width/track_image.width;
            papplet().image(track_image, 0.7f*papplet().width, 0,
                    track_image.width*scale,
                    track_image.height*scale);
            for (Vehicle vehicle : vehicles) {
                papplet().fill(vehicle.r, vehicle.g, vehicle.b);
                papplet().noStroke();
                papplet().ellipse(0.7f*papplet().width + vehicle.position.x*scale/track_image_scale,
                        vehicle.position.y*scale/track_image_scale, 10, 10);
            }
            papplet().pop();
        }
    }

}
