package gg.rosie.state;

import gg.rosie.DamageHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class WorldState extends PersistentState {
    protected HashMap<UUID, PersistentPlayerData> playerData = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        playerData.forEach((uuid, data) -> {
            NbtCompound playerNbt = new NbtCompound();
            playerNbt.putInt("playerHealthModifier", data.maxHealthModifier);
            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put("players", playersNbt);

        return nbt;
    }

    public static WorldState createFromNbt(NbtCompound nbt) {
        WorldState state = new WorldState();

        NbtCompound players = nbt.getCompound("players");
        players.getKeys().forEach(key -> {
            UUID uuid = UUID.fromString(key);

            PersistentPlayerData data = new PersistentPlayerData(uuid, players.getCompound(key).getInt("playerHealthModifier"));

            state.playerData.put(uuid, data);
        });

        return state;
    }

    public static WorldState getServerState(MinecraftServer server) {
        // Overworld is a random choice, any dimension works
        ServerWorld world = server.getWorld(World.OVERWORLD);

        if (world == null) {
            throw new NullPointerException();
        }

        PersistentStateManager manager = world.getPersistentStateManager();

        // Create new WorldState and store it inside the manager, next calls create from nbt on disk
        WorldState state = manager.getOrCreate(WorldState::createFromNbt, WorldState::new, DamageHelper.MOD_ID);

        // Only downside to marking dirty immediately is if literally no change happens and the world is quit
        state.markDirty();

        return state;
    }

    public static PersistentPlayerData getPlayerState(LivingEntity player) {
        MinecraftServer server = player.getWorld().getServer();

        if (server == null) {
            throw new NullPointerException();
        }

        WorldState serverState = getServerState(server);

        // Get existing playerdata or create new one
        return serverState.playerData.computeIfAbsent(player.getUuid(), PersistentPlayerData::new);
    }
}
