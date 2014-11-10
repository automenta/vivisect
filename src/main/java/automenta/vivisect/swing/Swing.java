/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.vivisect.swing;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author me
 */
public class Swing {
    
    static {
        System.setProperty("sun.java2d.opengl","True");        
    }

    
    public final static float hashFloat(final int h) {
        //return ((float)h) / (((float)Integer.MAX_VALUE) - ((float)Integer.MIN_VALUE));
        return ((float)(h%256)) / (256.0f);
    }

    public final static Color getColor(final String s, final float saturation, final float brightness) {             return getColor(s.hashCode(), saturation, brightness);
    }
    public final static Color getColor(int hashCode, float saturation, float brightness) {
        float hue = hashFloat(hashCode);
        return Color.getHSBColor(hue, saturation, brightness);        
    }
    public final static Color getColor(final Color c, float alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(255.0 * alpha));
    }
    public final static Color getColor(final String s, float sat, float bright, float alpha) {
        return getColor(getColor(s, sat, bright), alpha);
    }
    
//    //NOT WORKING YET
//    public static Color getColor(final String s, float saturation, float brightness, float alpha) {            
//        float hue = (((float)s.hashCode()) / Integer.MAX_VALUE);
//        int a = (int)(255.0*alpha);
//        
//        int c = Color.HSBtoRGB(hue, saturation, brightness);
//        c |= (a << 24);
//        
//        return new Color(c, true);
//    }
    

    
    public static Font monofont;
    static {
        monofont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        /*
        try {
            //monofont = Font.createFont(Font.TRUETYPE_FONT, NARSwing.class.getResourceAsStream("Inconsolata-Regular.ttf"));
            
            
        } catch (FontFormatException ex) {
            Logger.getLogger(NARSwing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NARSwing.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
        
    
    public static Font FontAwesome;
    static {        
        try {
            FontAwesome = Font.createFont(Font.TRUETYPE_FONT, Swing.class.getResourceAsStream("FontAwesome.ttf")).deriveFont(Font.PLAIN, 14);
        } catch (Exception ex) {         
            ex.printStackTrace();
        }

    }

    public static Font fontMono(float size) {
        return monofont.deriveFont(size);
    }    

    


    
    
}
