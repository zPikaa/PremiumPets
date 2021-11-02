package it.pika.premiumpets.listeners;

import it.pika.premiumpets.PremiumPets;
import it.pika.premiumpets.files.MessagesFile;
import it.pika.premiumpets.functions.PetFunctions;
import it.pika.premiumpets.objects.SpawnedPet;
import it.pika.premiumpets.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

public class PetGUIClickListener implements Listener {

    @EventHandler
    public void onPetGUIClick(InventoryClickEvent e){
        if(e.getWhoClicked() instanceof Player){
            Player p = (Player) e.getWhoClicked();
            InventoryView view = e.getView();
            if(view.getTitle().equals(Utils.chat(MessagesFile.getMessagesFile().getString("gui-title")))){
                if(e.getCurrentItem() != null && !e.getCurrentItem().getType().isAir()){
                    if(e.getCurrentItem().getType() == Material.PLAYER_HEAD){
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()){
                            Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1.25D, p.getLocation().getZ());;
                            PetFunctions.spawnPet(e.getCurrentItem().getItemMeta().getDisplayName(), p, loc);
                            p.closeInventory();
                        }
                    }else if(e.getCurrentItem().getType() == Material.BARRIER){
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()){
                            if(e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.chat(MessagesFile.getMessagesFile().getString("gui-despawnpet-name")))){
                                if(Utils.spawnedPets.containsKey(p)){
                                    SpawnedPet spawnedPet = Utils.spawnedPets.get(p);
                                    Utils.spawnedPets.remove(p, spawnedPet);
                                    spawnedPet.getPetArmorStand().remove();
                                    p.closeInventory();
                                    p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("pet-despawned")));
                                }else{
                                    p.closeInventory();
                                    p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("no-pet-spawned")));
                                }
                            }
                        }
                    }
                }
                e.setCancelled(true);
            }else if(view.getTitle().equals(Utils.chat(MessagesFile.getMessagesFile().getString("shop-gui-title")))){
                if(e.getCurrentItem() != null && !e.getCurrentItem().getType().isAir()){
                    if(e.getCurrentItem().getType() == Material.PLAYER_HEAD){
                        if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()){
                            int price = PremiumPets.getInstance().getConfig().getInt("pets."+e.getCurrentItem().getItemMeta().getDisplayName()+".price");
                            if(Utils.getEcon().getBalance(p) >= price){
                                Utils.getEcon().withdrawPlayer(p, price);
                                PetFunctions.givePetToPlayer(p, e.getCurrentItem().getItemMeta().getDisplayName());
                                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 10, 1);
                                p.closeInventory();
                                p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("pet-bought")));
                            }else{
                                p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("no-money")));
                            }
                        }
                    }
                }
                e.setCancelled(true);
            }
        }
    }

}
