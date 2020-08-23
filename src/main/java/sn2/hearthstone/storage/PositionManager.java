package sn2.hearthstone.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import sn2.hearthstone.storage.data.PositionData;
import sn2.hearthstone.storage.sql.SQLAccessor;

public class PositionManager{
	private Map<Integer, Map<String, PositionData>> posTable;
	private SQLAccessor sql;
	
	public PositionManager(SQLAccessor sql) {
		this.posTable = new HashMap<>();
		this.sql = sql;
		this.start();
	}
	
	public void start() {
		this.sql.executeCommand("CREATE TABLE IF NOT EXISTS POSITION " + 
				"(UUID 		VARCHAR(50) 	NOT NULL, " +
				"WORLDKEY 	VARCHAR(50)  	NOT NULL, " +
				"POSLON 	BIGINT 			NOT NULL, " +
				"YAW		REAL 			NOT NULL, " +
				"PITCH 		REAL			NOT NULL, " + 
				"STONETYPE INT				NOT NULL,"
				+ "PRIMARY KEY(UUID, STONETYPE));");
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
	}

	public void put(String uuid, int stoneType, PositionData posData) {
		posTable.putIfAbsent(stoneType, new HashMap<>());
		posTable.get(stoneType).put(uuid, posData);
	}

	public PositionData get(String uuid, int stoneType) {
		return posTable.getOrDefault(stoneType, new HashMap<>()).get(uuid);
	}

	public void loadFromSQL() throws SQLException {
		ResultSet rs = this.sql.getResult("SELECT * FROM POSITION;");
		while (rs.next()) {
			String uuid = rs.getString("UUID");
			int stoneType = rs.getInt("STONETYPE");
			this.put(uuid, stoneType, new PositionData(
					RegistryKey.of(Registry.DIMENSION, new Identifier(rs.getString("WORLDKEY"))), 
					BlockPos.fromLong(rs.getLong("POSLON")), 
					rs.getFloat("YAW"), 
					rs.getFloat("PITCH")));
		}
	}

	public void saveToSQL() throws SQLException {
		this.posTable.forEach((stoneType, map) -> {
			map.forEach((uuid, posData) -> {
				try {
					PreparedStatement sql1 = this.sql.executeCommandPreparedStatement("DELETE FROM POSITION WHERE UUID = ? AND STONETYPE = ?;");
					sql1.setString(1, uuid);
					sql1.setInt(2, stoneType);
					sql1.executeUpdate();
					PreparedStatement sql2 = this.sql.executeCommandPreparedStatement("INSERT INTO POSITION (UUID, WORLDKEY, POSLON,"
							+ "YAW, PITCH, STONETYPE) VALUES(?,?,?,?,?,?);" );
					sql2.setString(1, uuid);
					sql2.setString(2, posData.getWorldKey());
					sql2.setLong(3, posData.getPoslong());
					sql2.setFloat(4, posData.getYaw());
					sql2.setFloat(5, posData.getPitch());
					sql2.setInt(6, stoneType);
					sql2.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		});
	}
}
