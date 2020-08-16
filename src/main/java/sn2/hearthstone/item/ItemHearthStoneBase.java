package sn2.hearthstone.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sn2.hearthstone.HearthStone;
import sn2.hearthstone.data.PositionData;

public class ItemHearthStoneBase extends Item {

	public ItemHearthStoneBase(ToolMaterial material) {
		super(new Settings().maxCount(1).group(ItemGroup.TOOLS).maxDamage(material.getDurability()));
	}

	public void teleport(PlayerEntity player, ServerWorld world, BlockPos pos) {
		if (!world.getRegistryKey().equals(player.world.getRegistryKey()))
			player.moveToWorld(world);
		player.teleport(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 200;
	}

	@Override
	 public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
			if (world.isClient)
				return stack;
			ServerPlayerEntity player = (ServerPlayerEntity) user;
			if (player.isSneaking()) {
				PositionData posData = new PositionData(player.getUuidAsString(), player.getServerWorld().getRegistryKey(),
						player.getBlockPos(), player.yaw, player.pitch);
				HearthStone.playerDateManager.put(player, posData);
				return stack;
			} else {
				HearthStone.playerDateManager.get(player).teleport(player);
				return stack;
			}
	 }
	
	@Override
	 public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
	         ItemStack itemStack = user.getStackInHand(hand);
	            user.setCurrentHand(hand);
	            return TypedActionResult.consume(itemStack);
	       
	   }

}
