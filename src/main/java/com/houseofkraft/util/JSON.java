package com.houseofkraft.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSON {

    private final String filePath;

    public JSONObject readJSON() throws IOException, ParseException {
        return (JSONObject) new JSONParser().parse(new FileReader(this.filePath));
    }

    public JSON(String filePath) throws IOException {
        this.filePath = filePath;

        File fileObject = new File(filePath);
        if (!fileObject.canRead() || !fileObject.exists() || !fileObject.isFile()) { throw new IOException(); }
    }
}
