package edu.inlab.utils;


import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by hebowei on 2017/1/20.
 */
public class BlobObjectConv {
    public static java.sql.Blob ObjectToBlob(Object obj)  {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(obj);
            byte[] bytes = out.toByteArray();
            outputStream.close();
            return new SerialBlob(bytes);
        } catch (Exception e) {
            return null;
        }

    }

    public static Object BlobToObject(java.sql.Blob desblob)   {
        try {
            Object obj = null;
            ObjectInputStream in = new ObjectInputStream(
                    desblob.getBinaryStream());
            obj = in.readObject();
            in.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
