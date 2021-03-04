package cn.navy_master.economics;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 回收条目
 */
public class Recovery implements ConfigurationSerializable {
    /**
     *
     * @return 回收的物品
     */
    public ItemStack getThings() {
        return things;
    }

    ItemStack things;
    int price;
    Recovery(Material m,int p){
        things=new ItemStack(m);
        ArrayList<String> lore=new ArrayList<>();
        lore.add("回收单价："+p);
        ItemMeta im=things.getItemMeta();
        im.setLore(lore);
        things.setItemMeta(im);
        price=p;
    }
    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map=new HashMap<>();
        map.put("things",things);
        map.put("price",price);
        return map;
    }
    public static Recovery deserialize(Map<String,Object> map){
        ItemStack things=(ItemStack) map.get("things");
        int price=(int) map.get("price");
        return new Recovery(things.getType(),price);
    }
}
