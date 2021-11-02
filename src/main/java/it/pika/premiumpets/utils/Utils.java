package it.pika.premiumpets.utils;

import it.pika.premiumpets.PremiumPets;
import it.pika.premiumpets.commands.PetCommand;
import it.pika.premiumpets.files.MessagesFile;
import it.pika.premiumpets.functions.PetFunctions;
import it.pika.premiumpets.listeners.PetFollowPlayerListener;
import it.pika.premiumpets.listeners.PetGUIClickListener;
import it.pika.premiumpets.listeners.PetInteractListener;
import it.pika.premiumpets.objects.SpawnedPet;
import it.pika.premiumpets.sql.SQLHandler;
import it.pika.premiumpets.utils.powerlib.ReflectionAPI;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    @Getter private static SQLHandler sqlHandler;
    @Getter private static Economy econ = null;
    public static HashMap<Player, SpawnedPet> spawnedPets = new HashMap<>();

    public static void enable(){
        if(checkVersion()){
            if(setupEconomy()){
                long time = System.currentTimeMillis();

                setupConfig();
                sqlHandler = new SQLHandler();
                registerListeners();
                registerCommands();

                long after = System.currentTimeMillis() - time;
                Bukkit.getLogger().info("[PremiumPets] Plugin enabled! [" + after + "ms]");
                Bukkit.getLogger().info("[PremiumPets] Made with love and pizza by Pika.");
                Bukkit.getLogger().info("[PremiumPets] Server version: " + ReflectionAPI.getVersion());
            }else{
                Bukkit.getLogger().severe("[PremiumPets] Vault not found! Disabling the plugin...");
                Bukkit.getPluginManager().disablePlugin(PremiumPets.getInstance());
            }
        }else{
            Bukkit.getLogger().severe("[PremiumPets] Unsupported version, disabling the plugin...");
            Bukkit.getPluginManager().disablePlugin(PremiumPets.getInstance());
        }
    }

    public static void disable(){
        PetFunctions.killAllPets();
        Bukkit.getLogger().info("[PremiumPets] Plugin disabled!");
    }

    private static boolean checkVersion(){
        int version = ReflectionAPI.getNumericalVersion();
        if(version >= 14)
            return true;
        return false;
    }

    private static void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PetGUIClickListener(), PremiumPets.getInstance());
        pm.registerEvents(new PetInteractListener(), PremiumPets.getInstance());
        pm.registerEvents(new PetFollowPlayerListener(), PremiumPets.getInstance());
    }

    private static void registerCommands(){
        PremiumPets.getInstance().getCommand("premiumpets").setExecutor(new PetCommand());
    }

    @SneakyThrows
    private static void setupConfig(){
        PremiumPets.getInstance().getConfig().options().copyDefaults();
        PremiumPets.getInstance().saveDefaultConfig();

        ArrayList<String> helpMessage = new ArrayList<>();
        helpMessage.add("&7");
        helpMessage.add("&6Premium&lPets &8| &7Commands");
        helpMessage.add("&8» &7/pet gui");
        helpMessage.add("&8» &7/pet shop");
        helpMessage.add("&8» &7/pet give <player> <pet>");
        helpMessage.add("&7");

        MessagesFile.setup();
        MessagesFile.getMessagesFile().addDefault("gui-title", "&2| &aPet GUI");
        MessagesFile.getMessagesFile().addDefault("gui-pet-name", "&aPet name: {pet_name}");
        MessagesFile.getMessagesFile().addDefault("gui-pet-spawn", "&aClick to spawn!");
        MessagesFile.getMessagesFile().addDefault("gui-pet-open", "&2| &aYou opened the Pet GUI.");
        MessagesFile.getMessagesFile().addDefault("gui-despawnpet-name", "&cDespawn pet");
        MessagesFile.getMessagesFile().addDefault("pet-spawned", "&2| &aPet spawned!");
        MessagesFile.getMessagesFile().addDefault("pet-despawned", "&4| &cPet despawned!");
        MessagesFile.getMessagesFile().addDefault("no-pet-spawned", "&4| &cNo pet spawned!");
        MessagesFile.getMessagesFile().addDefault("pet-already-spawned", "&4| &cPet already spawned!");
        MessagesFile.getMessagesFile().addDefault("shop-gui-title", "&2| &aPet Shop");
        MessagesFile.getMessagesFile().addDefault("shop-pet-name", "&aPet name: {pet_name}");
        MessagesFile.getMessagesFile().addDefault("shop-pet-price", "&aPet price: {pet_price}");
        MessagesFile.getMessagesFile().addDefault("shop-pet-buy", "&aClick to buy!");
        MessagesFile.getMessagesFile().addDefault("shop-gui-open", "&2| &aYou opened the Pet Shop.");
        MessagesFile.getMessagesFile().addDefault("pet-bought", "&2| &aPet bought successfully!");
        MessagesFile.getMessagesFile().addDefault("no-money", "&4| &cYou don’t have enough money!");
        MessagesFile.getMessagesFile().addDefault("no-permission", "&4| &cNo permission!");
        MessagesFile.getMessagesFile().addDefault("pet-given", "&2| &aPet &7{pet_name} &ahas been given to &7{target}");
        MessagesFile.getMessagesFile().addDefault("pet-not-found", "&4| &cPet not found!");
        MessagesFile.getMessagesFile().addDefault("target-offline", "&4| &cTarget is offline!");
        MessagesFile.getMessagesFile().addDefault("only-player", "&4| &cYou need to be a player!");
        MessagesFile.getMessagesFile().addDefault("help-message", helpMessage);
        MessagesFile.getMessagesFile().options().copyDefaults(true);
        MessagesFile.save();
    }

    private static boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
