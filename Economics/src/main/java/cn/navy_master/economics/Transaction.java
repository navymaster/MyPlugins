package cn.navy_master.economics;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易条目类
 */
public class Transaction implements ConfigurationSerializable {
    public String getOwner() {
        return owner;
    }

    String owner;

    public ItemStack getThings() {
        return things;
    }

    ItemStack things;

    public int getPrice() {
        return price;
    }

    int price;
    int num;
    boolean inf=false;
    /**
     * 出售型构造函数
     * @param things 商品
     * @param owner 所有者
     * @param price 单价
     * @param num 销售总量
     */
    Transaction(ItemStack things,String owner,int price,int num,boolean inf){
        this.owner=owner;
        this.things=things;
        this.price=price;
        this.num=num;
        this.inf=inf;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map=new HashMap<>();
        map.put("owner",owner);
        map.put("things",things);
        map.put("price",price);
        map.put("num",num);
        map.put("inf",inf);
        return map;
    }
    public static Transaction deserialize(Map<String,Object> map){
        String owner=(String) map.get("owner");
        ItemStack things=(ItemStack) map.get("things");
        int price=(int) map.get("price");
        int num=(int) map.get("num");
        boolean inf=(boolean) map.get("inf");
        return new Transaction(things,owner,price,num,inf);
    }
}
