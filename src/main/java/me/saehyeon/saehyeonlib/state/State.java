package me.saehyeon.saehyeonlib.state;

import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.util.Filef;
import me.saehyeon.saehyeonlib.util.Serialize;

import java.util.HashMap;
import java.util.List;

public class State {
    static HashMap<String, Object> state = new HashMap<>();

    public static void set(String key, Object value) {

        if(value != null)
            state.put(key,value);
        else
            remove(key);

        state.put(key, value);
    }

    public static void setDefault(String key, Object value) {
        if(!state.containsKey(key))
            set(key, value);
    }

    public static void remove(String key) {

        state.remove(key);

    }

    public static Object get(String key) {

        return state.getOrDefault(key, null);
    }

    public static Object get(String key, Object def) {
        return state.getOrDefault(key, def);
    }

    public static boolean contains(String key) {
        return get(key) != null;
    }

    public static String getString(String key) {
        return get(key)+"";
    }

    public static int getInteger(String key) {
        return Integer.parseInt(get(key)+"");
    }

    public static float getFloat(String key) {
        return Float.parseFloat(get(key)+"");
    }

    public static double getDouble(String key) {
        return Double.parseDouble(get(key)+"");
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key)+"");
    }

    public void save() {

        String str = Serialize.serialize(state);

        Filef.write(SaehyeonLib.instance.getDataFolder()+"\\state.json",str);

    }

    public void load() {

        List<String> lines = Filef.read(SaehyeonLib.instance.getDataFolder()+"\\state.json");

        if(lines != null && !lines.isEmpty()) {

            String str = lines.get(0);

            state = (HashMap<String, Object>) Serialize.deSerialize(str);

        } else {

            state = new HashMap<>();

        }

    }
}
