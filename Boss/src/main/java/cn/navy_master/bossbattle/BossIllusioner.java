package cn.navy_master.bossbattle;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 对幻术大师的包装，给予其更多技能，更多血量
 */
public class BossIllusioner{
    private static List<BossIllusioner> instances=new ArrayList<>();
    private final Illusioner value;
    private final BossBar bossBar;
    private final BukkitRunnable synchronization;
    private int spell_time_cnt =200;
    protected boolean isDead(){
        return value.isDead();
    }
    private static void clear()
    {
        instances.removeIf(BossIllusioner::isDead);
    }

    /**
     * 判断一个实体是不是此种boss
     * @param i 目标实体
     * @return 是不是此种boss
     */
    public static boolean is_boss(Entity i){
        for(BossIllusioner bi:instances){
            if(i.equals(bi.value))return true;
        }
        return false;
    }

    /**
     * 向目标玩家展示boss血条
     * @param p 目标玩家
     */
    public static void show_bossBar_to(Player p){
        for(BossIllusioner instance:instances){
            instance.bossBar.addPlayer(p);
        }
    }

    /**
     * 创造一个幻术大师boss
     * @param l boss所在位置
     * @param plugin 创造boss的插件
     * @return boss的管理实例
     */
    public static BossIllusioner createBossIllusioner(Location l, Plugin plugin) {
        Illusioner il = (Illusioner) l.getWorld().spawnEntity(l, EntityType.ILLUSIONER);
        BossIllusioner BI=new BossIllusioner(il, plugin);
        instances.add(BI);
        return BI;
    }
    protected BossIllusioner(Illusioner i,Plugin plugin){
        value=i;
        value.setCustomName("IllusionerMaster");
        value.setCustomNameVisible(true);
        AttributeInstance attr=value.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attr.setBaseValue(100);
        value.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,210000000,2,false,false));
        value.setHealth(100);
        bossBar=Bukkit.createBossBar("IllusionerMaster", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG);
        bossBar.setVisible(true);
        for(Player p:Bukkit.getServer().getOnlinePlayers()){
            bossBar.addPlayer(p);
        }
        synchronization=new BukkitRunnable() {
            @Override
            public void run() {
                if(value.isDead()){
                    bossBar.setVisible(false);
                    this.cancel();
                    clear();
                }else{
                    double progress=value.getHealth()/100;
                    bossBar.setProgress(progress);
                    if(spell_time_cnt--==0){
                        int n=r.nextInt(4);
                        if(n==2){
                            create_cloud();
                            spell_time_cnt =200;
                        }else if(n==1){
                            illusion_summon();
                            spell_time_cnt =200;
                        }
                    }
                }
            }
        };
        synchronization.runTaskTimer(plugin,1,1);
        value.setLootTable(Bukkit.getServer().getLootTable(NamespacedKey.minecraft("illusioner_master")));
    }
    Random r=new Random();

    /**
     * 技能虚影召唤，召唤四个一碰就死的劫掠者
     */
    protected void illusion_summon(){
        Pillager p;
        p=(Pillager) value.getWorld().spawnEntity(value.getLocation().add(r.nextInt(10),0,r.nextInt(10)),EntityType.PILLAGER);
        p.setHealth(1);
        p.setLootTable(null);
        p=(Pillager) value.getWorld().spawnEntity(value.getLocation().add(r.nextInt(10),0,r.nextInt(10)),EntityType.PILLAGER);
        p.setHealth(1);
        p.setLootTable(null);
        p=(Pillager) value.getWorld().spawnEntity(value.getLocation().add(r.nextInt(10),0,r.nextInt(10)),EntityType.PILLAGER);
        p.setHealth(1);
        p.setLootTable(null);
        p=(Pillager) value.getWorld().spawnEntity(value.getLocation().add(r.nextInt(10),0,r.nextInt(10)),EntityType.PILLAGER);
        p.setHealth(1);
        p.setLootTable(null);
    }

    /**
     * 技能恶心药水云
     */
    protected void create_cloud(){
        AreaEffectCloud aec=(AreaEffectCloud) value.getWorld().spawnEntity(value.getLocation(),EntityType.AREA_EFFECT_CLOUD);
        aec.setParticle(Particle.SNOW_SHOVEL);
        aec.setDuration(300);
        aec.setColor(Color.BLUE);
        aec.setRadius(10);
        aec.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION,100,1),true);
    }
}
