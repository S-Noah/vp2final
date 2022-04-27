package View.JGraphics;

import java.awt.Color;
import java.awt.Graphics;
import Model.Rep;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class RepNode extends RectBound implements Drawable{
    public static Random random = new Random();
    public static Color generateRepColor(){
        Color c = Color.getHSBColor(random.nextFloat(), 0.5f, 0.9f);
        return c;
    }
    private boolean isSingleDate;
    
    private String name;
    private Rep rep;
    private LocalDate startingDate;
    private LocalDate endingDate;
    private TimePoint start;
    private TimePoint end;
    private TimePeriodBar bar;
    private TimePanel.Mode mode;
    private Color color = Color.ORANGE;
    
    public RepNode(Rep r, TemporalNode startingDateNode, TemporalNode endingDateNode, TimePanel.Mode mode, int timepointY, int space){
        super();
        rep = r;
        
        this.mode = mode;
        this.name = rep.getName();
        this.startingDate = rep.getDateCreated();
        this.endingDate = rep.getDateLastPushed();
        this.isSingleDate = startingDate.equals(endingDate);
        
        if(isSingleDate){
            this.start = new TimePoint(startingDateNode, timepointY, Color.ORANGE);
            this.end = new TimePoint(endingDateNode, timepointY, Color.ORANGE);
        }
        else{
            this.start = new TimePoint(startingDateNode, timepointY, Color.GREEN);
            this.end = new TimePoint(endingDateNode, timepointY, Color.RED);
         
        }
//        if(isSingleDate){
//            this.start = new TimePoint(r.getDateCreated(), minDate, this.mode, space, timepointY, Color.ORANGE);
//            this.end = new TimePoint(r.getDateLastPushed(), minDate, this.mode, space, timepointY, Color.ORANGE);
//        }
//        else{
//            System.out.println(name);
//            this.start = new TimePoint(r.getDateCreated(), minDate, this.mode, space, timepointY, Color.GREEN);
//            this.end = new TimePoint(r.getDateLastPushed(), minDate, this.mode, space, timepointY, Color.RED);
//            start.debug();
//            end.debug();
//            System.out.println();
//        }
        this.bar = new TimePeriodBar(start, end, generateRepColor());
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
        start.InXRange(x1, x2);
        end.InXRange(x1, x2); 
        return start.getX() <= x2 && end.getX() >= x1;
    } 
    public void zoomUpdate(int space){
        start.zoomUpdate(space);
        end.zoomUpdate(space);
        bar.zoomUpdate();
    }
    public void changeTimeMode(TemporalNode starting, TemporalNode ending, TimePanel.Mode mode){
        this.mode = mode;
        start.setDateNode(starting);
        end.setDateNode(ending);;
        bar.zoomUpdate();
    }
    public void draw(Graphics g, int minX, int tickH){
//        if(mode == TimePanel.Mode.MONTHS){
//            System.out.println(name + ": " + start.getNumMonths());
//        }
//        else if(mode == TimePanel.Mode.YEARS){
//            System.out.println(name + ": " + start.getNumYears());
//        }
//        else{
//            System.out.println(name + ": " + start.getNumDays());
//        }
        start.draw(g, minX, tickH);
        if(!isSingleDate){
            end.draw(g, minX, tickH);
            bar.draw(g, minX, name, isSingleDate);
        }
        g.setColor(Color.DARK_GRAY);
    }
    public boolean touchingMouse(int mx, int my){
        return bar.isTouching(mx, my);
    }

    public boolean isSingleDate() {
        return isSingleDate;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }
    
    public void setI(int i){
        this.bar.setI(i);
    }
}
