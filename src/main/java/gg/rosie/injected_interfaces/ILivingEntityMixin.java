package gg.rosie.injected_interfaces;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Function;

public interface ILivingEntityMixin {
    void setCritical(boolean flag);

    void setCriticalAction(Identifier item, Function<Void, Void> result);

    boolean isCritical();
}
