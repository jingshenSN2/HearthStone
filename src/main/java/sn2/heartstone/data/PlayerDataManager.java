package sn2.heartstone.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import sn2.heartstone.data.sql.SQLAccessor;

public class PlayerDataManager{
	public MinecraftServer server;
	public Map<String, PositionData> posDatas;
	public SQLAccessor sql;
	
	public PlayerDataManager(MinecraftServer server, SQLAccessor sql) {
		this.sql = sql;
		this.start();
	}
	
	public void start() {
		this.posDatas = new HashMap<String, PositionData>();
		this.sql.connect();
		this.sql.executeCommand("CREATE TABLE IF NOT EXISTS POSITION " + 
				"(UUID 		TEXT PRIMARY KEY 	NOT NULL UNIQUE, " +
				"WORLDKEY 	STRING 				NOT NULL, " +
				"POSLON 	INTEGER 			NOT NULL, " +
				"YAW 		FLOAT 				NOT NULL, " +
				"PITCH 		FLOAT				NOT NULL);");
		try {
			this.loadFromSQL();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			this.saveToSQL();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.sql.disconnect();
	}
	
	public void put(ServerPlayerEntity player, PositionData posData) {
		this.posDatas.put(player.getUuidAsString(), posData);
	}
	
	public PositionData get(ServerPlayerEntity player) {
		return this.posDatas.get(player.getUuidAsString());
	}
	
	public void loadFromSQL() throws SQLException {
		ResultSet rs = this.sql.getResult("SELECT * FROM POSITION;");
		System.out.println("1");
		while (rs.next()) {
			String uuid = rs.getString("UUID");
			System.out.println(uuid);
			posDatas.put(uuid, new PositionData(uuid,
					RegistryKey.of(Registry.DIMENSION, new Identifier(rs.getString("WORLDKEY"))), 
					BlockPos.fromLong(rs.getLong("POSLON")), 
					rs.getFloat("YAW"), 
					rs.getFloat("PITCH")));
		}
	}
	
	public void saveToSQL() throws SQLException {
		posDatas.forEach((uuid, posData) -> {
			try {
				PreparedStatement sql1 = this.sql.executeCommandPreparedStatement("DELETE FROM POSITION WHERE UUID = ?;");
				sql1.setString(1, uuid);
				sql1.executeUpdate();
				PreparedStatement sql2 = this.sql.executeCommandPreparedStatement("INSERT INTO POSITION (UUID, WORLDKEY, POSLON,"
						+ "YAW, PITCH) VALUES(?,?,?,?,?);" );
				sql2.setString(1, uuid);
				sql2.setString(2, posData.getWorldKey());
				sql2.setLong(3, posData.poslong);
				sql2.setFloat(4, posData.yaw);
				sql2.setFloat(5, posData.pitch);
				sql2.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		});
	}
	
	
}
