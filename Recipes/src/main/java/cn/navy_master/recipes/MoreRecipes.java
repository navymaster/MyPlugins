package cn.navy_master.recipes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class MoreRecipes extends JavaPlugin {
    public static MoreRecipes instance;
    public boolean debug_mode=true;
    public static ConfigurationSection homes;
    @Override
    public void onEnable() {
        RecipesRegister.Reg();
        Bukkit.getPluginManager().registerEvents(new Wabbajack(),this);
        Bukkit.getPluginManager().registerEvents(new FishStickListener(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerForMoreRecipes(),this);
        Bukkit.getPluginCommand("sethome").setExecutor(new HomeSetter());
        Bukkit.getPluginCommand("home").setExecutor(new HomeBack());
        Bukkit.getPluginCommand("viewhome").setExecutor(new HomeGetter());
        Assemble a=new Assemble();
        Bukkit.getPluginCommand("assemble").setExecutor(a);
        Bukkit.getPluginCommand("response").setExecutor(a);
        saveDefaultConfig();
        homes =getConfig().getConfigurationSection("homes");
        if(Objects.isNull(homes))
            homes=getConfig().createSection("homes");
        instance =this;
    }

    @Override
    public void onDisable() {
        getServer().clearRecipes();
        saveConfig();
    }
}
