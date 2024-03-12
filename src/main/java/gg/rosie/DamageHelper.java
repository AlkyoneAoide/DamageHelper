package gg.rosie;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import gg.rosie.helper_classes.CustomItemCrits;
import gg.rosie.injected_interfaces.ILivingEntityMixin;
import gg.rosie.mixin.LivingEntityMixin;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageHelper implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    // public static final Logger LOGGER = LoggerFactory.getLogger("damagehelper");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// LOGGER.info("Hello Fabric world!");
		CustomItemCrits.add(new Identifier("miencraft", "air"), (source, amount) -> {
			System.out.println("inside the thing but empty hand");
		});

		CustomItemCrits.add(new Identifier("miencraft", "wooden_sword"), (source, amount) -> {
			System.out.println("inside the thing but wooden sword");
		});
	}
}