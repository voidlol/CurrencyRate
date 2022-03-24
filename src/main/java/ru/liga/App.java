package ru.liga;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.data.CSVParser;
import ru.liga.input.*;
import ru.liga.repository.InMemoryCurrencyRepository;

@Slf4j
public class App {

    private static final String BOT_NAME = System.getenv("NAME");
    private static final String BOT_TOKEN = System.getenv("TOKEN");

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot(BOT_NAME, BOT_TOKEN, new InMemoryCurrencyRepository(new CSVParser()));
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error in bot registering. Token {}, Name {}", BOT_TOKEN, BOT_TOKEN, e);
        }
    }
}
