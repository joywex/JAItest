package com.justai.testassignment.java_developer_v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VkMessage {

    private Integer id; // Идентификатор сообщения
    private Integer date; // Время отправки в Unixtime
    private Integer peerId; // Идентификатор назначения
    @JsonProperty("from_id")
    private Integer fromId; // Идентификатор отправителя
    private String text; // Текст сообщения
    private Integer randomId; // Идентификатор для отправки сообщения
    private String ref; // Произвольный параметр
    private String refSource; // Произвольный параметр
    private List<Attachments> attachments; // Медиавложения
    private Boolean important; // Важное сообщение
    private Object geo; // Информация о местоположении
    private String payload; // Полезная нагрузка
    private Object keyboard; // Объект клавиатуры
    private List<VkMessage> fwdMessages; // Массив пересланных сообщений
    private VkMessage replyMessage; // Сообщение, на которое отправлено текущее
    private Action action; // Информация о сервисном действии
    private Integer adminAuthorId; // Идентификатор администратора
    private Integer conversationMessageId; // Уникальный номер сообщения
    private Boolean isCropped; // Сообщение обрезано для бота
    private Integer membersCount; // Количество участников
    private Integer updateTime; // Дата обновления сообщения
    private Boolean wasListened; // Прослушано ли вложенное аудиосообщение
    private Integer pinnedAt; // Дата закрепления сообщения
    private String messageTag; // Строка для сопоставления

    @Getter
    @Setter
    public static class Action {
        private String type; // Тип действия
        private Integer memberId; // Идентификатор пользователя
        private String text; // Название беседы
        private String email; // Email
        private Photo photo; // Объект с изображением

        @Getter
        @Setter
        public static class Photo {
            private String photo50; // URL изображения 50x50px
            private String photo100; // URL изображения 100x100px
            private String photo200; // URL изображения 200x200px
        }
    }
}
