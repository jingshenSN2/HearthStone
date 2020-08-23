package sn2.hearthstone.item;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sn2.hearthstone.HearthStone;
import sn2.hearthstone.storage.data.PositionData;

public class ItemHearthStoneBase extends Item {
	
	private int maxCooldown;
	private int maxDist;
	protected int cooldown;
	protected int stoneType;
	
	
	public ItemHearthStoneBase(int maxCooldown, int stoneType, int maxDist) {
		super(new Settings().maxCount(1).group(ItemGroup.TOOLS));
		this.maxCooldown = maxCooldown;
		this.maxDist = maxDist;
		this.cooldown = 0;
		this.stoneType = stoneType;
	}
	
	public void teleport(PlayerEntity player, ServerWorld world, BlockPos pos) {
		if (!canTeleport(world, player, pos))
			return;
		if (!world.getRegistryKey().equals(player.world.getRegistryKey()))
			player.moveToWorld(world);
		player.teleport(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public boolean canTeleport(ServerWorld world, PlayerEntity player, BlockPos pos) {
		return pos.isWithinDistance(player.getPos(), this.maxDist) || this.maxDist == -1;
	}
	
	public boolean canRecord(ServerWorld world) {
		return true;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world.isClient) 
			return;
		this.cooldown = HearthStone.cooldownManager.get(entity.getUuidAsString(), this.stoneType);
	}

	@Override
	 public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
	      ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient) {
			ServerPlayerEntity player = (ServerPlayerEntity) user;
			PositionData posData = null;
			if (player.isSneaking() && canRecord((ServerWorld)world)) {
				posData = new PositionData(player.getServerWorld().getRegistryKey(),
					player.getBlockPos(), player.yaw, player.pitch);
				HearthStone.posManager.put(player.getUuidAsString(), stoneType, posData);
			}
			else {
				posData = HearthStone.posManager.get(player.getUuidAsString(), stoneType);
				if (posData != null && canTeleport(posData.getWorld(player), player, posData.getPos())) {
					this.teleport(player, posData.getWorld(player), posData.getPos());
					HearthStone.cooldownManager.put(player.getUuidAsString(), stoneType, maxCooldown);
				}
			}
		}
		return TypedActionResult.method_29237(itemStack, world.isClient());
	   }
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if (this.cooldown > 0) {
			int minutes = (int) (cooldown / 1200);
			int seconds = (int) (cooldown / 20 - (minutes * 60));
			tooltip.add(new TranslatableText("Cooldown: " + minutes + " minutes" + seconds + "seconds"));
		}
		else 
			tooltip.add(new TranslatableText("Ready!"));
	}
}
