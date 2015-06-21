package hello.dcsms.plak.ss;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import hello.dcsms.plak.Utils.Debugger;

/**
 * Created by jmkl on 5/1/2015.
 */
public class SSConfigurationParser {
    public static SSFrameData parse(String file){

        String json=null;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file+"prop.plk"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
            json = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(json!=null){
            Gson gson = new Gson();
            return gson.fromJson(json,SSFrameData.class);

        }else
            return null;
    }
}
