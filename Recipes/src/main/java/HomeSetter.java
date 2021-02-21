
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeSetter implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(Player.class.isAssignableFrom(commandSender.getClass())) {
            MoreRecipes.conf.set(commandSender.getName(), ((Player) commandSender).getLocation());
            return true;
        }else{
            return false;
        }
    }
}
