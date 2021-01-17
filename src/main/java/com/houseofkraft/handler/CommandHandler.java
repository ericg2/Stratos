package com.houseofkraft.handler;

import com.houseofkraft.command.Index;
import com.houseofkraft.core.DiscordBot;
import com.houseofkraft.exception.InvalidLevelException;
import com.houseofkraft.util.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

public class CommandHandler {
    public HashMap<String, String> commandPaths;
    private JDA jda;

    /*
    public void scanDirectory(String commandDir, String classPath) throws IOException {
        File directoryObject = new File(commandDir);
        if (!directoryObject.isDirectory()) { throw new IOException(); }

        String[] commandList = directoryObject.list();

        for (String command : commandList) {
            if (command.endsWith(".java")) {
                String commandName = command.split(".java")[0].toLowerCase();
                String className = classPath + "." + commandName;

                commandPaths.put(commandName, className);
            }
        }
    }
     */

    public void scanIndex(Index index) throws IOException, InvalidLevelException {
        String[] commandList = index.indexClass;

        for (String classPath : commandList) {
            if (classPath.startsWith("com.houseofkraft")) {

                String[] classPathSplit = classPath.split("\\.");
                String commandName = classPathSplit[classPathSplit.length-1].toLowerCase();

                this.commandPaths.put(commandName, classPath);
                DiscordBot.logger.log("Added " + commandName + " / " + classPath + " to path.", Logger.INFO);
            }
        }
    }

    @SuppressWarnings({"deprecation"})
    private Object runClassFromString(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return Class.forName(className).newInstance();
    }

    private void callMethodFromClass(Object classObj, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        classObj.getClass().getMethod(methodName).invoke(classObj);
    }

    private void callMethodFromClass(Object classObj, String methodName, JDA jdaObject, MessageReceivedEvent event) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        classObj.getClass().getMethod(methodName, JDA.class, MessageReceivedEvent.class).invoke(classObj, jdaObject, event);
    }

    private void callMethodFromClass(Object classObj, String methodName, JDA jdaObject, MessageReceivedEvent event, String[] arg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        classObj.getClass().getMethod(methodName, JDA.class, MessageReceivedEvent.class, String[].class).invoke(classObj, jdaObject, event, arg);
    }

    public void execute(Object classObj, MessageReceivedEvent event, String[] _arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        callMethodFromClass(classObj, "execute", DiscordBot.jda, event, _arg);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getCommandDetails(Object classObj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (HashMap<String, String>) classObj.getClass().getMethod("getDetails").invoke(classObj);
    }

    public Object getConstructor(String commandName) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        Object classObject = runClassFromString(commandPaths.get(commandName.toLowerCase()));
        //callMethodFromClass(classObject, commandName.substring(0, 1).toUpperCase() + commandName.substring(1));

        return classObject;
    }

    @SuppressWarnings("unchecked")
    public CommandHandler(String commandDir, String classPath) throws IOException {
        super();
    }

    @SuppressWarnings("unchecked")
    public CommandHandler() {
        HashMap<String, String> commandPaths = new HashMap<String, String>();
        this.commandPaths = commandPaths;

    }
}
