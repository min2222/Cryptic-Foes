package com.min01.crypticfoes.blockentity;

import java.util.List;

import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.block.ScreamerBlock;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.misc.SmoothAnimationState;
import com.min01.crypticfoes.particle.CrypticParticles;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ScreamerBlockEntity extends BlockEntity
{
	public int tickCount;
	public final SmoothAnimationState screamAnimationState = new SmoothAnimationState();
	
	public ScreamerBlockEntity(BlockPos p_155229_, BlockState p_155230_) 
	{
		super(CrypticBlocks.SCREAMER_BLOCK_ENTITY.get(), p_155229_, p_155230_);
	}
	
	public static void update(Level level, BlockPos pos, BlockState state, ScreamerBlockEntity block)
	{
		boolean activate = state.getValue(ScreamerBlock.ACTIVATE);
		
		if(level.isClientSide)
		{
			block.screamAnimationState.updateWhen(activate, block.tickCount);
		}
		
		if(activate)
		{
			block.tickCount++;
			if(block.tickCount == 30)
			{
	    		level.addParticle(CrypticParticles.HOWLER_SHOCKWAVE.get(), pos.getX() + 0.5F, pos.getY() + 0.01F, pos.getZ() + 0.5F, 80.0F, 0.0F, 0.0F);
			}
			if(block.tickCount == 34)
			{
				boolean charged = state.getValue(ScreamerBlock.CHARGED);
		    	List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(-6.0F, 0.0F, -6.0F, 6.0F, 6.0F, 6.0F).move(pos), EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(t -> !(t instanceof EntityHowler)));
		    	list.forEach(t -> 
		    	{
		    		Vec3 motion = CrypticUtil.fromToVector(Vec3.atBottomCenterOf(pos), t.position().add(0, 1, 0), 1.0F);
		    		t.setDeltaMovement(motion.x, motion.y, motion.z);
		    		if(t instanceof ServerPlayer player)
		    		{
		    			player.connection.send(new ClientboundSetEntityMotionPacket(t));
		    		}
					if(charged)
					{
						t.addEffect(new MobEffectInstance(CrypticEffects.STUNNED.get(), 100));
					}
		    	});
	    		if(charged)
	    		{
					level.setBlockAndUpdate(pos, state.setValue(ScreamerBlock.CHARGED, false));
	    		}
			}
			if(block.tickCount >= 45)
			{
				level.setBlockAndUpdate(pos, state.setValue(ScreamerBlock.ACTIVATE, !activate));
			}
		}
		else
		{
			block.tickCount = 0;
		}
	}
	
	@Override
	public void load(CompoundTag p_155245_) 
	{
		super.load(p_155245_);
		this.tickCount = p_155245_.getInt("TickCount");
	}
	
	@Override
	protected void saveAdditional(CompoundTag p_187471_)
	{
		super.saveAdditional(p_187471_);
		p_187471_.putInt("TickCount", this.tickCount);
	}
}
