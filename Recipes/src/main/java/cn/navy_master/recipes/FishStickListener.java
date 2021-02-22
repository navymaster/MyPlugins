package cn.navy_master.recipes;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FishStickListener implements Listener {
    public static ItemStack fish_stick;
    @EventHandler
    public void handle(EntityDeathEvent e){
        try {
            //Bukkit.getLogger().info("handle");
            if(e.getEntity().getKiller().getInventory().getItem(EquipmentSlot.HAND).
                    getItemMeta().getLore().equals(fish_stick.getItemMeta().getLore())&&
                    e.getEntity().getKiller().getInventory().getItem(EquipmentSlot.HAND).
                            getItemMeta().getDisplayName().equals(fish_stick.getItemMeta().getDisplayName())){
                //Bukkit.getLogger().info("do!");
                Random r=new Random();
                int a=r.nextInt()%300;
                switch (a){
                    case 0:
                        e.getDrops().add(new ItemStack(Material.HEART_OF_THE_SEA));
                        break;
                    case 1:
                        e.getDrops().add(new ItemStack(Material.PRISMARINE_SHARD));
                        break;
                    case 2:
                        e.getDrops().add(new ItemStack(Material.PUFFERFISH));
                        break;
                    case 3:
                        e.getDrops().add(new ItemStack(Material.PRISMARINE_CRYSTALS));
                        break;
                    default:
                        if(a<=27)
                        {e.getDrops().add(new ItemStack(Material.COD));}
                        else if(a<=51){e.getDrops().add(new ItemStack(Material.TROPICAL_FISH));}
                        else if(a<=75){e.getDrops().add(new ItemStack(Material.SALMON));}
                        else if(a<=99){
                            e.getDrops().add(new ItemStack(Material.INK_SAC));
                        }
                        break;
                }
            }
        }catch (NullPointerException n){n.printStackTrace();
        }
    }
}
