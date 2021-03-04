package org.example;

import cn.navy_master.enhanceframework.ExecutorMethod;
import cn.navy_master.enhanceframework.MagicExecutor;
import cn.navy_master.enhanceframework.MagicManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 这是一个EnhanceFrameWork的使用例子
 * @author navy_master
 * @version 1.0.0
 * @see org.bukkit.plugin.java.JavaPlugin
 */
public class ExampleMagic extends JavaPlugin implements Listener {
    /**
     * 插件初始化函数，这里注册了一个名为“TEST”的附魔，在玩家打破方块时发送消息
     */
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this);
        MagicManager mm=new MagicManager(this);
        MagicExecutor TEST = new MagicExecutor(BlockBreakEvent.class,20, true) {
            /**
             * 视需求重载两个runMagic函数中的一个<br>
             * 但注意，如果重载的是下面这个，并且有多个触发事件类型，你需要手动判断具体是哪个事件
             * @param e 触发的事件
             * @return 执行成功与否
             */
            @ExecutorMethod
            public boolean runMagic(BlockBreakEvent e) {
                e.getPlayer().sendMessage("这是一个测试用例，如果成了，那么就成功了");
                return true;
            }
            /**
             * 装备槽校验方法，可选重载，默认只允许主手
             * @param es 触发时，施法物品所在的装备槽
             * @return 这里要求附魔必须在头上才起作用
             */
            @Override
            public boolean checkEquipmentSlot(EquipmentSlot es) {
                return es==EquipmentSlot.HEAD;
            }
        };
        mm.register_magic("TEST", TEST);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    /**
     * 由于对于动态生成监听器的尝试失败了，我们不得不手动监听后传递<br>
     * 如果有多个插件使用EnhanceFrameWork,它们可以分别注册对应监听<br>
     * 不必担心法术被多次执行，监听相同事件类型的函数中只有一个会生效
     * @param e 捕获事件
     */
    @EventHandler
    public void handleTest(BlockBreakEvent e){
        MagicManager.handle_all(e,this);
    }
}
