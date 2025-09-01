package com.min01.crypticfoes.world;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class CrypticSavedData extends SavedData
{
	public static final String NAME = "cryptic_foes_data";
	
	private final List<BlockPos> blocks = new ArrayList<BlockPos>();
	
    public static CrypticSavedData get(Level level)
    {
        if(level instanceof ServerLevel serverLevel) 
        {
            DimensionDataStorage storage = serverLevel.getDataStorage();
            CrypticSavedData data = storage.computeIfAbsent(t -> load(serverLevel, t), CrypticSavedData::new, NAME);
            return data;
        }
        return null;
    }

    public static CrypticSavedData load(ServerLevel level, CompoundTag nbt) 
    {
    	CrypticSavedData data = new CrypticSavedData();
    	ListTag blocks = nbt.getList("Blocks", 10);
		for(int i = 0; i < blocks.size(); ++i)
		{
			CompoundTag tag = blocks.getCompound(i);
			BlockPos pos = NbtUtils.readBlockPos(tag);
			BlockState state = level.getBlockState(pos);
			if(!state.isAir())
			{
				data.setBlockSilence(pos);
			}
		}
    	return data;
    }
	
	@Override
	public CompoundTag save(CompoundTag nbt)
	{
		ListTag blocks = new ListTag();
		this.blocks.forEach(t -> 
		{
			blocks.add(NbtUtils.writeBlockPos(t));
		});
		nbt.put("Blocks", blocks);
		return nbt;
	}
	
	public void setBlockSilence(BlockPos pos)
	{
		this.blocks.add(pos);
		this.setDirty();
	}
	
	public boolean isBlockSilenced(Level level, BlockPos pos)
	{
		this.blocks.removeIf(t -> level.getBlockState(pos).isAir());
		return this.blocks.contains(pos);
	}
}
