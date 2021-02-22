package cn.navy_master.WizardStaff;

import cn.navy_master.ehanceframework.MagicManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.navy_master.WizardStaff.WizardStaffMain.debug_mode;

/**
 * 插件的主监听器
 * 监听绝大多数事件
 * @author navy_master
 * @version 1.1.1
 * @see Listener
 */
public class WizardStaffListener implements Listener {
    List<String> playerList;
    /**
     * 检查玩家进入服务器的事件
     * 检查玩家是否是本版本首次进入服务器，如果是，发送phb更新提醒消息
     * @param e 捕获的玩家进入服务器事件
     */
    @EventHandler
    public void handle_enter(PlayerJoinEvent e){
        if(!WizardStaffMain.player_magics.containsKey(e.getPlayer())) {
            if(debug_mode){
                for(String s: MagicManager.MagicList.keySet()){
                    ItemStack is=new ItemStack(Material.STICK);
                    ItemMeta im=is.getItemMeta();
                    List<String> l=new ArrayList<>();
                    l.add(s);
                    im.setLore(l);
                    is.setItemMeta(im);
                    e.getPlayer().getInventory().addItem(is);
                }
                ItemStack is=new ItemStack(Material.STICK);
                ItemMeta im=is.getItemMeta();
                List<String> l=new ArrayList<>();
                l.add("FIRE_BALL<4/4>");
                im.setLore(l);
                is.setItemMeta(im);
                e.getPlayer().getInventory().addItem(is);
            }
            if(Objects.isNull(playerList)){
                if(Objects.isNull(WizardStaffMain.FC.get("PlayerList"))){
                    WizardStaffMain.FC.set("PlayerList",new ArrayList<>());
                }
                playerList=(List<String>) WizardStaffMain.FC.getList("PlayerList");
            }
            if(!playerList.contains(e.getPlayer().getDisplayName())){
                playerList.add(e.getPlayer().getDisplayName());
                e.getPlayer().sendMessage(ChatColor.YELLOW +"欢迎来到守夜人服务器，你还没有在本次更新后登陆本服务器，这意味着玩家手册已经更新，建议你使用/phb获取新的玩家手册");
            }
            WizardStaffMain.FC.set("PlayerList",playerList);
        }
    }

    /**
     * 更改魔法飞弹造成的伤害，使此类雪球可以造成2伤害
     * @param e 捕获的弹射物击打生物事件
     */
    @EventHandler
    public void handle_magic_missile(ProjectileHitEvent e)
    {
        if(Objects.equals(e.getEntity().getCustomName(), "魔法飞弹")){
            if(LivingEntity.class.isAssignableFrom(Objects.requireNonNull(e.getHitEntity()).getClass())) {
                ((LivingEntity) e.getHitEntity()).setNoDamageTicks(0);
                ((LivingEntity) e.getHitEntity()).damage(2, (Entity) e.getEntity().getShooter());
            }
        }
    }
    public static List<LivingEntity> tp_choice;
    /**
     * 检测到正在开启的gui是传送法术的gui时<br>
     * 进行的针对性处理
     * @param e 捕获点击gui事件
     */
    @EventHandler
    public void handle_inventory(InventoryClickEvent e){
        if(e.getWhoClicked().getOpenInventory().getTitle().equals("teleport_target")){
            int slot=e.getRawSlot();
            int page=e.getInventory().getItem(49).getAmount();
            int start_at;
            if(slot<45&&!Objects.isNull(e.getInventory().getItem(slot))){
                start_at=45*(page-1);
                e.getWhoClicked().teleport(tp_choice.get(start_at+slot));
            }else if(slot==45&&!Objects.isNull(e.getInventory().getItem(45))){
                page--;
                start_at=45*(page-1);
                SkullMeta im;
                ItemStack is;
                int i;
                for(i=start_at;i<start_at+45&&i<tp_choice.size();i++)
                {
                    im=(SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                    if(Player.class.isAssignableFrom(e.getClass()))
                        im.setOwningPlayer((Player)tp_choice.get(i));
                    else
                        im.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_"+tp_choice.get(i).getName()));
                    im.setDisplayName(tp_choice.get(i).getName());
                    is=new ItemStack(Material.PLAYER_HEAD);
                    is.setItemMeta(im);
                    e.getInventory().setItem(i-start_at,is);
                }
                if(i<start_at+45){
                    for(;i<start_at+45;i++)
                    {
                        e.getInventory().setItem(i-start_at,null);
                    }
                }
                if(start_at+45<tp_choice.size()){
                    is=new ItemStack(Material.GRASS_BLOCK);
                    ItemMeta imx=is.getItemMeta();
                    imx.setDisplayName("下一页");
                    is.setItemMeta(imx);
                    e.getInventory().setItem(53,is);
                }else{
                    e.getInventory().setItem(53,null);
                }
                if(page!=1){
                    is=new ItemStack(Material.GRASS_BLOCK);
                    ItemMeta imx=is.getItemMeta();
                    imx.setDisplayName("上一页");
                    is.setItemMeta(imx);
                    e.getInventory().setItem(45,is);
                }else{
                    e.getInventory().setItem(45,null);
                }
                e.getInventory().getItem(49).setAmount(page);
            }else if(slot==53&&!Objects.isNull(e.getInventory().getItem(53))){
                page++;
                start_at=45*(page-1);
                SkullMeta im;
                ItemStack is;
                int i;
                for(i=start_at;i<start_at+45&&i<tp_choice.size();i++)
                {
                    im=(SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                    if(Player.class.isAssignableFrom(e.getClass()))
                        im.setOwningPlayer((Player)tp_choice.get(i));
                    else
                        im.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_"+tp_choice.get(i).getName()));
                    im.setDisplayName(tp_choice.get(i).getName());
                    is=new ItemStack(Material.PLAYER_HEAD);
                    is.setItemMeta(im);
                    e.getInventory().setItem(i-start_at,is);
                }
                if(i<start_at+45){
                    for(;i<start_at+45;i++)
                    {
                        e.getInventory().setItem(i-start_at,null);
                    }
                }
                if(start_at+45<tp_choice.size()){
                    is=new ItemStack(Material.GRASS_BLOCK);
                    ItemMeta imx=is.getItemMeta();
                    imx.setDisplayName("下一页");
                    is.setItemMeta(imx);
                    e.getInventory().setItem(53,is);
                }else{
                    e.getInventory().setItem(53,null);
                }
                if(page!=1){
                    is=new ItemStack(Material.GRASS_BLOCK);
                    ItemMeta imx=is.getItemMeta();
                    imx.setDisplayName("上一页");
                    is.setItemMeta(imx);
                    e.getInventory().setItem(45,is);
                }else{
                    e.getInventory().setItem(45,null);
                }
                e.getInventory().getItem(49).setAmount(page);
            }
            e.setCancelled(true);
        }
    }
    Random r=new Random();
    /**
     * 附魔时随机法术生成
     * 概率默认为10%
     * @param e 捕获的附魔事件
     */
    @EventHandler
    public void handle_enhance(EnchantItemEvent e) {
        int a = r.nextInt(100);
        if(a<10||WizardStaffMain.debug_mode) {
            ItemStack is = e.getItem();
            List<String> Lore;
            try{
                Lore= is.getItemMeta().getLore();
                if(Objects.isNull(Lore)){
                    Lore= new ArrayList<>();
                }
            }catch (NullPointerException ex){
                Lore= new ArrayList<>();
            }
            ItemMeta im = is.getItemMeta();
            a=r.nextInt(MagicManager.enhance_registered);
            int i=0;
            /*if(EnhanceFrameWork.debug_mode)
                Bukkit.getLogger().info(a+" "+MagicExecutor.enhance_registered);*/
            for(String s:MagicManager.MagicList.keySet()){
                if(MagicManager.MagicList.get(s).isEnhanceable())
                    if(a==i++){
                        Bukkit.getLogger().info(s);
                        Lore.add(s);
                        break;
                    }
            }
            im.setLore(Lore);
            is.setItemMeta(im);
        }
    }

    @EventHandler
    public void default_magic_handle(PlayerInteractEvent e){
        MagicManager.handle_all(e,this);
    }
}
