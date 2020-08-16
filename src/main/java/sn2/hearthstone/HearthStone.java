package sn2.hearthstone;


import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.WorldSavePath;
import sn2.hearthstone.data.PlayerDataManager;
import sn2.hearthstone.data.sql.H2;
import sn2.hearthstone.data.sql.SQLite;
import sn2.hearthstone.item.ItemRegistry;

public class HearthStone implements ModInitializer {

	public static final String MODID = "heartstone";
	public static final String VERSION = "0.1.0";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static PlayerDataManager playerDateManager;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			File dir = new File(server.getSavePath(WorldSavePath.ROOT).toString() + "\\heartstoneH2");
			if (!dir.exists())
				dir.mkdir();
			H2 sql = new H2(dir.toString(), "heartstone");
			playerDateManager = new PlayerDataManager(server, sql);
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> { playerDateManager.stop(); });
		ItemRegistry.init();
	}
	
}
