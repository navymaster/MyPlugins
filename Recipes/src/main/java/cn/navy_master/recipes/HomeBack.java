package cn.navy_master.recipes;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeBack implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(MoreRecipes.homes.contains(commandSender.getName())&&Player.class.isAssignableFrom(commandSender.getClass()))
        {
            if(strings.length==0)
            {
                Location loc=(Location) MoreRecipes.homes.get(commandSender.getName());
                ((Player) commandSender).teleport(loc);
                return true;
            }else{
                try {
                    Location loc = (Location) MoreRecipes.homes.get(strings[0]);
                    ((Player) commandSender).teleport(loc);
                }catch (Exception e){
                    commandSender.sendMessage("错误的指向");
                }
                return true;
            }
        }else {
            commandSender.sendMessage("你还没有设置home,或你不是玩家");
            return false;
        }
    }
}
