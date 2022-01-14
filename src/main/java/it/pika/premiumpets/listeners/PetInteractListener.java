package it.pika.premiumpets.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class PetInteractListener implements Listener {

    @EventHandler
    public void onPetRightClick(PlayerArmorStandManipulateEvent e){
        ArmorStand pet = e.getRightClicked();
        if(!pet.getCustomName().startsWith("PET"))
            return;
        e.setCancelled(true);
    }

}
