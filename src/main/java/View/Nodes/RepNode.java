/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Nodes;

import java.awt.Color;
import java.awt.Graphics;
import Model.Rep;
import View.TimePanel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author NoahS
 */
public class RepNode extends RectBound{
    
    private int halfW, halfH, quarterW;
    private String name;
    private Color color = Color.ORANGE;
    private Rep rep;
    private TimePoint start;
    private TimePoint end;
    private TimePanel.Mode mode;
    
    public RepNode(Rep r, int space, LocalDate minDate, TimePanel.Mode mode, int timepointY){
        super();
        rep = r;
        halfW = w/2;
        halfH = h/2;
        quarterW = halfW / 2;
        this.mode = mode;
        this.name = rep.getName(); 
        this.start = new TimePoint(r.getDateCreated(), minDate, this.mode, space, timepointY);
        //this.end = new TimePoint(r.getDateLastPushed(), minDate, this.mode, space, timepointY);
    }
    
    public Rep getRep(){
        return rep;
    }

    public String getName() {
        return name;
    }
    public boolean inDateRange(){
        return false;
    }
    public boolean InXRange(int x1, int x2){
        return (x + halfW >= x1 && x - halfW <= x2);
    }
    public void draw(Graphics g, int minX){
        /*
        int quarterTextW = g.getFontMetrics().stringWidth(name) / 4;
        int newX = x - minX;
        g.setColor(color);
        g.fillOval(newX, y, w, h);
        g.setColor(Color.WHITE);
        g.drawString(name, newX + quarterW - quarterTextW, y - 5);
        */
        start.draw(g, minX);
    }
    public boolean touchingMouse(int mx, int my){
        return mx <= (x + w) && mx >= x && my <= (y + h) && my >= y;
    }
}
