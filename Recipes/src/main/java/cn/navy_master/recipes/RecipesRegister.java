package cn.navy_master.recipes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipesRegister {
    public static void Reg(){
        ItemStack result=new ItemStack(Material.LEATHER);
        FurnaceRecipe FR=new FurnaceRecipe(NamespacedKey.minecraft("more_recipes_for_wizardstaff"),result,Material.ROTTEN_FLESH,(float)0.1,200);
        Bukkit.getServer().addRecipe(FR);
        //链甲
        result=new ItemStack(Material.CHAINMAIL_BOOTS);
        ShapedRecipe sr=new ShapedRecipe(NamespacedKey.minecraft("chain_boots"),result);
        sr.shape("x x","x x");
        sr.setIngredient('x',Material.CHAIN);
        Bukkit.getServer().addRecipe(sr);

        result=new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        sr=new ShapedRecipe(NamespacedKey.minecraft("chain_chestplate"),result);
        sr.shape("x x","xxx","xxx");
        sr.setIngredient('x',Material.CHAIN);
        Bukkit.getServer().addRecipe(sr);

        result=new ItemStack(Material.CHAINMAIL_HELMET);
        sr=new ShapedRecipe(NamespacedKey.minecraft("chain_helmet"),result);
        sr.shape("xxx","x x");
        sr.setIngredient('x',Material.CHAIN);
        Bukkit.getServer().addRecipe(sr);

        result=new ItemStack(Material.CHAINMAIL_LEGGINGS);
        sr=new ShapedRecipe(NamespacedKey.minecraft("chain_leggings"),result);
        sr.shape("xxx","x x","x x");
        sr.setIngredient('x',Material.CHAIN);
        Bukkit.getServer().addRecipe(sr);

        ItemStack is=new ItemStack(Material.STICK);
        ItemMeta im=is.getItemMeta();
        List<String> l=new ArrayList<>();
        l.add("打人不疼");
        l.add("但是有毒");
        im.setDisplayName("有毒的棒棒");
        im.setLore(l);
        im.addEnchant(Enchantment.KNOCKBACK,4,true);
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",4, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("poisonous_stick"),is);
        sr.shape("xxx","xxx","xxx");
        sr.setIngredient('x',Material.POISONOUS_POTATO);
        Bukkit.getServer().addRecipe(sr);
        ListenerForMoreRecipes.watch_poisonus_stick=is;

        result=new ItemStack(Material.BLAZE_ROD);
        im=result.getItemMeta();
        l=new ArrayList<>();
        l.add("打人很疼");
        l.add("而且有毒");
        im.setDisplayName("有毒的大棒");
        im.setLore(l);
        im.addEnchant(Enchantment.KNOCKBACK,16,true);
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",7, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        result.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("powered_poisonous_stick"),result);
        sr.shape("xxx","xxx","xxx");
        RecipeChoice rc=new RecipeChoice.ExactChoice(is);
        sr.setIngredient('x',rc);
        Bukkit.getServer().addRecipe(sr);
        ListenerForMoreRecipes.watch_powered_poisonus_stick=result;

        //鱼棒棒
        is=new ItemStack(Material.STICK);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add("打怪掉鱼两不误");
        im.setDisplayName("鱼棒棒");
        im.setLore(l);
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",5, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        //result=new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        sr=new ShapedRecipe(NamespacedKey.minecraft("fish_stick"),is);
        sr.shape("xya","bzb","ayx");
        sr.setIngredient('x',Material.COD);
        sr.setIngredient('y',Material.SALMON);
        sr.setIngredient('z',Material.PUFFERFISH);
        sr.setIngredient('a',Material.TROPICAL_FISH);
        sr.setIngredient('b',Material.NAUTILUS_SHELL);
        Bukkit.getServer().addRecipe(sr);
        FishStickListener.fish_stick=is;

        //瓦巴杰克-水产限定版
        result=new ItemStack(Material.BLAZE_ROD);
        im=result.getItemMeta();
        l=new ArrayList<>();
        l.add("可以将被击中之非强大生物变为水产");
        l.add("战利品也会改变，请自行斟酌");
        im.setDisplayName("瓦巴杰克-水产限定版");
        im.setLore(l);
        result.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("wabbajack"),result);
        sr.shape("xxx","xzx","xxx");
        rc=new RecipeChoice.ExactChoice(is);
        sr.setIngredient('x',rc);
        sr.setIngredient('z',Material.CONDUIT);
        Bukkit.getServer().addRecipe(sr);
        Wabbajack.wabbajack=result.getItemMeta();

        is=new ItemStack(Material.BREAD);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add("柔韧性好，不过很脆");
        l.add("可以让敌人装备松动");
        im.setDisplayName("压缩硬面包");
        im.setLore(l);
        //im.addEnchant(Enchantment.KNOCKBACK,4,true);
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",5, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("france_bread"),is);
        sr.shape("  x"," x ","x  ");
        sr.setIngredient('x',Material.BREAD);
        Bukkit.getServer().addRecipe(sr);
        ListenerForMoreRecipes.France_Bread=is;

        is=new ItemStack(Material.BLAZE_ROD);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add(ChatColor.WHITE+"向敌人发射三枚追踪的法术飞弹");
        l.add("MAGIC_MISSILE<80/80>");
        im.setDisplayName(ChatColor.BLUE+"魔法飞弹法杖");
        im.setLore(l);
        im.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        //im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",5, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("magic_missile_staff"),is);
        sr.shape("xxx"," y "," z ");
        sr.setIngredient('x',Material.SNOWBALL);
        sr.setIngredient('y',Material.BLAZE_ROD);
        sr.setIngredient('z',Material.OBSIDIAN);
        Bukkit.getServer().addRecipe(sr);
        //cn.navy_master.recipes.ListenerForMoreRecipes.France_Bread=is;

        is=new ItemStack(Material.BLAZE_POWDER);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add(ChatColor.WHITE+"召唤一颗火球向前飞去");
        l.add("FIRE_BALL<200/200>");
        im.setDisplayName(ChatColor.BLUE+"炽热余烬");
        im.setLore(l);
        im.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        //im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",5, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("fire_ball_staff"),is);
        sr.shape("zkz","xyx","xxx");
        sr.setIngredient('x',Material.MAGMA_CREAM);
        sr.setIngredient('y',Material.BLAZE_POWDER);
        sr.setIngredient('z',Material.BASALT);
        sr.setIngredient('k',Material.GHAST_TEAR);
        Bukkit.getServer().addRecipe(sr);

        is=new ItemStack(Material.FEATHER);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add(ChatColor.WHITE+"获得临时生命，在消耗完前获得反甲，不可叠加");
        l.add("FROST_ARMOR<20/20>");
        im.setDisplayName(ChatColor.YELLOW+"落雪之羽");
        im.setLore(l);
        im.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        //im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",5, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("frost_armor_staff"),is);
        sr.shape("xxx","xyx","xxx");
        sr.setIngredient('x',Material.SNOW_BLOCK);
        sr.setIngredient('y',Material.FEATHER);
        Bukkit.getServer().addRecipe(sr);

        is=new ItemStack(Material.BOOK);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add(ChatColor.WHITE+"检视所有被加载的活物，你可以传送到其中任意一个的位置");
        l.add("TELEPORT<500/500>");
        im.setDisplayName(ChatColor.GOLD+"监察法典");
        im.setLore(l);
        im.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        //im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",5, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("teleport_staff"),is);
        sr.shape("xzx","zyz","xzx");
        sr.setIngredient('x',Material.ENDER_EYE);
        sr.setIngredient('y',Material.BOOK);
        sr.setIngredient('z',Material.CHORUS_FRUIT);
        Bukkit.getServer().addRecipe(sr);

        is=new ItemStack(Material.NETHERITE_INGOT);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add(ChatColor.WHITE+"立刻在你看向的位置召唤一道闪电");
        l.add("THUNDER_CALLING<250/250>");
        im.setDisplayName(ChatColor.GOLD+"引雷石");
        im.setLore(l);
        im.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        //im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",5, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("thunder_calling_staff"),is);
        sr.shape("xzx","zyz","xzx");
        sr.setIngredient('x',Material.IRON_INGOT);
        sr.setIngredient('y',Material.NETHERITE_INGOT);
        sr.setIngredient('z',Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(sr);

        is=new ItemStack(Material.CLOCK);
        im=is.getItemMeta();
        l=new ArrayList<>();
        l.add(ChatColor.WHITE+"立刻在你看向的位置造成一阵巨大的震动");
        l.add("SHATTER<40/40>");
        im.setDisplayName(ChatColor.GOLD+"嗡响时钟");
        im.setLore(l);
        im.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        //im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID(),"xx",5, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        is.setItemMeta(im);
        sr=new ShapedRecipe(NamespacedKey.minecraft("shatter_staff"),is);
        sr.shape("xzx","zyz","xzx");
        sr.setIngredient('x',Material.REDSTONE_BLOCK);
        sr.setIngredient('y',Material.CLOCK);
        sr.setIngredient('z',Material.FLINT);
        Bukkit.getServer().addRecipe(sr);

        is=new ItemStack(Material.TRIDENT);
        sr=new ShapedRecipe(NamespacedKey.minecraft("trident"),is);
        sr.shape("xxx","zzz"," z ");
        sr.setIngredient('x',Material.IRON_INGOT);
        sr.setIngredient('z',Material.PRISMARINE);
        Bukkit.getServer().addRecipe(sr);

        is=new ItemStack(Material.NAME_TAG);
        sr=new ShapedRecipe(NamespacedKey.minecraft("name_tag"),is);
        sr.shape("xkx","xkx"," y ");
        sr.setIngredient('x',Material.PAPER);
        sr.setIngredient('y',Material.IRON_INGOT);
        sr.setIngredient('k',Material.GLOWSTONE_DUST);
        Bukkit.getServer().addRecipe(sr);

    }
}
