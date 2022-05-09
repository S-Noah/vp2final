package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileIO {
    public static boolean objectOut(Object obj, String filepath){
        try{
            ObjectOutputStream fout = new ObjectOutputStream(
                   new FileOutputStream(filepath)
            );
            fout.writeObject(obj);
            fout.close();
            return true;
        }
        catch(FileNotFoundException e){
            
            return false;
        }
        catch(IOException e){
            
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
            
            return null;
        }
        catch(FileNotFoundException e){
            
            return null;
        }
        catch(IOException e){
           
            return null;
        }
    }
}
