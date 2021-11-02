package it.pika.premiumpets.commands;

import it.pika.premiumpets.PremiumPets;
import it.pika.premiumpets.files.MessagesFile;
import it.pika.premiumpets.functions.PetFunctions;
import it.pika.premiumpets.objects.Pet;
import it.pika.premiumpets.utils.Inventories;
import it.pika.premiumpets.utils.Utils;
import it.pika.premiumpets.utils.powerlib.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PetCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length == 0){
                for(String s : MessagesFile.getMessagesFile().getStringList("help-message")){
                    p.sendMessage(Utils.chat(s));
                }
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("gui")){
                    ItemStack glass = new ItemBuilder()
                            .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                            .build();

                    ItemStack despawnPet = new ItemBuilder()
                            .setMaterial(Material.BARRIER)
                            .setName(Utils.chat(MessagesFile.getMessagesFile().getString("gui-despawnpet-name")))
                            .build();

                    Inventory inventory = Bukkit.createInventory(null, 54, Utils.chat(MessagesFile.getMessagesFile().getString("gui-title")));
                    inventory.setItem(45, glass);
                    inventory.setItem(46, glass);
                    inventory.setItem(47, glass);
                    inventory.setItem(48, glass);
                    inventory.setItem(49, despawnPet);
                    inventory.setItem(50, glass);
                    inventory.setItem(51, glass);
                    inventory.setItem(52, glass);
                    inventory.setItem(53, glass);

                    ArrayList<Pet> playerPets = PetFunctions.getPlayerPets(p);
                    if(!playerPets.isEmpty()){
                        for(Pet pet : playerPets){
                            String petName = pet.getName();
                            String petDisplayName = pet.getDisplayName();
                            String petCustomHead = pet.getCustomHead();
                            ArrayList<String> petLore = new ArrayList<>();
                            petLore.add(Utils.chat(MessagesFile.getMessagesFile().getString("gui-pet-name").replace("{pet_name}", petDisplayName)));
                            petLore.add(Utils.chat("&7"));
                            petLore.add(Utils.chat(MessagesFile.getMessagesFile().getString("gui-pet-spawn")));

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

                    p.openInventory(inventory);
                    p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("gui-pet-open")));
                }else if(args[0].equalsIgnoreCase("shop")){
                    p.openInventory(Inventories.getShopGUI());
                    p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("shop-gui-open")));
                }else{
                    for(String s : MessagesFile.getMessagesFile().getStringList("help-message")){
                        p.sendMessage(Utils.chat(s));
                    }
                }
            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("give")){
                    if(p.hasPermission("premiumpets.give")){
                        Player target = Bukkit.getPlayer(args[1]);
                        String petName = args[2];
                        if(target != null){
                            if(PremiumPets.getInstance().getConfig().getString("pets."+petName+".name") != null){
                                PetFunctions.givePetToPlayer(target, petName);
                                p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("pet-given").replace("{pet_name}", petName)).replace("{target}", target.getName()));
                            }else{
                                p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("pet-not-found")));
                            }
                        }else{
                            p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("target-offline")));
                        }
                    }else{
                        p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("no-permission")));
                    }
                }else{
                    for(String s : MessagesFile.getMessagesFile().getStringList("help-message")){
                        p.sendMessage(Utils.chat(s));
                    }
                }
            }else{
                for(String s : MessagesFile.getMessagesFile().getStringList("help-message")){
                    p.sendMessage(Utils.chat(s));
                }
            }
        }else{
            ConsoleCommandSender c = (ConsoleCommandSender) sender;
            if(args.length == 3){
                if(args[0].equalsIgnoreCase("give")){
                    Player target = Bukkit.getPlayer(args[1]);
                    String petName = args[2];
                    if(target != null){
                        if(PremiumPets.getInstance().getConfig().getString("pets."+petName+".name") != null){
                            PetFunctions.givePetToPlayer(target, petName);
                            c.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("pet-given").replace("{pet_name}", petName)).replace("{target}", target.getName()));
                        }else{
                            c.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("pet-not-found")));
                        }
                    }else{
                        c.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("target-offline")));
                    }
                }else{
                    c.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("only-player")));
                }
            }else{
                c.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("only-player")));
            }
        }

        return false;
    }

}
