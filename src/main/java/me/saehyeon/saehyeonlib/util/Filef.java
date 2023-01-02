package me.saehyeon.saehyeonlib.util;

import me.saehyeon.saehyeonlib.main.LogLevel;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Filef {

    public static List<String> read(String filePath) {

        File file = new File(filePath);

        try {

            if(!file.exists())
                file.createNewFile();

            FileReader r = new FileReader(file);
            BufferedReader buf = new BufferedReader(r);

            String line = "";

            List<String> lines = new ArrayList<>();

            while((line = buf.readLine()) != null) {
                lines.add(line);
            }

            buf.close();

            return lines;

        } catch (Exception e) {

            SaehyeonLib.log(LogLevel.ERROR,filePath+"(을)를 읽던 도중 오류가 발생했습니다.\n"+e);
            return null;

        }

    }

    public static boolean write(String filePath, String... strings) {

        File file = new File(filePath);

        try {

            if(!file.exists())
                file.createNewFile();

            FileWriter w = new FileWriter(file);
            BufferedWriter bufWriter = new BufferedWriter(w);

            for(int i = 0; i < strings.length; i++) {

                String line = strings[i];

                bufWriter.write(line+(i+1 < strings.length ? "\n" : ""));

            }

            bufWriter.close();

            return true;

        } catch (Exception e) {

            SaehyeonLib.log(LogLevel.ERROR,filePath+"(을)를 쓰던 도중 오류가 발생했습니다.\n"+e);

            return false;

        }

    }

}
