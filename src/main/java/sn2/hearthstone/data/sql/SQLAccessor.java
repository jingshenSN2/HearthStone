package sn2.hearthstone.data.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface SQLAccessor {
	
	void connect();

    void disconnect();

    boolean isConnected();

    ResultSet getResult(String command);

    ResultSet getResultPreparedStatement(String command);

    void executeCommand(String command);

    PreparedStatement executeCommandPreparedStatement(String command);
}
