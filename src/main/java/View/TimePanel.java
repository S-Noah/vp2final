package View;

import Model.Rep;
import Model.User;
import java.awt.Color;
import java.awt.Graphics;

import com.mycompany.vp2final.TimelineChangeHandler;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TimePanel extends javax.swing.JPanel {
    // Static Fields.
    private static final int barWidth = 20;
    private static final int halfBarWidth = 10;
    
    public static enum TimeType{
        DAYS, MONTHS, YEARS
    }
    
    // Instance Fields.
    private User user;
    
    // Node Fields.
    private ArrayList<TemporalNode> temporalNodes;
    private ArrayList<RepNode> nodes;
    private ArrayList<RepNode> nodesInWindow;
    private ArrayList<TimelineChangeHandler> observers;
    
    // DateRange Fields.
    private LocalDateTime minDate;
    private LocalDateTime maxDate;
    private TimeType currentTimeType = TimeType.DAYS;
    private int numDays;
    private int numMonths;
    private int numYears;
    private int numNodes;
    
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
    private int temporalWidth;
    private int numTemporalNodesInWin;
    // Date Range Fields.
    
    // Control Window Measurment Update and Initialization, Cannot be done in Constructor because the panel is not packed, instead, I set a flag for a function that will be called before it is painted.
    private boolean initialized;
    private boolean needsUpdate;
    
    public TimePanel() {
        temporalNodes = new ArrayList();
        nodes = new ArrayList();
        nodesInWindow = new ArrayList();
        observers = new ArrayList();
        
        initialized = false;
        needsUpdate = true;
        temporalWidth = 80;
        zoom = 100;
        maxDate = LocalDateTime.now();
        
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
        numTemporalNodesInWin = w / temporalWidth;
        winMinX = 0;
        winMaxX = w;
        setTemporalNodes();
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
        minDate = this.user.getDateCreated();
        numDays = (int)ChronoUnit.DAYS.between(minDate, maxDate);
        numMonths = (int)ChronoUnit.MONTHS.between(minDate, maxDate);
        numYears = (int)ChronoUnit.YEARS.between(minDate, maxDate);
        numNodes = numDays;
        maxX = numNodes * temporalWidth;
        System.out.println(minDate.toString() + "   -   " + maxDate.toString());
        if(initialized){
            setTemporalNodes();
        }
        setRepNodes(user.getReps());
        repaint();
    }
    public void setTemporalNodes(){
        temporalNodes.clear();
        StringBuilder sb = new StringBuilder();
        if(currentTimeType == TimeType.MONTHS){
            numNodes = numMonths;
            for(int i = 0; i <= numNodes; i++){
                LocalDateTime tempDate = minDate.plusMonths(i);
                
                sb.append(tempDate.getYear());
                sb.append("/");
                sb.append(tempDate.getMonthValue());
                
                if(i % 2 == 1){
                    temporalNodes.add(new TemporalNode(sb.toString(), i, true, (i + 1) * temporalWidth, halfH - 2 + halfBarWidth, 4, 4));
                }
                else{
                    temporalNodes.add(new TemporalNode(sb.toString(), i, false, (i + 1) * temporalWidth, halfH - 2 - halfBarWidth, 4, 4));
                }
                
                sb.setLength(0);
            }
        }
        else if(currentTimeType == TimeType.YEARS){
            numNodes = numYears;
            for(int i = 0; i <= numNodes + 1; i++){
                LocalDateTime tempDate = minDate.plusYears(i);
                
                sb.append(tempDate.getYear());
                
                if(i % 2 == 1){
                    temporalNodes.add(new TemporalNode(sb.toString(), i, true, (i + 1) * temporalWidth, halfH - 2 + halfBarWidth, 4, 4));
                }
                else{
                    temporalNodes.add(new TemporalNode(sb.toString(), i, false, (i + 1) * temporalWidth, halfH - 2 - halfBarWidth, 4, 4));
                }
                
                sb.setLength(0);
            }
        }
        else{
            numNodes = numDays;
            for(int i = 0; i <= numNodes; i++){
                LocalDateTime tempDate = minDate.plusDays(i);
                
                sb.append(tempDate.getYear());
                sb.append("/");
                sb.append(tempDate.getMonthValue());
                sb.append("/");
                sb.append(tempDate.getDayOfMonth());
                
                if(i % 2 == 1){
                    temporalNodes.add(new TemporalNode(sb.toString(), i, true, (i + 1) * temporalWidth, halfH - 2 + halfBarWidth, 4, 4));
                }
                else{
                    temporalNodes.add(new TemporalNode(sb.toString(), i, false, (i + 1) * temporalWidth, halfH - 2 - halfBarWidth, 4, 4));
                }
                
                sb.setLength(0);
            }
        }
        maxX = numNodes * temporalWidth;
    }
    public void updateTemporalNodes(){
        for(TemporalNode node : temporalNodes){
            node.zoomUpdate(temporalWidth);
        }
    }
    public void setRepNodes(Rep[] reps){
        nodes.clear();
        for(int i = 0; i < reps.length; i++){
            nodes.add(new RepNode(reps[i], (i + 1) * 200, 200, 50, 50));
        }
        int lastIndex = nodes.size() - 1;
    }
    // Panel Control Methods.
    public void setTimeType(int i){
        if(i == 1){
            currentTimeType = TimeType.MONTHS;
        }
        else if(i == 2){
            currentTimeType = TimeType.YEARS;
        }
        else{
            currentTimeType = TimeType.DAYS;
        }
        setTemporalNodes();
        repaint();
    }
    public void changeTemporalWidth(int mod){
        temporalWidth += 10 * mod;
        numTemporalNodesInWin = w / temporalWidth;
        maxX = numNodes * temporalWidth;
        
        updateTemporalNodes();
        repaint();
    }
    public void scrollWindow(int val, int maxScroll){
        int scrollRatio = maxX / maxScroll;
        winMinX = val * scrollRatio;
        winMaxX = w + winMinX;
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
        g.fillRect(0, halfH - halfBarWidth, w, barWidth);
       
        for(TemporalNode node : temporalNodes){
            if(node.InXRange(winMinX, winMaxX)){
                node.draw(g, winMinX);
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
