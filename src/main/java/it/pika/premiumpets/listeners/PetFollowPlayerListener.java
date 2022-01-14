package it.pika.premiumpets.listeners;

import it.pika.premiumpets.PremiumPets;
import it.pika.premiumpets.objects.SpawnedPet;
import it.pika.premiumpets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.EulerAngle;

public class PetFollowPlayerListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if(!Utils.spawnedPets.containsKey(p))
            return;

        // get spawnedpet and armorstand
        SpawnedPet spawnedPet = Utils.spawnedPets.get(p);
        ArmorStand pet = spawnedPet.getPetArmorStand();

        // set armorstand head pose asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(PremiumPets.getInstance(), () -> {
            double x=0,y=0,z=0;
            x = Math.toRadians(p.getLocation().getPitch());
            y = Math.toRadians(p.getLocation().getYaw());
            EulerAngle a = new EulerAngle(x,y,z);
            pet.setHeadPose(a);
        });

        // follow player
        Location loc = new Location(p.getWorld(), e.getTo().getX(), e.getTo().getY() + 1.25D, e.getTo().getZ());
        pet.teleport(loc);
    }

}
