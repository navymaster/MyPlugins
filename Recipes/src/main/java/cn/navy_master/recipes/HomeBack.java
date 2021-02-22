package cn.navy_master.recipes;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeBack implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(MoreRecipes.conf.contains(commandSender.getName())&&Player.class.isAssignableFrom(commandSender.getClass()))
        {
            Location loc=(Location) MoreRecipes.conf.get(commandSender.getName());
            ((Player) commandSender).teleport(loc);
            return true;
        }else {
            commandSender.sendMessage("你还没有设置home,或你不是玩家");
            return false;
        }
    }
}
