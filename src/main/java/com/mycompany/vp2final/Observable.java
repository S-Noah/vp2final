package com.mycompany.vp2final;

public interface Observable {
    public void notifyObservers();
    public void addObserver(Updatable u);
}
