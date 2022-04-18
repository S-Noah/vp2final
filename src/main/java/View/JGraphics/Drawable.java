/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package View.JGraphics;

import java.awt.Graphics;

/**
 *
 * @author NoahS
 */
public interface Drawable {
    public boolean InXRange(int x1, int x2);
    public void zoomUpdate(int space);
    public void draw(Graphics g, int minX, int barHeight);
}
