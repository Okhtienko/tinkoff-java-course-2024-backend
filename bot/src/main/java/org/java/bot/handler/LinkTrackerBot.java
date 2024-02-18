package org.java.bot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.java.bot.bot.Bot;
import org.java.bot.component.Menu;
import org.java.bot.configuration.ApplicationConfig;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinkTrackerBot implements Bot {
    private final TelegramBot bot;
    private final MessageProcessor messageProcessor;

    public LinkTrackerBot(MessageProcessor messageProcessor, ApplicationConfig config, Menu menu) {
        this.bot = new TelegramBot(config.telegramToken());
        this.messageProcessor = messageProcessor;
        this.execute(new SetMyCommands(menu.get()));
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        return bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        int processedCount = 0;
        for (Update update : updates) {
            SendMessage sendMessage = messageProcessor.process(update);
            if (sendMessage != null) {
                SendResponse response = bot.execute(sendMessage);
                if (response.isOk()) {
                    processedCount++;
                }
            }
        }
        return processedCount;
    }

    @Override
    public void start() {
        bot.setUpdatesListener(updates -> {
            log.info("Starting updates listener...");
            int processedCount = process(updates);
            log.info("Processed {} updates", processedCount);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @Override
    public void close() {
        bot.removeGetUpdatesListener();
        log.info("Updates listener removed");
    }
}
