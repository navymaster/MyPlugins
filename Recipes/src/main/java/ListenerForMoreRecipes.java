import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ListenerForMoreRecipes implements Listener {
    public static ItemStack watch_poisonus_stick;
    public static ItemStack watch_powered_poisonus_stick;
    public static ItemStack France_Bread;
    @EventHandler
    public void poisonous(EntityDamageByEntityEvent e){
        if(LivingEntity.class.isAssignableFrom(e.getDamager().getClass())&&
                LivingEntity.class.isAssignableFrom(e.getEntity().getClass())
        ) {
            //Bukkit.getLogger().info("handle");
            LivingEntity damager = (LivingEntity) e.getDamager();
            try {
                ItemStack is = damager.getEquipment().getItemInMainHand();
                if(is.getItemMeta().getDisplayName().equals(watch_poisonus_stick.getItemMeta().getDisplayName())
                &&is.getItemMeta().getLore().equals(watch_poisonus_stick.getItemMeta().getLore()))
                {
                    //Bukkit.getLogger().info("success");
                    ((LivingEntity)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON,100,1,false,false));
                }
                if(is.getItemMeta().getDisplayName().equals(watch_powered_poisonus_stick.getItemMeta().getDisplayName())
                        &&is.getItemMeta().getLore().equals(watch_powered_poisonus_stick.getItemMeta().getLore()))
                {
                    //Bukkit.getLogger().info("success");
                    ((LivingEntity)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON,120,2,false,false));
                }
                if(is.getItemMeta().getDisplayName().equals(France_Bread.getItemMeta().getDisplayName())
                        &&is.getItemMeta().getLore().equals(France_Bread.getItemMeta().getLore()))
                {
                    //Bukkit.getLogger().info("success");
                    if(!Player.class.isAssignableFrom(e.getEntity().getClass()))
                    {
                        ((LivingEntity)e.getEntity()).getEquipment().setHelmetDropChance((float)0.1+((LivingEntity)e.getEntity()).getEquipment().getHelmetDropChance());
                        ((LivingEntity)e.getEntity()).getEquipment().setChestplateDropChance((float)0.1+((LivingEntity)e.getEntity()).getEquipment().getChestplateDropChance());
                        ((LivingEntity)e.getEntity()).getEquipment().setBootsDropChance((float)0.1+((LivingEntity)e.getEntity()).getEquipment().getBootsDropChance());
                        ((LivingEntity)e.getEntity()).getEquipment().setLeggingsDropChance((float)0.1+((LivingEntity)e.getEntity()).getEquipment().getLeggingsDropChance());
                        ((LivingEntity)e.getEntity()).getEquipment().setItemInMainHandDropChance((float)0.1+((LivingEntity)e.getEntity()).getEquipment().getItemInMainHandDropChance());
                        ((LivingEntity)e.getEntity()).getEquipment().setItemInOffHandDropChance((float)0.1+((LivingEntity)e.getEntity()).getEquipment().getItemInOffHandDropChance());
                        is.setAmount(is.getAmount()-1);
                        ((LivingEntity) e.getDamager()).getEquipment().setItemInMainHand(is);
                    }
                }
            } catch (NullPointerException n) {return;}
        }
    }
    public double P_rand(double Lamda){ // 泊松分布
        double x=0,b=1,c=Math.exp(-Lamda),u;
        do {
            u=Math.random();
            b *=u;
            if(b>=c)
                x++;
        }while(b>=c);
        return x;
    }
}
