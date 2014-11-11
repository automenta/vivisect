/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.vivisect;

import automenta.vivisect.swing.Swing;
import java.awt.Color;

/**
 *
 * @author me
 */
public class Video {
 

  public static final int color(int x, int y, int z, int a) {
    
      if (a > 255) a = 255; else if (a < 0) a = 0;
      if (x > 255) x = 255; else if (x < 0) x = 0;
      if (y > 255) y = 255; else if (y < 0) y = 0;
      if (z > 255) z = 255; else if (z < 0) z = 0;

      return (a << 24) | (x << 16) | (y << 8) | z;
  }


  public static final int color(float x, float y, float z, float a) {
      if (a > 255) a = 255; else if (a < 0) a = 0;
      if (x > 255) x = 255; else if (x < 0) x = 0;
      if (y > 255) y = 255; else if (y < 0) y = 0;
      if (z > 255) z = 255; else if (z < 0) z = 0;

      return ((int)a << 24) | ((int)x << 16) | ((int)y << 8) | (int)z;
  }
  
    public static int getColor(final String s, final float alpha) {
        double hue = (((double)s.hashCode()) / Integer.MAX_VALUE);
        return Swing.getColor(Color.getHSBColor((float)hue,0.8f,0.9f), alpha).getRGB();        
    }
    

    public static int getColor(final Class c) {            
        double hue = (((double)c.hashCode()) / Integer.MAX_VALUE);
        return Color.getHSBColor((float)hue,0.8f,0.9f).getRGB();                
    }
    
    public static int getColor(final String s) {            
        double hue = (((double)s.hashCode()) / Integer.MAX_VALUE);
        return Color.getHSBColor((float)hue,0.8f,0.9f).getRGB();        
    }
  
}
