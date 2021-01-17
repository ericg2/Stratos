package com.houseofkraft;

import com.houseofkraft.core.DiscordBot;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Stratos {

    public static void main(String[] args) {
        try {
            new DiscordBot();
        } catch (Exception i) {
            System.out.println("PRE-LAUNCH: Exception has occurred, this is most likely because there is no config.json file in the directory, or the Discord Bot token is invalid.");
            i.printStackTrace();
        }
    }
}
