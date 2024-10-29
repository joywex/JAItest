package com.justai.testassignment.java_developer_v3.server;

import com.justai.testassignment.java_developer_v3.dto.VkObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LongPollUpdate {

    private String type;
    private VkObject object;
    private Long groupId;
}
