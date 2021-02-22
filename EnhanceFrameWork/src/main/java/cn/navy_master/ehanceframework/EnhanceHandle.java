package cn.navy_master.ehanceframework;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
}
