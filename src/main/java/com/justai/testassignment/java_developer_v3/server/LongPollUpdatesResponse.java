package com.justai.testassignment.java_developer_v3.server;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LongPollUpdatesResponse {

    private List<LongPollUpdate> updates;
    private String ts;
}
