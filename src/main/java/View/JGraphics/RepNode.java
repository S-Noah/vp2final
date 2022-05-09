package View.JGraphics;

import java.awt.Color;
import java.awt.Graphics;
import Model.Rep;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * This class renders a Rep onto the Timeline.
 * It consists of a start, end, and bar inbetween.
 * Each bar generates a nice random pastel color.
 */
public class RepNode extends RectBound implements Drawable{
    public static Random random = new Random();
    public static Color generateRepColor(){
        Color c = Color.getHSBColor(random.nextFloat(), 0.5f, 0.9f);
        return c;
    }
    private boolean isSingleDate;
    private String name;
    
    private Rep rep; // Rep to render.
    private LocalDate startingDate;
    private LocalDate endingDate;
    private TimePoint start; // Point for the starting date.
    private TimePoint end; // Point for the endingDate.
    private TimePeriodBar bar; // Rep Bar.
    private TimePanel.Mode mode;
    private Color color = Color.ORANGE; // Defaults to orange for single date spanning Reps because they are not temporally interesting.
    
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
    /**
     * Checks if the Repository is in the sliding window at all.
     * @param x1
     * @param x2
     * @return A Boolean indicating if it does.
     */
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
        
        start.draw(g, minX, tickH);
        if(!isSingleDate){// excludes single date spanning Reps because they are not temporally interesting.
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
    /**
     * Controls its Y when RepNodes are stacked.
     * @param i 
     */
    public void setI(int i){
        this.bar.setI(i);
    }
}
