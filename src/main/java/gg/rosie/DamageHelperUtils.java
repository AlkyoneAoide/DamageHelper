package gg.rosie;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class DamageHelperUtils {
    // How to get Identifier from Item in a non-deprecated way
    public static Identifier IdentifierFromItem(Item item) {
        RegistryEntry<Item> entry = Registries.ITEM.getEntry(item);

        Optional<RegistryKey<Item>> optionalKey = entry.getKey();

        if (optionalKey.isEmpty()) {
            return null;
        }

        RegistryKey<Item> key = optionalKey.get();

        return key.getValue();
    }

    public static Identifier IdentifierFromEntity(Entity entity) {
        RegistryEntry<EntityType<?>> entry = Registries.ENTITY_TYPE.getEntry(entity.getType());

        Optional<RegistryKey<EntityType<?>>> optionalKey = entry.getKey();

        if (optionalKey.isEmpty()) {
            return null;
        }

        RegistryKey<EntityType<?>> key = optionalKey.get();

        return key.getValue();
    }

    public interface QuadConsumer<A, B, C, D> {
        void accept(A a, B b, C c, D d);
    }
}
