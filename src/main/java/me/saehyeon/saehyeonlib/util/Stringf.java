package me.saehyeon.saehyeonlib.util;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.*;

public class Stringf {
    /**
     * 문자열을 UUID로 변경합니다. <br>
     * 예를들어, aaaabbbbccccdddd 이런 문자열을 aaaa-bbbb-cccc-dddd 이 처럼 변경하거나<br>
     * 또는 aaaabbbb-ccccdddd 등 UUID 클래스의 fromString 메소드로 파싱되지 않는 문자열을 파싱합니다.
     * @return 문자열로부터 파싱된 UUID
     */
    public static UUID toUUID(String uuidStr) {

        // 먼저 - 지우기
        uuidStr = uuidStr.replace("-","").replace("\"","");

        // UUID로 파싱가능한 문자 수를 가지고 있는지 확인하기 (16자)
        if(uuidStr.length() != 32) {
            System.out.println(ChatColor.RED+uuidStr+"(은)는 UUID로 변환할 수 없습니다. (16자가 아닙니다.)");
            return null;
        }

        // - 새로 넣기
        String totalUUIDStr = uuidStr.substring(0,7)+"-"+uuidStr.substring(8,11)+"-"+uuidStr.substring(12,15)+"-"+uuidStr.substring(16,19)+"-"+uuidStr.substring(20,31);

        return UUID.fromString(totalUUIDStr);

    }

    public static String decodeBase64(String encodedStr) {
        byte[] bytes = Base64.getDecoder().decode(encodedStr);
        return new String(bytes);
    }

    public static String encodeBase64(String decodedStr) {
        byte[] bytes = Base64.getEncoder().encode(decodedStr.getBytes());
        return new String(bytes);
    }

    /**
     * 클릭, 호버 이벤트가 있는 이벤트 채팅을 생성 후 반환합니다.<br>
     * &a, &b, &c 등은 자동으로 시스템에서 색상으로 사용할 수 있는 문자열로 변환됩니다.
     * @param message 메세지
     * @param runCommand 메세지를 클릭 시 실행될 마인크래프트 명령어( '/' 를 붙이지 않으면 그냥 채팅으로 전송됩니다. )
     * @param hoverMessage 메세지에 마우스를 올려뒀을 때 나타날 메세지
     * @return 이벤트 기능이 담긴 채팅인 BaseComponent[]
     */
    public static BaseComponent[] createEventChat(String message, String runCommand, String hoverMessage) {

        TextComponent text = new TextComponent(message);
        ComponentBuilder builder = new ComponentBuilder(text);

        if(runCommand != null)
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, runCommand));

        if(hoverMessage != null) {
            List<Content> hoverMessages = new ArrayList<>();

            for(String str : hoverMessage.split("\n"))
                hoverMessages.add( new Text(toSystemColor(str)) );

            builder.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverMessages));
        }

        return builder.create();
    }

    /**
     * 시스템 상에서 출력 시에 색상이 적용되는 코드로 변경합니다.<br>
     * 예를들어, &a, &b, &c가 §a, §b, §c으로 변경됩니다.
     * @param str
     * @return
     */
    public static String toSystemColor(String str) {
        String[] colorChar = new String[] {
                "a","b","c","d","e","f","1","2","3","4","5","0","m","n","u","l"
        };

        for(String s : colorChar)
            str = str.replace("&"+s,"§"+s);

        return str;
    }

    /**
     * 시스템 상에서 출력 시에 색상이 적용되는 문자열을 인 게임에서 자주 쓰이는 색상 코드로 변경합니다.<br>
     * 예를들어, §a,§b,§c가 &a, &b, &c 로 변경됩니다.
     * @param str
     * @return
     */
    public static String toChatColor(String str) {
        String[] colorChar = new String[] {
                "a","b","c","d","e","f","1","2","3","4","5","0","m","n","u"
        };

        for(String s : colorChar)
            str = str.replace("§"+s,"&"+s);

        return str;
    }

    /**
     * 좌표를 보기 좋은 문자열로 바꿉니다.
     * @param location
     * @return
     */
    public static String toLocationStr(Location location) {
        StringBuilder sb = new StringBuilder();
        sb.append( Math.round(location.getX()) ).append(", ");
        sb.append( Math.round(location.getY()) ).append(", ");
        sb.append( Math.round(location.getZ()) );

        return sb.toString();
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
