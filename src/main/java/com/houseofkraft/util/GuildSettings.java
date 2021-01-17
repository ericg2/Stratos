package com.houseofkraft.util;

// Controls the Individual Guild Settings using the SQL API.
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuildSettings {
    private SQL sqlHandler;

    public GuildSettings(SQL sqlHandler) {
        this.sqlHandler = sqlHandler;
    }

    public void newGuild(String guildID, String data) throws SQLException {
        sqlHandler.execute("INSERT INTO stratos(guildID, info) VALUES (" + guildID + ", " + "\"" + data + "\");");
    }

    public void addItem(String guildID, String key, String value) throws SQLException {
        String text = "," + key + "=" + value;
        String currentData = null;
        ResultSet currentQuery = sqlHandler.query("SELECT * FROM stratos WHERE guildID = " + guildID + ";");

        while (currentQuery.next()) {
            currentData = currentQuery.getString("info").replace("\"", "");
        }

        assert currentData != null;
        if (currentData.endsWith(",")) {
            currentData = currentData.substring(0, currentData.length()-1);
        }

        String combinedData = currentData + text;

        sqlHandler.execute("UPDATE stratos SET info = " + "\"" + combinedData + "\" WHERE guildID = " + guildID + ";");
    }

    public void changeRawValue(String guildID, String oldValue, String newValue) throws SQLException {
        ResultSet dataQuery = sqlHandler.query("SELECT * FROM stratos WHERE guildID = " + guildID + ";");
        String currentData = null, value = null;

        while (dataQuery.next()) {
            currentData = dataQuery.getString("info");
        }

        assert currentData != null;
        String newData = currentData.replace(oldValue, newValue);

        sqlHandler.execute("UPDATE stratos SET info = " + "\"" + newData + "\" WHERE guildID = " + guildID + ";");
    }

    public String getValue(String guildID, String key) throws SQLException {
        ResultSet dataQuery = sqlHandler.query("SELECT * FROM stratos WHERE guildID = " + guildID + ";");
        String currentData = null, value = null;

        while (dataQuery.next()) {
            currentData = dataQuery.getString("info");
        }
        
        String[] entrySplit = currentData.split(",");

        for (String entry : entrySplit) {
            String[] entryKeySplit = entry.split("=");

            if (entryKeySplit[0].equals(key)) {
                value = entryKeySplit[1];
            }
        }

        if (value != null) {
            return value;
        } else {
            return "";
        }
    }
}
