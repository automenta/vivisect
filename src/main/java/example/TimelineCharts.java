/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.TreeMLData;
import automenta.vivisect.swing.NWindow;
import automenta.vivisect.swing.PCanvas;
import automenta.vivisect.timeline.BarChart;
import automenta.vivisect.timeline.LineChart;
import automenta.vivisect.timeline.SpectrumChart;
import automenta.vivisect.timeline.StackedPercentageChart;
import automenta.vivisect.timeline.TimelineVis;
import java.awt.Color;
import java.util.UUID;

/**
 *
 * @author me
 */
public class TimelineCharts {
    
    public static class RandomTimeSeries extends TreeMLData {

        public RandomTimeSeries(int historySize, float range) {
            super(UUID.randomUUID().toString().substring(0,8), Color.getHSBColor((float)Math.random(), 0.85f, 0.85f), historySize);
            
            for (int i = 0; i < historySize; i++) {
                setData(i, (float)Math.random() * range);
            }
        }
        
    }
    
    public static void main(String[] args) {

        TreeMLData a = new RandomTimeSeries(1000, 0.5f);
        TreeMLData b = new RandomTimeSeries(1000, 0.9f);
        TreeMLData c = new RandomTimeSeries(1000, 0.6f);
        
        ArrayIndexOutOfBoundsException e;
        new NWindow("_", 
                new PCanvas(
                        new TimelineVis(
                            new StackedPercentageChart(a, b, c).height(4),
                            new LineChart(a, b).height(3f),
                            new BarChart(c).height(2.5f),
                            new SpectrumChart(c, 8).height(3f)
//
//            
//            new LineChart(t.getCharts("task.novel.add", "task.immediate_processed")).height(3),
//            new LineChart(t.getCharts("task.goal.process", "task.question.process", "task.judgment.process")).height(3),
//            new LineChart(t.getCharts("emotion.busy")).height(1),
//            new BarChart(new TreeMLData.FirstOrderDifferenceTimeSeries("d(concepts)", t.charts.get("concept.count"))),
            
                        ))).show(800, 800, true);
        
        
    }
}
