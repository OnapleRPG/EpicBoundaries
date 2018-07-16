package com.onaple.epicboundaries.data.access;

import com.onaple.epicboundaries.data.handlers.DatabaseHandler;

import javax.inject.Inject;

public class InstanceDao {
    private static String errorDatabasePrefix = "Error while connecting to database : ";

    @Inject
    private DatabaseHandler databaseHandler;

    /**
     * Generate database table if it does not exist
     */
    public void createTableIfNotExist() {
        String query = "CREATE TABLE IF NOT EXISTS instance (id INT PRIMARY KEY, worldname VARCHAR(50), playercount INT DEFAULT 0, lastexit INT )";
        databaseHandler.execute(query);
    }
}
