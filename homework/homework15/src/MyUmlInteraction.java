import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlInteraction;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlInteraction {
    private UmlInteraction umlInteraction;
    private int participantCount;
    private HashMap<String, ArrayList<MyUmlLifeLine>> nameLifeLineMap;

    public MyUmlInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
        this.participantCount = 0;
        this.nameLifeLineMap = new HashMap<>();
    }

    public UmlInteraction getUmlInteraction() {
        return umlInteraction;
    }

    //添加参与者
    public void addParticipant(MyUmlLifeLine myUmlLifeLine) {
        participantCount++;
        String name = myUmlLifeLine.getUmlLifeline().getName();
        if (nameLifeLineMap.containsKey(name)) {
            nameLifeLineMap.get(name).add(myUmlLifeLine);
        } else {
            ArrayList<MyUmlLifeLine> arrayList = new ArrayList<>();
            arrayList.add(myUmlLifeLine);
            nameLifeLineMap.put(name, arrayList);
        }
    }

    /*给定UML顺序图和参与对象，有多少个incoming消息
    输入指令格式：INCOMING_MSG_COUNT umlinteraction_name lifeline_name
    举例：INCOMING_MSG_COUNT normal door
    输出：
    Ok, incoming message count of lifeline "door" in umlinteraction "normal" is x.
    x为顺序图模型normal（UMLInteraction）中发送给door的消息个数
    Failed, umlinteraction "normal" not found. 不存在normal这个顺序图模型
    Failed, duplicated umlinteraction "normal". 存在多个normal顺序图模型
    Failed, lifeline "door" in umlinteraction "normal" not found.在顺序图模型normal中未找到参与对象door
    Failed, duplicated lifeline "door" in umlinteraction "normal".在顺序图模型normal中存在多个door参与对象
    注意：
    这里的UMLInteraction指UML所定义的一个类型
     */
    public int getParticipantCount() {
        return participantCount;
    }

    //检查生命线
    private MyUmlLifeLine getLifeLine(String interactionName, String lifeLineName)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!nameLifeLineMap.containsKey(lifeLineName)) {
            throw new LifelineNotFoundException(interactionName, lifeLineName);
        } else {
            ArrayList<MyUmlLifeLine> arrayList = nameLifeLineMap.get(lifeLineName);
            if (arrayList.size() > 1) {
                throw new LifelineDuplicatedException(interactionName, lifeLineName);
            } else {
                return arrayList.get(0);
            }
        }
    }

    //INCOMING_MSG_COUNT umlinteraction_name lifeline_name
    public int getIncomingMessageCount(String interactionName, String lifeLineName)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        MyUmlLifeLine myUmlLifeLine = getLifeLine(interactionName, lifeLineName);
        return myUmlLifeLine.getIncomingMessageCount();
    }

    //SENT_MESSAGE_COUNT umlinteraction_name lifeline_name messagesort
    public int getSentMessageCount(String interactionName, String lifeLineName, MessageSort sort)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        MyUmlLifeLine myUmlLifeLine = getLifeLine(interactionName, lifeLineName);
        return myUmlLifeLine.getSentMessageCountMap(sort);
    }
}
