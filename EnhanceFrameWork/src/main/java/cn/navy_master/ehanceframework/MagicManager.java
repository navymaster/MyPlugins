package cn.navy_master.ehanceframework;

import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MagicManager {
    public static HashMap<String, MagicExecutor> MagicList=new HashMap<>();
    public static int enhance_registered=0;
    private static final String reg="(^[^0-9<]+)(<(\\d+)/(\\d+)>)?";
    private static final Pattern p=Pattern.compile(reg);
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
     * @param e 传入的事件
     */
    public static void handle_all(PlayerEvent e){
        ItemStack is=e.getPlayer().getInventory().getItem(EquipmentSlot.HAND);
        if(Objects.isNull(is)||(!is.hasItemMeta())||(!is.getItemMeta().hasLore())){
            return;
        }
        ItemMeta im;
        List<String> Lore;
        im=is.getItemMeta();
        Lore =im.getLore();
        for(int i=0;i<Lore.size();i++) {
            String s = Lore.get(i);
            Matcher m = p.matcher(s);
            if (m.find()) {
                if (MagicList.containsKey(m.group(1))) {
                    MagicExecutor me=MagicList.get(m.group(1));
                    if(me.can_trigger_by(e.getClass())) {
                        if (m.group(2) == null) {
                            MagicManager.MagicList.get(m.group(1)).play_magic(e.getPlayer());
                        } else {
                            if (!m.group(3).equals("0")) {
                                boolean suc = MagicList.get(m.group(1)).runMagic(e.getPlayer());
                                if (!suc) continue;
                                s = m.group(1) + "<" + (Integer.valueOf(m.group(3)) - 1) + "/" + m.group(4) + ">";
                                Lore.set(i, s);
                                im.setLore(Lore);
                                e.getPlayer().getInventory().getItem(EquipmentSlot.HAND).setItemMeta(im);
                            }
                        }
                    }
                }
            }
        }
    }
}
