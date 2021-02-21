import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Assemble implements CommandExecutor {
    Location assemble_loc;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equals("assemble")){
            if(Objects.isNull(assemble_loc)) {
                if (Entity.class.isAssignableFrom(commandSender.getClass())) {
                    Bukkit.broadcastMessage(ChatColor.RED+"An Assemble is calling by " + commandSender.getName() + ", if you want to teleport to the sponsor, use /response in 30s:\n" +ChatColor.YELLOW+((strings.length==0)?"The sponsor is lazy, leave no reason here.":strings[0]));
                    assemble_loc = ((Entity) commandSender).getLocation();
                    new BukkitRunnable() {
                        int i = 600;

                        @Override
                        public void run() {
                            if (i > 0) {
                                i--;
                            } else {
                                assemble_loc = null;
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(MoreRecipes.oneinstance, 1, 1);
                    return true;
                } else
                    return false;
            }else{
                commandSender.sendMessage("There is already an assemble calling.");
            }
        }
        if(command.getName().equals("response")){
            if(Objects.isNull(assemble_loc)){
                commandSender.sendMessage("No assemble is calling.");
            }else{
                if(Entity.class.isAssignableFrom(commandSender.getClass())) {
                    ((Entity) commandSender).teleport(assemble_loc);
                }else{
                    commandSender.sendMessage("you are unable to teleport");
                }
            }
            return true;
        }
        return false;
    }
}
