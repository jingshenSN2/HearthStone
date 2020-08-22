package sn2.hearthstone.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import sn2.hearthstone.HearthStone;
import sn2.hearthstone.storage.data.PositionData;

public class ItemHearthStoneMiner extends ItemHearthStoneBase{

	public ItemHearthStoneMiner(int maxCooldown, int stoneType) {
		super(maxCooldown, stoneType);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world.isClient) 
			return;
		ServerPlayerEntity player = (ServerPlayerEntity) entity;
		this.cooldown = HearthStone.cooldownManager.get(entity.getUuidAsString(), this.stoneType).getCoolDown();
		PositionData posData = new PositionData(player.getServerWorld().getRegistryKey(),
			player.getBlockPos(), player.yaw, player.pitch);
		HearthStone.posManager.put(player.getUuidAsString(), stoneType, posData);
	}
}
