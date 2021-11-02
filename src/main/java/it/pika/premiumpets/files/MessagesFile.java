package it.pika.premiumpets.files;

import it.pika.premiumpets.PremiumPets;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesFile {

    private static File file;
    @Getter private static FileConfiguration messagesFile;

    public static void setup(){
        file = new File(PremiumPets.getInstance().getDataFolder(), "messages.yml");

        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(IOException e){}
        }
        messagesFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void save(){
        try{
            messagesFile.save(file);
        }catch(IOException e){
            Bukkit.getLogger().severe("[PremiumPets] Could not save messages file!");
        }
    }

}
