package com.houseofkraft.command.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;

public class Ban {
    private JDA jda;
    private MessageReceivedEvent event;

    public HashMap<String, String> getDetails() {
        HashMap<String, String> details = new HashMap<String, String>();

        details.put("Name", "Ban");
        details.put("Usage", "Bans players on the server.");
        details.put("Minimum_Permission", Permission.BAN_MEMBERS.toString());

        return details;
    }

    public void banError(String message) {
        event.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("Ban Error")
                .setDescription(message)
                .build()).queue();
    }

    public void banSuccess(String message) {
        event.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("Ban Successful")
                .setDescription(message)
                .build()).queue();
    }

    public void execute(JDA jda, MessageReceivedEvent event, String[] args) {
        this.jda = jda;
        this.event = event;

        Message message = event.getMessage();
        Guild guild = event.getGuild();
        MessageChannel channel = event.getChannel();
        Member selfMember = guild.getSelfMember();

        if (message.getMentionedUsers().isEmpty()) {
            banError(":x: You didn't mention a user!");
            return;
        }

        List<User> banMemberList = message.getMentionedUsers();
        for (User banUser : banMemberList) {

            if (banUser != null) {
                Member banMember = guild.getMember(banUser);

                if (banMember != null) {
                    guild.ban(banMember, 14).queue(
                            success -> banSuccess(":white_check_mark: Successfully banned " + banMember.getEffectiveName() + "!"),
                            error -> banError(":x: Failed to ban " + banMember.getEffectiveName() + "!")
                    );
                }
            }
        }
    }
}
