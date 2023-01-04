package me.saehyeon.saehyeonlib.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Playerf {

    public static final float DEFAULT_WALK_SPEED = 0.2f;

    public static String getSkinURL(String playerName) {
        String mojangAPIStr = URLUtility.getContent("https://api.mojang.com/users/profiles/minecraft/"+playerName);

        // 모장 API에서 받은 문자열 JSON 파싱
        JsonObject jsonObject = new JsonParser().parse(mojangAPIStr).getAsJsonObject();

        // 파싱된 JSON으로부터 정보 저장 (필요한거: uuid, properties의 value 값
        String id = jsonObject.get("id").toString().replace("\"","");

        // 모장 API에서 스킨 정보 가져오기
        String mojangStr2 = URLUtility.getContent("https://sessionserver.mojang.com/session/minecraft/profile/"+id);

        // 문자열을 JSON으로 파싱 후, properties배열의 첫번째 -> value 값을 가져오기
        JsonArray properties = new JsonParser().parse(mojangStr2).getAsJsonObject().get("properties").getAsJsonArray();
        JsonElement value = properties.get(0).getAsJsonObject().get("value");

        // value Base64 해독하기
        String decodedValue = Stringf.decodeBase64(value.toString().replace("\"",""));

        // 해독된 value에서 textures -> SKIN -> url 값 얻기
        JsonObject textures = new JsonParser().parse(decodedValue).getAsJsonObject().getAsJsonObject("textures");
        JsonObject SKIN     = textures.getAsJsonObject("SKIN");
        JsonElement url     = SKIN.get("url");


        return url.toString().replace("\"","");
    }

    public static UUID getUUID(String playerName) {

        // 만약 서버에 있는 사람이라면 해당 플레이어의 UUID를 반환
        Player player = Bukkit.getPlayer(playerName);

        if(player != null)
            return player.getUniqueId();

        // 만약 playerName이 UUID로 변환될 수 있다면 해당 UUID를 반환
        try { return UUID.fromString(playerName); } catch (Exception ignored) { }

        // 서버에 없는 사람이라면 모장 API로부터 가져오기
        String apiStr = URLUtility.getContent("https://api.mojang.com/users/profiles/minecraft/"+playerName);

        // API로부터 가져온 정보가 없음 -> null 반환
        if(apiStr.equals(""))
            return null;

        // API로부터 가져온 정보가 있다면 해당 정보에서 uuid를 찾고 UUID형으로 바꾼 후 반환
        String uuidStr = new JsonParser().parse(apiStr).getAsJsonObject().get("id").toString().replace("\"", "");

        return Stringf.toUUID(uuidStr);
    }

    public static ItemStack getMainHand(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public static ItemStack getLeftHand(Player player) {
        return player.getInventory().getItemInOffHand();
    }

    public static void setArmor(Player player, ItemStack head, ItemStack chestplate, ItemStack leg, ItemStack boots) {
        player.getInventory().setArmorContents(new ItemStack[] { boots, leg, chestplate, head });
    }

    public static void applyEffect(Player player, PotionEffectType effectType, int time, int level, boolean hideParticle) {
        player.addPotionEffect(new PotionEffect(effectType,time,level,true,hideParticle));
    }

    public static void applyEffect(List<Player> players, PotionEffectType effectType, int time, int level, boolean hideParticle) {
        players.forEach(p -> applyEffect(p, effectType,time,level,hideParticle));
    }

    public static void sendActionbarAll(String actionbar) {
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbar)));
    }

    /**
     * 이벤트가 있는 채팅을 특정 플레이어게 전송합니다.<br>
     * 예를들어 이렇게 사용할 수 있습니다: <br>
     * <pre>
     *     sendEventChat(플레이어, Arrays.as(
     *      "안녕하세요. ", Stringf.createEventChat("&u여기를 클릭&f", "/gamemode creative", null), "를 클릭하면 크리에이티브가 되요."
     *     ));
     * </pre>
     * @param player 메세지를 전송할 플레이어
     * @param messages String형 변수, BaseComponent형 변수가 올 수 있는 배열입니다.<br>String, BaseComponent, BaseComponent[]가 아닌 데이터형의 인자는 무시됩니다.
     */
    public static void sendEventChat(Player player, List<Object> messages) {
        ComponentBuilder builder = new ComponentBuilder();

        for(Object message : messages) {

            if(message instanceof String)
                builder.append(message+"");

            if(message instanceof BaseComponent)
                builder.append( (BaseComponent)message );

            if(message instanceof BaseComponent[])
                builder.append( (BaseComponent[])message );
        }

    }

    public static void sendTitle(List<Player> players, String title, String subtitle, int fadein, int stay, int fadeout) {
        players.forEach(p -> p.sendTitle(title,subtitle,fadein,stay,fadeout));
    }

    public static void sendTitle(List<Player> players, String title, String subtitle) {
        players.forEach(p -> p.sendTitle(title,subtitle));
    }

    public static void sendTitleAll(String title, String subtitle, int fadein, int stay, int fadeout) {
        sendTitle(new ArrayList<>( Bukkit.getOnlinePlayers() ),title,subtitle,fadein, stay, fadeout);
    }

    public static void sendTitleAll(String title, String subtitle) {
        sendTitle(new ArrayList<>( Bukkit.getOnlinePlayers() ),title,subtitle);
    }


}
