package cn.navy_master.ehanceframework;

import org.bukkit.event.Event;

import java.lang.reflect.Method;

/**
 * 使用了不支持的事件作为触发器
 * @author navy_master
 * @version 1.0.0
 * @see java.lang.Exception
 */
public class UnsupportedEventException extends Exception {
    public UnsupportedEventException(){}
    public UnsupportedEventException(String msg){
        super(msg);
    }

    /**
     * 检查是否含有getPlayer方法，如果没有，抛出异常
     * @param c 要校验的类
     * @return 是否含有相应方法
     */
    public static boolean have_getPlayer(Class<? extends Event> c) throws UnsupportedEventException {
        return get_getPlayer(c)!=null;
    }
    protected static Method get_getPlayer(Class<? extends Event> c) throws UnsupportedEventException {
        while(c!=Event.class) {
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals("getPlayer")) {
                    return m;
                }
            }
            c= (Class<? extends Event>) c.getSuperclass();
        }
        throw new UnsupportedEventException("unsupported trigger event, Magic trigger must have getPlayer() Method.");
    }
}
