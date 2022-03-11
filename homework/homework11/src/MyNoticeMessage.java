import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

import java.util.Objects;

public class MyNoticeMessage implements NoticeMessage {
    private int id;
    private String string;
    private int socialValue;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;

    public MyNoticeMessage(int messageId, String string,
                           Person messagePerson1, Person messagePerson2) {
        this.type = 0;
        this.group = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.string = string;
        this.socialValue = string.length();
    }

    public MyNoticeMessage(int messageId, String string,
                           Person messagePerson1, Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.group = messageGroup;
        this.string = string;
        this.socialValue = string.length();
    }

    @Override
    public String getString() {
        return string;
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
        MyNoticeMessage that = (MyNoticeMessage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
