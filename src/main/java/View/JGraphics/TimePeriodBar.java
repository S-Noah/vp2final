package View.JGraphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * This class is a clickable Rendering of a Rep. It spans between 2 TimePoints which are created useing the Rep's data.
 */
public class TimePeriodBar extends RectBound{
    // Static variables for size and spacing.
    public static int baseH = 16;
    public static int halfBaseH = baseH/2;
    public static int hoveredHeight = 12;
    public static int spacing = 30;
    
    // The bar takes 2 parent TimePoints to span across.
    private int i;  // This determines the height of the bar so they all fit.
    private TimePoint start;
    private TimePoint end;
    private boolean isHovered;
    private Color color;
    
    public TimePeriodBar(TimePoint start, TimePoint end, Color color){
        super(start.getX(), 0, end.getX() - start.getX(), baseH);
        this.start = start;
        this.end = end;
        this.color = color;
        this.isHovered = false;
    }
    
    public void zoomUpdate(){
        this.x = start.getX();
        this.w = end.getX() - x;
    };
    
    public void draw(Graphics g, int minX, String name, boolean isSingleDate){
        g.setColor(color);
        g.fillRect(x - minX, y, w, h);
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Courier", Font.BOLD, 14)); 
        int textW = g.getFontMetrics().stringWidth(name);
        int textH = g.getFontMetrics().getHeight();
        int halfTextW = textW / 2;
        int halfTextH = textH / 2;
        if(!isSingleDate){ // This draws the name on the start if it can, then the end if it can, and if not 0 because the Rep the start and end are hidden.
           if(start.isInRange()){
               g.drawString(name, this.getX() - minX, this.getY() + textH - halfBaseH);
           }
           else if(end.isInRange()){
               g.drawString(name, this.getX() + this.getW() - minX - textW, this.getY() + textH - halfBaseH);
           }
           else{
               g.drawString(name, 0 , this.getY() + textH - halfBaseH);
           }
        } 
    }

    public void setI(int i) {
        this.i = i;
        this.y = start.getY() - (i * spacing);
    }
    public boolean isTouching(int mx, int my){
        return mx <= (x + w) && mx >= x && my <= (y + h) && my >= y;
    }
}
