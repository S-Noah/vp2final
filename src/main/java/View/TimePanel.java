/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Model.Rep;
import Model.User;
import java.awt.Color;
import java.awt.Graphics;

import com.mycompany.vp2final.TimelineChangeHandler;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author Noahb
 */
public class TimePanel extends javax.swing.JPanel {
    private User user;
    private ArrayList<DayNode> days;
    private ArrayList<RepNode> nodes;
    private ArrayList<RepNode> nodesInWindow;
    private ArrayList<TimelineChangeHandler> observers;
    
    private LocalDateTime minDate;
    private LocalDateTime maxDate;
    private Duration dateRange;
    
    private int w;
    private int h;
    private int halfW;
    private int halfH;
    
    private int zoom;
    private int halfZoom;
    
    private int maxX;
    
    private int winMinX;
    private int winMaxX;
    private int dayWidth;
    
    private boolean updated;
    
    
    /**
     * Creates new form test
     */
    public TimePanel() {
        nodes = new ArrayList();
        nodesInWindow = new ArrayList();
        days = new ArrayList();
        observers = new ArrayList();
        //nodes.add(new RepNode(100, 200, 50, 50, "Visual II Final"));
        //nodes.add(new RepNode(700, 200, 50, 50, "test rep2"));
        updated = false;
        dayWidth = 80;
        maxDate = LocalDateTime.now();
        setZoom(20);
        initComponents();
    }
    
    public void informObservers(RepNode r){
        for(TimelineChangeHandler h : observers){
            h.onChange(r.getRep());
        }
    }
    
    public void addObserver(TimelineChangeHandler handler){
        observers.add(handler);
    }
    
    public void setUser(User user){
        this.user = user;
        minDate = this.user.getDateCreated();
        dateRange = Duration.between(minDate, maxDate);
        
        maxX = (int)dateRange.toDays() * dayWidth;
        System.out.println(maxX);
        setNodes(user.getReps());
        repaint();
    }
    public void updateDays(){
        for(DayNode day : days){
            day.zoomUpdate(dayWidth);
        }
    }
    public void setNodes(Rep[] reps){
        nodes.clear();
        for(int i = 0; i < reps.length; i++){
            nodes.add(new RepNode(reps[i], (i + 1) * 200, 200, 50, 50));
        }
        int lastIndex = nodes.size() - 1;
        //maxX = nodes.get(lastIndex).getX() + (nodes.get(lastIndex).getW() * 2);
    }
    
    public void setZoom(int val){
        zoom = val;
        halfZoom = zoom / 2;
    }
    
    public void changeZoom(int mod){
        //zoom += 10 * mod;
        dayWidth += 10 * mod;
        maxX = (int)dateRange.toDays() * dayWidth;
        //halfZoom = zoom / 2;
        updateDays();
        repaint();
    }
    public void changeWindow(int val){
        if(val > 0){
            if(winMaxX < maxX){
                winMinX += val;
                winMaxX += val;
                repaint();
            }
        }
        else if(winMinX + val > 0){
            winMinX += val;
            winMaxX += val;
            repaint();
        }
    }
    public void scrollWindow(int val, int maxScroll){
        int scrollRatio = maxX / maxScroll;
        winMinX = val * scrollRatio;
        winMaxX = w + winMinX;
        repaint();
    }
       
    public void updateDimesions(){
        this.w = this.getWidth();
        this.h = this.getHeight();
        this.halfW = w/2;
        this.halfH = h/2;
        if(!updated){
            if(dateRange != null){
                for(int i = 0; i < dateRange.toDays(); i++){
                    days.add(new DayNode(i, i * dayWidth, halfH - halfZoom - 2, 4, 4));
                }   
            }
            winMinX = 0;
            winMaxX = w;
            updated = true;
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        updateDimesions();
        
        //System.out.println(winMinX + "-" + winMaxX);
        //g.fillRect(winMinX, halfH-25, 10, 50);
        //g.fillRect(winMaxX - 10, halfH-25, 10, 50);
        
        Color temp = g.getColor();
        
        g.setColor(Color.BLACK);
        
        g.fillRect(0, halfH - halfZoom, w, zoom);
        g.setColor(Color.WHITE);
        if(dateRange != null){
            for(DayNode day : days){
                if(day.InXRange(winMinX, winMaxX)){
                    day.draw(g, winMinX);
                }
            }
        }
        /*
        nodesInWindow.clear();
        for(RepNode node : nodes){
            if(node.InXRange(winMinX, winMaxX)){
                nodesInWindow.add(node);
                node.draw(g, winMinX);
            }
        }
        */
        g.setColor(temp);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
     
    }//GEN-LAST:event_formMouseClicked

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        int mouseX = evt.getX() + winMinX;
        int mouseY = evt.getY();
        for(RepNode node : nodesInWindow){
            if(node.touchingMouse(mouseX, mouseY)){
                informObservers(node);
                break;
            }
        }
        //System.out.println(mouseX + ", " + mouseY + ", " + (mouseX - winMinX));
    }//GEN-LAST:event_formMousePressed
    public int getMaxX(){
        return maxX;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
