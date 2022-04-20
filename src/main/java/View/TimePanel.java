package View;

import View.JGraphics.RepNode;
import View.JGraphics.TemporalNode;
import Model.Rep;
import Model.User;
import java.awt.Color;
import java.awt.Graphics;

import com.mycompany.vp2final.TimelineChangeHandler;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import View.JGraphics.Drawable;

public class TimePanel extends javax.swing.JPanel {
    // Static Fields.
    public static class Mode{
        public static Mode DAYS = new Mode(1);
        public static Mode MONTHS = new Mode(2);
        public static Mode YEARS = new Mode(3);
        
        private int numNodes;
        private int zoomMultiplier;
        
        private Mode(int zoomMultiplier){
            this.numNodes = 0;
            this.zoomMultiplier = zoomMultiplier;
        }

        public int getNumNodes() {
            return numNodes;
        }

        public void setNumNodes(int numNodes) {
            this.numNodes = numNodes;
        }

        public int getZoomMultiplier() {
            return zoomMultiplier;
        }
    }
    // Instance Fields.
    private User user;
    
    // Node Fields.
    private ArrayList<TemporalNode> temporalNodes;
    private ArrayList<RepNode> repNodes;
    private ArrayList<Drawable> nodesToDraw;
    private ArrayList<TimelineChangeHandler> observers;
    
    // DateRange Fields.
    private LocalDate minDate;
    private LocalDate maxDate;
    private Mode currentMode;
    
    // Window Measurement Fields.
    private int w;
    private int h;
    private int halfW;
    private int halfH;
    
    // Sliding Window Fields,
   // private int zoom;
    private int maxScrollVal;
    private int scrollVal;
    private int maxX;
    private int winMinX;
    private int winMaxX;
    private int temporalWidth;
    private LocalDate winMinDate;
    private LocalDate winMaxDate;
    
    // Bar Fields.
    private int barY;
    private int barHeight;
    private int halfBarHeight;
    // Control Window Measurment Update and Initialization, Cannot be done in Constructor because the panel is not packed, instead, I set a flag for a function that will be called before it is painted.
    private boolean initialized;
    private boolean needsDimensionUpdate;
    
    public TimePanel() {
        initialized = false;
        needsDimensionUpdate = false;
        
        currentMode = Mode.DAYS;
        temporalWidth = 50;
        //zoom = 100;
        barHeight = 20;
        halfBarHeight = barHeight / 2;
        
        temporalNodes = new ArrayList();
        repNodes = new ArrayList();
        nodesToDraw = new ArrayList();
        observers = new ArrayList();
        
        maxDate = LocalDate.now();
        
        
        initComponents();
    } 
    // Observer implementation.
    public void informObservers(RepNode r){
        for(TimelineChangeHandler h : observers){
            h.onChange(r.getRep());
        }
    }
    public void addObserver(TimelineChangeHandler handler){
        observers.add(handler);
    }
    
    // Panel Updating Methods.
    public void init(){
        initialized = true;
        winMinX = 0;
        winMaxX = w;
        setTemporalNodes();
        setRepNodes();
        findNodesInRange();
    }
    public void updateDimensions(){
        needsDimensionUpdate = false;
        this.w = this.getWidth();
        this.h = this.getHeight();
        this.halfW = w/2;
        this.halfH = h/2;
        this.barY = (this.h - (barHeight * 3)) - halfBarHeight;
        if(!initialized){
            init();
        }
    } 
    public void setUser(User user){
        this.user = user;
        this.minDate = this.user.getDateCreated();
        Mode.DAYS.setNumNodes((int)ChronoUnit.DAYS.between(minDate, maxDate.plusDays(1)));
        Mode.MONTHS.setNumNodes((int)ChronoUnit.MONTHS.between(minDate, maxDate.plusMonths(1)));
        Mode.YEARS.setNumNodes((int)ChronoUnit.YEARS.between(minDate, maxDate.plusYears(1)));
        
        if(initialized){
            winMinX = 0;
            winMaxX = w;
            setTemporalNodes();
            setRepNodes();
            findNodesInRange();
            repaint();
        }
        else{
            needsDimensionUpdate = true;
        }
    }
    public void setTemporalNodes(){
        temporalNodes.clear();
        for(int i = 0; i <= currentMode.getNumNodes(); i++){
            temporalNodes.add(new TemporalNode(i, barY, temporalWidth, i % 2 == 1, minDate, currentMode));
        }
        
        maxX = ((currentMode.getNumNodes() + 2) * temporalWidth);
        //System.out.println(maxX);
    }
    public void setRepNodes(){
        repNodes.clear();
        for(Rep r : user.getReps()){
            repNodes.add(new RepNode(r, minDate, currentMode, barY, temporalWidth));
        }
    }
    public void updateNodeSpacing(){
        for(TemporalNode node : temporalNodes){
            node.zoomUpdate(temporalWidth);
        }
        for(RepNode node : repNodes){
            node.zoomUpdate(temporalWidth);
        }
    }
    public void updateNodesTimeMode(){
        for(RepNode node : repNodes){
            node.changeTimeMode(currentMode);
        }
    }
    public void findNodesInRange(){
        nodesToDraw.clear();
        for(TemporalNode node : temporalNodes){
            if(node.InXRange(winMinX, winMaxX)){
                nodesToDraw.add(node);
            }
        }
        int i = 2;
        for(RepNode node: repNodes){
            if(node.InXRange(winMinX, winMaxX)){
                if(!node.isSingleDate()){
                    i++;
                }
                node.setI(i);
                nodesToDraw.add(node);
            }
        }
    }
    
    // Panel Control Methods.
    public void setTimeType(int i){
        if(i == 1){
            currentMode = Mode.MONTHS;
        }
        else if(i == 2){
            currentMode = Mode.YEARS;
        }
        else{
            currentMode = Mode.DAYS;
        }
        winMinX = 0;
        winMaxX = w;
        setTemporalNodes();
        updateNodesTimeMode();
        findNodesInRange();
        repaint();
    }
    public void changeTemporalWidth(int mod){
        int newTemporalWidth = temporalWidth + 10 * mod;
        if(newTemporalWidth > 40 && newTemporalWidth < 150){
            temporalWidth = newTemporalWidth;
            maxX = ((currentMode.getNumNodes() + 2) * temporalWidth);
            updateNodeSpacing();
            scrollWindow(scrollVal, maxScrollVal);
            findNodesInRange();
            repaint();
        }
    }
    public void scrollWindow(int val, int maxScroll){
        this.scrollVal = val;
        this.maxScrollVal = maxScroll;
        
        double scrollRatio = Math.ceil((double)maxX / maxScroll);
        int min = (int)(val * scrollRatio);
        if(min < winMinX || winMaxX < maxX){
            winMinX = min;
            winMaxX = winMinX + w;
        }
        if(winMaxX > maxX){
            winMaxX = maxX;
            winMinX = maxX-w;
        }
        
        //System.out.println(val + ", " + maxScroll + ", " + currentMode.getNumNodes() +  ", " + scrollRatio + ", " + maxX + ": " + min + " - " + max + ", " + winMinX +" - " + winMaxX);
        //System.out.println(maxX + ": " + min + " - " + max + ", " + winMinX +" - " + winMaxX);
        findNodesInRange();
        repaint();
    }
    
    // Painting Methods.
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(needsDimensionUpdate){
            updateDimensions();
        }
        
        Color temp = g.getColor();
        g.setColor(Color.BLACK);
        //g.fillRect(0, halfH - halfBarHeight, w, barHeight);
        g.fillRect(0, barY, w, barHeight);
        
        for(Drawable node : nodesToDraw){
            node.draw(g, winMinX, barHeight);
        }
       
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
        for(RepNode node : repNodes){
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
