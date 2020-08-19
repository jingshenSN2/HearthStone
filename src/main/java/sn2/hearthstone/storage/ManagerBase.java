package sn2.hearthstone.storage;

import java.sql.SQLException;

public interface ManagerBase<T> {
	public void start();
	public void stop();
	public void put(String uuid, int stoneType, T data);
	public T get(String uuid, int stoneType);
	public void loadFromSQL() throws SQLException;
	public void saveToSQL() throws SQLException;
}
