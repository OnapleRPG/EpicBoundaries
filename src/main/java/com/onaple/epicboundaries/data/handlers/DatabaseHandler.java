package com.onaple.epicboundaries.data.handlers;

import com.onaple.epicboundaries.EpicBoundaries;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.inject.Singleton;
import javax.naming.ServiceUnavailableException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class DatabaseHandler {
    private String JDBC_URL = "jdbc:sqlite:./epicboundaries.db";
    private SqlService sqlService;
    public DataSource getDatasource() throws SQLException, ServiceUnavailableException {
        if (sqlService == null) {
            sqlService = Sponge.getServiceManager().provide(SqlService.class).orElseThrow(() -> new ServiceUnavailableException("Sponge service manager not provided."));
        }
        return sqlService.getDataSource(JDBC_URL);
    }

    public void execute(String query){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            EpicBoundaries.getLogger().error("Error while connecting to database : ".concat(e.getMessage()));
        } catch (SQLException e) {
            EpicBoundaries.getLogger().error("Error while executing statement on database : ".concat(e.getMessage()));
        } finally {
            closeConnection(connection, statement, null);
        }
    }


    /**
     * Close a database connection
     * @param connection Connection to close
     */
    public void closeConnection(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                EpicBoundaries.getLogger().error("Error while closing result set : ".concat(e.getMessage()));
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                EpicBoundaries.getLogger().error("Error while closing statement : ".concat(e.getMessage()));
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                EpicBoundaries.getLogger().error("Error while closing connection : ".concat(e.getMessage()));
            }
        }
    }
}
