package me.saehyeon.saehyeonlib.state;

import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.util.Filef;
import me.saehyeon.saehyeonlib.util.Serialize;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerState {

    static HashMap<UUID, HashMap<String, Object>> state = new HashMap<>();

    public static void set(Player player, String key, Object value) {

        HashMap<String, Object> map = state.getOrDefault(player.getUniqueId(), new HashMap<>());

        if(value != null)
            map.put(key,value);
        else
            remove(player,key);

        state.put(player.getUniqueId(), map);
    }

    public static void remove(Player player, String key) {

        HashMap<String, Object> map = state.getOrDefault(player.getUniqueId(), new HashMap<>());
        map.remove(key);

        state.put(player.getUniqueId(), map);

    }

    public static Object get(Player player, String key) {

        HashMap<String, Object> map = state.getOrDefault(player.getUniqueId(), new HashMap<>());

        return map.getOrDefault(key, null);
    }

    public static String getString(Player player, String key) {
        return get(player,key)+"";
    }

    public static int getInteger(Player player, String key) {
        return Integer.parseInt(get(player,key)+"");
    }

    public static float getFloat(Player player, String key) {
        return Float.parseFloat(get(player,key)+"");
    }

    public static double getDouble(Player player, String key) {
        return Double.parseDouble(get(player,key)+"");
    }

    public static boolean getBoolean(Player player, String key) {
        return Boolean.parseBoolean(get(player,key)+"");
    }

    public void save() {

        String str = Serialize.serialize(state);

        Filef.write(SaehyeonLib.instance.getDataFolder()+"\\playerState.json",str);

    }

    public void load() {

        List<String> lines = Filef.read(SaehyeonLib.instance.getDataFolder()+"\\playerState.json");

        if(lines != null && !lines.isEmpty()) {

            String str = lines.get(0);

            state = (HashMap<UUID, HashMap<String, Object>>) Serialize.deSerialize(str);

        } else {
            state = new HashMap<>();
        }

    }

}
