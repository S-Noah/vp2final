/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author NoahS
 */
public class DayNode extends RectBound{
    int halfW, halfH, quarterW;
    String index;
    int i;
    
    public DayNode(int i, int x, int y, int w, int h){
        super(x, y, w, h);
        halfW = w/2;
        halfH = h/2;
        quarterW = halfW/2;
        this.i = i;
        this.index = String.valueOf(i);
    }
    public void zoomUpdate(int dayWidth){
        this.x = i * dayWidth;
    }
    public boolean InXRange(int x1, int x2){
        return (x + halfW >= x1 && x - halfW <= x2);
    }
    public void draw(Graphics g, int minX){
        int quarterTextW = g.getFontMetrics().stringWidth(index) / 4;
        int newX = x - minX;
        g.setColor(Color.WHITE);
        g.fillOval(newX, y, w, h);
        g.setColor(Color.BLACK);
        g.drawString(index, newX + quarterW - quarterTextW, y - 5);
    }
}
