package client.controllers;

import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class History {
    public static PrintWriter writer;

    private static String fileHistoryNameByLogin(String login) {
        return "history/history_" + login + ".txt";
    }

    public static void showChatHistory(String login, TextArea chatHistory) {
        try {
            String fileName = fileHistoryNameByLogin(login);
            if (!Files.exists(Paths.get(fileName))) {
                Path path = Files.createFile(Paths.get(fileName));
            }
            StringBuilder sb = new StringBuilder();
            List<String> history = Files.readAllLines(Paths.get(fileName));
            int min;
            min = (history.size() > 100) ? (history.size() - 100) : 0;
            for (int i = min; i < history.size(); i++) {
                sb.append(history.get(i)).append(System.lineSeparator());
            }
            chatHistory.appendText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void startWriter(String login) {
        try {
            writer = new PrintWriter(new FileOutputStream(fileHistoryNameByLogin(login), true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void saveMessage(String message) {
        writer.println(message);
    }

    public static void closeWriter() {
        if (writer != null) {
            writer.close();
        }
    }
}
