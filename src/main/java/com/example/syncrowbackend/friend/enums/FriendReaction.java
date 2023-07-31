package com.example.syncrowbackend.friend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FriendReaction {
    ACCEPT("accept"),
    REFUSE("refuse");

    private final String value;

    FriendReaction(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FriendReaction from(String value) {
        for (FriendReaction reaction : FriendReaction.values()) {
            if (reaction.getValue().equals(value)) {
                return reaction;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
