package View.JGraphics;

import Model.Rep;
import Model.User;
import java.awt.Color;
import java.awt.Graphics;

import com.mycompany.vp2final.TimelineChangeHandler;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class TimePanel extends javax.swing.JPanel {
    
    private static StringBuilder sb = new StringBuilder();
    /**
     * Creates a key using a LocalDate for the hash table of nodes. Also allows lookup up.
     * @param date The date to create a key string from.
     * @param mode The mode used to determine the formatting.
     * @return the resulting formatted key ex. "12/31/2022", "12/31", "2022".
     */
    public static String createKey(LocalDate date, TimePanel.Mode mode){
        sb.setLength(0);
        if(mode == TimePanel.Mode.MONTHS){
            sb.append(date.getYear());
            sb.append("/");
            sb.append(date.getMonthValue());
        }
        else if(mode == TimePanel.Mode.YEARS){
            sb.append(date.getYear());
        }
        else{
            sb.append(date.getYear());
            sb.append("/");
            sb.append(date.getMonthValue());
            sb.append("/");
            sb.append(date.getDayOfMonth());
        }
        return sb.toString();
    }
    
    // Static Fields.
    public static class Mode{ // TimePanels Custom Enumeration
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
    private HashMap<String, TemporalNode> temporalNodes; // Stores all the date ticks.
    private ArrayList<RepNode> repNodes; // Stores all of a users Repository's as a drawable form.
    private ArrayList<Drawable> nodesToDraw; // Frame to frame buffer of nodes that are in range.
    private ArrayList<TimelineChangeHandler> observers; // Obeservers that should react to a click fora repository selection.
    
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
    private int maxScrollVal;
    private int scrollVal;
    private int maxX;
    private int winMinX;
    private int winMaxX;
    private int temporalWidth;
    
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
        
        barHeight = 20;
        halfBarHeight = barHeight / 2;
        
        repNodes = new ArrayList();
        nodesToDraw = new ArrayList();
        observers = new ArrayList();
        temporalNodes = new HashMap<>();
        
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
    /**
     * This function runs when the timeline tries to draw the nodes, but they haven't been initialized yet.
     */
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
            init();
            repaint();
        }
        else{
            needsDimensionUpdate = true;
        }
    }
    /**
     * Clears old temporal nodes and replaces them with new owns based off the current mode.
     */
    public void setTemporalNodes(){
        LocalDate tempDate = minDate;
        String tempKey;
        temporalNodes.clear();
        for(int i = 0; i <= currentMode.getNumNodes(); i++){
            tempKey = createKey(tempDate, currentMode);
            temporalNodes.put(tempKey, new TemporalNode(i, barY, temporalWidth, i % 2 == 1, tempKey));
            if(currentMode == Mode.MONTHS){
                tempDate = tempDate.plusMonths(1);
            }
            else if(currentMode == Mode.YEARS){
                tempDate = tempDate.plusYears(1);
            }
            else{
                tempDate = tempDate.plusDays(1);
            }
        }
        maxX = ((currentMode.getNumNodes() + 2) * temporalWidth); // Calculating the maximum x for the sliding window.
    }
    /**
     * Clears old repository nodes and replaces them with new owns based off of the users repositories.
     */
    public void setRepNodes(){
        repNodes.clear();
        TemporalNode tempStart, tempEnd;
        for(Rep r : user.getReps()){
            tempStart = getTemporalNodeAt(r.getDateCreated(), currentMode);
            tempEnd = getTemporalNodeAt(r.getDateLastPushed(), currentMode);
            repNodes.add(new RepNode(r, tempStart, tempEnd, currentMode, barY, temporalWidth));
        }
    }
    /**
     * Updates each node with a new spacing.
     */
    public void updateNodeSpacing(){
        for(TemporalNode node : temporalNodes.values()){
            node.zoomUpdate(temporalWidth);
        }
        for(RepNode node : repNodes){
            node.zoomUpdate(temporalWidth);
        }
    }
    /**
     * Updates the rep nodes when the time mode changes.
     */
    public void updateNodesTimeMode(){
        TemporalNode tempStart, tempEnd;
        for(RepNode node : repNodes){
            tempStart = getTemporalNodeAt(node.getStartingDate(), currentMode);
            tempEnd = getTemporalNodeAt(node.getEndingDate(), currentMode);
            node.changeTimeMode(tempStart, tempEnd, currentMode);
        }
    }
    /**
     * Fills the drawable buffer with temporal nodes and repository nodes that are in range.
     */
    public void findNodesInRange(){
        nodesToDraw.clear();
        for(TemporalNode node : temporalNodes.values()){
            if(node.InXRange(winMinX, winMaxX)){
                nodesToDraw.add(node);
            }
        }
        // 
        int i = 2;
        for(RepNode node: repNodes){
            if(node.InXRange(winMinX, winMaxX)){
                if(!node.isSingleDate()){
                    i++;
                    node.setI(i);
                }
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
        init();
        repaint();
    }
    /**
     * changes the the temporal width by 10 based on mod.
     * @param mod The value to make the amount negative or positive.
     */
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
    /**
     * Usually used with a JScrollBar. Updates the sliding window based on the value given.
     * @param val The value to set the window to.
     * @param maxScroll The max scroll value used to create the bounds of the window.
     */
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
        // Handles a repository selection, when the user clickes a rep.
        int mouseX = evt.getX() + winMinX;
        int mouseY = evt.getY();
        for(RepNode node : repNodes){
            if(node.touchingMouse(mouseX, mouseY)){
                informObservers(node);
                break;
            }
        }
    }//GEN-LAST:event_formMousePressed
    public int getMaxX(){
        return maxX;
    }
    /**
     * Creates a key from the given date and returns back the TemporalNode that is associated with the date key.
     * @param date The date to turn into a search key.
     * @param mode The mode to format the key.
     * @return 
     */
    public TemporalNode getTemporalNodeAt(LocalDate date, Mode mode){
        String tempKey = createKey(date, mode);
        return temporalNodes.get(tempKey);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
