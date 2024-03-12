package gg.rosie;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import gg.rosie.injected_interfaces.ILivingEntityMixin;
import gg.rosie.network.DamageHelperPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.server.MinecraftServer;

@Environment(EnvType.CLIENT)
public class DamageHelperClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientPlayNetworking.registerGlobalReceiver(DamageHelperPacket.PACKET_NAME, (client, handler, buf, responseSender) -> {
			DamageHelperPacket packet = new DamageHelperPacket(buf);
			if (client.world != null && client.world.getEntityById(packet.getEntityId()) instanceof ILivingEntityMixin livingEntityMixin) {
				livingEntityMixin.setCritical(packet.getCritical());
			}
		});
	}
}