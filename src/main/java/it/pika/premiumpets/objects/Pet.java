package it.pika.premiumpets.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter @Setter
@AllArgsConstructor
public class Pet {

    private String name;
    private String displayName;
    private String customHead;
    private Player owner;

}
