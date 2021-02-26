package cn.navy_master.recipes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeSetter implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(Player.class.isAssignableFrom(commandSender.getClass())) {
            if(strings.length==0) {
                MoreRecipes.conf.set(commandSender.getName(), ((Player) commandSender).getLocation());
                commandSender.sendMessage("已成功设置你的家");
            }
            else{
                if(commandSender.isOp()||strings[0].equals(commandSender.getName())) {
                    MoreRecipes.conf.set(strings[0], ((Player) commandSender).getLocation());
                    commandSender.sendMessage("已成功设置你的家");
                }
                else
                    commandSender.sendMessage("权限不足");
            }
            return true;
        }else{
            return false;
        }
    }
}
