package cn.navy_master.enhanceframework;

import org.bukkit.entity.Player;

import java.util.*;
/**
 * 玩家法术参数类<br>
 * 目前仅保存冷却时间，其他等待后续开发
 * @author navy_master
 * @version 1.0.0
 */
public class PlayerMagic
{
    public static HashMap<Player, PlayerMagic> getPlayer_magics() {
        return player_magics;
    }

    public static HashMap<Player, PlayerMagic> player_magics =new HashMap<>();
    public int getCool_time() {
        return cool_time;
    }

    public void setCool_time(int cool_time) {
        this.cool_time = cool_time;
    }

    private int cool_time =0;
}