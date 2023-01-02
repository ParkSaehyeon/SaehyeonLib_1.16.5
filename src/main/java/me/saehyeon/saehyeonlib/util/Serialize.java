package me.saehyeon.saehyeonlib.util;

import me.saehyeon.saehyeonlib.main.LogLevel;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class Serialize {
    public static String serialize(Object obj) {

        try {

            byte[] serializedMember;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (BukkitObjectOutputStream oos = new BukkitObjectOutputStream(baos)) {

                    oos.writeObject(obj);

                    serializedMember = baos.toByteArray();

                }
            }

            return Base64.getEncoder().encodeToString(serializedMember);

        } catch (Exception e) {

            SaehyeonLib.log(LogLevel.ERROR,"직렬화 도중 오류가 발생했습니다. \n"+e);
            return "";

        }

    }

    public static Object deSerialize(String serializedStr) {

        try {

            byte[] serializedMember = Base64.getDecoder().decode(serializedStr);

            try (ByteArrayInputStream baos = new ByteArrayInputStream(serializedMember)) {
                try (BukkitObjectInputStream oos = new BukkitObjectInputStream(baos)) {

                    return oos.readObject();

                }
            }

        } catch (Exception e) {

            SaehyeonLib.log(LogLevel.ERROR,"비직렬화 도중 오류가 발생했습니다. \n"+e);
            return null;

        }

    }
}
