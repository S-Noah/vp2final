package View.JGraphics;

import java.awt.Graphics;
// Interface for components on the scrollable timeline panel.
public interface Drawable {
    public boolean InXRange(int x1, int x2); // Checks if this rect is in the range of the sliding window.
    public void zoomUpdate(int space); // Updates the nodes dimensions based on the window.
    public void draw(Graphics g, int minX, int barHeight);
}
