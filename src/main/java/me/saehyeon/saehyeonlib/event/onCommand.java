package me.saehyeon.saehyeonlib.event;

import me.saehyeon.saehyeonlib.dropitem.DropItemGUI;
import me.saehyeon.saehyeonlib.itemplacer.ItemPlacerGUI;
import me.saehyeon.saehyeonlib.itemplacer.GUIType;
import me.saehyeon.saehyeonlib.cool.CoolTime;
import me.saehyeon.saehyeonlib.gui.GUI;
import me.saehyeon.saehyeonlib.itemplacer.Rule;
import me.saehyeon.saehyeonlib.main.ErrorMessage;
import me.saehyeon.saehyeonlib.main.SaehyeonLib;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.role.ExPlayer;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.shop.Shop;
import me.saehyeon.saehyeonlib.state.State;
import me.saehyeon.saehyeonlib.timer.Timer;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.state.PlayerState;
import me.saehyeon.saehyeonlib.util.Playerf;
import me.saehyeon.saehyeonlib.util.Stringf;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class onCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals("s-lib")) {

            if(sender instanceof ConsoleCommandSender) {
                sender.sendMessage("§c인게임에서만 명령어를 입력할 수 있습니다.");
                return false;
            }
            Player p = (Player)sender;
            final Player finalP = p;

            Region targetRegion = Region.findByName(args.length > 2 ? args[2] : "");

            switch(args[0]) {
                case "debugMode":
                    SaehyeonLib.debugMode = !SaehyeonLib.debugMode;

                    p.sendMessage("debugMode: "+SaehyeonLib.debugMode);

                    break;

                case "debug":

                    if(args.length == 2) {
                        p = Bukkit.getPlayer(args[1]);

                        if(p == null)
                            p = (Player)sender;
                    }

                    sender.sendMessage("");
                    sender.sendMessage(p.getName()+"에 대한 정보:");
                    sender.sendMessage("적용 중인 역할: "+ Role.getByPlayer(p));
                    sender.sendMessage("적용 중인 쿨타임: "+CoolTime.findByPlayer(p));
                    sender.sendMessage("예외 플레이언가?: "+ ExPlayer.contains(p));
                    sender.sendMessage("현재 보고 있는 GUI 종류: "+GUI.getType(p));
                    sender.sendMessage("선택된 첫 번째 지점: "+ PlayerState.get(p, "pos1"));
                    sender.sendMessage("선택된 두 번째 지점: "+ PlayerState.get(p, "pos2"));
                    break;

                case "아이템":

                    ItemStack item = Playerf.getMainHand(p);

                    switch(args[1]) {
                        case "청소":

                            if(!sender.hasPermission("saehyeonlib.item.clean"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            Itemf.clearDroppedItems();

                            break;

                        case "이름변경":

                            if(!sender.hasPermission("saehyeonlib.item.rename"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            Itemf.setDisplayName(item, Stringf.toSystemColor(args[2]));

                            break;

                        case "설명추가":

                            if(!sender.hasPermission("saehyeonlib.item.addLore"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            Itemf.setLore(item, Stringf.toSystemColor(args[2]));

                            p.sendMessage("들고 있는 아이템의 설명을 추가했습니다.");

                            break;

                        case "설명제거":
                        case "설명삭제":

                            if(!sender.hasPermission("saehyeonlib.item.deleteLore"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            Itemf.removeLore(item, Integer.parseInt(args[2]));

                            p.sendMessage("들고 있는 아이템의 설명 중 §7"+args[2]+"번째 줄§f을 제거했습니다.");
                            break;

                        case "인첸트추가":

                            if(!sender.hasPermission("saehyeonlib.item.addEnchant"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            Enchantment enchantment = Enchantment.getByKey( NamespacedKey.minecraft( args[2] ));

                            // 알 수 없는 네임스페이스
                            if(enchantment == null) {
                                p.sendMessage("§c"+args[2]+"는 알 수 없는 인첸트 이름입니다.");
                                return false;
                            }

                            if(Stringf.isNumber(args[3])) {
                                p.sendMessage("§c"+args[3]+"는 자연수가 아닙니다. 인첸트 레벨은 숫자여야 합니다.");
                                return false;
                            }

                            item.addEnchantment(enchantment, Integer.parseInt( args[3] ));
                            break;

                        case "인첸트제거":

                            if(!sender.hasPermission("saehyeonlib.item.deleteEnchant"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            Enchantment enchantment2 = Enchantment.getByKey( NamespacedKey.minecraft( args[2] ));

                            // 알 수 없는 네임스페이스
                            if(enchantment2 == null) {
                                p.sendMessage("§c"+args[2]+"는 알 수 없는 인첸트 이름입니다.");
                                return false;
                            }

                            item.removeEnchantment(enchantment2);

                            break;

                        default:
                            p.sendMessage("§c사용법: /s-lib 아이템 [이름변경/설명추가/설명삭제/인첸트추가/인첸트제거]");
                            break;
                    }

                    break;

                case "지역":

                    switch (args[1]) {
                        case "추가":

                            if(!sender.hasPermission("saehyeonlib.region.add"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            Location pos1 = (Location)PlayerState.get(p,"pos1");
                            Location pos2 = (Location)PlayerState.get(p,"pos2");

                            if(pos1 == null || pos2 == null) {
                                p.sendMessage("§c지역을 추가하기 위해서는 첫번째 지점과 두번째 지점이 지정되어야 합니다. 지점은 나무도끼를 이용하여 설정할 수 있습니다.");
                                return false;
                            }

                            if(targetRegion != null) {
                                p.sendMessage("§c이미 같은 이름을 가지고 있는 지역이 등록되어 있습니다.");
                                return false;
                            }

                            Region r = new Region(args[2], pos1,pos2);

                            r.register();

                            p.sendMessage("§7"+r.getName()+"§f(이)라는 지역이 등록되었습니다.");
                            break;

                        case "삭제":

                            if(!sender.hasPermission("saehyeonlib.region.delete"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            if(targetRegion == null) {
                                SaehyeonLib.sendError(p, ErrorMessage.REGION_NOT_EXIST);
                                return false;
                            }

                            targetRegion.remove();
                            p.sendMessage("§7"+targetRegion.getName()+"§f(이)라는 지역이 제거되었습니다.");

                            break;

                        case "목록":

                            if(!sender.hasPermission("saehyeonlib.region.list"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            p.sendMessage("");
                            p.sendMessage("현재 등록된 지역은 다음과 같습니다: ");

                            final Player _p = p;

                            Region.getRegions().forEach(r3 ->
                                _p.sendMessage(" - "+r3.getName()+" ("+Stringf.toLocationStr(r3.getPosition()[0])+", "+Stringf.toLocationStr(r3.getPosition()[1])+")")
                            );

                            break;

                        default:

                            p.sendMessage("§c사용법: /s-lib 지역 [추가/삭제/목록]");
                            break;
                    }
                    break;

                case "상자":

                    switch (args[1]) {
                        case "아이템설정":

                            if(!sender.hasPermission("saehyeonlib.itemplacer.item.setting"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            if(targetRegion == null)
                                return SaehyeonLib.sendError(p, ErrorMessage.REGION_NOT_EXIST);

                            ItemPlacerGUI.openGUI(p, targetRegion, GUIType.ITEM_SETTING);

                            break;

                        case "아이템숨기기":

                            if(!sender.hasPermission("saehyeonlib.itemplacer.item.spread"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            if(targetRegion == null)
                                return SaehyeonLib.sendError(p, ErrorMessage.REGION_NOT_EXIST);

                            int amount = targetRegion.getItemPlacer().spreadItem();

                            if(amount != -1) {
                                p.sendMessage("§7"+targetRegion.getName()+"§f 지역에 아이템을 숨겼습니다. (총 "+amount+"개의 아이템을 숨김.)");
                            } else {
                                p.sendMessage("§c"+targetRegion.getName()+" 지역에 아이템을 숨기는 데에 실패했습니다. 서버버킷이나 채팅에 원인이 전송되었을 수 있습니다.");
                            }

                            break;

                        case "규칙추가":

                            if(!sender.hasPermission("saehyeonlib.itemplacer.rule.add"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            if(targetRegion == null)
                                return SaehyeonLib.sendError(p, ErrorMessage.REGION_NOT_EXIST);

                            if(args.length < 4) {
                                p.sendMessage("§c규칙을 입력해야 합니다.");
                                return false;
                            }

                            Rule rule = Rule.valueOf(args[3]);

                            targetRegion.getItemPlacer().addRule(new Rule[] { rule });
                            p.sendMessage("§7"+targetRegion.getName()+"§f 지역의 아이템 배치자에 §7"+rule+"§f 규칙이 추가되었습니다.");

                            break;

                        case "규칙삭제":

                            if(!sender.hasPermission("saehyeonlib.itemplacer.rule.delete"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            if(args.length < 4) {
                                p.sendMessage("§c규칙을 입력해야 합니다.");
                                return false;
                            }

                            Rule rule2 = Rule.valueOf(args[3]);

                            targetRegion.getItemPlacer().removeRule(new Rule[] { rule2 });
                            p.sendMessage("§7"+targetRegion.getName()+"§f 지역의 아이템 배치자에 적용되어 있던 §7"+rule2+"§f 규칙을 제거했습니다.");

                            break;

                        case "규칙목록":
                            
                            if(!sender.hasPermission("saehyeonlib.itemplacer.rule.list"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            p.sendMessage("");
                            p.sendMessage("§7"+targetRegion.getName()+"§f 지역 아이템 배치자에 적용된 규칙은 다음과 같습니다: ");
                            targetRegion.getItemPlacer().getRules().forEach(_r -> finalP.sendMessage(" - "+_r));

                            break;

                        case "원격":
                            if(!sender.hasPermission("saehyeonlib.itemplacer.remote"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            ItemPlacerGUI.openGUI(p, targetRegion, GUIType.REMOTE);

                            break;

                        default:
                            break;
                    }
                    break;

                case "예외플레이어":

                    switch (args[1]) {
                        case "추가":

                            if(!sender.hasPermission("saehyeonlib.explayer.add"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            UUID uuid = Playerf.getUUID(args[2]);

                            if(uuid == null) {
                                p.sendMessage("§c예외 플레이어 추가에 실패했습니다. "+args[2]+"(이)라는 이름을 가진 플레이어의 UUID를 얻지 못했습니다.");
                                return false;
                            }

                            ExPlayer.add(uuid);
                            p.sendMessage("UUID §7"+uuid+"§f(을)를 예외 플레이어 목록에 추가했습니다.");

                            break;

                        case "삭제":

                            if(!sender.hasPermission("saehyeonlib.explayer.delete"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            UUID uuid2 = Playerf.getUUID(args[2]);

                            if(uuid2 == null) {
                                p.sendMessage("§c예외 플레이어 추가에 실패했습니다. "+args[2]+"(이)라는 이름을 가진 플레이어의 UUID를 얻지 못했습니다.");
                                return false;
                            }

                            ExPlayer.remove(uuid2);
                            p.sendMessage("UUID §7"+uuid2+"§f(을)를 예외 플레이어 목록에서 제거했습니다.");

                            break;

                        case "목록":

                            if(!sender.hasPermission("saehyeonlib.explayer.list"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            break;

                        default:
                            break;
                    }
                    break;

                case "머리":

                    if(!sender.hasPermission("saehyeonlib.getHead"))
                        return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                    break;

                case "모자":

                    if(!sender.hasPermission("saehyeonlib.hat")) {
                        return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);
                    }

                    ItemStack[] items = p.getInventory().getArmorContents();
                    p.getInventory().setArmorContents(new ItemStack[] { items[0], items[1], items[2], Playerf.getMainHand(p) });
                    break;

                case "위치":

                    switch (args[1]) {
                        case "저장":

                            if(!sender.hasPermission("saehyeonlib.location.save"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            State.setDefault("warps",new HashMap<String, Location>());

                            ((HashMap<String, Location>)State.get("warps")).put(args[2],p.getLocation());

                            p.sendMessage("현재 위치를 §7"+args[2]+"§f(으)로 지정했습니다.");

                            break;

                        case "목록":

                            if(!sender.hasPermission("saehyeonlib.location.list"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);


                            HashMap<String, Location> map = (HashMap<String, Location>) State.get("warps", new HashMap<>());

                            p.sendMessage("");
                            p.sendMessage("현재 등록된 지역은 다음과 같습니다:");

                            map.forEach((key, value) -> {
                                finalP.sendMessage(" - "+key+": "+Stringf.toLocationStr(value));
                            });

                            break;

                        case "이동":

                            if(!sender.hasPermission("saehyeonlib.location.teleport"))
                                return SaehyeonLib.sendError(p, ErrorMessage.NO_PERMISSION);

                            Location warpLoc = ((HashMap<String, Location>) State.get("warps", new HashMap<>())).getOrDefault(args[2],null);

                            if(warpLoc == null) {
                                p.sendMessage(args[2]+"(은)는 §c알 수 없는 위치입니다.");
                                return false;
                            }

                            p.teleport( warpLoc );
                            p.sendMessage("§7"+args[2]+"§f(으)로 이동합니다!");

                            break;

                        default:
                            break;
                    }
                    break;

                case "역할":

                    switch (args[1]) {
                        case "목록":

                            if(args.length >= 3) {

                                Player target = Bukkit.getPlayer(args[2]);

                                if(target != null) {

                                    p.sendMessage("");
                                    p.sendMessage("§7"+target.getName()+"§f의 역할은 다음과 같습니다:");

                                    Role role = Role.getByPlayer(target);

                                    if(role != null) {
                                        p.sendMessage(" - 이름: "+role.getName()+" | 배정되어야 하는 인원 수: "+role.getNeedPeopleAmount()+" | 현재 인원 수: "+role.getPlayers().size()+"명");
                                    } else {
                                        p.sendMessage("§7이 플레이어는 역할이 없습니다.");
                                    }


                                } else {
                                    p.sendMessage("§c"+args[2]+"(이)라는 플레이어는 서버에 없습니다.");
                                }

                                return false;
                            }

                            p.sendMessage("");
                            p.sendMessage("현재 등록된 역할 목록은 다음과 같습니다:");

                            Role.roles.forEach(role -> finalP.sendMessage(" - 이름: "+role.getName()+" | 배정되어야 하는 인원 수: "+role.getNeedPeopleAmount()+"명 (0일 시 제한없음)"+" | 현재 인원 수: "+role.getPlayers().size()+"명"));

                            break;

                        case "설정":
                            if(args.length >= 4) {

                                Player target = Bukkit.getPlayer(args[2]);

                                if(target != null) {

                                    Role role = Role.getByName(args[3]);

                                    if(role != null) {

                                        role.add(target);
                                        p.sendMessage("§7"+target.getName()+"§f에게 §7"+role.getName()+"§f(을)를 부여했습니다.");

                                    } else {
                                        p.sendMessage("§c"+args[3]+"(이)라는 이름을 가진 역할을 찾을 수 없습니다.");
                                    }

                                } else {
                                    p.sendMessage("§c"+args[2]+"(이)라는 플레이어는 서버에 없습니다.");
                                }

                                return false;
                            }

                            break;

                        case "제거":
                            if(args.length >= 4) {

                                Player target = Bukkit.getPlayer(args[2]);

                                if(target != null) {

                                    Role role = Role.getByName(args[3]);

                                    if(role != null && role.getPlayers().contains(target)) {

                                        role.remove(target);
                                        p.sendMessage("§7"+target.getName()+"§f에게서 §7"+role.getName()+"§f(을)를 제거했습니다.");

                                    } else {
                                        p.sendMessage("§c"+target.getName()+"에게는 "+args[3]+"(이)라는 역할이 부여되어 있지 않습니다.");
                                    }

                                } else {
                                    p.sendMessage("§c"+args[2]+"(이)라는 플레이어는 서버에 없습니다.");
                                }

                                return false;
                            }

                            break;

                        case "모두재분배":

                            Role.applyRandomAll();
                            p.sendMessage("모든 역할을 모든 플레이어에게 재분배합니다.");

                            break;
                    }
                    break;

                case "드롭":

                    switch (args[1]) {
                        case "시작":

                            if(targetRegion == null)
                                return SaehyeonLib.sendError(p, ErrorMessage.REGION_NOT_EXIST);

                            if(args.length < 3) {
                                p.sendMessage("§c아이템을 떨구기 전, 땅에 떨어진 아이템을 청소하고 진행할 것 인지 결정해야 합니다.");
                                p.sendMessage("§c사용법: /s-lib 드롭 시작 [지역이름] [true/false]");
                                return false;
                            }

                            targetRegion.getDropItem().StartDrop(Boolean.parseBoolean(args[2]));

                            p.sendMessage("§7"+targetRegion.getName()+"§f 지역에 아이템 드롭을 시작했습니다. §7(주기: "+targetRegion.getDropItem().getDropPriod()+"초, 횟수: "+targetRegion.getDropItem().getDropTime()+"번)");
                            break;

                        case "중지":

                            if(targetRegion == null)
                                return SaehyeonLib.sendError(p, ErrorMessage.REGION_NOT_EXIST);

                            targetRegion.getDropItem().StopDrop();

                            p.sendMessage("§7"+targetRegion.getName()+"§f 지역의 아이템 드롭을 중지했습니다.");

                            break;

                        case "아이템설정":

                            DropItemGUI.open(p, targetRegion.getDropItem());

                            break;

                        case "주기설정":

                            if(!Stringf.isNumber(args[3])) {
                                p.sendMessage("§c주기는 자연수여야 합니다.");
                                return false;
                            }

                            targetRegion.getDropItem().setDropPriod(Long.parseLong(args[3]));
                            p.sendMessage("§7"+targetRegion.getName()+"§f 지역의 아이템 드롭 주기를 §7"+targetRegion.getDropItem().getDropPriod()+"초§f로 설정했습니다.");

                            break;

                        case "횟수설정":

                            if(!Stringf.isNumber(args[3])) {
                                p.sendMessage("§c횟수는 자연수여야 합니다.");
                                return false;
                            }

                            targetRegion.getDropItem().setDropTime(Integer.parseInt(args[3]));
                            p.sendMessage("§7"+targetRegion.getName()+"§f 지역의 아이템 드롭 횟수를 §7"+targetRegion.getDropItem().getDropTime()+"번§f으로 설정했습니다.");

                            break;

                        case "목록":

                            p.sendMessage("");
                            p.sendMessage("아이템 드롭 목록은 다음과 같습니다:");

                            Region.getRegions().forEach(region -> {

                                String rName    = region.getName();
                                Location pos1   = region.getPosition()[0];
                                Location pos2   = region.getPosition()[0];
                                int itemAmount  = region.getDropItem().getItems().size();
                                long dropPriod  = region.getDropItem().getDropPriod();
                                int dropTime    = region.getDropItem().getDropTime();

                                finalP.sendMessage(" - 지역이름: "+rName+" | 범위: "+Stringf.toLocationStr(pos1)+"~"+Stringf.toLocationStr(pos2)+" | 드롭할 아이템 수(슬롯 수): "+itemAmount+" | 드롭 주기: "+dropPriod+"초 | 드롭 횟수: "+dropTime+"번 | 아이템을 드롭중인가?: "+(region.getDropItem().isDropping() ? "네" : "아니요"));

                            });

                            break;

                        default:

                            p.sendMessage("§c사용법: /s-lib [드롭] [아이템설정/횟수설정/주기설정/목록]");
                            break;
                    }
                    break;

                case "t":

                    Playerf.removeItem(p, "§6옆전",10);

                    break;
            }
        }
        return false;
    }
}
