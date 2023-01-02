package me.saehyeon.saehyeonlib.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLUtility {

    /**
     * 특정 사이트의 텍스트를 반환합니다.
     * @param urlStr
     * @return 만약 아무런 텍스트도 얻지 못한다면 빈 문자열
     */
    public static String getContent(String urlStr) {

        StringBuilder sb = new StringBuilder("");

        try{

            URL url = new URL(urlStr);
            BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = "";

            while((line = buf.readLine()) != null)
                sb.append(line);

        } catch (Exception e) {

            System.out.println("§c"+urlStr+"의 내용을 읽어오는 데 실패했습니다. ("+e+")");

        }

        return sb.toString();
    }

}
