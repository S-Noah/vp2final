package View.JGraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 * This class simply attached onto a Temporal Node and acts as a flag for a rep, Whether it be a start, end, or Excluded.
 * @author NoahS
 */
public class TimePoint extends RectBound implements Drawable{
    // Static Dimension Fields.
    private static int diam = 6;
    private static int halfDiam = 3;
    
    private Color color;
    private int space;
    private boolean inRange;
    private TemporalNode dateNode;
    
    public TimePoint(TemporalNode dateNode, int y, Color color){
        super(diam, diam);
        this.dateNode = dateNode;
        
        this.y = y - halfDiam;
        this.color = color;
        this.inRange = false;
        
        this.update();
    }
    
    public boolean InXRange(int x1, int x2){
        this.inRange = (x + halfDiam >= x1 && x - halfDiam <= x2);
        return inRange;
    }
    
    public void update(){
        this.x = dateNode.getX() - halfDiam;
    }
    public void zoomUpdate(int space){
        this.space = space;
        this.update();
    }
    
    public void setDateNode(TemporalNode dateNode){
        this.dateNode = dateNode;
        this.update();
    }
    
    public void draw(Graphics g, int minX, int barHeight){
        int newX = x - minX;
        g.setColor(color);
        g.fillOval(newX, y, w, h);
    }

    public boolean isInRange() {
        return inRange;
    }
    
}
