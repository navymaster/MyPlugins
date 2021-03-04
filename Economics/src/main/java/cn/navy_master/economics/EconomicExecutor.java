package cn.navy_master.economics;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * trade指令执行器类
 */
public class EconomicExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()&&commandSender instanceof Player){
            ItemStack is= ((Player) commandSender).getInventory().getItem(EquipmentSlot.HAND);
            if(!Objects.isNull(is))
            {
                Shop.getInstance().pushReco(is.getType(),Integer.parseInt(strings[0]));
            }
            return true;
        }
        return false;
    }

}
