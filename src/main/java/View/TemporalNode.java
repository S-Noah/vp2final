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
public class TemporalNode extends RectBound{
    int halfW, halfH, quarterW;
    int i;
    boolean isBottom;
    String name;
    
    public TemporalNode(String name, int i, boolean isBottom, int x, int y, int w, int h){
        super(x, y, w, h);
        this.name = name;
        this.i = i;
        this.isBottom = isBottom;
        
        halfW = w/2;
        halfH = h/2;
        quarterW = halfW/2;
    }
    public void zoomUpdate(int space){
        this.x = (i + 1) * space;
    }
    public boolean InXRange(int x1, int x2){
        return (x + halfW >= x1 && x - halfW <= x2);
    }
    public void draw(Graphics g, int minX){
        int quarterTextW = g.getFontMetrics().stringWidth(name) / 4;
        int newX = x - minX;
        g.setColor(Color.WHITE);
        g.fillOval(newX, y, w, h);
        g.setColor(Color.BLACK);
        if(isBottom){
            g.drawString(name, newX + quarterW - quarterTextW, y + 15);
        }
        else{
            g.drawString(name, newX + quarterW - quarterTextW, y - 5);
        }
    }
}
