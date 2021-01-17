package com.houseofkraft.command.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Objects;

public class Play {
    private JDA jda;
    private MessageReceivedEvent event;

    public HashMap<String, String> getDetails() {
        HashMap<String, String> details = new HashMap<String, String>();

        details.put("Name", "Play");
        details.put("Usage", "Plays music on the server.");
        details.put("Minimum_Permission", Permission.VOICE_CONNECT.toString());

        return details;
    }


    public void voiceConnectError(String message) {
        event.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("Voice Connection Error")
                .setDescription(message)
                .build()).queue();
    }

    public void execute(JDA jda, MessageReceivedEvent event, String[] args) {
        this.jda = jda;
        this.event = event;

        Member member = event.getMember();
        MessageChannel textChannel = event.getChannel();

        if (!event.getGuild().getSelfMember().hasPermission(Permission.VOICE_CONNECT)) {
            voiceConnectError(":x: I do not have permission to join a Voice Channel!");
            return;
        }

        assert member != null;
        VoiceChannel connectedVoiceChannel = member.getVoiceState().getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (connectedVoiceChannel == null) {
            voiceConnectError(":x: You are currently not in a Voice Channel!");
            return;
        }

        audioManager.openAudioConnection(connectedVoiceChannel);
    }
}
