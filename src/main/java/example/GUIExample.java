/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.Vis;
import automenta.vivisect.gui.GButton;
import automenta.vivisect.gui.GKnob;
import automenta.vivisect.gui.GSlider;
import automenta.vivisect.gui.GSlider2D;
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
        p.setZoom(0.25f);
        
        b = new GButton(p, 50, 400, 100, 25, "OK");
        new GSlider(p, -400,-400,100,20, 15);
        new GSlider2D(p, -300,-300,600,600);
        
        GKnob k = new GKnob(p, 150,400,50,50, 0.25f);
        
        
        new GTextField(p, -300, -400, 300, 50);
        
        //new GWindow(p, "x", 10, 10, 100, 300, true, null);
        
        new NWindow( "x", p ).show(500, 500, true);
    }
}
