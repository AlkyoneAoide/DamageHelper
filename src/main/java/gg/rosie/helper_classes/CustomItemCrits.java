package gg.rosie.helper_classes;

import net.minecraft.entity.damage.DamageSource;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class CustomItemCrits {
    // A map of identifiers to functions, to run when a crit is hit
    // with the item the identifier corresponds to.
    // The function is passed the DamageSource and (float) amount of damage that
    // applyDamage normally gets.
    // Note that this is only additive, and does not touch existing crit handling code.
    // Also, it is a consumer and therefore has a guaranteed void return type.
    protected static HashMap<String, BiConsumer<DamageSource, Float>> CUSTOM_CRIT_LIST= new HashMap<>();

    public static void add(String ident, BiConsumer<DamageSource, Float> func) {
        CUSTOM_CRIT_LIST.put(ident, func);
    }

    public static BiConsumer<DamageSource, Float> get(String ident) {
        return CUSTOM_CRIT_LIST.get(ident);
    }
}
