package com.houseofkraft.command.setting;

import com.houseofkraft.core.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class Prefix {
    private JDA jda;
    private MessageReceivedEvent event;

    public HashMap<String, String> getDetails() {
        // This is a required command, that returns a HashMap<String,String> with all the details.
        HashMap<String, String> details = new HashMap<String, String>();

        details.put("Name", "Prefix");
        details.put("Usage", "Change the prefix for the server.");
        details.put("Minimum_Permission", Permission.MESSAGE_WRITE.toString());

        return details;
    }

    public void execute(JDA jda, MessageReceivedEvent event, String[] args) throws SQLException {
        // This will execute the command when the handler calls this.
        MessageChannel channel = event.getChannel();
        String prefix = DiscordBot.guildSettings.getValue(event.getGuild().getId().toString(), "prefix");
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Prefix")
                .setDescription("The prefix has been changed to " + args[1]);

        DiscordBot.guildSettings.changeRawValue(event.getGuild().getId().toString(), "prefix=" + prefix, "prefix=" + args[1]);

        channel.sendMessage(embed.build()).queue();
    }
}
