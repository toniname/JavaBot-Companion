package org.example;

import org.example.telegram.TelegramBotService;

public class AppLauncher {
    public static void main(String[] args) {
        try {
            TelegramBotService telegramBotService = new TelegramBotService();
            System.out.println("Telegram Bot has been started successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while starting the Telegram Bot.");
        }
    }

}
