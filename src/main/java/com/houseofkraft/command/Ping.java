package com.houseofkraft.command;

// This Ping command is really just an example, no really useful purpose.

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;

import static java.lang.Math.abs;

public class Ping {
    private JDA jda;
    private MessageReceivedEvent event;

    public HashMap<String, String> getDetails() {
        // This is a required command, that returns a HashMap<String,String> with all the details.
        HashMap<String, String> details = new HashMap<String, String>();

        details.put("Name", "Ping");
        details.put("Usage", "Pings the Server");
        details.put("Minimum_Permission", Permission.MESSAGE_WRITE.toString());

        return details;
    }

    public void execute(JDA jda, MessageReceivedEvent event, String[] args) {
        // This will execute the command when the handler calls this.
        MessageChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();

        /*
        channel.sendMessage(embed.build()).queue(message -> {
            long ping = event.getMessage().getTimeCreated().until(message.getTimeCreated(), ChronoUnit.MILLIS);
            embed.addField("Response", ping + " ms", true);

            message.editMessage(embed.build()).queue();
        });
         */

        long messageTime = (event.getMessage().getTimeCreated().toEpochSecond())*1000;
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long pingTime = abs(currentTime - messageTime);

        channel.sendMessage(embed.addField("Response", pingTime + " ms", true).build()).queue();
    }

    public Ping() {}
}
