package cn.navy_master.recipes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HomeGetter implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for(String key:MoreRecipes.homes.getKeys(false))
        {
            commandSender.sendMessage(key);
        }
        return true;
    }
}
