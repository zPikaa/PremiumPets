package it.pika.premiumpets.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

@Getter @Setter
@AllArgsConstructor
public class SpawnedPet {

    private String petName;
    private String customHead;
    private Player petOwner;
    private Location petLocation;
    private ArmorStand petArmorStand;

}
