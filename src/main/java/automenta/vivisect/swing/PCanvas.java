/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.vivisect.swing;

import automenta.vivisect.Vis;
import java.util.ArrayList;
import processing.core.PApplet;
import static processing.core.PConstants.DOWN;
import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;
import static processing.core.PConstants.UP;
import processing.core.PGraphics;
import processing.event.MouseEvent;

/**
 *
 * @author me
 */
public class PCanvas extends PApplet {

    int mouseScroll = 0;

    Hnav hnav = new Hnav();
    Hsim hsim = new Hsim();

    float zoom = 0.1f;
    float scale = 1f;
    float selection_distance = 10;
    float FrameRate = 25f;    


    boolean drawn = false;
    float motionBlur = 0.0f;
    private final Vis vis;

    
    public PCanvas(Vis vis) {
        super();
        init();
        this.vis = vis;

    }
 
    @Override
    protected void resizeRenderer(int newWidth, int newHeight) {
        super.resizeRenderer(newWidth, newHeight);
        drawn = false;
    }
    

    public void mouseScrolled() {
        hnav.mouseScrolled();
    }

    @Override
    public void keyPressed() {
        hnav.keyPressed();
    }

    @Override
    public void mouseMoved() {
    }

    @Override
    public void mouseReleased() {
        hnav.mouseReleased();
        hsim.mouseReleased();
    }

    @Override
    public void mouseDragged() {
        hnav.mouseDragged();
        hsim.mouseDragged();
    }

    @Override
    public void mousePressed() {
        hnav.mousePressed();
        hsim.mousePressed();
    }

    @Override
    public void draw() {
        hnav.Transform();

        if (drawn) {
            return;
        }

        drawn = false;

        if (motionBlur > 0) {
            fill(0, 0, 0, 255f * (1.0f - motionBlur));
            rect(0, 0, getWidth(), getHeight());
        } else {
            background(0, 0, 0, 0.001f);
        }

        vis.draw((PGraphics) g);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        super.mouseWheel(event);
        mouseScroll = -event.getCount();
        mouseScrolled();
    }

    
    @Override
    public void setup() {
        
        //size(500,500,P3D);

        frameRate(FrameRate);

        if (isGL()) {
            textFont(createDefaultFont(16));
            smooth();
            System.out.println("Processing.org enabled OpenGL");
        }

        
         
    }
    
    public void setFrameRate(float frameRate) {
        this.frameRate = frameRate;
        frameRate(frameRate);
    }

    public float getFrameRate() {
        return frameRate;
    }
    
    
    


    class Hnav {

        private float savepx = 0;
        private float savepy = 0;
        private int selID = 0;
        
        private float difx = 0;
        private float dify = 0;
        private int lastscr = 0;
        private boolean EnableZooming = true;
        private float scrollcamspeed = 1.1f;

        float MouseToWorldCoordX(int x) {
            return 1 / zoom * (x - difx - width / 2);
        }

        float MouseToWorldCoordY(int y) {
            return 1 / zoom * (y - dify - height / 2);
        }
        private boolean md = false;

        void mousePressed() {
            md = true;
            if (mouseButton == RIGHT) {
                savepx = mouseX;
                savepy = mouseY;
            }
            drawn = false;
        }

        void mouseReleased() {
            md = false;
        }

        void mouseDragged() {
            if (mouseButton == RIGHT) {
                difx += (mouseX - savepx);
                dify += (mouseY - savepy);
                savepx = mouseX;
                savepy = mouseY;
            }
            drawn = false;
        }
        private float camspeed = 20.0f;
        private float scrollcammult = 0.92f;
        boolean keyToo = true;

        void keyPressed() {
            if ((keyToo && key == 'w') || keyCode == UP) {
                dify += (camspeed);
            }
            if ((keyToo && key == 's') || keyCode == DOWN) {
                dify += (-camspeed);
            }
            if ((keyToo && key == 'a') || keyCode == LEFT) {
                difx += (camspeed);
            }
            if ((keyToo && key == 'd') || keyCode == RIGHT) {
                difx += (-camspeed);
            }
            if (!EnableZooming) {
                return;
            }
            if (key == '-' || key == '#') {
                float zoomBefore = zoom;
                zoom *= scrollcammult;
                difx = (difx) * (zoom / zoomBefore);
                dify = (dify) * (zoom / zoomBefore);
            }
            if (key == '+') {
                float zoomBefore = zoom;
                zoom /= scrollcammult;
                difx = (difx) * (zoom / zoomBefore);
                dify = (dify) * (zoom / zoomBefore);
            }
            drawn = false;
        }

        void Init() {
            difx = -width / 2;
            dify = -height / 2;
        }

        void mouseScrolled() {
            if (!EnableZooming) {
                return;
            }
            float zoomBefore = zoom;
            if (mouseScroll > 0) {
                zoom *= scrollcamspeed;
            } else {
                zoom /= scrollcamspeed;
            }
            difx = (difx) * (zoom / zoomBefore);
            dify = (dify) * (zoom / zoomBefore);
            drawn = false;
        }

        void Transform() {
            translate(difx + 0.5f * width, dify + 0.5f * height);
            scale(zoom, zoom);
        }
    }

    ////Object management - dragging etc.
    class Hsim {

        ArrayList obj = new ArrayList();

        void Init() {
            smooth();
        }

        void mousePressed() {
            if (mouseButton == LEFT) {
                checkSelect();
            }
        }
        boolean dragged = false;

        void mouseDragged() {
            if (mouseButton == LEFT) {
                dragged = true;
                dragElems();
            }
        }

        void mouseReleased() {
            dragged = false;
            //selected = null;
        }

        void dragElems() {
            /*
             if (dragged && selected != null) {
             selected.x = hnav.MouseToWorldCoordX(mouseX);
             selected.y = hnav.MouseToWorldCoordY(mouseY);
             hsim_ElemDragged(selected);
             }
             */
        }

        void checkSelect() {
            /*
             double selection_distanceSq = selection_distance*selection_distance;
             if (selected == null) {
             for (int i = 0; i < obj.size(); i++) {
             Vertex oi = (Vertex) obj.get(i);
             float dx = oi.x - hnav.MouseToWorldCoordX(mouseX);
             float dy = oi.y - hnav.MouseToWorldCoordY(mouseY);
             float distanceSq = (dx * dx + dy * dy);
             if (distanceSq < (selection_distanceSq)) {
             selected = oi;
             hsim_ElemClicked(oi);
             return;
             }
             }
             }
             */
        }
    }
    
}
