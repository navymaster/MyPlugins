package cn.navy_master.bossbattle;

import cn.navy_master.enhanceframework.MagicExecutor;
import cn.navy_master.enhanceframework.MagicManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.net.http.WebSocket;
import java.util.List;

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
            @Override
            public boolean runMagic(LivingEntity Caster) {
                BossIllusioner.createBossIllusioner(Caster.getLocation(),BossBattleMain.INSTANCE);
                ItemStack is = Caster.getEquipment().getItem(EquipmentSlot.HAND);
                is.setAmount(is.getAmount()-1);
                return true;
            }
        };
        mm.register_magic("CALLBOSS", CALLBOSS);
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
}
