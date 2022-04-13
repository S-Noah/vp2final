/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.Color;
import java.awt.Graphics;
import Github.RepRequest;
import Model.Rep;

/**
 *
 * @author NoahS
 */
public class RepNode extends RectBound{
    private int halfW, halfH, quarterW;
    private String name;
    private Color color = Color.ORANGE;
    private Rep rep;
    
    public RepNode(Rep r, int x, int y, int w, int h){
        super(x, y, w, h);
        rep = r;
        
        halfW = w/2;
        halfH = h/2;
        quarterW = halfW / 2;
        
        this.name = rep.getName(); 
    }
    
    public Rep getRep(){
        return rep;
    }

    public String getName() {
        return name;
    }
    
    public boolean InXRange(int x1, int x2){
        return (x + halfW >= x1 && x - halfW <= x2);
    }
    public void draw(Graphics g, int minX){
        int quarterTextW = g.getFontMetrics().stringWidth(name) / 4;
        int newX = x - minX;
        g.setColor(color);
        g.fillRect(newX, y, 50, 50);
        g.setColor(Color.WHITE);
        g.drawString(name, newX + quarterW - quarterTextW, y - 5);
    }
    public boolean touchingMouse(int mx, int my){
        return mx <= (x + w) && mx >= x && my <= (y + h) && my >= y;
    }
}
