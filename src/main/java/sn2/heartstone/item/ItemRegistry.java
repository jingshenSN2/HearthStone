package sn2.heartstone.item;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import sn2.heartstone.HeartStone;

public class ItemRegistry {

	public static final Item WOODEN_SLAB_HOOKER = new ItemHeartStoneBase(ToolMaterials.WOOD);

	public static void init() {
		Registry.register(Registry.ITEM, new Identifier(HeartStone.MODID, "wooden_slab_hooker"),
				WOODEN_SLAB_HOOKER);
	}
}
