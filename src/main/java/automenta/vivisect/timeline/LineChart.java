package automenta.vivisect.timeline;

import automenta.vivisect.TreeMLData;
import java.util.Arrays;
import java.util.List;

public class LineChart extends Chart {
    public final List<TreeMLData> sensors;
    float min;
    float max;
    boolean showVerticalLines = false;
    boolean showPoints = true;
    float lineThickness = 2.5f;
    float borderThickness = 0.5f;
    private int end;
    private int start;
    boolean overlayEnable = false;

  

    public LineChart(TreeMLData... series) {
        super();
        this.sensors = Arrays.asList(series);
    }

    public void setOverlayEnable(boolean overlayEnable) {
        this.overlayEnable = overlayEnable;
    }

    


    @Override
    public void draw(TimelineVis l, float y, float timeScale, float yScale) {
        yScale *= height;
        float screenyHi = l.g.screenY(l.cycleStart * timeScale, y);
        float screenyLo = l.g.screenY(l.cycleStart * timeScale, y + yScale);
        updateRange(l);
        l.g.stroke(127);
        l.g.strokeWeight(borderThickness);
        //bottom line
        l.g.line(0, y + yScale, width * (l.cycleEnd-l.cycleStart) * timeScale, y + yScale);
        //top line
        l.g.line(0, y, width * (l.cycleEnd-l.cycleStart) * timeScale, y);
        drawData(l, timeScale, yScale, y);
        
        if (overlayEnable) {
            drawOverlay(l, screenyLo, screenyHi);
        }
    }

    protected void updateRange(TimelineVis l) {
        min = Float.POSITIVE_INFINITY;
        max = Float.NEGATIVE_INFINITY;
        for (TreeMLData chart : sensors) {
            double[] mm = chart.getMinMax(l.cycleStart, l.cycleEnd);
            min = (float)Math.min(min,mm[0]);
            max = (float)Math.max(max,mm[1]);
        }
        
    }

    protected void drawOverlay(TimelineVis l, float screenyLo, float screenyHi) {
        //draw overlay
        l.g.pushMatrix();
        l.g.resetMatrix();
        l.g.textSize(15f);
        int dsy = (int) Math.abs(screenyLo - screenyHi);
        float dsyt = screenyHi + 0.15f * dsy;
        float ytspace = dsy * 0.75f / sensors.size() / 2;
        for (TreeMLData chart : sensors) {
            l.g.fill(chart.getColor().getRGB());
            dsyt += ytspace;
            l.g.text(chart.label, 0, dsyt);
            dsyt += ytspace;
        }
        l.g.textSize(11f);
        l.g.fill(200, 195f);
        
        //TODO number precision formatting
        l.g.text("" + ((double) min), 0, screenyLo - dsy / 10f);
        l.g.text("" + ((double) max), 0, screenyHi + dsy / 10f);
        l.g.popMatrix();
    }

    protected void drawData(TimelineVis l, float timeScale1, float yScale1, float y) {
        int ccolor = 0;
        for (TreeMLData chart : sensors) {
            ccolor = chart.getColor().getRGB();
            float lx = 0;
            float ly = 0;
            l.g.fill(255f);
            boolean firstPoint = false;
            int cs = l.cycleStart;
            for (int t = cs; t < l.cycleEnd; t++) {
                float x = (t-cs) * timeScale1;
                float v = (float)chart.getData(t);
                if (Float.isNaN(v)) {
                    continue;
                }
                
                float p = (float)((max == min) ? 0 : (double) ((v - min) / (max - min)));
                float px = width * x;
                float h = p * yScale1;
                float py = y + yScale1 - h;
                                
                if (firstPoint) {
                    l.g.strokeWeight(lineThickness);
                    if (showVerticalLines) {
                        l.g.stroke(ccolor, 127f);
                        l.g.line(px, py, px, py + h);
                    }
                    l.g.stroke(ccolor);

                    if (t != l.cycleStart) {
                        l.g.line(lx, ly, px, py);
                    }
                }
                
                lx = px;
                ly = py;
                
                firstPoint = true;
                
                if (showPoints) {
                    l.g.noStroke();
                    l.g.fill(ccolor);
                    float w = Math.min(timeScale1, yScale1) / 12f;
                    l.g.rect(px - w / 2f, py - w / 2f, w, w);
                }
            }
        }
    }

    @Override
    public int getStart() {
        start = Integer.MAX_VALUE;
        end = 0;
        for (TreeMLData s  : sensors) {
            int ss = s.getStart();
            int se = s.getEnd();
                    
            if (start > ss) start = ss;
            if (end < se) end = se;
        }
        return start;
    }

    @Override
    public int getEnd() {
        //call getStart() prior to this
        return end;
    }
    
    
}
