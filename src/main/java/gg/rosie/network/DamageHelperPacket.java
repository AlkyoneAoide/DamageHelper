package gg.rosie.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class DamageHelperPacket {
    public static final Identifier PACKET_NAME = new Identifier("damagehelper", "sync");
    private final int entityId;
    private final boolean isCritical;

    public DamageHelperPacket(int entityId, boolean flag) {
        this.entityId = entityId;
        this.isCritical = flag;
    }

    public DamageHelperPacket(PacketByteBuf byteBuf) {
        this.entityId = byteBuf.readVarInt();
        this.isCritical = byteBuf.readBoolean();
    }

    public PacketByteBuf write(PacketByteBuf byteBuf) {
        byteBuf.writeVarInt(this.entityId);
        byteBuf.writeBoolean(this.isCritical);
        return byteBuf;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean getCritical() {
        return this.isCritical;
    }
}
