package ru.liga.input;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.exception.BaseException;
import ru.liga.executor.CommandExecutor;
import ru.liga.executor.ExecutorFactory;
import ru.liga.output.CommandResult;
import ru.liga.repository.CurrencyRepository;

@Slf4j
public class Bot extends TelegramLongPollingCommandBot {

    private final String botName;
    private final String botToken;
    private final CurrencyRepository repository;

    public Bot(String botName, String botToken, CurrencyRepository repository) {
        super();
        this.repository = repository;
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message userInput = update.getMessage();
        log.info("Input: {}; from user: {}", userInput.getText(), userInput.getFrom().getUserName());
        try {
            UserCommand userCommand = UserCommand.createFromString(userInput.getText());
            CommandExecutor executor = new ExecutorFactory(repository).getExecutor(userCommand);
            CommandResult result = executor.execute();
            sendMessageToChat(userInput.getChatId(), userInput.getFrom().getUserName(), result);
        } catch (BaseException e) {
            log.error(e.getMessage());
            sendMessageToChat(userInput.getChatId(), userInput.getFrom().getUserName(), e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void sendMessageToChat(Long chatId, String userName, String error) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());
        answer.setText(error);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error: {}; from: {}; result: {}", e.getMessage(), userName, error);
        }
    }

    private void sendMessageToChat(Long chatId, String userName, CommandResult result) {
        try {
            if (result.isText()) {
                SendMessage answer = new SendMessage();
                answer.setChatId(chatId.toString());
                answer.setText(result.getTextResult());
                execute(answer);
            } else {
                SendPhoto answer = new SendPhoto();
                answer.setChatId(chatId.toString());
                answer.setPhoto(new InputFile(result.getGraphResult()));
                execute(answer);
            }
        } catch (TelegramApiException e) {
            log.error("Error: {}; from: {}; result: {}", e.getMessage(), userName, result);
        }
    }

}
