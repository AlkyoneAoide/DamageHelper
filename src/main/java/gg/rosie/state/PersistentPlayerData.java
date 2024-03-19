package gg.rosie.state;

import java.util.UUID;

public class PersistentPlayerData {
    public static final int DEFAULT_HEALTH = 20;
    public UUID uuid;
    public int maxHealthModifier;

    public PersistentPlayerData(UUID uuid) {
        this(uuid, 0);
    }

    public PersistentPlayerData(UUID uuid, int maxHealthModifier) {
        this.uuid = uuid;
        this.maxHealthModifier = maxHealthModifier;
    }
}
