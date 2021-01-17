package com.houseofkraft.handler;

import com.houseofkraft.core.DiscordBot;
import com.houseofkraft.exception.InvalidLevelException;
import com.houseofkraft.util.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jdbi.v3.core.Handle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.houseofkraft.core.DiscordBot.logger;

public class EventHandler extends ListenerAdapter {
    private CommandHandler handler;
    private Handle jdbiHandle;
    private String defaultPrefix;
    private String args;

    public EventHandler(CommandHandler handler) { this.handler = handler; }

    public EventHandler(CommandHandler handler, Handle jdbiHandle) {
        this.handler = handler;
        this.jdbiHandle = jdbiHandle;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Guild guild = event.getGuild();

        try {
            DiscordBot.guildSettings.newGuild(guild.getId().toString(), "prefix=" + DiscordBot.config.get("defaultPrefix").toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (this.defaultPrefix == null) {
            this.defaultPrefix = DiscordBot.config.get("defaultPrefix").toString();
        }

        try {
            String[] message = event.getMessage().getContentDisplay().split(" ");
            String command = message[0].toLowerCase();
            String guildPrefix = DiscordBot.guildSettings.getValue(event.getGuild().getId().toString(), "prefix");

            if (!event.getAuthor().isBot()) {

                if (command.startsWith(guildPrefix)) {
                    String textCommand = command.substring(guildPrefix.length());
                    System.out.println("Command = " + textCommand);

                    Object commandObj = handler.getConstructor(textCommand);


                    // First, check if the command actually exists before attempting to run.
                    Iterator commandIterator = handler.commandPaths.entrySet().iterator();
                    boolean commandExists = false;

                    while (commandIterator.hasNext()) {
                        Map.Entry mapElement = (Map.Entry) commandIterator.next();
                        String commandName = mapElement.getKey().toString();

                        if (commandName.toLowerCase().equals(textCommand)) {
                            commandExists = true;
                            break;
                        }
                    }

                    if (commandExists) {
                        HashMap<String, String> commandDetails = handler.getCommandDetails(commandObj);
                        String permissionRequired = commandDetails.get("Minimum_Permission");

                        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.valueOf(permissionRequired))) {
                            handler.execute(commandObj, event, message);
                        } else {
                            event.getChannel().sendMessage(new EmbedBuilder()
                                    .setTitle("No Permission")
                                    .setDescription(":x: You need the **" + permissionRequired + "** permission to run this command!")
                                    .build()).queue();
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                logger.log("Discord Bot Exception: " + e.toString(), Logger.ERROR);
                e.printStackTrace();

            } catch (InvalidLevelException | IOException iv) {
                iv.printStackTrace();
            }
        }
    }
}
