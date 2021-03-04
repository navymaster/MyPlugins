package cn.navy_master.bossbattle;

import cn.navy_master.enhanceframework.ExecutorMethod;
import cn.navy_master.enhanceframework.MagicExecutor;
import cn.navy_master.enhanceframework.MagicManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * boss战插件主类
 * @author navy_master
 * @see org.bukkit.plugin.java.JavaPlugin
 * @see org.bukkit.command.CommandExecutor
 * @see org.bukkit.event.Listener
 */
public class BossBattleMain extends JavaPlugin implements Listener, CommandExecutor {
    public static BossBattleMain INSTANCE;
    @Override
    public void onEnable() {
        INSTANCE=this;
        Bukkit.getPluginManager().registerEvents(this,this);
        Bukkit.getPluginCommand("callboss").setExecutor(this);
        MagicManager mm=new MagicManager(this);
        MagicExecutor CALLBOSS = new MagicExecutor(PlayerInteractEvent.class,1, false) {
            /**
             * 必须重载执行器的runMagic函数
             * @param Caster 施法者
             * @return 施法成功与否
             */
            @ExecutorMethod
            @Override
            public boolean runMagic(LivingEntity Caster) {
                BossIllusioner.createBossIllusioner(Caster.getLocation(),BossBattleMain.INSTANCE);
                ItemStack is = Caster.getEquipment().getItem(EquipmentSlot.HAND);
                is.setAmount(is.getAmount()-1);
                return true;
            }
        };
        mm.register_magic("CALLBOSS", CALLBOSS);
        reg_boss_summon();
    }

    @Override
    public void onDisable() {}
    @EventHandler
    public void handle(PlayerJoinEvent e) {
        BossIllusioner.show_bossBar_to(e.getPlayer());
        //BossIllusioner.class
        //Bukkit.getLogger().info(e.getEntity().getClass().getCanonicalName());
    }
    @EventHandler
    public void handle(PlayerInteractEvent e){
        MagicManager.handle_all(e,this);
    }

    /**
     * 指令召唤boss
     * @param sender 指令请求者
     * @param command 指令实例
     * @param label 使用的指令名称
     * @param args 指令参数
     * @return 执行成功与否
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()){
            if(args.length!=0) {
                if (args[0].equals("IllusionerMaster")) {
                    if (Player.class.isAssignableFrom(sender.getClass()))
                        BossIllusioner.createBossIllusioner(((Player) sender).getLocation(), this);
                    else BossIllusioner.createBossIllusioner(Bukkit.getWorlds().get(0).getSpawnLocation(), this);
                }
            }
        }
        return true;
    }
    public void reg_boss_summon(){
        ItemStack is;
        ItemMeta im;
        ArrayList<String> l;
        ShapedRecipe sr;
        is=new ItemStack(Material.BOOK);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add(ChatColor.WHITE+"立召唤幻术大师");
        l.add("CALLBOSS");
        im.setDisplayName(ChatColor.GOLD+"呼喝邀令");
        im.setLore(l);
        im.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("boss_call"),is);
        sr.shape("zzz","xyx","xxx");
        sr.setIngredient('x',Material.PAPER);
        sr.setIngredient('y',Material.DIAMOND);
        sr.setIngredient('z',Material.LAPIS_LAZULI);
        Bukkit.getServer().addRecipe(sr);
    }
}
