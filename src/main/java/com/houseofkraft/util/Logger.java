package com.houseofkraft.util;

import com.github.tomaslanger.chalk.*;
import com.houseofkraft.exception.InvalidLevelException;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public String fileLocation = null;
    public boolean fileSaving = false, debug = false;
    private FileWriter writer;

    public static final int
        DEBUG = 0,
        INFO = 1,
        SUCCESS = 2,
        SUCCESSFUL = 2,
        WARN = 3,
        WARNING = 3,
        ERROR = 4,
        FATAL = 5;

    public void log(String message, int logLevel) throws InvalidLevelException, IOException {
        boolean noCaseLevel = false;
        String prefix = null;
        Chalk ansiText = null;

        switch (logLevel) {
            case 0: ansiText = Chalk.on("[DEBUG] " + message).gray(); prefix = "[DEBUG] ";       break;
            case 1: ansiText = Chalk.on("[INFO] " + message).white(); prefix = "[INFO] ";        break;
            case 2: ansiText = Chalk.on("[SUCCESS] " + message).green(); prefix = "[SUCCESS] ";  break;
            case 3: ansiText = Chalk.on("[WARNING] " + message).yellow(); prefix = "[WARNING] "; break;
            case 4: ansiText = Chalk.on("[ERROR] " + message).red(); prefix = "[ERROR] ";        break;
            case 5: ansiText = Chalk.on("[FATAL] " + message).red().bold(); prefix = "[FATAL] "; break;
            default: noCaseLevel = true;
        }

        if (noCaseLevel) { throw new InvalidLevelException(); }

        if (this.fileSaving) {
            // Do not write debug messages if the user didn't enable debug.
            if (logLevel == 0 && debug) {
                writer.write(prefix + message);
            } else if (logLevel > 0) {
                writer.write(prefix + message);
            }
        }

        if (logLevel == 0 && debug) {
            System.out.println(ansiText);
        } else if (logLevel > 0) {
            System.out.println(ansiText);
        }
    }

    public void close() throws IOException {
        if (this.fileSaving) {
            writer.close();
        }
    }

    public Logger(boolean _debug) {
        super();
        debug = _debug;
    }

    public Logger(String logFile) {
        super();
        this.fileLocation = logFile;
        this.fileSaving = true;
    }

    public Logger() throws IOException {
        if (this.fileSaving) {
            FileWriter writer = new FileWriter(this.fileLocation);
        }
    }
}
