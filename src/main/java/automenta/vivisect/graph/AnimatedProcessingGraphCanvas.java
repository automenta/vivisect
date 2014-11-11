package automenta.vivisect.graph;

import automenta.vivisect.dimensionalize.FastOrganicLayout;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedMultigraph;

/**
 *
 * @author me
 */


public class AnimatedProcessingGraphCanvas<V,E> extends ProcessingGraphCanvas<V,E> {
    DirectedMultigraph<V, E> graph;    
    private final FastOrganicLayout layout;
    private boolean vertexUpdateAlways;

    public AnimatedProcessingGraphCanvas(DirectedMultigraph<V,E> graph, GraphDisplay<V,E> display) {
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
        
        scale = 10f;
        layout.setInitialTemp(10f);
        layout.setMinDistanceLimit(75f);
        layout.setMaxDistanceLimit(200f);
        
        layout.setMaxIterations(5);
        
        
        layout.execute(this);
        
    }

    @Override
    public void draw() {
        updateGraph();

        drawn = false;
        
        super.draw(); //To change body of generated methods, choose Tools | Templates.
    }


    
    @Override
    protected boolean hasUpdate() {
        
        return false;
    }

    
    
}
