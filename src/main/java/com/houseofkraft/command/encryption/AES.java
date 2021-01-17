package com.houseofkraft.command.encryption;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import com.houseofkraft.util.RSA;

import java.util.HashMap;

public class AES {
    private JDA jda;
    private MessageReceivedEvent event;
    private RSA aesHandler;
    private HashMap<String, String> details;

    public HashMap<String, String> getDetails() {
        try {
            HashMap<String, String> details = new HashMap<String, String>();
            this.aesHandler = new RSA(false, 1, 1); // we don't need separate handlers for AES, so put it here.

            details.put("Name", "AES");
            details.put("Usage", "Encrypts text with AES.");
            details.put("Minimum_Permission", Permission.MESSAGE_WRITE.toString());

            return details;
        } catch (Exception e) {
            System.out.println(e.toString());
            return details;
        }
    }

    public String combineArgs(String[] arg, int startAt) {
        StringBuilder builder = new StringBuilder();
        for (int i=startAt; i<arg.length-1; i++) {
            builder.append(arg[i]).append(" ");
        }
        return builder.toString().trim();
    }

    public void execute(JDA jda, MessageReceivedEvent event, String[] args) throws Exception {
        this.jda = jda;
        this.event = event;

        if (args.length < 4) {
            event.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("Invalid Parameters")
                    .setDescription("Usage: aes <encrypt/decrypt> <password> <text>")
                    .build()).queue();
            return;
        }

        String aesType = args[1];
        String aesKey = args[2];

        String aesText = combineArgs(args, 3);

        if (aesType.equals("encrypt")) {
            event.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("AES Encryption Successful")
                    .setDescription("Output: " + aesHandler.rawAESEncrypt(aesText, aesKey))
                    .build()).queue();

        } else if (aesType.equals("decrypt")) {
            event.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("AES Decryption Successful")
                    .setDescription("Output: " + aesHandler.rawAESDecrypt(aesText, aesKey))
                    .build()).queue();
        } else {
            event.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("Invalid Parameters")
                    .setDescription("Usage: aes <encrypt/decrypt> <password> <text>")
                    .build()).queue();
        }
    }
}
