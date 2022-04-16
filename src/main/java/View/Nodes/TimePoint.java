/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Nodes;

import View.TimePanel;
import java.awt.Color;
import java.awt.Graphics;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author NoahS
 */
public class TimePoint extends RectBound{
    private static int diam = 6;
    private static int half_diam = 3;
    private int numDays;
    private int numMonths;
    private int numYears;
    private TimePanel.Mode mode;
    private LocalDate date;

    public TimePoint(LocalDate date, LocalDate minDate, TimePanel.Mode mode, int space, int y){
        super(diam, diam);
        this.mode = mode;
        this.date = date;

        numDays = (int)ChronoUnit.DAYS.between(minDate, date.plusDays(1));
        numMonths = (int)ChronoUnit.MONTHS.between(minDate, date);
        numYears = (int)ChronoUnit.YEARS.between(minDate, date);

        this.update(space);
        this.y = y - half_diam;
        System.out.println(date);
    }

    public void update(int space){
        if(mode == TimePanel.Mode.MONTHS){
            this.x = ((numMonths + 1) * space) - half_diam;
        }
        else if(mode == TimePanel.Mode.YEARS){
            this.x = ((numYears + 1) * space) - half_diam;
        }
        else{
            this.x = ((numDays) * space) - half_diam;
        }
    }
    public void draw(Graphics g, int minX){
        int newX = x - minX;
        g.setColor(Color.MAGENTA);
        g.fillOval(newX, y, w, h);
    }
}
