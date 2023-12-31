package es.unican.carchargers.common;

import android.app.AlertDialog;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts the given input stream to a String
     * @param is
     * @return
     */
    public static String toString(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            String fileContent = out.toString();
            reader.close();
            return fileContent;

        } catch (IOException e) {}
        return null;
    }




}
