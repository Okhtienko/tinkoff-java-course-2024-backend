package org.java.bot.aspect;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.java.bot.service.UserService;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommandAspect {
    private final UserService userService;

    private boolean isAuthorized(Update update) {
        Long userId = update.message().from().id();
        if (!userService.isExists(userId)) {
            String command = update.message().text();
            return command.equals("/start") || command.equals("/help");
        }
        return true;
    }

    @Around("@annotation(BotCommand)")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        Update update = (Update) joinPoint.getArgs()[0];

        if (!isAuthorized(update)) {
            log.info("Unauthorized access attempt from user {} with command {}.",
                     update.message().from().id(),
                     update.message().text()
            );
            return new SendMessage(update.message().chat().id(), "Please register using the /start command.");
        }

        return joinPoint.proceed();
    }
}
