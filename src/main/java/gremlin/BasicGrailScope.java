/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gremlin;

import syncleus.gremlann.util.JGraphTGraph;
import automenta.vivisect.graph.AnimatingGraphVis;
import automenta.vivisect.graph.GraphDisplay;
import com.google.common.base.Predicate;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import example.SimpleDirectedGraph.DefaultDisplay;
import org.jgrapht.graph.EdgeReversedGraph;

/**
 *
 * @author me
 */
public class BasicGrailScope<V extends Vertex, E extends Edge> extends AnimatingGraphVis<V,E> {

    int layoutIterations = 75;
    int layoutNum = 0;
    
    public BasicGrailScope(Graph graph) {
        this(graph, new DefaultDisplay());

    }
    public BasicGrailScope(Graph graph, GraphDisplay<V,E> display) {
        //TODO edgereversedgraph shouldnt be necessary
        super( new JGraphTGraph(graph) , display);
    }
    
    public BasicGrailScope(Graph graph, Predicate<Vertex> vertexFilter, Predicate<Edge> edgeFilter, GraphDisplay<V,E> display) {
        //TODO edgereversedgraph shouldnt be necessary
        super(new JGraphTGraph(graph, vertexFilter, edgeFilter), display);
    }

    
//    @Override
//    protected void postUpdate(Graph graph) {
//        if (layoutNum++ >= layoutIterations)
//            return;
//        super.postUpdate(); //To change body of generated methods, choose Tools | Templates.
//    }

       
    
}
