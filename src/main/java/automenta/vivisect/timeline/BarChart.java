package automenta.vivisect.timeline;

import automenta.vivisect.TreeMLData;


public class BarChart extends LineChart {

    float barWidth = 0.9f;

    public BarChart(TreeMLData t) {
        super(t);
    }


    @Override
    protected void drawData(Timeline2DCanvas l, float timeScale, float yScale1, float y) {
        int ccolor = 0;
        TreeMLData chart = sensors.get(0);
        ccolor = chart.getColor().getRGB();
        l.noStroke();
        for (int t = l.cycleStart; t < l.cycleEnd; t++) {
            float x = t * timeScale;
            float v = (float)chart.getData(t);
            
            if (Float.isNaN(v)) {
                continue;
            }
            
            float p = (max == min) ? 0 : (float) ((v - min) / (max - min));
            float px = x;
            float h = p * yScale1;
            float py = y + yScale1 - h;
            l.fill(ccolor, 255f * (0.5f + 0.5f * p));
            l.rect(px, py, timeScale * barWidth, h);
        }
    }
}
