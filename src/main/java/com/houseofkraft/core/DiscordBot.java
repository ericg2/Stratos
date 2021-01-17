package com.houseofkraft.core;

import com.houseofkraft.command.Index;
import com.houseofkraft.exception.InvalidLevelException;
import com.houseofkraft.util.*;
import com.houseofkraft.handler.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.jdbi.v3.core.ConnectionException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DiscordBot {
    public static JSONObject config;
    private CommandHandler commandHandler = new CommandHandler();
    public static GuildSettings guildSettings;
    public static JDA jda;
    public static Logger logger;
    private Jdbi jdbi;
    private Handle handle;

    private boolean debug = true;

    public void info(String message) throws IOException, InvalidLevelException { logger.log(message, Logger.INFO); }

    public DiscordBot() throws IOException, ParseException, LoginException, InvalidLevelException {
        try {
            config = new JSON("config.json").readJSON();

            if ((boolean) config.get("writeLogToFile")) {
                logger = new Logger(config.get("logFilePath").toString());
            } else {
                logger = new Logger(debug);
            }


            info("Stratos V1");
            info("Copyright (c) 2021 houseofkraft");

            Properties properties = new Properties();
            properties.setProperty("username", config.get("sqlUsername").toString());
            properties.setProperty("password", config.get("sqlPassword").toString());

            System.setProperty("com.mysql.jdbc.Driver", "");

            info("Attempting connection to SQL...");
            SQL sqlHandle = new SQL(config.get("sqlURL").toString(), properties.getProperty("username"), properties.getProperty("password"));

            if (!sqlHandle.isClosed()) {
                if (sqlHandle.isReadOnly()) {
                    logger.log("The SQL Database is read-only, you will not be able to write to the database!", Logger.ERROR);
                } else {
                    logger.log("Connection Successful!", Logger.SUCCESS);
                }
            }

            // Create a new GuildSettings instance with the SQL.
            guildSettings = new GuildSettings(sqlHandle);

            info("Indexing commands...");
            // Add the Commands from the Index
            commandHandler.scanIndex(new Index());
            info("Done.");

            info("Connecting to Discord Instance...");
            jda = JDABuilder.createDefault(config.get("token").toString()).addEventListeners(new EventHandler(commandHandler)).build();

            if (jda != null) {
                logger.log("SQL Connection Successful!", Logger.SUCCESS);
            } else {
                logger.log("SQL Connection Failed!", Logger.FATAL);
            }

            jda.getPresence().setActivity(Activity.watching("your mom"));

        } catch (ConnectionException c) {
            logger.log("Failed to connect to SQL Database!", Logger.FATAL);
            System.exit(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
