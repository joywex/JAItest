package com.justai.testassignment.java_developer_v3.server;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LongPollServerResponse {

    private LongPollServer response;

    @Getter
    @Setter
    public static class LongPollServer {
        private String key;
        private String server;
        private String ts;
    }
}