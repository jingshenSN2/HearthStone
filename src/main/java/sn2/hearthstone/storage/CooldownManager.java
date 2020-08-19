package sn2.hearthstone.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import sn2.hearthstone.storage.data.CooldownData;
import sn2.hearthstone.storage.sql.SQLAccessor;

public class CooldownManager implements ManagerBase<CooldownData>{
	public Table<String, Integer, CooldownData> cooldownTable = HashBasedTable.create();
	public SQLAccessor sql;
	
	public CooldownManager(SQLAccessor sql) {
		this.sql = sql;
		this.start();
	}
	
	public void tick() {
		for (Cell<String, Integer, CooldownData> cooldownData : cooldownTable.cellSet()) {
			cooldownData.getValue().tick();
		}
	}

	@Override
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

	@Override
	public void stop() {
		try {
			this.saveToSQL();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void put(String uuid, int stoneType, CooldownData data) {
		this.cooldownTable.put(uuid, stoneType, data);
	}

	@Override
	public CooldownData get(String uuid, int stoneType) {
		if (cooldownTable.containsRow(uuid) && cooldownTable.containsColumn(stoneType)) {
			return this.cooldownTable.get(uuid, stoneType);
		}
		return new CooldownData(stoneType);
	}

	@Override
	public void loadFromSQL() throws SQLException {
		ResultSet rs = this.sql.getResult("SELECT * FROM COOLDOWN;");
		while (rs.next()) {
			String uuid = rs.getString("UUID");
			int stoneType = rs.getInt("STONETYPE");
			cooldownTable.put(uuid, stoneType, new CooldownData(rs.getInt("COOLDOWN"), stoneType));
		}
	}

	@Override
	public void saveToSQL() throws SQLException {
		for (Cell<String, Integer, CooldownData> cell : cooldownTable.cellSet()) {
			String uuid = cell.getRowKey();
			int stoneType = cell.getColumnKey();
			CooldownData cooldownData = cell.getValue();
			try {
				PreparedStatement sql1 = this.sql.executeCommandPreparedStatement("DELETE FROM COOLDOWN WHERE UUID = ? AND STONETYPE = ?;");
				sql1.setString(1, uuid);
				sql1.setInt(2, stoneType);
				sql1.executeUpdate();
				PreparedStatement sql2 = this.sql.executeCommandPreparedStatement("INSERT INTO COOLDOWN (UUID, COOLDOWN,"
						+ " STONETYPE) VALUES(?,?,?);" );
				sql2.setString(1, uuid);
				sql2.setInt(2, cooldownData.getCoolDown());
				sql2.setInt(3, stoneType);
				sql2.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
