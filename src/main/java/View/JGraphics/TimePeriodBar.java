package View.JGraphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class TimePeriodBar extends RectBound{
    public static int baseH = 16;
    public static int halfBaseH = baseH/2;
    public static int hoveredHeight = 12;
    public static int spacing = 30;
    
    private int i;
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
        if(!isSingleDate){
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
