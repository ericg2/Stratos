package com.houseofkraft.command.encryption;

import com.houseofkraft.core.DiscordBot;
import com.houseofkraft.util.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RSA {
    private JDA jda;
    private MessageReceivedEvent event;
    private RSA aesHandler;
    private HashMap<String, String> details;

    public HashMap<String, String> getDetails() {
        try {
            HashMap<String, String> details = new HashMap<String, String>();

            details.put("Name", "RSA");
            details.put("Usage", "Encrypts text with RSA");
            details.put("Minimum_Permission", Permission.MESSAGE_ATTACH_FILES.toString());

            return details;
        } catch (Exception e) {
            System.out.println(e.toString());
            return details;
        }
    }

    public void encryptError(String message) {
        event.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("RSA Encryption Error")
                .setDescription(message)
                .build()).queue();
    }


    public void execute(JDA jda, MessageReceivedEvent event, String[] args) throws Exception {
        this.jda = jda;
        this.event = event;

        switch (args[1]) {
            case "new":
                int rsaSize = Integer.parseInt(args[2]);
                int aesSize = Integer.parseInt(args[3]);
                String maxRSAStr = (String)DiscordBot.config.get("maxRSA");
                String maxAESStr = (String)DiscordBot.config.get("maxAES");

                int maxRSA = Integer.parseInt(maxRSAStr);
                int maxAES = Integer.parseInt(maxAESStr);

                if (rsaSize > maxRSA || aesSize > maxAES) {
                    encryptError(":x: Maximum Limit Reached!\nMax RSA: " + maxRSA + " bits\nMax AES: " + maxAES + " bits");
                    return;
                }

                if (rsaSize < 512) {
                    encryptError(":x: RSA Size must be at least 512 bits!");
                    return;
                }

                // Initialize a new RSA handler to generate the keyfile.
                com.houseofkraft.util.RSA rsaGenerator = new com.houseofkraft.util.RSA(true, rsaSize, aesSize);
                File keyfileObj = new File(event.getGuild().getId().toString() + ".keyfile");
                Files.deleteIfExists(Paths.get(event.getGuild().getId().toString() + ".keyfile"));

                rsaGenerator.generateKeyFile(event.getGuild().getId().toString() + ".keyfile");

                if (keyfileObj.exists()) {
                    MessageEmbed successEmbed = new EmbedBuilder()
                            .setTitle("RSA Key Generation Successful!")
                            .setDescription("The file has successfully been generated.")
                            .build();
                    event.getChannel().sendMessage(successEmbed).addFile(keyfileObj).queue();
                }
                break;
            case "encrypt":
                String encryptText = args[2];
                List<Message.Attachment> attachments = event.getMessage().getAttachments();
                if (attachments.isEmpty()) { encryptError(":x: No file was uploaded, please make sure you have uploaded the keyfile with the message you have sent."); return; }

                Message.Attachment fileAttachment = attachments.get(0);
                if (!Objects.equals(fileAttachment.getFileExtension(), "keyfile")) {
                    encryptError(":x: Invalid file type, please make sure you are uploading a keyfile!");
                    return;
                }

                fileAttachment.downloadToFile(event.getGuild().getId().toString() + ".pkeyfile");

                // Now, make a new RSA handler to encrypt the data with the keyfile.
                com.houseofkraft.util.RSA rsaEncrypted = new com.houseofkraft.util.RSA(false,1,1);
                rsaEncrypted.loadKeyFile(event.getGuild().getId().toString() + ".pkeyfile");

                String encryptedText = rsaEncrypted.encrypt(encryptText);

                MessageEmbed successEmbed = new EmbedBuilder()
                        .setTitle("RSA Encryption Successful!")
                        .setDescription("Output: " + encryptedText)
                        .build();
                event.getChannel().sendMessage(successEmbed).queue();

                Files.deleteIfExists(Paths.get(event.getGuild().getId().toString() + ".pkeyfile"));
        }
    }
}
