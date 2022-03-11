package com.oocourse.spec3.main;

public interface EmojiMessage extends Message {
    //@ public instance model non_null int emojiId;

    //@ invariant socialValue == emojiId;

    //@ ensures \result == emojiId;
    public /*@pure@*/ int getEmojiId();
}
