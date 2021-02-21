package cn.navy_master.ehanceframework;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *插件主类
 * @author navy_master
 * @version 1.0.0
 * @since spigot1.16.5
 * @see org.bukkit.plugin.java.JavaPlugin
 * */
public class EnhanceFrameWork extends JavaPlugin {
    public static EnhanceFrameWork INSTANCE;

    /*
    public static boolean debug_mode;*/
    /**
     * 插件启动函数<br>
     * 初始化孤例，设立附魔监听器
     */
    @Override
    public void onEnable() {
        INSTANCE =this;
        Bukkit.getPluginManager().registerEvents(new EnhanceHandle(),this);
    }

    /**
     * 插件关闭函数<br>
     */
    @Override
    public void onDisable() {
    }
}
