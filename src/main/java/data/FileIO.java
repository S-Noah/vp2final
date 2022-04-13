/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 *
 * @author NoahS
 */
public class FileIO {
    public static boolean objectOut(Object obj, String filepath){
        try{
            ObjectOutputStream fout = new ObjectOutputStream(
                   new FileOutputStream(filepath)
            );
            System.out.println(fout);
            fout.writeObject(obj);
            fout.close();
            return true;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public static Object objectIn(String filepath){
        try{
            ObjectInputStream fin = new ObjectInputStream(
                   new FileInputStream(filepath)
            );
            Object obj = fin.readObject();
            fin.close();
            return obj;
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
        catch(FileNotFoundException e){
            //e.printStackTrace();
            return null;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
