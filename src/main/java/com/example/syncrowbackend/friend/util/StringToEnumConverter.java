package com.example.syncrowbackend.friend.util;

import com.example.syncrowbackend.friend.enums.FriendReaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverter implements Converter<String, FriendReaction> {

    @Override
    public FriendReaction convert(String source) {
        return FriendReaction.valueOf(source.toUpperCase());
    }
}
