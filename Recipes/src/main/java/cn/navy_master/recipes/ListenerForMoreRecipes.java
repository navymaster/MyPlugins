package cn.navy_master.recipes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class ListenerForMoreRecipes implements Listener {
    public static ItemStack watch_poisonous_stick;
    public static ItemStack watch_powered_poisonous_stick;
    public static ItemStack France_Bread;
    @EventHandler
    public void poisonous(EntityDamageByEntityEvent e){
        if(LivingEntity.class.isAssignableFrom(e.getDamager().getClass())&&
                LivingEntity.class.isAssignableFrom(e.getEntity().getClass())
        ) {
            //Bukkit.getLogger().info("handle");
            LivingEntity damage = (LivingEntity) e.getDamager();
            try {
                ItemStack is = damage.getEquipment().getItemInMainHand();
                if(is.getItemMeta().getDisplayName().equals(watch_poisonous_stick.getItemMeta().getDisplayName())
                &&is.getItemMeta().getLore().equals(watch_poisonous_stick.getItemMeta().getLore()))
                {
                    //Bukkit.getLogger().info("success");
                    ((LivingEntity)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON,100,1,false,false));
                }
                if(is.getItemMeta().getDisplayName().equals(watch_powered_poisonous_stick.getItemMeta().getDisplayName())
                        &&is.getItemMeta().getLore().equals(watch_powered_poisonous_stick.getItemMeta().getLore()))
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
            } catch (NullPointerException ignored) {}
        }
    }
}
