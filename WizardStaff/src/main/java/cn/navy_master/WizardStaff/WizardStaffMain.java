package cn.navy_master.WizardStaff;

import cn.navy_master.enhanceframework.MagicExecutor;
import cn.navy_master.enhanceframework.MagicManager;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

/**
 *插件主类
 * @author navy_master
 * @version 1.0.0
 * @since spigot1.16.5
 * @see JavaPlugin
 * */
public class WizardStaffMain extends JavaPlugin {
    public static WizardStaffMain only;
    public static HashMap<Player, PlayerMagic> player_magics =new HashMap<>();
    public static FileConfiguration FC;
    public static boolean debug_mode;
    /**
     * 插件启动函数<br>
     * 这会初始化孤例only，建立一个由玩家到其法术冷却时间的映射player_magics<br>
     * 并将插件的配置置为static以便调用，同时也会从中读出是否为debug_mode
     */
    @Override
    public void onEnable() {
        only=this;
        //ConfigurationSerialization.registerClass(PlayerMagicList.class);
        PlayerHandBook phb=new PlayerHandBook();
        Objects.requireNonNull(Bukkit.getPluginCommand("playerhandbook")).setExecutor(phb);
        Objects.requireNonNull(Bukkit.getPluginCommand("setplayerhandbook")).setExecutor(phb);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(),this);
        Bukkit.getPluginManager().registerEvents(new WizardStaffListener(),this);
        saveDefaultConfig();
        FC=getConfig();
        if(Objects.isNull(FC.get("Debug_Mode"))){
            FC.set("Debug_Mode",false);
        }
        debug_mode=(boolean)FC.get("Debug_Mode");

        PlayerHandBook.setPhb((ItemStack) FC.get("phbdata"));

        if(Objects.isNull(FC.get("MagicALL"))){
            FC.set("MagicALL",true);
        }
        if((boolean)FC.get("MagicALL"))
            register_default();
    }

    /**
     * 插件关闭函数<br>
     * 保存配置文件中有保存需求的项目，然后终止所有插件进程
     */
    @Override
    public void onDisable() {
        saveConfig();
        Bukkit.getScheduler().cancelTasks(this);
    }
    /**
     * 注册默认法术的函数
     */
    public void register_default(){
        MagicManager mm=new MagicManager(this);
        boolean install;
        Map<String,Boolean> defaults_magic;
        defaults_magic=new HashMap<>();
        if(!Objects.isNull(WizardStaffMain.FC.get("Magic"))){
            ConfigurationSection cs =WizardStaffMain.FC.getConfigurationSection("Magic");
            for(String key : cs.getKeys(false)) {
                defaults_magic.put(key,(Boolean) cs.get(key));
            }
        }

        if(defaults_magic.containsKey("FIRE_BALL")) {
            install=defaults_magic.get("FIRE_BALL");
        }else{
            install=true;
            defaults_magic.put("FIRE_BALL",true);
        }

        if(install) {
            //火球
            MagicExecutor FIRE_BALL = new MagicExecutor(PlayerInteractEvent.class,20, true) {
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

        if(defaults_magic.containsKey("FLAME_ARROW")) {
            install=defaults_magic.get("FLAME_ARROW");
        }else{
            install=true;
            defaults_magic.put("FLAME_ARROW",true);
        }
        if(install) {
            //火焰箭
            MagicExecutor FLAME_ARROW = new MagicExecutor(PlayerInteractEvent.class,1, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    World w = Caster.getWorld();
                    Location loc = Caster.getLocation();
                    loc.add(0, 2, 0);
                    Arrow ar = w.spawnArrow(loc, loc.getDirection(), (float) 0.6, 60);
                    ar.setFireTicks(1200);
                    ar.setShooter(Caster);
                    ar.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    return true;
                }
            };
            mm.register_magic("FLAME_ARROW", FLAME_ARROW);
        }

        if(defaults_magic.containsKey("THUNDER_WAVE")) {
            install=defaults_magic.get("THUNDER_WAVE");
        }else{
            install=true;
            defaults_magic.put("THUNDER_WAVE",true);
        }
        if(install) {
            //雷鸣波
            MagicExecutor THUNDER_WAVE = new MagicExecutor(PlayerInteractEvent.class,60, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    Location loc = Caster.getLocation();
                    List<Entity> al = Caster.getNearbyEntities(9, 9, 9);
                    Vector ln;
                    for (Entity en : al) {
                        ln = en.getLocation().toVector().subtract(loc.toVector());
                        en.getVelocity().add(ln.multiply(6.0 / en.getLocation().distance(loc)));
                        if (LivingEntity.class.isAssignableFrom(en.getClass())) {
                            ((LivingEntity) en).damage(5, Caster);
                        }
                    }
                    Caster.getWorld().playSound(Caster.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2F, 0F);
                    return true;
                }
            };
            mm.register_magic("THUNDER_WAVE", THUNDER_WAVE);
        }

        if(defaults_magic.containsKey("SHATTER")) {
            install=defaults_magic.get("SHATTER");
        }else{
            install=true;
            defaults_magic.put("SHATTER",true);
        }
        if(install) {
            //粉碎音波
            MagicExecutor SHATTER = new MagicExecutor(PlayerInteractEvent.class,100, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    World w = Caster.getWorld();
                    try {
                        Location loc = Caster.rayTraceBlocks(36, FluidCollisionMode.NEVER).getHitBlock().getLocation();
                        Collection<Entity> al = w.getNearbyEntities(loc, 6, 6, 6);
                        Vector ln;
                        for (Entity en : al) {
                            ln = en.getLocation().toVector().subtract(loc.toVector());
                            en.getVelocity().add(ln.multiply(6.0 / en.getLocation().distance(loc)));
                            if (LivingEntity.class.isAssignableFrom(en.getClass())) {
                                ((LivingEntity) en).damage(7, Caster);
                            }
                        }
                        Caster.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 2F, 0F);
                    } catch (NullPointerException n) {
                        return false;
                    }
                    return true;
                }
            };
            mm.register_magic("SHATTER", SHATTER);
        }

        if(defaults_magic.containsKey("MAGIC_MISSILE")) {
            install=defaults_magic.get("MAGIC_MISSILE");
        }else{
            install=true;
            defaults_magic.put("MAGIC_MISSILE",true);
        }
        if(install) {
            //魔法飞弹
            MagicExecutor MAGIC_MISSILE = new MagicExecutor(PlayerInteractEvent.class,60, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    World w = Caster.getWorld();
                    Location loc = Caster.getLocation();
                    try {
                        Entity tar = Objects.requireNonNull(w.rayTraceEntities(Caster.getEyeLocation(), loc.getDirection(), 72, 0, x -> x != Caster)).getHitEntity();
                        if (WizardStaffMain.debug_mode) {
                            Bukkit.getLogger().info(loc.toString());
                            Bukkit.getLogger().info(Caster.getEyeLocation().toString());
                            Bukkit.getLogger().info(loc.clone().add(0, 1, 0).toString());
                            Bukkit.getLogger().info(Objects.requireNonNull(w.rayTraceEntities(loc, loc.getDirection(), 72)).getHitEntity().getName());
                            Bukkit.getLogger().info(Objects.requireNonNull(w.rayTraceEntities(loc.clone().add(0, 1, 0), loc.getDirection(), 72)).getHitEntity().getName());
                            Bukkit.getLogger().info(Objects.requireNonNull(w.rayTraceEntities(Caster.getEyeLocation(), loc.getDirection(), 72)).getHitEntity().getName());
                            Bukkit.getLogger().info(tar.getName());
                        }
                        if (tar != Caster && LivingEntity.class.isAssignableFrom(tar.getClass())) {
                            loc.add(1, 2, 0);
                            FollowTask FT;
                            Snowball a;
                            Vector ln;
                            a = (Snowball) w.spawnEntity(loc, EntityType.SNOWBALL);
                            ln = (tar.getLocation().clone().add(0, 1, 0)).toVector().subtract(a.getLocation().toVector());
                            a.setVelocity(ln.multiply(1.0 / a.getLocation().distance(tar.getLocation())));//
                            a.setCustomName("魔法飞弹");
                            a.setTicksLived(300);
                            a.setGravity(false);
                            a.setShooter(Caster);
                            FT = new FollowTask((LivingEntity) tar, a);
                            FT.runTaskTimer(WizardStaffMain.only, 1, 1);
                            loc.add(-2, 0, 1);
                            a = (Snowball) w.spawnEntity(loc, EntityType.SNOWBALL);
                            ln = (tar.getLocation().clone().add(0, 1, 0)).toVector().subtract(a.getLocation().toVector());
                            a.setVelocity(ln.multiply(1.0 / a.getLocation().distance(tar.getLocation())));//
                            a.setCustomName("魔法飞弹");
                            a.setTicksLived(300);
                            a.setGravity(false);
                            a.setShooter(Caster);
                            FT = new FollowTask((LivingEntity) tar, a);
                            FT.runTaskTimer(WizardStaffMain.only, 1, 1);
                            loc.add(0, 0, -2);
                            a = (Snowball) w.spawnEntity(loc, EntityType.SNOWBALL);
                            ln = (tar.getLocation().clone().add(0, 1, 0)).toVector().subtract(a.getLocation().toVector());
                            a.setVelocity(ln.multiply(1.0 / a.getLocation().distance(tar.getLocation())));//
                            a.setCustomName("魔法飞弹");
                            a.setTicksLived(300);
                            a.setGravity(false);
                            a.setShooter(Caster);
                            FT = new FollowTask((LivingEntity) tar, a);
                            FT.runTaskTimer(WizardStaffMain.only, 1, 1);
                        }
                    } catch (NullPointerException n) {
                        return false;
                    }
                    return true;
                }
            };
            mm.register_magic("MAGIC_MISSILE", MAGIC_MISSILE);
        }

        if(defaults_magic.containsKey("FROST_ARMOR")) {
            install=defaults_magic.get("FROST_ARMOR");
        }else{
            install=true;
            defaults_magic.put("FROST_ARMOR",true);
        }
        if(install) {
            //冰铠
            MagicExecutor FROST_ARMOR = new MagicExecutor(PlayerInteractEvent.class,1200, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    if(Caster.getAbsorptionAmount()==0)return false;
                    PlayerDamageListener.watchlist.add(Caster);
                    Caster.setAbsorptionAmount(Caster.getAbsorptionAmount() + 4);
                    FrostArmorTask FAT = new FrostArmorTask(Caster);
                    FAT.runTaskTimer(WizardStaffMain.only, 1, 1);
                    return true;
                }
            };
            mm.register_magic("FROST_ARMOR", FROST_ARMOR);
        }

        if(defaults_magic.containsKey("THUNDER_CALLING")) {
            install=defaults_magic.get("THUNDER_CALLING");
        }else{
            install=true;
            defaults_magic.put("THUNDER_CALLING",true);
        }
        if(install) {
            //召雷术
            MagicExecutor THUNDER_CALLING = new MagicExecutor(PlayerInteractEvent.class,100, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    try {
                        Location loc = Caster.rayTraceBlocks(36, FluidCollisionMode.NEVER).getHitBlock().getLocation();
                        Caster.getWorld().strikeLightning(loc);
                    } catch (NullPointerException n) {
                        return false;
                    }
                    return true;
                }
            };
            mm.register_magic("THUNDER_CALLING", THUNDER_CALLING);
        }

        if(defaults_magic.containsKey("HOLD_MONSTER")) {
            install=defaults_magic.get("HOLD_MONSTER");
        }else{
            install=true;
            defaults_magic.put("HOLD_MONSTER",true);
        }
        if(install) {
            //怪物定身术
            MagicExecutor HOLD_MONSTER = new MagicExecutor(PlayerInteractEvent.class,120, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    World w = Caster.getWorld();
                    Location loc = Caster.getLocation();
                    try {
                        Entity tar = Objects.requireNonNull(w.rayTraceEntities(Caster.getEyeLocation(), loc.getDirection(), 72, 0, x -> x != Caster)).getHitEntity();
                        if (tar != Caster && LivingEntity.class.isAssignableFrom(tar.getClass())) {
                            ((LivingEntity) tar).setAI(false);
                            new BukkitRunnable() {
                                int count = 60;

                                @Override
                                public void run() {
                                    if (count > 0) {
                                        count--;
                                    } else {
                                        ((LivingEntity) tar).setAI(true);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(WizardStaffMain.only, 1, 1);
                        }
                    } catch (NullPointerException n) {
                        return false;
                    }
                    return true;
                }
            };
            mm.register_magic("HOLD_MONSTER", HOLD_MONSTER);
        }

        if(defaults_magic.containsKey("TELEPORT")) {
            install=defaults_magic.get("TELEPORT");
        }else{
            install=true;
            defaults_magic.put("TELEPORT",true);
        }
        if(install) {
            //传送术
            MagicExecutor TELEPORT = new MagicExecutor(PlayerInteractEvent.class,1, true) {
                @Override
                @SuppressWarnings("deprecation")
                public boolean runMagic(LivingEntity Caster) {
                    if (Player.class.isAssignableFrom(Caster.getClass())) {
                        Inventory inventory = Bukkit.createInventory((Player) Caster, 9 * 6, "teleport_target");
                        List<Entity> l = Caster.getWorld().getEntities();
                        List<LivingEntity> choice;
                        choice = new ArrayList<>();
                        for (Entity e : l) {
                            if (LivingEntity.class.isAssignableFrom(e.getClass())) {
                                choice.add((LivingEntity) e);
                            }
                        }
                        int i = 0;
                        ItemStack is;
                        SkullMeta im;
                        for (LivingEntity e : choice) {
                            if (i >= 45) break;
                            im = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                            if (Player.class.isAssignableFrom(e.getClass()))
                                im.setOwningPlayer((Player) e);
                            else
                                im.setOwningPlayer(org.bukkit.Bukkit.getOfflinePlayer("MHF_" + e.getName()));
                            im.setDisplayName(e.getName());
                            is = new ItemStack(Material.PLAYER_HEAD);
                            is.setItemMeta(im);
                            inventory.setItem(i++, is);
                        }
                        if (i == 45) {
                            is = new ItemStack(Material.GRASS_BLOCK);
                            ItemMeta imx = is.getItemMeta();
                            imx.setDisplayName("下一页");
                            is.setItemMeta(imx);
                            inventory.setItem(53, is);
                        }
                        is = new ItemStack(Material.STONE);
                        ItemMeta imx = is.getItemMeta();
                        imx.setDisplayName("个数表示页码");
                        is.setItemMeta(imx);
                        inventory.setItem(49, is);
                        ((Player) Caster).openInventory(inventory);
                        WizardStaffListener.tp_choice = choice;
                        return true;
                    } else return false;
                }
            };
            mm.register_magic("TELEPORT", TELEPORT);
        }
        if(defaults_magic.containsKey("HEAL_WIND")) {
            install=defaults_magic.get("HEAL_WIND");
        }else{
            install=true;
            defaults_magic.put("HEAL_WIND",true);
        }
        if(install) {
            //治愈之风
            MagicExecutor HEAL_WIND = new MagicExecutor(PlayerMoveEvent.class,200, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    Caster.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,40,5,false,false));
                    return true;
                }

                @Override
                public boolean checkEquipmentSlot(EquipmentSlot es) {
                    return true;
                }
            };
            mm.register_magic("HEAL_WIND", HEAL_WIND);
        }
        if(defaults_magic.containsKey("CALL_BACK")) {
            install=defaults_magic.get("CALL_BACK");
        }else{
            install=true;
            defaults_magic.put("CALL_BACK",true);
        }
        if(install) {
            //召还
            MagicExecutor CALL_BACK = new MagicExecutor(PlayerItemDamageEvent.class,20, true) {
                @Override
                public boolean runMagic(LivingEntity Caster) {
                    if (Player.class.isAssignableFrom(Caster.getClass())) {
                        ((Player)Caster).setTotalExperience(((Player)Caster).getTotalExperience()+1);
                    }
                    return true;
                }

                @Override
                public boolean checkEquipmentSlot(EquipmentSlot es) {
                    return true;
                }
            };
            mm.register_magic("CALL_BACK", CALL_BACK);
        }
    }
}
