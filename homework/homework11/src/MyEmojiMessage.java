import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.Objects;

public class MyEmojiMessage implements EmojiMessage {
    private int id;
    private int emojiId;
    private int socialValue;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;

    public MyEmojiMessage(int messageId, int emojiNumber,
                          Person messagePerson1, Person messagePerson2) {
        this.type = 0;
        this.group = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.emojiId = emojiNumber;
        this.socialValue = emojiNumber;
    }

    public MyEmojiMessage(int messageId, int emojiNumber,
                          Person messagePerson1, Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.group = messageGroup;
        this.emojiId = emojiNumber;
        this.socialValue = emojiNumber;
    }

    @Override
    public int getEmojiId() {
        return emojiId;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        return person2;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyEmojiMessage that = (MyEmojiMessage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
