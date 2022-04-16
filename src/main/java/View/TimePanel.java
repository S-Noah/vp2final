package View;

import View.Nodes.RepNode;
import View.Nodes.TemporalNode;
import Model.Rep;
import Model.User;
import java.awt.Color;
import java.awt.Graphics;

import com.mycompany.vp2final.TimelineChangeHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TimePanel extends javax.swing.JPanel {
    // Static Fields.
    private static final int barHeight = 24;
    private static final int halfBarHeight = barHeight / 2;
    
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
    private ArrayList<TemporalNode> temporalNodesInWindow;
    private ArrayList<RepNode> repNodes;
    private ArrayList<RepNode> repNodesInWindow;
    private ArrayList<TimelineChangeHandler> observers;
    
    // DateRange Fields.
    private LocalDate minDate;
    private LocalDate maxDate;
    //private TimeType currentTimeType = TimeType.DAYS;
    private Mode currentMode;
    //private int numDays; // Flagged for update.
    //private int numMonths;// Flagged for update.
    //private int numYears; // Flagged for update.
    //private int numNodes; // Flagged for update.
    
    // Measurement Fields.
    private int w;
    private int h;
    private int halfW;
    private int halfH;
    
    // Sliding Window Fields,
    private int zoom;
    private int maxX;
    private int winMinX;
    private int winMaxX;
    private int temporalWidth; // Flagged for update.
    //private int numTemporalNodesInWin;
    private LocalDate winMinDate;
    private LocalDate winMaxDate;
    
    // Control Window Measurment Update and Initialization, Cannot be done in Constructor because the panel is not packed, instead, I set a flag for a function that will be called before it is painted.
    private boolean initialized;
    private boolean needsUpdate;
    
    public TimePanel() {
        temporalNodes = new ArrayList();
        temporalNodesInWindow = new ArrayList();
        repNodes = new ArrayList();
        repNodesInWindow = new ArrayList();
        observers = new ArrayList();
        
        maxDate = LocalDate.now();
        
        initialized = false;
        needsUpdate = false;
        temporalWidth = 50;
        zoom = 100;
        currentMode = Mode.DAYS;
        
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
        needsUpdate = false;
        this.w = this.getWidth();
        this.h = this.getHeight();
        this.halfW = w/2;
        this.halfH = h/2;
        if(!initialized){
            init();
        }
    } 
    public void setUser(User user){
        this.user = user;
        this.minDate = this.user.getDateCreated();
        Mode.DAYS.setNumNodes((int)ChronoUnit.DAYS.between(minDate, maxDate));
        Mode.MONTHS.setNumNodes((int)ChronoUnit.MONTHS.between(minDate, maxDate));
        Mode.YEARS.setNumNodes((int)ChronoUnit.YEARS.between(minDate, maxDate));
        
        if(initialized){
            setTemporalNodes();
            findNodesInRange();
            repaint();
        }
        else{
            needsUpdate = true;
        }
    }
    public void setTemporalNodes(){
        temporalNodes.clear();
        for(int i = 0; i <= currentMode.getNumNodes() + 1; i++){
            temporalNodes.add(new TemporalNode(i, halfH - halfBarHeight, temporalWidth, i % 2 == 1, minDate, currentMode));
        }
        
        maxX = (currentMode.getNumNodes() * temporalWidth) + temporalWidth;
    }
    public void setRepNodes(){
        repNodes.clear();
        for(Rep r : user.getReps()){
            repNodes.add(new RepNode(r, temporalWidth, minDate, currentMode, halfH - halfBarHeight));
        }
    }
    public void updateTemporalNodes(){
        for(TemporalNode node : temporalNodes){
            node.zoomUpdate(temporalWidth);
        }
    }
    public void findNodesInRange(){
        temporalNodesInWindow.clear();
        for(TemporalNode node : temporalNodes){
            if(node.InXRange(winMinX, winMaxX - temporalWidth)){
                temporalNodesInWindow.add(node);
            }
        }
        winMinDate = temporalNodesInWindow.get(0).getDate();
        winMaxDate = temporalNodesInWindow.get(temporalNodesInWindow.size() -1).getDate();
    }
    
    // Panel Control Methods.
    public void setTimeType(int i){
        winMinX = 0;
        winMaxX = w;
        if(i == 1){
            currentMode = Mode.MONTHS;
        }
        else if(i == 2){
            currentMode = Mode.YEARS;
        }
        else{
            currentMode = Mode.DAYS;
        }
        setTemporalNodes();
        findNodesInRange();
        repaint();
    }
    public void changeTemporalWidth(int mod){
        int newTemporalWidth = temporalWidth + 10 * mod;
        if(newTemporalWidth > 40 && newTemporalWidth < 150){
            temporalWidth = newTemporalWidth;
            maxX = (currentMode.getNumNodes() * temporalWidth) + temporalWidth;
            updateTemporalNodes();
            findNodesInRange();
            repaint();
        }
    }
    public void scrollWindow(int val, int maxScroll){
        int scrollRatio = maxX / maxScroll;
        winMinX = val * scrollRatio;
        winMaxX = w + winMinX;
        findNodesInRange();
        repaint();
    }
    
    // Painting Methods.
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(needsUpdate){
            updateDimensions();
        }
        
        Color temp = g.getColor();
        g.setColor(Color.BLACK);
        //g.setColor(Color.getHSBColor(0, 0, 0.55f));
        g.fillRect(0, halfH - halfBarHeight, w, barHeight);
        
        for(TemporalNode node : temporalNodesInWindow){
            if(node.InXRange(winMinX, winMaxX - temporalWidth)){
                node.draw(g, winMinX, barHeight);
            }
        }
        
        //System.out.println(currentMode.getNumNodes());
        
        repNodesInWindow.clear();
        for(RepNode node : repNodes){
            //if(node.InXRange(winMinX, winMaxX)){
                repNodesInWindow.add(node);
                node.draw(g, winMinX);
            //}
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
        for(RepNode node : repNodesInWindow){
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
