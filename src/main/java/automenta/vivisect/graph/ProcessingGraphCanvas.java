package automenta.vivisect.graph;

import static automenta.vivisect.graph.GraphDisplay.Shape.Ellipse;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jgrapht.Graph;
import processing.core.PApplet;
import static processing.core.PApplet.radians;
import static processing.core.PConstants.DOWN;
import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.PROJECT;
import static processing.core.PConstants.RIGHT;
import static processing.core.PConstants.SQUARE;
import static processing.core.PConstants.UP;
import processing.event.MouseEvent;

/**
 *
 * @author me
 */
abstract public class ProcessingGraphCanvas<V, E> extends PApplet {

    int mouseScroll = 0;

    Hnav hnav = new Hnav();
    Hsim hsim = new Hsim();

    float zoom = 0.1f;
    float scale = 1f;
    float selection_distance = 10;
    float FrameRate = 25f;    

    static final float vertexTargetThreshold = 4;

    boolean drawn = false;

    int maxNodesWithLabels = 5000;
    int maxNodes = 5000;
    int maxEdgesWithArrows = 10000;
    int maxEdges = 10000;

    float nodeSpeed = 0.1f;

    float sx = 800;
    float sy = 800;

    boolean autofetch = true;
    boolean updateNext;
    float arrowHeadScale = 1f/16f;

    

    Map<V, VertexDisplay> vertices = new HashMap();
    Set<V> deadVertices = new HashSet();
    Map<Object, Integer> edgeColors = new HashMap(16);

    Graph<V,E> currentGraph;
    boolean showSyntax;

    //bounds of last positioned vertices
    float minX = 0, minY = 0, maxX = 0, maxY = 0;
    float motionBlur = 0.0f;
    
    private GraphDisplay<V,E> display;
    private boolean vertexUpdateAlways;

    public ProcessingGraphCanvas(GraphDisplay display) {
        super();
        this.display = display;
        init();
        
    }

    public void setDisplay(GraphDisplay display) {
        this.display = display;
    }

    public GraphDisplay getDisplay() {
        return display;
    }
    

    public VertexDisplay getVertexDisplay(V v) {
        return vertices.get(v);
    }
    
    public static class VertexDisplay<V,E> {

        public final V vertex;
        public float x, y, tx, ty;        
        public float radius;
        public float stroke;
        public String label;
        public int color, textColor, strokeColor;
        public Set<E> edges;
        public final ProcessingGraphCanvas<V,E> canvas;

        public VertexDisplay(ProcessingGraphCanvas<V,E> canvas, V o) {
            this.vertex = o;
            this.canvas = canvas;
            
            x = y = 0;
            tx = x;
            ty = y;
            stroke = 0;            
            strokeColor = 0;

            update(o);
        }

        @Override
        public int hashCode() {
            return vertex.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return vertex.equals(obj);
        }

        public boolean draw(PApplet p, boolean text, float nodeSpeed, float scale) {
            boolean needsUpdate = update(nodeSpeed);

            //System.out.println(radius + " " + color + " " + label + " " + x + " " + y);
            
            if (stroke > 0) {
                p.stroke(strokeColor);
                p.strokeWeight(stroke*scale);
            }
            
            float r = radius * scale;
            if (r == 0)
                return needsUpdate;

            p.fill(color);//, alpha * 255 / 2);

            switch (canvas.display.getVertexShape(vertex)) {
                case Rectangle:                
                    p.rect(x*scale-r/2f, y*scale-r/2f, r, r);
                    break;
                case Ellipse:
                    p.ellipse(x*scale, y*scale, r, r);
                    break;
                
            }
            
            if (text && (label != null)) {
                p.fill(textColor); //, alpha * 255 * 0.75f);
                p.textSize(r / 2f);
                p.text(label, x*scale, y*scale);
            }

            if (stroke > 0) {                
                //reset stroke
                p.noStroke();
            }
            
            return needsUpdate;
        }

        protected boolean update(final float nodeSpeed) {
            if (this.edges == null) {
                if (canvas.currentGraph!=null)
                    this.edges = canvas.currentGraph.edgesOf(vertex);
            }
            x = (x * (1.0f - nodeSpeed) + tx * (nodeSpeed));
            y = (y * (1.0f - nodeSpeed) + ty * (nodeSpeed));

            if ((Math.abs(tx - x) + Math.abs(ty - y)) > vertexTargetThreshold) {
                //keep animating if any vertex hasnt reached its target
                return false;
            }
                   
            return true;
        }
        
        public Set<E> getEdges() {
            return edges;
        }

        public V getVertex() {
            return vertex;
        }
        
        private void update(final V o) {
            GraphDisplay<V, E> d = canvas.display;
            
            this.edges = null;
            this.textColor = d.getTextColor(o);
            this.radius = d.getVertexSize(o);
            this.color = d.getVertexColor(o);
            this.label = d.getVertexLabel(o);
            this.stroke = d.getVertexStroke(o);
            this.strokeColor = d.getVertexStrokeColor(o);
        }

        public void setPosition(final float x, final float y) {
            this.tx = x; this.ty = y;
        }

        public void movePosition(final float dx, final float dy) {
            this.tx += dx; this.ty += dy;
        }

        public float getX() { return tx; }
        public float getY() { return ty; }

        public float getRadius() {            
            return radius;
        }

    }

    @Override
    protected void resizeRenderer(int newWidth, int newHeight) {
        super.resizeRenderer(newWidth, newHeight);
        drawn = false;
    }

    public VertexDisplay updateVertex(final V o) {        
        
        deadVertices.remove(o);
        
        VertexDisplay v = vertices.get(o);      
        if (v != null) {
            v.update(o);
            return v;
        }
        
        v = new VertexDisplay(this, o);
        vertices.put(o, v);

        return v;
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

    public abstract Graph<V,E> getGraph();
    abstract protected boolean hasUpdate();
    
    /**
     * called from NAR update thread, not swing thread
     */
    public void updateGraph() {
        
            
        if (hasUpdate() || (updateNext) || display.updateNext()) {

            updateNext = false;

            synchronized (vertices) {
                deadVertices.clear();
                deadVertices.addAll(vertices.keySet());

                try {
                    currentGraph = getGraph();
                } catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }

                if (currentGraph == null)
                    return;
                
                for (final V v : currentGraph.vertexSet())
                   updateVertex(v);            
                

                for (final V v : deadVertices)
                    vertices.remove(v);
            }

            
            updateVertices();
            drawn = false;
        }
    }
    
    
    /** called after the graph has updated the current set of vertices; override to layout */
    protected void updateVertices() {
        
    }
    
    @Override
    public void draw() {

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

        //pushMatrix();
        hnav.Transform();
        
        
        //long start = System.nanoTime();
        {
            drawGraph();
        }
        /*long end = System.nanoTime();
        float time = end - start;
        if (currentGraph!=null)
            System.out.println(time + "ns for " + currentGraph.vertexSet().size() + "|" + currentGraph.edgeSet().size());
        */
        
        
        //popMatrix();        

    }

    
    public void resurrectVertex(V v) {
        deadVertices.remove(v);        
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

    public void drawGraph() {        
        
        if (currentGraph == null) {
            return;
        }

        synchronized (vertices) {
            //for speed:
            strokeCap(SQUARE);
            strokeJoin(PROJECT);

            int numEdges = currentGraph.edgeSet().size();
            if (numEdges < maxEdges) {
                for (final E edge : currentGraph.edgeSet()) {

                    final VertexDisplay elem1 = vertices.get(currentGraph.getEdgeSource(edge));
                    final VertexDisplay elem2 = vertices.get(currentGraph.getEdgeTarget(edge));
                    if ((elem1 == null) || (elem2 == null)) {
                        continue;
                    }

                    //TODO create EdgeDisplay class to cacahe these properties
                    stroke(display.getEdgeColor(edge));
                    strokeWeight(display.getEdgeThickness(edge, elem1, elem2));

                    float x1 = elem1.x*scale;
                    float y1 = elem1.y*scale;
                    float x2 = elem2.x*scale;
                    float y2 = elem2.y*scale;

                    if (numEdges < maxEdgesWithArrows) {
                        drawArrow(x1, y1, x2, y2);
                    } else {
                        drawLine(x1, y1, x2, y2);
                    }

                    //float cx = (x1 + x2) / 2.0f;
                    //float cy = (y1 + y2) / 2.0f;
                    //text(edge.toString(), cx, cy);
                }
            }

            noStroke();

            int numNodes = vertices.size();
            boolean text = numNodes < maxNodesWithLabels;
            boolean changed = false;
            if (numNodes < maxNodes) {
                for (final VertexDisplay d : vertices.values()) {
                    changed |= d.draw(this, text, nodeSpeed, scale);
                }
            }
            drawn = !changed;

        }
    }

    void drawArrowAngle(final float cx, final float cy, final float len, final float angle, float arrowHeadRadius) {
        pushMatrix();
        translate(cx, cy);
        rotate(radians(angle));
        line(0, 0, len, 0);
        line(len, 0, len - arrowHeadRadius, -arrowHeadRadius/2f);
        line(len, 0, len - arrowHeadRadius, arrowHeadRadius/2f);
        popMatrix();
    }

    
    void drawArrow(final float x1, final float y1, final float x2, final float y2) {
        float cx = (x1 + x2) / 2f;
        float cy = (y1 + y2) / 2f;
        float len = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        float a = (float) (Math.atan2(y2 - y1, x2 - x1) * 180.0 / Math.PI);

        drawArrowAngle(x1, y1, len, a, len * arrowHeadScale /* nodeSize/16f*/);
    }

    void drawLine(final float x1, final float y1, final float x2, final float y2) {
        line(x1, y1, x2, y2);
    }

    public void setFrameRate(float frameRate) {
        this.frameRate = frameRate;
        frameRate(frameRate);
    }

    public float getFrameRate() {
        return frameRate;
    }
    
    
    

    public void setUpdateNext() {
        updateNext = true;
        drawn = false;
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

    static class Edge {

        public final int from;
        public final int to;
        public final int alpha;

        public Edge(final int from, final int to, int alpha) {
            this.from = from;
            this.to = to;
            this.alpha = alpha;
        }

        public Edge(final int from, final int to, float alpha) {
            this(from, to, (int) (255.0 * alpha));
        }
    }
}
