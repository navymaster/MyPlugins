import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Wabbajack implements Listener {
    public static ItemMeta wabbajack;
    @EventHandler
    public void handle(EntityDamageByEntityEvent e){
        if(Player.class.isAssignableFrom(e.getDamager().getClass())){
            ItemStack is= ((Player)e.getDamager()).getInventory().getItem(EquipmentSlot.HAND);
            try {
                if (is.getItemMeta().getLore().equals(wabbajack.getLore()) &&
                        is.getItemMeta().getDisplayName().equals(wabbajack.getDisplayName())) {
                    if (LivingEntity.class.isAssignableFrom(e.getEntity().getClass())) {
                        LivingEntity entity = (LivingEntity) e.getEntity();
                        if (Boss.class.isAssignableFrom(entity.getClass())) {
                            return;
                        }
                        Location loc = entity.getLocation();
                        Random r = new Random();
                        int a = r.nextInt() % 11;
                        switch (a) {
                            case 0:
                                entity.getWorld().spawnEntity(loc, EntityType.DOLPHIN);
                                break;
                            case 1:
                                entity.getWorld().spawnEntity(loc, EntityType.SQUID);
                                break;
                            case 2:
                                entity.getWorld().spawnEntity(loc, EntityType.GUARDIAN);
                                break;
                            case 3:
                                entity.getWorld().spawnEntity(loc, EntityType.ELDER_GUARDIAN);
                                break;
                            case 4:
                                entity.getWorld().spawnEntity(loc, EntityType.TURTLE);
                                break;
                            case 5:
                                entity.getWorld().spawnEntity(loc, EntityType.COD);
                                break;
                            case 6:
                                entity.getWorld().spawnEntity(loc, EntityType.PUFFERFISH);
                                break;
                            case 7:
                                entity.getWorld().spawnEntity(loc, EntityType.TROPICAL_FISH);
                                break;
                            case 8:
                                entity.getWorld().spawnEntity(loc, EntityType.DROWNED);
                                break;
                            case 9:
                                entity.getWorld().spawnEntity(loc, EntityType.POLAR_BEAR);
                                break;
                            default:
                                entity.getWorld().spawnEntity(loc, EntityType.SALMON);
                                break;
                        }
                        entity.remove();
                    }
                }
            }catch(NullPointerException n){

            }
        }
    }
}
