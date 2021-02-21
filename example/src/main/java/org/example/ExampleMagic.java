package org.example;

import cn.navy_master.ehanceframework.MagicExecutor;
import cn.navy_master.ehanceframework.MagicManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 这是一个EnhanceFrameWork的使用例子
 * @author navy_master
 * @version 1.0.0
 * @see org.bukkit.plugin.java.JavaPlugin
 */
public class ExampleMagic extends JavaPlugin implements Listener {
    /**
     * 初始化函数，这里注册了一个名为“FIRE_BALL”的法术，也就是火球术
     */
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this);
        MagicManager mm=new MagicManager();
        MagicExecutor FIRE_BALL = new MagicExecutor(PlayerInteractEvent.class,20, true) {
            /**
             * 你需要重载执行器的runMagic函数
             * @param Caster 施法者
             * @return 施法成功与否
             */
            @Override
            public boolean runMagic(LivingEntity Caster) {
                World w = Caster.getWorld();
                Location loc = Caster.getLocation();
                loc.add(0, 1, 0);
                Fireball fb = (Fireball) w.spawnEntity(loc, EntityType.FIREBALL);
                fb.setIsIncendiary(true);
                fb.setShooter(Caster);
                return true;
            }
        };
        mm.register_magic("FIRE_BALL", FIRE_BALL);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    /**
     * 由于对于动态生成监听器的尝试失败了，我们不得不手动监听后传递
     * 如果有多个插件使用EnhanceFrameWork，我衷心地祈祷你们没有注册相同的监听，因为这会导致法术多次执行
     * @param e
     */
    @EventHandler
    public void handle_fire_ball(PlayerInteractEvent e){
        MagicManager.handle_all(e);
    }
}
