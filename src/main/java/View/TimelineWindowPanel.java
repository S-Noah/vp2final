/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Model.Rep;
import Model.User;

/**
 *
 * @author NoahS
 */
public class TimelineWindowPanel extends javax.swing.JPanel {

    /**
     * Creates new form TimelineWindowPanel
     */
    public TimelineWindowPanel() {
        initComponents();
        pnlTimeline.addObserver(repInfoPanel);
    }
    public void setUser(User user){
        //updateTimelineNodes(user.getReps());
        pnlTimeline.setUser(user);
        timelineScrollBar.setValue(0);
        githubUserInfoPanel1.setUser(user);
        repInfoPanel.setUser(user);
        repInfoPanel.resetComponents();
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTimeline = new View.TimePanel();
        githubUserInfoPanel1 = new View.GithubUserInfoPanel();
        repInfoPanel = new View.RepInfoPanel();
        timelineScrollBar = new javax.swing.JScrollBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        cmboTimeType = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 300), new java.awt.Dimension(0, 300), new java.awt.Dimension(32767, 300));

        javax.swing.GroupLayout pnlTimelineLayout = new javax.swing.GroupLayout(pnlTimeline);
        pnlTimeline.setLayout(pnlTimelineLayout);
        pnlTimelineLayout.setHorizontalGroup(
            pnlTimelineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlTimelineLayout.setVerticalGroup(
            pnlTimelineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 688, Short.MAX_VALUE)
        );

        timelineScrollBar.setBlockIncrement(1);
        timelineScrollBar.setMaximum(2500);
        timelineScrollBar.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        timelineScrollBar.setVisibleAmount(250);
        timelineScrollBar.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                timelineScrollBarMouseWheelMoved(evt);
            }
        });
        timelineScrollBar.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                timelineScrollBarAdjustmentValueChanged(evt);
            }
        });

        jButton1.setText("-");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("+");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        cmboTimeType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Days", "Months", "Years" }));
        cmboTimeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmboTimeTypeActionPerformed(evt);
            }
        });

        jLabel1.setText("Time Setting");

        filler1.setBackground(new java.awt.Color(204, 204, 204));
        filler1.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 711, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmboTimeType, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(timelineScrollBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTimeline, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(repInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(githubUserInfoPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlTimeline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timelineScrollBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(cmboTimeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(githubUserInfoPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(repInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void timelineScrollBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_timelineScrollBarAdjustmentValueChanged
        int largestValue = timelineScrollBar.getMaximum() - timelineScrollBar.getVisibleAmount();
        pnlTimeline.scrollWindow(timelineScrollBar.getValue(), largestValue);
    }//GEN-LAST:event_timelineScrollBarAdjustmentValueChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        pnlTimeline.changeTemporalWidth(+1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        pnlTimeline.changeTemporalWidth(-1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmboTimeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmboTimeTypeActionPerformed
        pnlTimeline.setTimeType(cmboTimeType.getSelectedIndex());
        timelineScrollBar.setValue(0);
    }//GEN-LAST:event_cmboTimeTypeActionPerformed

    private void timelineScrollBarMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_timelineScrollBarMouseWheelMoved
       int clicks = evt.getWheelRotation();
       timelineScrollBar.setValue(timelineScrollBar.getValue() - clicks);
    }//GEN-LAST:event_timelineScrollBarMouseWheelMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmboTimeType;
    private javax.swing.Box.Filler filler1;
    private View.GithubUserInfoPanel githubUserInfoPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private View.TimePanel pnlTimeline;
    private View.RepInfoPanel repInfoPanel;
    private javax.swing.JScrollBar timelineScrollBar;
    // End of variables declaration//GEN-END:variables
}
