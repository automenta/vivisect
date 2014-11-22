/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.Vis;
import automenta.vivisect.gui.GButton;
import automenta.vivisect.gui.GKnob;
import automenta.vivisect.gui.GPanel;
import automenta.vivisect.gui.GSlider;
import automenta.vivisect.gui.GSlider2D;
import automenta.vivisect.gui.GStick;
import automenta.vivisect.gui.GTextField;
import automenta.vivisect.swing.NWindow;
import automenta.vivisect.swing.PCanvas;
import processing.core.PGraphics;

/**
 *
 * @author me
 */
public class GUIExample {

    static GButton b = null;
    
    public static void main(String[] args) {
        
        
        
        PCanvas p = new PCanvas( new Vis() {
            
            @Override
            public boolean draw(PGraphics g) {
                if (b!=null)
                    b.scale( 1.0f + (float)Math.random() * 0.25f, 1.0f + (float)Math.random() * 0.25f);
                return true;
            }
            
        });
        p.setZoom(0.55f);
        
        b = new GButton(p, 50, 200, 100, 25, "OK");
        new GSlider(p, -200,-200,100,20, 15);
        new GSlider2D(p, -100,-100,300,300);
        
        GKnob k = new GKnob(p, 150,200,50,50, 0.25f);
        
        
        new GTextField(p, -100, -200, 300, 50).setText("VIVISECT");
        
        //new GWindow(p, "x", 10, 10, 100, 300, true, null);
        
        new NWindow( "x", p ).show(500, 500, true);
        
        GPanel panel = new GPanel(p, -300,-300,200,100);
        //panel.setRotation(0.573f);
        
        GStick g = new GStick(p,20,20,50,50);
        /*
        g.addEventHandler(new Object() {
            public void handleStickEvents(GStick g, GEvent e) {
                //System.out.println(g.getPositionAngle());
                
                //panel.setRotation( g.getPositionAngle() );
            }
        }, "handleStickEvents");*/
        panel.add(g);
                
        //panel.setCollapsed(true);
    }
}
