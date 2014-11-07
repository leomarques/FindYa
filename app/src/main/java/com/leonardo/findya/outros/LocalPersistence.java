package com.leonardo.findya.outros;

import android.app.Activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LocalPersistence {
    public static void writeObjectToFile(Object object, String filename) {
        ObjectOutputStream objectOut = null;
        try {
            FileOutputStream fileOut = App.inst().openFileOutput(filename, Activity.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
    }

    public static void deleteFile(String filename) {
        App.inst().deleteFile(filename);
    }

    public static Object readObjectFromFile(String filename) {
        ObjectInputStream objectIn = null;
        Object object = null;
        try {
            FileInputStream fileIn = App.inst().getApplicationContext().openFileInput(filename);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();
        } catch (FileNotFoundException e) {
            // Do nothing
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }

        return object;
    }
}