package gg.rosie;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ItemUtils {
    // How to get Identifier from Item in a non-deprecated way
    public static Identifier identifierFromItem(Item item) {
        RegistryEntry<Item> entry = Registries.ITEM.getEntry(item);

        Optional<RegistryKey<Item>> optionalKey = entry.getKey();

        if (optionalKey.isEmpty()) {
            return null;
        }

        RegistryKey<Item> key = optionalKey.get();

        return key.getValue();
    }
}
