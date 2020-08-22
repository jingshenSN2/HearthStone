package sn2.hearthstone.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import sn2.hearthstone.HearthStone;

public class ItemRegistry {

	private static int minTick = 20*60;
	public static final Item SIMPLE_OVERWORLD_HEARTHSTONE = new ItemHearthStoneWorld(30 * minTick, 0, World.OVERWORLD, 500);
	public static final Item SIMPLE_NETHER_HEARTHSTONE = new ItemHearthStoneWorld(30 * minTick, 1, World.NETHER, 500);
	public static final Item SIMPLE_END_HEARTHSTONE = new ItemHearthStoneWorld(30 * minTick, 2, World.END, 500);
	public static final Item OVERWORLD_HEARTHSTONE = new ItemHearthStoneWorld(20 * minTick, 3, World.OVERWORLD, -1);
	public static final Item NETHER_HEARTHSTONE = new ItemHearthStoneWorld(20 * minTick, 4, World.NETHER, -1);
	public static final Item END_HEARTHSTONE = new ItemHearthStoneWorld(20 * minTick, 5, World.END, -1);
	public static final Item MINER_HEARTHSTONE = new ItemHearthStoneMiner(20 * minTick, 6);

	public static void init() {
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "simple_overworld_hearthstone"), SIMPLE_OVERWORLD_HEARTHSTONE);
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "simple_nether_hearthstone"), SIMPLE_NETHER_HEARTHSTONE);
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "simple_end_hearthstone"), SIMPLE_END_HEARTHSTONE);
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "overworld_hearthstone"), OVERWORLD_HEARTHSTONE);
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "nether_hearthstone"), NETHER_HEARTHSTONE);
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "end_hearthstone"), END_HEARTHSTONE);
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "miner_hearthstone"), MINER_HEARTHSTONE);
	}
}
