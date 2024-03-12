package gg.rosie.injected_interfaces;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import gg.rosie.helper_classes.CustomItemCrits;

public interface ILivingEntityMixin {
    void setCritical(boolean flag);

    boolean isCritical();
}
