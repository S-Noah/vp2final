/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.JGraphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author NoahS
 */
public class TimePeriodBar extends RectBound{
    public static int baseH = 16;
    public static int halfBaseH = baseH/2;
    public static int hoveredHeight = 12;
    public static int spacing = 30;
    
    private int i;
    private TimePoint start;
    private TimePoint end;
    private boolean isHovered;
    
    public TimePeriodBar(TimePoint start, TimePoint end){
        super(start.getX(), 0, end.getX() - start.getX(), baseH);
        this.start = start;
        this.end = end;
        this.isHovered = false;
    }
    
    public void zoomUpdate(){
        this.x = start.getX();
        this.w = end.getX() - x;
    };
    
    public void draw(Graphics g, int minX, String name, boolean isSingleDate){
        g.setColor(Color.WHITE);
        g.fillRect(x - minX, y, w, h);
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Courier", Font.BOLD, 14)); 
        int textW = g.getFontMetrics().stringWidth(name);
        int textH = g.getFontMetrics().getHeight();
        int halfTextW = textW / 2;
        int halfTextH = textH / 2;
        if(!isSingleDate){
           g.drawString(name, this.getX() - minX, this.getY() + textH - halfBaseH);
        } 
        /*
        if(start.isInRange()){
            g.fillRect(x, y, w, h);
        }
        else{
            g.fillRect(minX, y, w, h);
        }
        */
    }

    public void setI(int i) {
        this.i = i;
        this.y = start.getY() - (i * spacing);
    }
}
