/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.Nodes;

/**
 *
 * @author NoahS
 */
abstract public class RectBound {
    protected int x;
    protected int y;
    
    protected int w;
    protected int h;
    
    protected RectBound(){
        this.x = 0;
        this.y = 0;
        this.w = 0;
        this.h = 0;
    }
    protected RectBound(int w, int h){
        this.x = 0;
        this.y = 0;
        this.w = w;
        this.h = h;
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
