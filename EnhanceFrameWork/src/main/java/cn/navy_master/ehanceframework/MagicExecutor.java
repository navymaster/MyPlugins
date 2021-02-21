package cn.navy_master.ehanceframework;

import cn.navy_master.ehanceframework.EnhanceFrameWork;
import cn.navy_master.ehanceframework.PlayerMagic;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * 法术执行器类
 * 用于方便地注册新的法术
 * @author navy_master
 * @version 1.0.0
 */
public abstract class MagicExecutor {
    List<Class<?extends Event>> trigger;
    private final int cold_time;

    public boolean can_trigger_by(Class<?> e){
        for(Class x:trigger){
            if(x.isAssignableFrom(e))return true;
        }
        return false;
    }
    public boolean isEnhanceable() {
        return enhance;
    }

    private final boolean enhance;
    /**
     * 法术执行器的构造函数
     * @param c 冷却时间
     */
    @Deprecated
    public MagicExecutor(int c){
        trigger=new ArrayList<>();
        trigger.add(PlayerInteractEvent.class);
        cold_time=c;
        enhance=false;
    }
    /**
     * 法术执行器的构造函数,不自动配置触发器
     * @param c 冷却时间
     * @param whenEnhance 是否在附魔时考虑（如果希望仅在自行设置的情况下获得法术词缀，则false）
     */
    @Deprecated
    public MagicExecutor(int c,boolean whenEnhance){
        trigger=new ArrayList<>();
        trigger.add(PlayerInteractEvent.class);
        cold_time=c;
        enhance=whenEnhance;
    }
    /**
     * 法术执行器的构造函数
     * @param l 所有触发事件类型
     * @param c 冷却时间
     * @param whenEnhance 是否在附魔时考虑（如果希望仅在自行设置的情况下获得法术词缀，则false）
     */
    public MagicExecutor(List<Class<? extends Event>> l,int c,boolean whenEnhance){
        trigger=l;
        cold_time=c;
        enhance=whenEnhance;
    }
    /**
     * 法术执行器的构造函数
     * @param l 单一触发事件类型
     * @param c 冷却时间
     * @param whenEnhance 是否在附魔时考虑（如果希望仅在自行设置的情况下获得法术词缀，则false）
     */
    public MagicExecutor(Class<? extends Event> l,int c,boolean whenEnhance){
        trigger=new ArrayList<>();
        trigger.add(l);
        cold_time=c;
        enhance=whenEnhance;
    }

    /**
     * 法术执行函数，如果新建法术，必须重载此函数
     * @param Caster 施法者
     * @return 施法成功与否
     */
    public boolean runMagic(LivingEntity Caster){return true;}

    /**
     * 包装过的施法执行函数，判断完冷却时间后，会执行runMagic函数
     * @param Caster 施法者
     */
    public void play_magic(LivingEntity Caster){
        if(Player.class.isAssignableFrom(Caster.getClass())) {
            PlayerMagic PML = PlayerMagic.player_magics.get(Caster);
            if (PML.getCool_time()== 0) {
                boolean suc = runMagic(Caster);
                if (suc) {
                    PML.setCool_time(cold_time) ;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (PML.getCool_time()> 0) {
                                PML.setCool_time(PML.getCool_time()-1);
                            } else {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(EnhanceFrameWork.INSTANCE, 1, 1);
                }
            }
        }else{
            runMagic(Caster);
        }
    }
}