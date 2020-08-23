package sn2.hearthstone.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import sn2.hearthstone.storage.sql.SQLAccessor;

public class CooldownManager{
	private Map<Integer, Map<String, Integer>> cooldownTable;
	public SQLAccessor sql;
	
	public CooldownManager(SQLAccessor sql) {
		this.cooldownTable = new HashMap<>();
		this.sql = sql;
		this.start();
	}
	
	public void tick() {
		cooldownTable.forEach((stoneType, map) -> {
			map.forEach((uuid, cooldown) -> {
				if (cooldown > 0)
					map.replace(uuid, cooldown - 1);
			});
		});
	}

	public void start() {
		this.sql.executeCommand("CREATE TABLE IF NOT EXISTS COOLDOWN " + 
				"(UUID 		VARCHAR(50) 	NOT NULL, " +
				"COOLDOWN 	INT  			NOT NULL, " +
				"STONETYPE	INT				NOT NULL,"
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

	public void put(String uuid, int stoneType, int data) {
		cooldownTable.putIfAbsent(stoneType, new HashMap<>());
		cooldownTable.get(stoneType).put(uuid, data);
	}

	public int get(String uuid, int stoneType) {
		return cooldownTable.getOrDefault(stoneType, new HashMap<>()).getOrDefault(uuid, 0);
	}

	public void loadFromSQL() throws SQLException {
		ResultSet rs = this.sql.getResult("SELECT * FROM COOLDOWN;");
		while (rs.next()) {
			String uuid = rs.getString("UUID");
			int stoneType = rs.getInt("STONETYPE");
			this.put(uuid, stoneType, rs.getInt("COOLDOWN"));
		}
	}

	public void saveToSQL() throws SQLException {
		cooldownTable.forEach((stoneType, map) -> {
			map.forEach((uuid, cooldown) -> {
				try {
					PreparedStatement sql1 = this.sql.executeCommandPreparedStatement("DELETE FROM COOLDOWN WHERE UUID = ? AND STONETYPE = ?;");
					sql1.setString(1, uuid);
					sql1.setInt(2, stoneType);
					sql1.executeUpdate();
					PreparedStatement sql2 = this.sql.executeCommandPreparedStatement("INSERT INTO COOLDOWN (UUID, COOLDOWN,"
							+ " STONETYPE) VALUES(?,?,?);" );
					sql2.setString(1, uuid);
					sql2.setInt(2, cooldown);
					sql2.setInt(3, stoneType);
					sql2.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		});
	}
}
