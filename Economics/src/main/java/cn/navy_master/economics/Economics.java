package cn.navy_master.economics;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 插件主类
 * @see org.bukkit.plugin.java.JavaPlugin
 */
public class Economics extends JavaPlugin {
    private static Economics INSTANCE;

    /**
     * @return 孤例
     */
    public static Economics getInstance(){
        return INSTANCE;
    }
    public FileConfiguration getConf(){
        return getConfig();
    }
    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(Recovery.class);
        ConfigurationSerialization.registerClass(Transaction.class);
        INSTANCE=this;
        Bukkit.getPluginCommand("trade").setExecutor((commandSender, command, s, strings) -> {
            if(commandSender instanceof Player){
                Shop.getInstance().shop_view((Player) commandSender);
                return true;
            }
            return false;
        });
        Bukkit.getPluginCommand("recovery").setExecutor(new EconomicExecutor());
        Bukkit.getPluginManager().registerEvents(new EconomicListener(),this);
        saveDefaultConfig();
        Shop.getInstance().loadConfig();
        Bank.getInstance().loadConfig();
    }

    @Override
    public void onDisable() {
        Shop.getInstance().saveConfig();
        Bank.getInstance().saveConfig();
        saveConfig();
    }
}
