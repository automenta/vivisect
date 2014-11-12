/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package automenta.vivisect.timeline;

import automenta.vivisect.TreeMLData;



/**
 *
 * @author me
 */
public class StackedPercentageChart extends LineChart {
    float barWidth = 0.9f;
    float barHeight = 0.9f;

    public StackedPercentageChart(TreeMLData... series) {
        super(series);
    }


    @Override
    protected void updateRange(TimelineVis l) {
        super.updateRange(l);
        min = 0;
        max = 1.0f;
    }

    @Override
    protected void drawData(TimelineVis l, float timeScale, float yScale, float y) {
        l.g.noStroke();
        for (int t = l.cycleStart; t < l.cycleEnd; t++) {
            float total = 0;
            for (TreeMLData chart : sensors) {
                float v = (float)chart.getData(t);
                if (Float.isNaN(v)) {
                    continue;
                }
                total += v;
            }
            if (total == 0) {
                continue;
            }
            float sy = y;
            float gap = yScale * (1.0f - barHeight) / sensors.size();
            for (TreeMLData chart : sensors) {
                int ccolor = chart.getColor().getRGB();
                float lx = 0;
                float ly = 0;
                l.g.strokeWeight(1f);
                l.g.fill(255f);
                float x = t * timeScale;
                float v = (float)chart.getData(t);
                if (Float.isNaN(v)) {
                    continue;
                }
                float p = v / total;
                float px = x;
                float h = p * yScale;
                l.g.fill(ccolor, 255f * (0.5f + 0.5f * p));
                l.g.rect(px, sy + gap / 2, timeScale * barWidth, h - gap / 2);
                sy += h;
            }
        }
    }
    
}
