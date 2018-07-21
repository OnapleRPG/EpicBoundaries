package com.onaple.epicboundaries.data.access;

import com.onaple.epicboundaries.EpicBoundaries;
import com.onaple.epicboundaries.data.beans.InstanceBean;
import com.onaple.epicboundaries.data.handlers.DatabaseHandler;

import javax.inject.Inject;
import javax.naming.ServiceUnavailableException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InstanceDao {
    private String errorDatabasePrefix = "Error while connecting to database : ";

    @Inject
    private DatabaseHandler databaseHandler;

    /**
     * Generate database table if it does not exist
     */
    public void createTableIfNotExist() {
        String query = "CREATE TABLE IF NOT EXISTS instance (id INTEGER PRIMARY KEY, worldname VARCHAR(50), playercount INT DEFAULT 0, lastexit INT )";
        databaseHandler.execute(query);
    }

    /**
     * Add an instance to the database
     * @param instance Instance to add
     */
    public void addInstance(InstanceBean instance) {
        String query = "INSERT INTO instance (worldname, playercount, lastexit) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = databaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, instance.getWorldName());
            statement.setInt(2, instance.getPlayerCount());
            statement.setLong(3, instance.getLastExit());
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            EpicBoundaries.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            EpicBoundaries.getLogger().error("Error while inserting new instance : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Update the player count currently in an instance
     * @param worldName Instance name
     * @param playerIncrement Player count to increment (or decrement)
     */
    public void updateInstancePlayerCount(String worldName, int playerIncrement) {
        String query = "UPDATE instance SET playercount = playercount + ?, lastexit = ? WHERE worldname = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        try {
            connection = databaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, playerIncrement);
            statement.setLong(2, currentTimestamp.getTime()/1000);
            statement.setString(3, worldName);
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            EpicBoundaries.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            EpicBoundaries.getLogger().error("Error while updating instance player count : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Fetch database instances which have no player inside and are old enough
     * @return List of deprecated Instances
     */
    public List<InstanceBean> getDeprecatedInstances() {
        String query = "SELECT id, worldname, playercount, lastexit FROM instance WHERE playercount <= 0 AND strftime('%s', 'now') > lastexit + 2*60";
        List<InstanceBean> deprecatedInstances = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        try {
            connection = databaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            results = statement.executeQuery();
            while (results.next()) {
                deprecatedInstances.add(new InstanceBean(results.getInt("id"), results.getString("worldname"),
                        results.getInt("playercount"), results.getLong("lastexit")));
            }
            statement.close();
        } catch (ServiceUnavailableException e) {
            EpicBoundaries.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            EpicBoundaries.getLogger().error("Error while fetching deprecated instances : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, results);
        }
        return deprecatedInstances;
    }

    /**
     * Remove list of instances from database
     * @param instances List of instances to remove
     */
    public void removeInstances(List<InstanceBean> instances) {
        String query = "DELETE FROM instance WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = databaseHandler.getDatasource().getConnection();
            for (InstanceBean instance : instances) {
                statement = connection.prepareStatement(query);
                statement.setInt(1, instance.getId());
                statement.execute();
                statement.close();
            }
        } catch (ServiceUnavailableException e) {
            EpicBoundaries.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            EpicBoundaries.getLogger().error("Error while deleting instance : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Close a database connection
     * @param connection Connection to close
     */
    private static void closeConnection(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                EpicBoundaries.getLogger().error("Error while closing result set : " + e.getMessage());
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                EpicBoundaries.getLogger().error("Error while closing statement : " + e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                EpicBoundaries.getLogger().error("Error while closing connection : " + e.getMessage());
            }
        }
    }
}
