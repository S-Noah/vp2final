/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.vp2final;

/**
 *
 * @author NoahS
 */
public interface Observable {
    public void notifyObservers();
    public void addObserver(Updatable u);
}
