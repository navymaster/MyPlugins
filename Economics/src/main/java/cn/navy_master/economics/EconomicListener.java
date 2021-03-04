package cn.navy_master.economics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * 监听箱子点击事件和玩家加入游戏事件
 */
public class EconomicListener implements Listener {
    @EventHandler
    public void handle(PlayerJoinEvent e){
        if(!Bank.getInstance().isRegistered(e.getPlayer().getName())){
            Bank.getInstance().register(e.getPlayer().getName());
            e.getPlayer().sendMessage("检测到你尚未拥有服务器银行账户，已为你注册。使用/trade可以打开市场界面");
        }
    }
    @EventHandler
    public void handle(InventoryClickEvent e){
        if(e.getWhoClicked().getOpenInventory().getTitle().equals("Shop")){
            int n=e.getRawSlot();
            int page=-1;
            int mode=-1;
            if(!Objects.isNull(e.getInventory().getItem(49)))
                page=e.getInventory().getItem(49).getAmount();
            if(n<54) {
                if (e.getInventory().getItem(0).getType().equals(Material.STONE)) {
                    mode = Shop.LIST;
                    if (e.getRawSlot() > 8&&e.getRawSlot()<45) {
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        if (!Objects.isNull(is)) {
                            int slot = e.getRawSlot() - 9;
                            Shop.getInstance().show_trading(e.getInventory(), Shop.getInstance().transactions.get(slot));
                        }
                    }
                    if(e.getRawSlot()==45){
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        if(!Objects.isNull(is)){
                            Shop.getInstance().show_transactions(e.getInventory(),page-1);
                        }
                    }
                    if(e.getRawSlot()==53){
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        if(!Objects.isNull(is)){
                            Shop.getInstance().show_transactions(e.getInventory(),page+1);
                        }
                    }
                    e.setCancelled(true);
                }
                if (e.getInventory().getItem(2).getType().equals(Material.STONE)) {
                    mode = Shop.RECOVERY;
                    if (e.getRawSlot() > 8&&e.getRawSlot()<45) {
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        if (!Objects.isNull(is)) {
                            int slot = e.getRawSlot() - 9;
                            is = new ItemStack(is.getType());
                            for (ItemStack s : e.getWhoClicked().getInventory()) {
                                if (!Objects.isNull(s))
                                    if (s.isSimilar(is)) {
                                        s.setAmount(s.getAmount() - 1);
                                        Bank.getInstance().merge(e.getWhoClicked().getName(), Shop.getInstance().recoveryList.get(slot).price);
                                        Shop.getInstance().generateHeader(e.getInventory(), Shop.RECOVERY);
                                        break;
                                    }
                            }
                        }
                    }
                    if(e.getRawSlot()==45){
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        if(!Objects.isNull(is)){
                            Shop.getInstance().show_recovery(e.getInventory(),page-1);
                        }
                    }
                    if(e.getRawSlot()==53){
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        if(!Objects.isNull(is)){
                            Shop.getInstance().show_recovery(e.getInventory(),page+1);
                        }
                    }
                    e.setCancelled(true);
                }
                if (e.getInventory().getItem(4).getType().equals(Material.STONE)) {
                    mode = Shop.NEWTRA;
                    if (n > 8 && n < 18) {
                        Shop.getInstance().flash_trade_maker(e.getInventory(), n - 8);
                    }
                    if (n == 18) Shop.getInstance().flash_trade_maker(e.getInventory(), 0);
                    if (n == 29 || n > 53) {

                    } else {
                        e.setCancelled(true);
                    }
                    if (n == 35 && e.getWhoClicked().isOp()) {
                        Shop.getInstance().flash_inf_sell(e.getInventory());
                    }
                    if (n == 44 && e.getWhoClicked().isOp()) {
                        if (!Objects.isNull(e.getInventory().getItem(29)))
                            Shop.getInstance().pushTranAsSystem(e.getInventory());
                    }
                    if (n == 53) {
                        if (!Objects.isNull(e.getInventory().getItem(29))) {

                            Shop.getInstance().pushTran(e.getInventory());
                        }
                    }
                }
                if (e.getInventory().getItem(6).getType().equals(Material.STONE)) {
                    mode = Shop.REMOVETRA;
                    if (e.getRawSlot() > 8&&e.getRawSlot()<45) {
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        Shop.getInstance().removeTran(is, (Player) e.getWhoClicked());
                        Shop.getInstance().show_my_trade(e.getInventory(), -1);
                    }
                    if(e.getRawSlot()==45){
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        if(!Objects.isNull(is)){
                            Shop.getInstance().show_my_trade(e.getInventory(),page-1);
                        }
                    }
                    if(e.getRawSlot()==53){
                        ItemStack is = e.getInventory().getItem(e.getRawSlot());
                        if(!Objects.isNull(is)){
                            Shop.getInstance().show_my_trade(e.getInventory(),page+1);
                        }
                    }
                    e.setCancelled(true);
                }
                if (e.getInventory().getItem(8).getType().equals(Material.STONE)) {
                    mode = Shop.TRADING;
                    if (e.getRawSlot() == 53) {
                        Shop.getInstance().makeTran(e.getInventory(), (Player) e.getWhoClicked(), e.getInventory().getItem(29));
                    }
                    e.setCancelled(true);
                }
                if(mode==-1)e.setCancelled(true);
            }
            if(n<9) {
                if(mode==Shop.NEWTRA){
                    ItemStack is=e.getInventory().getItem(29);
                    if(!Objects.isNull(is)){
                        e.getWhoClicked().getInventory().addItem(is);
                    }
                }
                if (n == 0) {
                    if (e.getInventory().getItem(0).getType().equals(Material.GRASS_BLOCK))
                        Shop.getInstance().show_transactions(e.getInventory(), -1);
                    e.setCancelled(true);
                }
                if (n == 2) {
                    if (e.getInventory().getItem(2).getType().equals(Material.GRASS_BLOCK))
                        Shop.getInstance().show_recovery(e.getInventory(), page);
                    e.setCancelled(true);
                }
                if (n == 4) {
                    if (e.getInventory().getItem(4).getType().equals(Material.GRASS_BLOCK))
                        Shop.getInstance().show_trade_maker(e.getInventory());
                    e.setCancelled(true);
                }
                if (n == 6) {
                    if (e.getInventory().getItem(6).getType().equals(Material.GRASS_BLOCK))
                        Shop.getInstance().show_my_trade(e.getInventory(), page);
                    e.setCancelled(true);
                }
            }
            //Bukkit.getLogger().info("点击位置为："+n);
        }
    }
}
