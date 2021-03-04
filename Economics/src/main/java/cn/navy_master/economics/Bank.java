package cn.navy_master.economics;

import org.bukkit.configuration.ConfigurationSection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 玩家钱币保存类
 */
public class Bank implements Serializable {
    /**
     * @return 孤例
     */
    public static Bank getInstance(){
        if(Objects.isNull(INSTANCE)){
            INSTANCE=new Bank();
        }
        return INSTANCE;
    }
    private static Bank INSTANCE;
    Map<String,Integer> bank=new HashMap<>();

    /**
     * 加载玩家存款
     */
    public void loadConfig(){
        ConfigurationSection cs =Economics.getInstance().getConf().getConfigurationSection("Bank");
        for(String key : cs.getKeys(false)) {
            bank.put(key,(int) cs.get(key));
        }
    }

    /**
     * 保存玩家存款
     */
    public void saveConfig(){
        ConfigurationSection cs =Economics.getInstance().getConf().createSection("Bank",bank);
    }
    /**
     * 注册一个新的账户
     * @param owner 所有者
     */
    public void register(String owner){
        bank.put(owner,0);
    }
    public boolean merge(String owner,int count){
        int a= bank.get(owner);
        if((a+count)==0)return false;
        bank.replace(owner,a+count);
        return true;
    }

    /**
     * 获取
     * @param owner 目标玩家
     * @return 存款数
     */
    public int get(String owner){
        return bank.get(owner);
    }

    /**
     * 检查
     * @param name 目标玩家
     * @return 是否注册存款账户
     */
    public boolean isRegistered(String name){
        return bank.containsKey(name);
    }
}
