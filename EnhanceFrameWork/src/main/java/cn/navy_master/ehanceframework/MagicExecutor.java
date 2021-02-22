package cn.navy_master.ehanceframework;

import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * 法术执行器类
 * 用于方便地注册新的法术
 * @author navy_master
 * @version 1.0.0
 */
public abstract class MagicExecutor {
    private final boolean enhance;
    private MagicManager belong_to;
    List<Class<?extends Event>> trigger;
    private final int cold_time;
    protected void setBelong_to(MagicManager belong_to) {
        this.belong_to = belong_to;
    }
    public boolean isEnhanceable() {
        return enhance;
    }
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
        for(Class<? extends Event> cl:l)
        {
            try{
                UnsupportedEventException.have_getPlayer(cl);
            }catch (UnsupportedEventException e){
                e.printStackTrace();
            }
        }
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
        try{
            if(!UnsupportedEventException.have_getPlayer(l))
            {
                throw new UnsupportedEventException("The new MagicExecutor has unsupported trigger event.");
            }
        }catch (UnsupportedEventException e){
            e.printStackTrace();
        }
        trigger=new ArrayList<>();
        trigger.add(l);
        cold_time=c;
        enhance=whenEnhance;
    }
    /**
     * 校验触发事件
     * @param e 事件类型
     * @return 事件是否可以触发本法术
     */
    protected boolean can_trigger_by(Class<? extends Event> e){
        for(Class<? extends Event> x:trigger){
            if(x.isAssignableFrom(e))return true;
        }
        return false;
    }
    /**
     * 法术执行函数，如果新建法术，必须重载此函数
     * @param Caster 施法者
     * @return 施法成功与否
     */
    public abstract boolean runMagic(LivingEntity Caster);
    /**
     * 装备校验方法，如果希望在非主手的位置调用法术效果，请重载本方法
     * @param es 触发时，施法物品所在的装备槽
     * @return 是否触发
     */
    public boolean checkEquipmentSlot(EquipmentSlot es){
        return es == EquipmentSlot.HAND;
    }
    /**
     * 包装过的施法执行函数，判断完冷却时间后，会执行runMagic函数
     * @param Caster 施法者
     */
    protected void play_magic(LivingEntity Caster) {
        if (Player.class.isAssignableFrom(Caster.getClass())) {
            PlayerMagic PML = PlayerMagic.player_magics.get(Caster);
            if (PML.getCool_time() == 0) {
                boolean suc;
                suc = runMagic(Caster);
                if (suc) {
                    PML.setCool_time(cold_time);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (PML.getCool_time() > 0) {
                                PML.setCool_time(PML.getCool_time() - 1);
                            } else {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(belong_to.plugin_INSTANCE, 1, 1);
                }
            }
        } else {
            runMagic(Caster);
        }
    }
    /**
     * 无冷却调用
     * @param Caster 施法者
     * @return 施法成功与否
     */
    protected boolean play_magic_without_cool_time(LivingEntity Caster){
        return runMagic(Caster);
    }
}