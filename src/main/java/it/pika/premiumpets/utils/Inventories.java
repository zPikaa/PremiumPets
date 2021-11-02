package it.pika.premiumpets.utils;

import it.pika.premiumpets.PremiumPets;
import it.pika.premiumpets.files.MessagesFile;
import it.pika.premiumpets.utils.powerlib.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Inventories {

    public static Inventory getShopGUI(){
        ItemStack glass = new ItemBuilder()
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .build();

        Inventory inventory = Bukkit.createInventory(null, 54, Utils.chat(MessagesFile.getMessagesFile().getString("shop-gui-title")));
        inventory.setItem(45, glass);
        inventory.setItem(46, glass);
        inventory.setItem(47, glass);
        inventory.setItem(48, glass);
        inventory.setItem(49, glass);
        inventory.setItem(50, glass);
        inventory.setItem(51, glass);
        inventory.setItem(52, glass);
        inventory.setItem(53, glass);

        for(String key : PremiumPets.getInstance().getConfig().getConfigurationSection("pets").getKeys(false)) {
            if(PremiumPets.getInstance().getConfig().getBoolean("pets."+key+".buyable")){
                String petName = PremiumPets.getInstance().getConfig().getString("pets."+key+".name");
                String petDisplayName = PremiumPets.getInstance().getConfig().getString("pets."+key+".displayName");
                String petCustomHead = PremiumPets.getInstance().getConfig().getString("pets."+key+".customHead");
                int petPrice = PremiumPets.getInstance().getConfig().getInt("pets."+key+".price");
                ArrayList<String> petLore = new ArrayList<>();
                petLore.add(Utils.chat(MessagesFile.getMessagesFile().getString("shop-pet-name").replace("{pet_name}", petDisplayName)));
                petLore.add(Utils.chat(MessagesFile.getMessagesFile().getString("shop-pet-price").replace("{pet_price}", String.valueOf(petPrice))));
                petLore.add(Utils.chat("&7"));
                petLore.add(Utils.chat(MessagesFile.getMessagesFile().getString("shop-pet-buy")));

                try {
                    ItemStack petItem = new ItemBuilder()
                            .setName(petName)
                            .setLore(petLore)
                            .customHeadBuild(petCustomHead);
                    try{
                        inventory.addItem(petItem);
                    }catch(ArrayIndexOutOfBoundsException e){
                        Bukkit.getLogger().severe("[PremiumPets] Failed to add pet item!");
                    }
                } catch (Exception exception) {
                    Bukkit.getLogger().severe("[PremiumPets] Could not load pet: " + petName);
                }
            }
        }
        return inventory;
    }

}
