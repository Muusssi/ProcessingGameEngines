package ttdrge;

import processing.core.PConstants;

public class Car extends Vehicle {
    
    @Override
    public void fundamental_draw() {
        TTDRGE.papplet().push();
        // Tires
        TTDRGE.papplet().rectMode(PConstants.CENTER);
        TTDRGE.papplet().noStroke();
        TTDRGE.papplet().fill(0);
        TTDRGE.papplet().rect(-0.35f*frame_length, -frame_width/2, frame_width/3, frame_width/3);
        TTDRGE.papplet().rect(-0.35f*frame_length, +frame_width/2, frame_width/3, frame_width/3);
        TTDRGE.papplet().rect(0.35f*frame_length, -frame_width/2, frame_width/3, frame_width/3);
        TTDRGE.papplet().rect(0.35f*frame_length, +frame_width/2, frame_width/3, frame_width/3);
        // Frame
        TTDRGE.papplet().fill(r, g, b);
        TTDRGE.papplet().rectMode(PConstants.CENTER);
        TTDRGE.papplet().rect(0, 0, frame_length, frame_width);
        TTDRGE.papplet().pop();
    }

}
