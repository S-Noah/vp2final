package View.JGraphics;

import java.awt.Graphics;

public interface Drawable {
    public boolean InXRange(int x1, int x2);
    public void zoomUpdate(int space);
    public void draw(Graphics g, int minX, int barHeight);
}
