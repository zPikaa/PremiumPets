package it.pika.premiumpets;

import it.pika.premiumpets.utils.Utils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class PremiumPets extends JavaPlugin {

    @Getter
    private static PremiumPets instance;

    @Override
    public void onEnable() {
        instance = this;
        Utils.enable();
    }

    @Override
    public void onDisable() {
        Utils.disable();
    }

}
