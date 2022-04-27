package View.JGraphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.time.LocalDate;

public class TemporalNode extends RectBound implements Drawable{
    private static int diam = 4;
    private static int halfDiam = 2;
   
    //private LocalDate date;
    //private TimePanel.Mode mode;
    private int i;
    private boolean isBottomNode;
    private String name;
    
    public TemporalNode(int i, int y, int space, boolean isBottomNode, String name){
        super((i + 1) * space, y, diam, diam);
        this.i = i;
        this.isBottomNode = isBottomNode;
        //this.mode = mode;
        
        this.y -= halfDiam;
        
        this.name = name;
    }
//    public LocalDate getDate(){
//        return date;
//    }
    public void zoomUpdate(int space){
        this.x = (i + 1) * space;
    }
    public boolean InXRange(int x1, int x2){
        return (x + halfDiam >= x1 && x - halfDiam <= x2);
    }
    public void draw(Graphics g, int minX, int tickH){
        g.setFont(new Font("Courier", Font.PLAIN, 14)); 
        int textW = g.getFontMetrics().stringWidth(name);
        int textH = g.getFontMetrics().getHeight();
        int halfTextW = textW / 2;
        int halfTextH = textH / 2;
        int newX = x - minX - halfDiam;

        //g.setColor(Color.DARK_GRAY);
        g.setColor(Color.WHITE);
        g.fillRect(newX + 1, y + halfDiam, 2, tickH);
        
        g.setColor(Color.BLACK);
        g.fillOval(newX, y, w, h);
        g.fillOval(newX, y + tickH, w, h);
        
        g.setColor(Color.WHITE);
        if(isBottomNode){ 
            g.drawString(name, newX - halfTextW, y + tickH + textH);
        }
        else{
           g.drawString(name, newX - halfTextW, y - halfTextH);
        }
    }
}
