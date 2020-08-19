package sn2.hearthstone;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import sn2.hearthstone.item.ItemRegistry;
import sn2.hearthstone.storage.CooldownManager;
import sn2.hearthstone.storage.PositionManager;
import sn2.hearthstone.storage.sql.H2;

public class HearthStone implements ModInitializer {

	public static final String MODID = "hearthstone";
	public static final String VERSION = "0.1.0";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static PositionManager posManager;
	public static CooldownManager cooldownManager;
	public static MinecraftServer server;
	public static H2 sql;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			HearthStone.server = server;
			File dir = new File(server.getSavePath(WorldSavePath.ROOT).toString() + "\\hearthstone");
			if (!dir.exists())
				dir.mkdir();
			sql = new H2(dir.toString(), "hearthstone");
			sql.connect();
			posManager = new PositionManager(sql);
			cooldownManager = new CooldownManager(sql);
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			cooldownManager.tick();
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> { 
			posManager.stop();
			cooldownManager.stop();
			sql.disconnect();
		});
		ItemRegistry.init();
	}
	
}
