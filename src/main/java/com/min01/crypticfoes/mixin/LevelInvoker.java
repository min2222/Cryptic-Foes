package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;

@Mixin(Level.class)
public interface LevelInvoker
{
    @Invoker("getEntities")
    LevelEntityGetter<Entity> crypticfoes$invoke_getEntities();
}
