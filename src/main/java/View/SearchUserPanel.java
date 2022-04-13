/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import com.mycompany.vp2final.Main;

/**
 *
 * @author NoahS
 */
public class SearchUserPanel extends javax.swing.JPanel {

    /**
     * Creates new form SearchUserPanel
     */
    public SearchUserPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSearch = new javax.swing.JButton();
        jtfLogin = new javax.swing.JTextField();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jtfLogin.setText("User Login");
        jtfLogin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfLoginFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfLoginFocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        this.setVisible(false);
    }//GEN-LAST:event_formFocusLost

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String login = jtfLogin.getText();
        if(login != ""){
            Main.LoginSearchEvent(login);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void jtfLoginFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfLoginFocusGained
        jtfLogin.setText("");
    }//GEN-LAST:event_jtfLoginFocusGained

    private void jtfLoginFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfLoginFocusLost
        this.setVisible(true);
    }//GEN-LAST:event_jtfLoginFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JTextField jtfLogin;
    // End of variables declaration//GEN-END:variables
}
