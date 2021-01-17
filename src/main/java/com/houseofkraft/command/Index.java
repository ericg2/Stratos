package com.houseofkraft.command;

public class Index {
    public String[] indexClass;

    public String[] getIndexClass() {
        return indexClass;
    }

    public Index() {
        this.indexClass = new String[] {
                "com.houseofkraft.command.Ping",
                "com.houseofkraft.command.setting.Prefix",
                "com.houseofkraft.command.music.Play",
                "com.houseofkraft.command.moderation.Ban",
                "com.houseofkraft.command.encryption.AES",
                "com.houseofkraft.command.encryption.RSA"
        };
    }
}
