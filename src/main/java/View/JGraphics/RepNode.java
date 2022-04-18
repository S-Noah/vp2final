package View.JGraphics;

import java.awt.Color;
import java.awt.Graphics;
import Model.Rep;
import View.TimePanel;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RepNode extends RectBound implements Drawable{
    private boolean isSingleDate;
    
    private String name;
    private Rep rep;
    private TimePoint start;
    private TimePoint end;
    private TimePeriodBar bar;
    private TimePanel.Mode mode;
    private Color color = Color.ORANGE;
    
    public RepNode(Rep r, LocalDate minDate, TimePanel.Mode mode, int timepointY, int space){
        super();
        rep = r;
        
        this.mode = mode;
        this.name = rep.getName();
        this.isSingleDate = rep.getDateCreated().equals(rep.getDateLastPushed());
        if(isSingleDate){
            this.start = new TimePoint(r.getDateCreated(), minDate, this.mode, space, timepointY, Color.ORANGE);
            this.end = new TimePoint(r.getDateLastPushed(), minDate, this.mode, space, timepointY, Color.ORANGE);
        }
        else{
            this.start = new TimePoint(r.getDateCreated(), minDate, this.mode, space, timepointY, Color.GREEN);
            this.end = new TimePoint(r.getDateLastPushed(), minDate, this.mode, space, timepointY, Color.RED);
        }
        this.bar = new TimePeriodBar(start, end);
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
        //return start.InXRange(x1, x2) || end.InXRange(x1, x2) || 
        return start.getX() <= x2 && end.getX() >= x1;
    } 
    public void zoomUpdate(int space){
        start.zoomUpdate(space);
        end.zoomUpdate(space);
        bar.zoomUpdate();
    }
    public void draw(Graphics g, int minX, int tickH){
        System.out.println(start.getDate() + " - " + end.getDate());
        start.draw(g, minX, tickH);
        end.draw(g, minX, tickH);
        bar.draw(g, minX, name, isSingleDate);
        g.setColor(Color.DARK_GRAY);
    }
    public boolean touchingMouse(int mx, int my){
        return mx <= (x + w) && mx >= x && my <= (y + h) && my >= y;
    }

    public boolean isSingleDate() {
        return isSingleDate;
    }
    
    public void setI(int i){
        this.bar.setI(i);
    }
}