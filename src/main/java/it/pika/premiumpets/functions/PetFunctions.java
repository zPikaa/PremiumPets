package it.pika.premiumpets.functions;

import it.pika.premiumpets.PremiumPets;
import it.pika.premiumpets.files.MessagesFile;
import it.pika.premiumpets.objects.Pet;
import it.pika.premiumpets.objects.SpawnedPet;
import it.pika.premiumpets.utils.Utils;
import it.pika.premiumpets.utils.powerlib.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PetFunctions {

    public static void spawnPet(String petName, Player p, Location loc){
        if(!Utils.spawnedPets.containsKey(p)){
            ItemStack petHead = new ItemBuilder()
                    .setName(Utils.chat("&7"))
                    .setLore(Utils.chat("&7"))
                    .customHeadBuild(PremiumPets.getInstance().getConfig().getString("pets."+petName+".customHead"));

            ArmorStand pet = loc.getWorld().spawn(loc, ArmorStand.class);
            double x,y,z=0;
            x = Math.toRadians(p.getLocation().getPitch());
            y = Math.toRadians(p.getLocation().getYaw());
            EulerAngle a = new EulerAngle(x,y,z);

            pet.setGravity(false);
            pet.setCollidable(false);
            pet.setVisible(false);
            pet.setCustomName("PET-"+p.getName());
            pet.setCustomNameVisible(false);
            pet.getEquipment().setHelmet(petHead);
            pet.getLocation().setYaw(0);
            pet.setHeadPose(a);

            SpawnedPet spawnedPet = new SpawnedPet(petName, PremiumPets.getInstance().getConfig().getString("pets." + petName + ".customHead"), p, loc, pet);
            Utils.spawnedPets.put(p, spawnedPet);
            p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("pet-spawned")));
        }else{
            p.sendMessage(Utils.chat(MessagesFile.getMessagesFile().getString("pet-already-spawned")));
        }
    }

    public static void givePetToPlayer(Player p, String petName) {
        try {
            String QUERY = "INSERT INTO pets(ownerUUID, ownerName, petName) VALUES(?,?,?)";
            PreparedStatement stmt = Utils.getSqlHandler().connection.prepareStatement(QUERY);
            stmt.setString(1, p.getUniqueId().toString());
            stmt.setString(2, p.getName());
            stmt.setString(3, petName);
            stmt.executeUpdate();
            Bukkit.getLogger().info("[PremiumPets] Pet " + petName + " has been gived to " + p.getName());
        } catch (SQLException e) {
            Bukkit.getLogger().warning("[PremiumPets] " + e.getMessage());
        }
    }

    @SneakyThrows
    public static ArrayList<Pet> getPlayerPets(Player p){
        String QUERY = "SELECT * FROM pets";
        PreparedStatement stmt = Utils.getSqlHandler().connection.prepareStatement(QUERY);
        ResultSet rs = stmt.executeQuery();
        ArrayList<Pet> playerPets = new ArrayList<>();
        for(int i = 1; i <= Utils.getSqlHandler().getRows("pets"); i++){
            if(rs.next()){
                if(rs.getString("ownerUUID").equalsIgnoreCase(p.getUniqueId().toString()) && rs.getString("ownerName").equalsIgnoreCase(p.getName())){
                    String petName = rs.getString("petName");
                    String petDisplayName = PremiumPets.getInstance().getConfig().getString("pets."+petName+".displayName");
                    String petCustomHead = PremiumPets.getInstance().getConfig().getString("pets."+petName+".customHead");
                    Pet pet = new Pet(petName, petDisplayName, petCustomHead, p);
                    playerPets.add(pet);
                }
            }
        }
        return playerPets;
    }

    public static void killAllPets(){
        for(World w : Bukkit.getWorlds()){
            for(ArmorStand pets : w.getEntitiesByClass(ArmorStand.class)){
                if(pets.getCustomName() != null){
                    if(pets.getCustomName().startsWith("PET-")){
                        pets.remove();
                    }
                }
            }
        }
    }

}
