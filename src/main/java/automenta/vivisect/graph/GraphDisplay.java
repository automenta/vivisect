/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.vivisect.graph;

import automenta.vivisect.graph.ProcessingGraphCanvas.VertexDisplay;

/**
 *
 * @author me
 */
public interface GraphDisplay<V,E> {

    public boolean updateNext();

    public static enum Shape { Rectangle, Ellipse };

    
    public Shape getVertexShape(V v);
    public String getVertexLabel(final V v);
    
    /** return 0 to hide vertex */
    public float getVertexSize(final V v);
    
    public int getVertexColor(V o);
    public float getEdgeThickness(E edge, VertexDisplay source, VertexDisplay target);
    public int getEdgeColor(E e);
    public int getTextColor(V v);

}
