package com.justai.testassignment.java_developer_v3.service;


import com.justai.testassignment.java_developer_v3.dto.VkMessage;
import com.justai.testassignment.java_developer_v3.server.LongPollUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class BotService {

    @Value("${vk.access.token}")
    private String accessKey;

    @Value("${vk.poll.api.version}")
    private String apiVersion;

    @Value("${vk.send.messages}")
    private String apiSendMessage;

    private final RestTemplate restTemplate;
    private static final String MESSAGE_TYPE = "message_new";
    private static final Logger logger = LoggerFactory.getLogger(BotService.class);

    public BotService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void processUpdate(LongPollUpdate update) {
        if (update.getType().equals(MESSAGE_TYPE)) {
            logger.info("Получено новое сообщение от пользователя с id: {} ", update.getObject().getMessage().getFromId());
            VkMessage message = update.getObject().getMessage();

            if (isTextOnlyMessage(message)) {
                logger.info("Сообщение пользователя с id {} не содержит вложений.", update.getObject().getMessage().getFromId());
                String text = update.getObject().getMessage().getText();
                sendMessage(message.getFromId(), "Вы сказали: " + text);
            } else {
                logger.info("Сообщение пользователя с id {} содержит вложения.", update.getObject().getMessage().getFromId());
                sendMessage(message.getFromId(), "Вы отправили сообщение, которое не содержит текст или содержит ещё какое-либо вложение. Попробуйте снова.");
            }
        }
    }

    private boolean isTextOnlyMessage(VkMessage message) {
        logger.debug("Проверка сообщения от пользователя {} на наличие вложений.", message.getFromId());
        return message.getText() != null && !message.getText().isEmpty() &&
                (message.getAttachments() == null || message.getAttachments().isEmpty());
    }

    public void sendMessage(Integer userId, String text) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_id", String.valueOf(userId));
        params.add("message", text);
        params.add("access_token", accessKey);
        params.add("v", apiVersion);
        params.add("random_id", String.valueOf(System.currentTimeMillis()));

        logger.debug("Подготовка данных для отправки сообщения пользователю с id {}.", userId);

        String response = restTemplate.postForObject(apiSendMessage, params, String.class);
        logger.info("Сообщение отправлено пользователю с id {}", userId);
        logger.debug("VK response: {}", response);
    }

}
