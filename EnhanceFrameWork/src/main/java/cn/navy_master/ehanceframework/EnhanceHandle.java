package cn.navy_master.ehanceframework;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * 监听器，监听附魔事件和配置初始冷却
 * @author navy_master
 * @version 1.1.1
 * @see org.bukkit.event.Listener
 */
public class EnhanceHandle implements Listener {

    /**
     * 检查玩家进入服务器的事件
     * 检查玩家是否是服务器启动后首次进入服务器，如果是，为其配置冷却
     * @param e 捕获的玩家进入服务器事件*/
    @EventHandler
    public void handle_enter(PlayerJoinEvent e){
        if(!PlayerMagic.player_magics.containsKey(e.getPlayer())) {
            PlayerMagic PML=new PlayerMagic();
            PlayerMagic.player_magics.put(e.getPlayer(),PML);
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
        if(a<10//|| EnhanceFrameWork.debug_mode
        ) {
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
}
