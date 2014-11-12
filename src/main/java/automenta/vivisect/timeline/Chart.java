/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package automenta.vivisect.timeline;

/**
 * Modes: Line Line with vertical pole to base Stacked bar Stacked bar
 * normalized each step Scatter Spectral Event Bubble
 *
 */
public abstract class Chart {
    protected float height = 1.0f;
    protected float width = 1.0f;

    public Chart() {
        height = 1f;
    }

    public Chart size(float w , float h) {
        this.width = w;
        this.height = h;
        return this;
    }
    public Chart height(float h) {
        this.height = h;
        return this;
    }
    public Chart width(float w) {
        this.width = w;
        return this;
    }

    //called during NAR thread
    public void update(TimelineVis l, float timeScale, float yScale) {
    }

    //called during Swing thread
    public abstract void draw(TimelineVis l, float y, float timeScale, float yScale);

    public float getHeight() {
        return height;
    }
    public float getWidth() {
        return width;
    }    
    abstract public int getStart();
    abstract public int getEnd();
    
}
