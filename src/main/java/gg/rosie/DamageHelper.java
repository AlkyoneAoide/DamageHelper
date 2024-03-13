package gg.rosie;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

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
		CustomItemCrits.add("minecraft:air", (source, amount) -> System.out.println("inside the thing but empty hand"));

		CustomItemCrits.add(new Identifier("minecraft", "wooden_sword").toString(), (source, amount) -> {
			System.out.println("inside the thing");
			System.out.println("but wooden sword");
		});
	}
}