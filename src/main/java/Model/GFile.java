/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Github.FileRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author NoahS
 */
public class GFile extends FileRequest{
    public GFile(FileRequest fr){
        this.copy(fr);
    }
}