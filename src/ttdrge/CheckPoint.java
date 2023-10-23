package ttdrge;

import processing.core.PVector;

public class CheckPoint {

    public PVector left_point, right_point;

    public CheckPoint(float x1, float y1, float x2, float y2) {
        this.left_point = new PVector(x1, y1);
        this.right_point = new PVector(x2, y2);
        TTDRGE.check_points.add(this);

    }

    public void draw() {
        TTDRGE.papplet().push();
        TTDRGE.papplet().noStroke();
        TTDRGE.papplet().fill(250);
        TTDRGE.papplet().ellipse(left_point.x, left_point.y, 20, 20);
        TTDRGE.papplet().ellipse(right_point.x, right_point.y, 20, 20);
        TTDRGE.papplet().pop();

        TTDRGE.papplet().push();
        TTDRGE.papplet().stroke(250);
        TTDRGE.papplet().strokeWeight(5);
        TTDRGE.papplet().line(left_point.x, left_point.y, right_point.x, right_point.y);
        TTDRGE.papplet().pop();
    }

    public void draw_on_minimap() {
        TTDRGE.papplet().push();
        TTDRGE.papplet().stroke(250);
        TTDRGE.papplet().strokeWeight(5*TTDRGE.track_image_scale);
        TTDRGE.papplet().line(left_point.x, left_point.y, right_point.x, right_point.y);
        TTDRGE.papplet().pop();
    }

}
