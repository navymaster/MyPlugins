package cn.navy_master.enhanceframework;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 法术管理类<br>
 * 整个程序包的入口
 * @author navy_master
 * @version 1.0.1
 */
public class MagicManager {
    protected Plugin plugin_INSTANCE;
    public static HashMap<String, MagicExecutor> MagicList=new HashMap<>();
    public static int enhance_registered=0;
    private static final String reg="(^[^0-9<]+)(<(\\d+)/(\\d+)>)?";
    private static final Pattern p=Pattern.compile(reg);
    private static List<Method> methods= new ArrayList<>();
    private static List<Class<?extends Event>> events=new ArrayList<>();

    /**
     * MagicManager的构造函数，为了保证冷却注册系统工作正常，需要传入插件单例<br>
     *     需要注意的是，即使有多个插件使用了本模块，也不会产生额外的冷却<br>
     *     TODO：将各个MagicManager的冷却值分别管理
     * @param plugin 插件单例
     */
    public MagicManager(Plugin plugin){
        plugin_INSTANCE=plugin;
        Bukkit.getPluginManager().registerEvents(new EnhanceHandle(),plugin);
    }
    /**
     * 注册一个新的法术
     * @param name 法术的名称
     * @param m 法术的执行器
     */
    public void register_magic(String name,MagicExecutor m){
        if(m.isEnhanceable())enhance_registered++;
        m.setBelong_to(this);
        MagicList.put(name,m);
    }
    /**
     * 注册执行器之后，在相关event的handle中执行这个函数就可以了
     * 由于插件中尝试使用动态编译类失败，所以event还需要手动handle
     * 传入监听器以便于检验是否有重复监听同一事件的不同监听器并进行区分
     * @param e 传入的事件
     * @param l 监听器
     */
    public static void handle_all(Event e, Listener l) {
        try {
            Method met=UnsupportedEventException.get_caster_method(e.getClass());
            Method m=check_method(e,l);
            if (!events.contains(e.getClass())) {
                methods.add(m);
                events.add(e.getClass());
                try_to_run(e,met);
            } else {
                if (methods.contains(m)) {
                    try_to_run(e,met);
                }
            }
        } catch (UnsupportedEventException | InvocationTargetException | IllegalAccessException unsupportedEventException) {
            unsupportedEventException.printStackTrace();
        }
    }

    /**
     * 反射获取监听方法
     * @param e 被监听的事件
     * @param l 监听器
     * @return 监听事件的方法
     */
    private static Method check_method(Event e, Listener l){
        Method[] method=l.getClass().getDeclaredMethods();
        for(Method m:method){
            if(m.isAnnotationPresent(EventHandler.class)&&m.getParameterCount()==1){
                Class<?>[] p= m.getParameterTypes();
                //Bukkit.getLogger().info("方法"+m.getName()+"是触发器");
                if(p[0].isInstance(e)){
                    //Bukkit.getLogger().info("方法"+m.getName()+"是触发此次校验的方法");
                    return m;
                }
            }
        }
        return null;
    }
    /**
     * 校验事件触发者的装备是否符合条件，符合条件则调出法术执行器
     * @param e 事件
     */
    private static void try_to_run(Event e,Method met) throws InvocationTargetException, IllegalAccessException {
        if(met.invoke(e) instanceof Player) {
            Player caster=(Player)met.invoke(e);
            for (EquipmentSlot key : EquipmentSlot.values()) {
                ItemStack is;
                is = caster.getInventory().getItem(key);
                if (Objects.isNull(is) || (!is.hasItemMeta()) || (!is.getItemMeta().hasLore())) {
                    continue;
                }
                ItemMeta im;
                List<String> Lore;
                im = is.getItemMeta();
                Lore = im.getLore();
                for (int i = 0; i < Lore.size(); i++) {
                    String s = Lore.get(i);
                    Matcher m = p.matcher(s);
                    if (m.find()) {//正则匹配校验触发词条
                        if (MagicList.containsKey(m.group(1))) {
                            MagicExecutor me = MagicList.get(m.group(1));
                            if (me.can_trigger_by(e.getClass()) && me.checkEquipmentSlot(key)) {
                                if (m.group(2) == null) {
                                    MagicManager.MagicList.get(m.group(1)).play_magic(e, met);
                                } else {
                                    if (!m.group(3).equals("0")) {
                                        boolean suc = MagicList.get(m.group(1)).play_magic_without_cool_time(e, met);
                                        if (!suc) continue;
                                        s = m.group(1) + "<" + (Integer.parseInt(m.group(3)) - 1) + "/" + m.group(4) + ">";
                                        Lore.set(i, s);
                                        im.setLore(Lore);
                                        caster.getInventory().getItem(EquipmentSlot.HAND).setItemMeta(im);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //TODO:让附魔支持非玩家生物
    }
}
