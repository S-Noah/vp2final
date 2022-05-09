package View.JGraphics;
/**
 * This is a class that defines a rectangular boundary for the Timeline Panel.
 */
abstract public class RectBound {
    protected int x;
    protected int y;
    
    protected int w;
    protected int h;
    
    protected RectBound(){
        this(0, 0, 0, 0);
    }
    protected RectBound(int w, int h){
        this(0, 0, w, h);
    }
    
    protected RectBound(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
}
