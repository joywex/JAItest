package com.justai.testassignment.java_developer_v3.service;


import com.justai.testassignment.java_developer_v3.server.LongPollUpdate;
import com.justai.testassignment.java_developer_v3.server.LongPollServerResponse;
import com.justai.testassignment.java_developer_v3.server.LongPollUpdatesResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LongPollService {

    private final RestTemplate restTemplate;
    private final BotService botService;
    private static final Logger logger = LoggerFactory.getLogger(LongPollService.class);

    @Value("${vk.access.token}")
    private String accessToken;

    @Value("${vk.group.id}")
    private String groupId;

    @Value("${vk.poll.api.version}")
    private String apiVersion;

    @Value("${get.long.poll.server}")
    private String longPollServer;

    private String server;
    private String key;
    private String ts;

    public LongPollService(RestTemplate restTemplate, BotService botService) {
        this.restTemplate = restTemplate;
        this.botService = botService;
    }

    @PostConstruct
    public void init() {
        startLongPolling();
    }

    public void startLongPolling() {
        getLongPollServer();

        while (true) {
            try {
                pollServer();
            } catch (Exception e) {
                logger.error("Ошибка при опросе Long Poll сервера: " + e.getMessage());
                getLongPollServer();
            }
        }
    }

    private void getLongPollServer() {
        String url = String.format(longPollServer, groupId, accessToken, apiVersion);
        logger.info("Запрос на получение Long Poll сервера: {}", url);

        LongPollServerResponse response = restTemplate.getForObject(url, LongPollServerResponse.class);

        if (response != null) {
            System.out.println("Response: " + response);
            if (response.getResponse() != null) {
                this.server = response.getResponse().getServer();
                this.key = response.getResponse().getKey();
                this.ts = response.getResponse().getTs();
                logger.info("Long Poll сервер успешно настроен. Server: {}, Key: {}, Ts: {}", server, key, ts);
            } else {
                System.err.println("Ошибка: response.getResponse() вернул null.");
            }
        } else {
            logger.error("Ошибка: не удалось получить настройки Long Poll сервера. Response: {}", response);
        }
    }

    private void pollServer() {
        String absoluteServerUrl = server.startsWith("http") ? server : "https://" + server;

        String url = String.format("%s?act=a_check&key=%s&ts=%s&wait=25", absoluteServerUrl, key, ts);
        logger.debug("Запрос к Long Poll серверу: {}", url);

        LongPollUpdatesResponse response = restTemplate.getForObject(url, LongPollUpdatesResponse.class);

        if (response != null && response.getUpdates() != null) {
            logger.info("Получено {} обновлений от Long Poll сервера.", response.getUpdates().size());

            List<LongPollUpdate> updates = response.getUpdates();
            for (LongPollUpdate update : updates) {
                botService.processUpdate(update);
                logger.debug("Обновление обработано: {}", update);
            }
            this.ts = response.getTs();
            logger.debug("Обновлен параметр Ts: {}", ts);
        } else {
            System.err.println("Ошибка: не удалось получить обновления.");
        }
    }

}
