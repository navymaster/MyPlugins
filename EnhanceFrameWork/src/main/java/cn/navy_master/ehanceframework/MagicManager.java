package cn.navy_master.ehanceframework;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 法术管理类<br>
 * 整个程序包的入口
 * @author navy_master
 * @version 1.0.0
 */
public class MagicManager {
    protected Plugin plugin_INSTANCE;
    public static HashMap<String, MagicExecutor> MagicList=new HashMap<>();
    public static int enhance_registered=0;
    private static final String reg="(^[^0-9<]+)(<(\\d+)/(\\d+)>)?";
    private static final Pattern p=Pattern.compile(reg);
    private static List<Method> methods=new ArrayList<Method>();
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
    /*private int importindex=3;
    private int methodindex=5;
    public static int unnamedListener=0;
    List<Class> registered=new ArrayList<>();
    private static final String[] source_code={
              "package cn.navy_master.ehanceframework.inter;                \n"
            , "import org.bukkit.event.EventHandler;                              \n"
            , "import cn.navy_master.ehanceframework.manager.*;                              \n"
            , "                                                               \n"
            , "public class MagicListener implements ListenerofMagic {                \n"
            , "                                                               \n"
            , "}                                                              \n"};
    private List<String> class_builder=new ArrayList<>(Arrays.asList(source_code.clone()));

    public void reset(){
        class_builder=new ArrayList<>(Arrays.asList(source_code.clone()));
    }*/
    /**
     * 注册一个新的法术
     * @param name 法术的名称
     * @param m 法术的执行器
     */
    public void register_magic(String name,MagicExecutor m){

        if(m.isEnhanceable())enhance_registered++;
        m.setBELONGTO(this);
        MagicList.put(name,m);
         /*这一部分是尝试动态生成Listener时编写的，但运行失败，代码暂时保留。
         List<Class> l=m.trigger;

         for(Class c:l)
         {
            if(!registered.contains(c)){
                registered.add(c);
            }
         }
       /*for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet()) {
            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
        }*/
    }
    /*关于动态生成Listener的尝试，以失败告终
    public void create_listener(){
        String clazz="";
        for(String s:class_builder){
            clazz=clazz+s;
        }
        ClassPool pool=ClassPool.getDefault();
        try {
            CtClass Lis=pool.makeClass("cn.navy_master.ehanceframework.inter.MagicListenr"+unnamedListener);
            ClassFile ccFile = Lis.getClassFile();
            ConstPool constpool = ccFile.getConstPool();
            AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
            Annotation annot = new Annotation(EventHandler.class.getCanonicalName(), constpool);
            attr.addAnnotation(annot);
            for(Class c:registered){
                CtMethod m=new CtMethod(CtClass.voidType, "handle"+c.getSimpleName(), new CtClass[]{pool.get(c.getCanonicalName())}, Lis);
                m.setModifiers(Modifier.PUBLIC);
                m.setBody("{cn.navy_master.ehanceframework.MagicManager.handle_all($1);}");
                m.getMethodInfo().addAttribute(attr);
                Lis.addMethod(m);
            }
            //Lis.toClass();
            Lis.writeFile("C:\\Users\\22524\\IdeaProjects\\MyPlugins\\EhanceFrameWork\\target\\classes");
        } catch (CannotCompileException | NotFoundException | IOException e) {
            e.printStackTrace();
        }
        //System.out.println(clazz);
       /* try {

            //Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava("cn.navy_master.ehanceframework.inter.MagicListener",clazz);
            /*try {
                Bukkit.getPluginManager().registerEvents((Listener) cla.newInstance(),plugin);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(MagicManager.class.getCanonicalName());
        }
        //System.out.println(clazz);
        //Class<?> clazz=
    }*/

    /**
     * 注册执行器之后，在相关event的handle中执行这个函数就可以了
     * 由于插件中尝试使用动态编译类失败，所以event还需要手动handle
     * 传入监听器以便于检验是否有重复监听同一事件的不同监听器并进行区分
     * @param e 传入的事件
     * @param l 监听器
     */
    public static void handle_all(Event e, Listener l) {
        try {
            if (!UnsupportedEventException.have_getPlayer(e.getClass()))
                throw new UnsupportedEventException("The new MagicExecuter has unsupported trigger event.");
            if (!events.contains(e.getClass())) {
                methods.add(check_method(e, l));
                events.add(e.getClass());
                trytorun(e);

            } else {
                Method m = check_method(e, l);
                if (methods.contains(m)) {
                    trytorun(e);
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
                if(e.getClass().isAssignableFrom(p.getClass())){
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
    private static void trytorun(Event e) throws InvocationTargetException, IllegalAccessException {
        Method met= null;
        try {
            met = UnsupportedEventException.get_getPlayer(e.getClass());
        } catch (UnsupportedEventException unsupportedEventException) {
            unsupportedEventException.printStackTrace();
        }
        //Bukkit.getLogger().info("事件类型校验通过");
        for(EquipmentSlot key:EquipmentSlot.values()) {
            //Bukkit.getLogger().info("正在校验装备槽："+key.toString());
            ItemStack is=((Player)met.invoke(e)).getInventory().getItem(key);
            if (Objects.isNull(is)||(!is.hasItemMeta()) || (!is.getItemMeta().hasLore())) {
                continue;
            }
            ItemMeta im;
            List<String> Lore;
            im = is.getItemMeta();
            Lore = im.getLore();
            for (int i = 0; i < Lore.size(); i++) {
                String s = Lore.get(i);
                Matcher m = p.matcher(s);
                if (m.find()) {
                    //Bukkit.getLogger().info("装备槽"+key.toString()+"检测到附魔物品");
                    if (MagicList.containsKey(m.group(1))) {
                        MagicExecutor me = MagicList.get(m.group(1));
                        if (me.can_trigger_by(e.getClass())&&me.checkEquipmentSlot(key)) {
                           // Bukkit.getLogger().info("装备槽校验通过");
                            if (m.group(2) == null) {
                                MagicManager.MagicList.get(m.group(1)).play_magic(((Player)met.invoke(e)), key);
                            } else {
                                if (!m.group(3).equals("0")) {
                                    boolean suc = MagicList.get(m.group(1)).play_magic_without_cool_time(((Player)met.invoke(e)), key);
                                    if (!suc) continue;
                                    s = m.group(1) + "<" + (Integer.valueOf(m.group(3)) - 1) + "/" + m.group(4) + ">";
                                    Lore.set(i, s);
                                    im.setLore(Lore);
                                    ((Player)met.invoke(e)).getInventory().getItem(EquipmentSlot.HAND).setItemMeta(im);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
