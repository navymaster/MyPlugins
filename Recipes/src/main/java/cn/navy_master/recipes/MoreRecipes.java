package cn.navy_master.recipes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreRecipes extends JavaPlugin {
    public static MoreRecipes instance;
    public boolean debug_mode=true;
    public static FileConfiguration conf;
    @Override
    public void onEnable() {
        conf=getConfig();
        RecipesRegister.Reg();
        Bukkit.getPluginManager().registerEvents(new Wabbajack(),this);
        Bukkit.getPluginManager().registerEvents(new FishStickListener(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerForMoreRecipes(),this);
        Bukkit.getPluginCommand("sethome").setExecutor(new HomeSetter());
        Bukkit.getPluginCommand("home").setExecutor(new HomeBack());
        Bukkit.getPluginCommand("viewhome").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                //for(String s:conf.getList())
                return false;
            }
        });
        Assemble a=new Assemble();
        Bukkit.getPluginCommand("assemble").setExecutor(a);
        Bukkit.getPluginCommand("response").setExecutor(a);
        saveDefaultConfig();
        instance =this;
    }

    @Override
    public void onDisable() {
        getServer().clearRecipes();
        saveConfig();
    }
}
