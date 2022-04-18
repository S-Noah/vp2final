package View.JGraphics;

import View.TimePanel;
import java.awt.Color;
import java.awt.Graphics;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimePoint extends RectBound implements Drawable{
    private static int diam = 6;
    private static int halfDiam = 3;
    
    private LocalDate date;
    private TimePanel.Mode mode;
    private Color color;
    private int numDays;
    private int numMonths;
    private int numYears;
    private int space;
    private boolean inRange;
    
    public TimePoint(LocalDate date, LocalDate minDate, TimePanel.Mode mode, int space, int y, Color color){
        super(diam, diam);
        this.date = date;
        this.mode = mode;
        this.space = space;
        this.y = y - halfDiam;
        this.color = color;
        this.inRange = false;
        this.numDays = (int)ChronoUnit.DAYS.between(minDate, date.plusDays(1));
        this.numMonths = (int)ChronoUnit.MONTHS.between(minDate, date.plusMonths(1));
        this.numYears = (int)ChronoUnit.YEARS.between(minDate, date.plusYears(1));

        this.update();
    }
    public boolean InXRange(int x1, int x2){
        this.inRange = (x + halfDiam >= x1 && x - halfDiam <= x2);
        return inRange;
    }
    public void changeTimeMode(TimePanel.Mode mode){
        this.mode = mode;
        this.update();
    }
    public void update(){
        if(mode == TimePanel.Mode.MONTHS){
            this.x = ((numMonths) * space) - halfDiam;
        }
        else if(mode == TimePanel.Mode.YEARS){
            this.x = ((numYears) * space) - halfDiam;
        }
        else{
            this.x = ((numDays) * space) - halfDiam;
        }
    }
    public void zoomUpdate(int space){
        this.space = space;
        this.update();
    }
    
    public void draw(Graphics g, int minX, int barHeight){
        int newX = x - minX;
        g.setColor(color);
        g.fillOval(newX, y, w, h);
    }

    public boolean isInRange() {
        return inRange;
    }

    public LocalDate getDate() {
        return date;
    }
    
}
