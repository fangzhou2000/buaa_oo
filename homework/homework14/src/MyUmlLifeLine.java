import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlLifeline;

import java.util.HashMap;

public class MyUmlLifeLine {
    private UmlLifeline umlLifeline;
    private HashMap<MessageSort, Integer> sentMessageCountMap;
    private int incomingMessageCount;

    public MyUmlLifeLine(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
        this.sentMessageCountMap = new HashMap<>();
        this.incomingMessageCount = 0;
    }

    public UmlLifeline getUmlLifeline() {
        return umlLifeline;
    }

    //添加发送的消息
    public void addSentMessage(MyUmlMessage myUmlMessage) {
        MessageSort ms = myUmlMessage.getUmlMessage().getMessageSort();
        if (sentMessageCountMap.containsKey(ms)) {
            int old = sentMessageCountMap.get(ms);
            sentMessageCountMap.put(ms, old + 1);
        } else {
            sentMessageCountMap.put(ms, 1);
        }
    }

    //添加收到的消息
    public void addIncomingMessage(MyUmlMessage myUmlMessage) {
        incomingMessageCount++;
    }

    /*给定UML顺序图和参与对象，发出了多少个消息
    输入指令格式：SENT_MESSAGE_COUNT umlinteraction_name lifeline_name messagesort
    举例：SENT_MESSAGE_COUNT normal door SYNCH_CALL
    输出：
    Ok, sent message count of lifeline "door" of umlinteraction "normal" is x.
    x为顺序图模型normal（UMLInteraction）中 door 发送的类别为 SYNCH_CALL 的消息个数
    Failed, umlinteraction "normal" not found. 不存在normal这个顺序图模型
    Failed, duplicated umlinteraction "normal". 存在多个normal顺序图模型
    Failed, lifeline "door" in umlinteraction "normal" not found.在顺序图模型normal中未找到参与对象door
    Failed, duplicated lifeline "door" in umlinteraction "normal".在顺序图模型normal中存在多个door参与对象
     */
    public int getSentMessageCountMap(MessageSort sort) {
        if (sentMessageCountMap.containsKey(sort)) {
            return sentMessageCountMap.get(sort);
        } else {
            return 0;
        }
    }

    /*给定UML顺序图和参与对象，有多少个incoming消息
    输入指令格式：INCOMING_MSG_COUNT umlinteraction_name lifeline_name
    举例：INCOMING_MSG_COUNT normal door
    输出：
    Ok, incoming message count of lifeline "door" in umlinteraction "normal" is x. x为顺序图模型normal（UMLInteraction）中发送给door的消息个数
    Failed, umlinteraction "normal" not found. 不存在normal这个顺序图模型
    Failed, duplicated umlinteraction "normal". 存在多个normal顺序图模型
    Failed, lifeline "door" in umlinteraction "normal" not found.在顺序图模型normal中未找到参与对象door
    Failed, duplicated lifeline "door" in umlinteraction "normal".在顺序图模型normal中存在多个door参与对象
    注意：
    这里的UMLInteraction指UML所定义的一个类型
     */
    public int getIncomingMessageCount() {
        return incomingMessageCount;
    }
}
