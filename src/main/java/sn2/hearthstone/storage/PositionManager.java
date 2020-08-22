package sn2.hearthstone.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import sn2.hearthstone.storage.data.PositionData;
import sn2.hearthstone.storage.sql.SQLAccessor;

public class PositionManager implements ManagerBase<PositionData>{
	public Table<String, Integer, PositionData> posTable = HashBasedTable.create();
	public SQLAccessor sql;
	
	public PositionManager(SQLAccessor sql) {
		this.sql = sql;
		this.start();
	}
	
	@Override
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

	@Override
	public void stop() {
		try {
			this.saveToSQL();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void put(String uuid, int stoneType, PositionData posData) {
		this.posTable.put(uuid, stoneType, posData);
	}

	@Override
	public PositionData get(String uuid, int stoneType) {
		if (posTable.containsRow(uuid) && posTable.containsColumn(stoneType)) {
			return this.posTable.get(uuid, stoneType);
		}
		return null;
	}

	@Override
	public void loadFromSQL() throws SQLException {
		ResultSet rs = this.sql.getResult("SELECT * FROM POSITION;");
		while (rs.next()) {
			String uuid = rs.getString("UUID");
			int stoneType = rs.getInt("STONETYPE");
			posTable.put(uuid, stoneType, new PositionData(
					RegistryKey.of(Registry.DIMENSION, new Identifier(rs.getString("WORLDKEY"))), 
					BlockPos.fromLong(rs.getLong("POSLON")), 
					rs.getFloat("YAW"), 
					rs.getFloat("PITCH")));
		}
	}

	@Override
	public void saveToSQL() throws SQLException {
		for (Cell<String, Integer, PositionData> cell : posTable.cellSet()) {
			String uuid = cell.getRowKey();
			int stoneType = cell.getColumnKey();
			PositionData posData = cell.getValue();
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
		}
	}

	public void teleport(ServerPlayerEntity player, PositionData posData) {
		BlockPos pos = posData.getPos();
		player.teleport(posData.getWorld(player), pos.getX(), pos.getY(), pos.getZ(), posData.getYaw(), posData.getPitch());
	}

}
