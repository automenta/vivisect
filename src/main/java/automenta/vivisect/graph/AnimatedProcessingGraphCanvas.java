package automenta.vivisect.graph;

import automenta.vivisect.dimensionalize.FastOrganicLayout;
import org.jgrapht.Graph;
import processing.core.PGraphics;

/**
 *
 * @author me
 */


public class AnimatedProcessingGraphCanvas<V,E> extends AbstractGraphVis<V,E> {
    Graph<V, E> graph;    
    private final FastOrganicLayout layout;
    private boolean vertexUpdateAlways;

    public AnimatedProcessingGraphCanvas(Graph<V,E> graph, GraphDisplay<V,E> display) {
        super(display);
        
        this.graph = graph;
        this.layout = new FastOrganicLayout();
        
        updateGraph();
        setUpdateNext();
    }

    
    @Override
    public Graph<V,E> getGraph() {
        return this.graph;
    }

    @Override
    protected void updateVertices() {     
        
        layout.setInitialTemp(3f);
        layout.setMinDistanceLimit(75f);
        layout.setMaxDistanceLimit(200f);
        
        layout.setMaxIterations(1);
        
        
        layout.execute(this);
        
    }

    @Override
    public boolean draw(PGraphics g) {
        updateGraph();
        
        return super.draw(g);
    }


    
    @Override
    protected boolean hasUpdate() {
        
        return false;
    }

    
    
}
