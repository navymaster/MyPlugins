package cn.navy_master.economics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品条目管理类
 */
public class Shop {
    public static final int MAIN=0;
    public static final int LIST=1;
    public static final int RECOVERY=2;
    public static final int NEWTRA=3;
    public static final int REMOVETRA=4;
    public static final int TRADING=5;
    public static Shop getInstance(){
        if(Objects.isNull(INSTANCE)){
            INSTANCE=new Shop();
        }
        return INSTANCE;
    }
    private static Shop INSTANCE;
    List<Transaction> transactions=new ArrayList<>();
    List<Recovery> recoveryList=new ArrayList<>();
    public void loadConfig(){
        transactions= (ArrayList<Transaction>)Economics.getInstance().getConf().get("TransactionList");
        recoveryList= (ArrayList<Recovery>)Economics.getInstance().getConf().get("RecoveryList");
    }
    public void saveConfig(){
        Economics.getInstance().getConf().set("TransactionList",transactions);
        Economics.getInstance().getConf().set("RecoveryList",recoveryList);
    }

    /**
     * 展示市集初始界面
     * @param requester 请求者
     */
    public void shop_view(Player requester){
        Inventory i= Bukkit.createInventory(requester,9*6,"Shop");
        generateHeader(i,MAIN);
        requester.openInventory(i);
    }

    /**
     * 展示市集所有交易
     * @param in 市集使用的gui
     * @param page 页码
     */
    public void show_transactions(Inventory in,int page){
        if(page==-1)page=1;
        in.clear();
        generateHeader(in,LIST);
        int start_at=(page-1)*36;
        for(int i=0;i<36;i++)
        {
            if(transactions.size()>start_at+i)
                in.setItem(i+9,transactions.get(start_at+i).getThings());
        }
        ItemStack is=new ItemStack(Material.TRIDENT);
        ItemMeta im=is.getItemMeta();
        if(page!=1){
            im.setDisplayName("上一页");
            is.setItemMeta(im);
            in.setItem(45,is);
        }
        if(transactions.size()>=start_at+36){
            im.setDisplayName("下一页");
            is.setItemMeta(im);
            in.setItem(53,is);
        }
        im.setDisplayName("数量为页码");
        is.setItemMeta(im);
        is.setAmount(page);
        in.setItem(49,is);
        /*if(){
            in.setItem();
        }*/
    }
    /**
     * 展示市集所有回收
     * @param in 市集使用的gui
     * @param page 页码
     */
    public void show_recovery(Inventory in,int page){
        if(page==-1)page=1;
        in.clear();
        generateHeader(in,RECOVERY);
        int start_at=(page-1)*36;
        for(int i=0;i<36;i++)
        {
            if(recoveryList.size()>start_at+i)
                in.setItem(i+9,recoveryList.get(start_at+i).getThings());
        }
        ItemStack is=new ItemStack(Material.TRIDENT);
        ItemMeta im=is.getItemMeta();
        if(page!=1){
            im.setDisplayName("上一页");
            is.setItemMeta(im);
            in.setItem(45,is);
        }
        if(recoveryList.size()>=start_at+36){
            im.setDisplayName("下一页");
            is.setItemMeta(im);
            in.setItem(53,is);
        }
        im.setDisplayName("数量为页码");
        is.setItemMeta(im);
        is.setAmount(page);
        in.setItem(49,is);
        /*if(transactions.size()>start_at+36){
            in.setItem();
        }*/
    }

    /**
     * 我的交易
     * @param in 交易gui
     * @param page 页码
     */
    public void show_my_trade(Inventory in,int page){
        if(page==-1)page=1;
        in.clear();
        generateHeader(in,REMOVETRA);
        int start_at=(page-1)*36;
        List<Transaction> my=new ArrayList<>();
        for(Transaction t:transactions){
            if(t.getOwner().equals(((Player)in.getHolder()).getName())){
                my.add(t);
            }
        }
        for(int i=0;i<36;i++)
        {
            if(my.size()>start_at+i)
                in.setItem(i+9,my.get(start_at+i).getThings());
        }
        ItemStack is=new ItemStack(Material.TRIDENT);
        ItemMeta im=is.getItemMeta();
        if(page!=1){
            im.setDisplayName("上一页");
            is.setItemMeta(im);
            in.setItem(45,is);
        }
        if(my.size()>=start_at+36){
            im.setDisplayName("下一页");
            is.setItemMeta(im);
            in.setItem(53,is);
        }
        im.setDisplayName("数量为页码");
        is.setItemMeta(im);
        is.setAmount(page);
        in.setItem(49,is);
    }

    /**
     * 发布交易页面
     * @param in 箱子gui
     */
    public void show_trade_maker(Inventory in){
        in.clear();
        generateHeader(in,NEWTRA);
        ItemStack is=new ItemStack(Material.PAPER);
        ItemMeta im=is.getItemMeta();
        im.setDisplayName("确认");
        is.setItemMeta(im);
        in.setItem(53,is);
        for(int i=1;i<=9;i++){
            im.setDisplayName(""+i);
            is.setItemMeta(im);
            in.setItem(8+i,is);
        }
        im.setDisplayName(""+0);
        is.setItemMeta(im);
        in.setItem(18,is);
        im.setDisplayName("当前设定单价");
        ArrayList<String> lore=new ArrayList<>();
        lore.add(""+0);
        im.setLore(lore);
        is.setItemMeta(im);
        in.setItem(45,is);
        if(((Player)in.getHolder()).isOp())
        {
            im.setDisplayName("设置无限售卖");
            lore=new ArrayList<>();
            lore.add("当前设置为：");
            lore.add("false");
            lore.add("只有以System的名义挂起交易时，此项才有效");
            im.setLore(lore);
            is.setItemMeta(im);
            in.setItem(35,is);
            im.setDisplayName("以System的名义售卖");
            lore=new ArrayList<>();
            lore.add("这意味着售卖收益不会进入你的账户");
            lore.add("仅op可用");
            im.setLore(lore);
            is.setItemMeta(im);
            in.setItem(44,is);
        }
        is=new ItemStack(Material.GOLD_INGOT);
        im=is.getItemMeta();
        im.setDisplayName("要出售的东西请放在中间");
        is.setItemMeta(im);
        in.setItem(28,is);
        in.setItem(30,is);
        in.setItem(19,is);
        in.setItem(20,is);
        in.setItem(21,is);
        in.setItem(37,is);
        in.setItem(38,is);
        in.setItem(39,is);
    }

    /**
     * 输入数字后刷新交易页面
     * @param in 交易gui
     * @param add 按下的数值
     */
    public void flash_trade_maker(Inventory in,int add){
        ItemMeta im=in.getItem(45).getItemMeta();
        int value= Integer.parseInt(im.getLore().get(0));
        value=value*10+add;
        ItemStack is=new ItemStack(Material.PAPER);
        im=is.getItemMeta();
        im.setDisplayName("当前设定价格");
        ArrayList<String> lore=new ArrayList<>();
        lore.add(""+value);
        im.setLore(lore);
        is.setItemMeta(im);
        in.setItem(45,is);
    }

    /**
     * 刷新无限出售设置
     * @param in 箱子gui
     */
    public void flash_inf_sell(Inventory in){
        ItemStack is=in.getItem(35);
        ItemMeta im=is.getItemMeta();
        List<String> lore=im.getLore();
        boolean inf=lore.get(1).equals("true");
        if(inf){
            lore.set(1,"false");
        }else{
            lore.set(1,"true");
        }
        im.setLore(lore);
        is.setItemMeta(im);
    }

    /**
     * 显示正在检视的交易
     * @param in 箱子gui
     * @param t 交易
     */
    public void show_trading(Inventory in,Transaction t){
        in.clear();
        generateHeader(in,TRADING);
        ItemStack is=new ItemStack(Material.PAPER);
        ItemMeta im=is.getItemMeta();
        im.setDisplayName("确认");
        is.setItemMeta(im);
        in.setItem(53,is);
        im.setDisplayName("当前设定单价");
        ArrayList<String> lore=new ArrayList<>();
        lore.add("所有者:");
        lore.add(t.getOwner());
        lore.add("价格:");
        lore.add(""+t.getPrice());
        if(t.inf)
        lore.add("无限出售");
        im.setLore(lore);
        is.setItemMeta(im);
        in.setItem(45,is);
        is=new ItemStack(Material.GOLD_INGOT);
        im=is.getItemMeta();
        im.setDisplayName("要出售的东西在中间");
        is.setItemMeta(im);
        in.setItem(28,is);
        in.setItem(30,is);
        in.setItem(19,is);
        in.setItem(20,is);
        in.setItem(21,is);
        in.setItem(37,is);
        in.setItem(38,is);
        in.setItem(39,is);
        in.setItem(29,t.getThings());
    }

    /**
     * 生成页面头
     * @param i 箱子gui
     * @param mode gui所属模式
     */
    protected void generateHeader(Inventory i,int mode){
        ItemStack is=new ItemStack(Material.GRASS_BLOCK);
        ItemStack is2=new ItemStack(Material.STONE);
        ItemMeta im=is.getItemMeta();
        im.setDisplayName("查看集市所有商品");
        if(mode==LIST){
        is2.setItemMeta(im);
        i.setItem(0,is2);
        }else{
        is.setItemMeta(im);
        i.setItem(0,is);
        }
        im.setDisplayName("查看物品回收条目");
        if(mode==RECOVERY){
            is2.setItemMeta(im);
            i.setItem(2,is2);
        }else{
            is.setItemMeta(im);
            i.setItem(2,is);
        }
        im.setDisplayName("添加我的商品");
        if(mode==NEWTRA){
            is2.setItemMeta(im);
            i.setItem(4,is2);
        }else{
            is.setItemMeta(im);
            i.setItem(4,is);
        }
        im.setDisplayName("移除我的商品");
        if(mode==REMOVETRA){
            is2.setItemMeta(im);
            i.setItem(6,is2);
        }else{
            is.setItemMeta(im);
            i.setItem(6,is);
        }
        im.setDisplayName("仅供查看");
        ArrayList<String> lore=new ArrayList<>();
        lore.add("你的绿宝石数："+Bank.getInstance().get(((Player)i.getHolder()).getName()));
        im.setLore(lore);
        if(mode==TRADING){
            is2.setItemMeta(im);
            i.setItem(8,is2);
        }else{
            is.setItemMeta(im);
            i.setItem(8,is);
        }
    }

    /**
     * 新建交易
     * @param in 箱子gui
     */
    public void pushTran(Inventory in){
        ItemStack is=in.getItem(45);
        int price=Integer.parseInt(is.getItemMeta().getLore().get(0));
        is=in.getItem(29);
        transactions.add(new Transaction(is,((Player)in.getHolder()).getName(),price,is.getAmount(),false));
        in.setItem(29,null);
    }

    /**
     * 以系统的名义新建交易
     * @param in 箱子gui
     */
    public void pushTranAsSystem(Inventory in){
        ItemStack is=in.getItem(45);
        int price=Integer.parseInt(is.getItemMeta().getLore().get(0));
        is=in.getItem(29);
        boolean inf=in.getItem(35).getItemMeta().getLore().get(1).equals("true");
        transactions.add(new Transaction(is,"System",price,is.getAmount(),inf));
        in.setItem(29,null);
    }

    /**
     * 新建回收
     * @param m 回收物类别
     * @param price 回收价格
     */
    public void pushReco(Material m,int price){
        recoveryList.add(new Recovery(m,price));
    }

    /**
     * 移除交易
     * @param is 物品
     * @param owner 所有者
     */
    public void removeTran(ItemStack is,Player owner){
        if(!Objects.isNull(is)){
            for(Transaction t:Shop.getInstance().transactions){
                if(t.getOwner().equals(owner.getName())&&t.getThings().equals(is)){
                    transactions.remove(t);
                    owner.getInventory().addItem(t.getThings());
                    break;
                }
            }
        }
    }

    /**
     * 进行交易
     * @param i 箱子gui
     * @param buyer 购买者
     * @param is 物品
     */
    public void makeTran(Inventory i,Player buyer,ItemStack is){
        for(Transaction t:transactions)
        {
            List<String> lore=i.getItem(45).getItemMeta().getLore();
            String owner=lore.get(1);
            int price=Integer.parseInt(lore.get(3));
            if(t.things.equals(is)
                    &&t.getPrice()==price
                    &&t.getOwner().equals(owner)
                    &&Bank.getInstance().get(buyer.getName())>t.getPrice()){
                ItemStack ix=is;
                if(!t.inf)
                {
                    t.getThings().setAmount(t.getThings().getAmount()-1);
                    t.num--;
                }
                ix.setAmount(1);
                buyer.getInventory().addItem(ix);
                Bank.getInstance().merge(buyer.getName(),-t.getPrice());
                Bank.getInstance().merge(t.getOwner(),t.getPrice());
                if(t.getThings().getAmount()!=0)
                    show_trading(i,t);
                else{
                    for(Player p:Bukkit.getOnlinePlayers()){
                        if(t.getOwner().equals(p.getName())){
                            p.sendMessage("[市集提醒]你有一个商品已经售卖一空");
                        }
                    }
                    transactions.remove(t);
                    shop_view(buyer);
                }
                break;
            }
        }
    }
}
