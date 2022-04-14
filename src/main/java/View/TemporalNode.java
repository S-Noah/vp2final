/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.Color;
import java.awt.Graphics;
import java.time.LocalDateTime;

/**
 *
 * @author NoahS
 */
public class TemporalNode extends RectBound{
    private static StringBuilder sb = new StringBuilder();
    
    private int halfW, halfH, quarterW;
    private int i;
    private boolean isBottomNode;
    private String name;
    private LocalDateTime date;
    private TimePanel.TimeType timeType;
    
    
    public TemporalNode(int i, int y, int diam, int space, boolean isBottomNode, LocalDateTime minDate, TimePanel.TimeType timeType){
        super((i + 1) * space, y, diam, diam);
        
        this.i = i;
        this.isBottomNode = isBottomNode;
        this.timeType = timeType;
        
        halfW = w/2;
        halfH = h/2;
        quarterW = halfW/2;
        
        this.y -= halfH;
        
        sb.setLength(0);
        if(timeType == TimePanel.TimeType.MONTHS){
            this.date = minDate.plusMonths(i);
            sb.append(date.getYear());
            sb.append("/");
            sb.append(date.getMonthValue());
        }
        else if(timeType == TimePanel.TimeType.YEARS){
            this.date = minDate.plusYears(i);
            sb.append(date.getYear());
        }
        else{
            this.date = minDate.plusDays(i);
            sb.append(date.getYear());
            sb.append("/");
            sb.append(date.getMonthValue());
            sb.append("/");
            sb.append(date.getDayOfMonth());
        }
        this.name = sb.toString();
    }
    public LocalDateTime getDate(){
        return date;
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
        if(isBottomNode){ 
            g.drawString(name, newX + quarterW - quarterTextW, y + 15);
        }
        else{
            g.drawString(name, newX + quarterW - quarterTextW, y - 5);
        }
    }
}
