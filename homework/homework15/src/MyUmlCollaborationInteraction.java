import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlCollaboration;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlCollaborationInteraction implements UmlCollaborationInteraction {
    private HashMap<String, MyUmlCollaboration> myUmlCollaborationMap;
    private HashMap<String, ArrayList<MyUmlInteraction>> nameInteractionMap;
    private HashMap<String, MyUmlInteraction> myUmlInteractionMap;
    private HashMap<String, MyUmlLifeLine> myUmlLifeLineMap;
    private HashMap<String, MyUmlAttribute> myUmlAttributeMap;
    //缓存 collaborationId, attr
    private HashMap<String, HashMap<String, MyUmlAttribute>> collaborationIdAttrMap =
            new HashMap<>();

    public MyUmlCollaborationInteraction(UmlElement[] elements) {
        this.myUmlCollaborationMap = new HashMap<>();
        this.nameInteractionMap = new HashMap<>();
        this.myUmlInteractionMap = new HashMap<>();
        this.myUmlLifeLineMap = new HashMap<>();
        this.myUmlAttributeMap = new HashMap<>();

        ArrayList<UmlElement> elements1 = new ArrayList<>();
        for (UmlElement e : elements) {
            if (e.getElementType() == ElementType.UML_COLLABORATION) {
                parseCollaboration((UmlCollaboration) e);
            } else if (e.getElementType() == ElementType.UML_INTERACTION) {
                elements1.add(e);
            } else if (e.getElementType() == ElementType.UML_ATTRIBUTE) {
                elements1.add(e);
            } else if (e.getElementType() == ElementType.UML_LIFELINE) {
                elements1.add(e);
            } else if (e.getElementType() == ElementType.UML_MESSAGE) {
                elements1.add(e);
            }
        }

        ArrayList<UmlElement> elements2 = new ArrayList<>();
        for (UmlElement e : elements1) {
            if (e.getElementType() == ElementType.UML_INTERACTION) {
                parseInteraction((UmlInteraction) e);
            } else if (e.getElementType() == ElementType.UML_ATTRIBUTE) {
                parseAttribute((UmlAttribute) e);
            } else if (e.getElementType() == ElementType.UML_LIFELINE) {
                elements2.add(e);
            } else if (e.getElementType() == ElementType.UML_MESSAGE) {
                elements2.add(e);
            }
        }

        ArrayList<UmlElement> elements3 = new ArrayList<>();
        for (UmlElement e : elements2) {
            if (e.getElementType() == ElementType.UML_LIFELINE) {
                parseLifeLine((UmlLifeline) e);
            } else if (e.getElementType() == ElementType.UML_MESSAGE) {
                elements3.add(e);
            }
        }

        for (UmlElement e : elements3) {
            if (e.getElementType() == ElementType.UML_MESSAGE) {
                parseMessage((UmlMessage) e);
            }
        }

    }

    //解析Collaboration
    private void parseCollaboration(UmlCollaboration umlCollaboration) {
        MyUmlCollaboration myUmlCollaboration = new MyUmlCollaboration(umlCollaboration);
        myUmlCollaborationMap.put(umlCollaboration.getId(), myUmlCollaboration);
    }

    //解析Interaction
    private void parseInteraction(UmlInteraction umlInteraction) {
        MyUmlInteraction myUmlInteraction = new MyUmlInteraction(umlInteraction);
        myUmlInteractionMap.put(umlInteraction.getId(), myUmlInteraction);
        if (nameInteractionMap.containsKey(umlInteraction.getName())) {
            nameInteractionMap.get(umlInteraction.getName()).add(myUmlInteraction);
        } else {
            ArrayList<MyUmlInteraction> arrayList = new ArrayList<>();
            arrayList.add(myUmlInteraction);
            nameInteractionMap.put(umlInteraction.getName(), arrayList);
        }
    }

    //解析属性
    private void parseAttribute(UmlAttribute umlAttribute) {
        MyUmlAttribute myUmlAttribute = new MyUmlAttribute(umlAttribute);
        myUmlAttributeMap.put(umlAttribute.getId(), myUmlAttribute);
        if (collaborationIdAttrMap.containsKey(umlAttribute.getParentId())) {
            collaborationIdAttrMap.get(umlAttribute.getParentId()).
                    put(umlAttribute.getId(), myUmlAttribute);
        } else {
            HashMap<String, MyUmlAttribute> hashMap = new HashMap<>();
            hashMap.put(umlAttribute.getId(), myUmlAttribute);
            collaborationIdAttrMap.put(umlAttribute.getParentId(), hashMap);
        }
        if (myUmlCollaborationMap.containsKey(umlAttribute.getParentId())) {
            MyUmlCollaboration myUmlCollaboration =
                    myUmlCollaborationMap.get(umlAttribute.getParentId());
            myUmlCollaboration.addAttribute(myUmlAttribute);
        }
    }

    //解析生命线
    private void parseLifeLine(UmlLifeline umlLifeline) {
        MyUmlLifeLine myUmlLifeLine = new MyUmlLifeLine(umlLifeline);
        myUmlLifeLineMap.put(umlLifeline.getId(), myUmlLifeLine);
        if (myUmlInteractionMap.containsKey(umlLifeline.getParentId())) {
            MyUmlInteraction myUmlInteraction = myUmlInteractionMap.get(umlLifeline.getParentId());
            myUmlInteraction.addParticipant(myUmlLifeLine);
        }
    }

    //解析消息
    private void parseMessage(UmlMessage umlMessage) {
        MyUmlMessage myUmlMessage = new MyUmlMessage(umlMessage);
        if (myUmlLifeLineMap.containsKey(myUmlMessage.getUmlMessage().getSource())) {
            MyUmlLifeLine source = myUmlLifeLineMap.get(myUmlMessage.getUmlMessage().getSource());
            source.addSentMessage(myUmlMessage);
        }
        if (myUmlLifeLineMap.containsKey(myUmlMessage.getUmlMessage().getTarget())) {
            MyUmlLifeLine target = myUmlLifeLineMap.get(myUmlMessage.getUmlMessage().getTarget());
            target.addIncomingMessage(myUmlMessage);
        }
    }

    //R007：lifeline必须表示在同一sequence diagram中定义的attribute
    public void checkForUml007() throws UmlRule007Exception {
        for (MyUmlLifeLine myUmlLifeLine : myUmlLifeLineMap.values()) {
            if (myUmlInteractionMap.containsKey(myUmlLifeLine.getUmlLifeline().getParentId())) {
                MyUmlInteraction myUmlInteraction =
                        myUmlInteractionMap.get(myUmlLifeLine.getUmlLifeline().getParentId());
                if (collaborationIdAttrMap.
                        containsKey(myUmlInteraction.getUmlInteraction().getParentId())) {
                    HashMap<String, MyUmlAttribute> hashMap = collaborationIdAttrMap.
                            get(myUmlInteraction.getUmlInteraction().getParentId());
                    if (!hashMap.containsKey(myUmlLifeLine.getUmlLifeline().getRepresent())) {
                        throw new UmlRule007Exception();
                    }
                } else {
                    //如果上层一个属性也没有直接抛异常
                    throw new UmlRule007Exception();
                }
            } else {
                //lifeline 不存在 interaction
                throw new UmlRule007Exception();
            }
        }
    }

    //检查Interaction
    private MyUmlInteraction getInteraction(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!nameInteractionMap.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else {
            ArrayList<MyUmlInteraction> arrayList = nameInteractionMap.get(interactionName);
            if (arrayList.size() > 1) {
                throw new InteractionDuplicatedException(interactionName);
            } else {
                return arrayList.get(0);
            }
        }
    }

    //PTCP_OBJ_COUNT umlinteraction_name
    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyUmlInteraction myUmlInteraction = getInteraction(interactionName);
        return myUmlInteraction.getParticipantCount();
    }

    //INCOMING_MSG_COUNT umlinteraction_name lifeline_name
    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyUmlInteraction myUmlInteraction = getInteraction(interactionName);
        return myUmlInteraction.getIncomingMessageCount(interactionName, lifelineName);
    }

    //SENT_MESSAGE_COUNT umlinteraction_name lifeline_name messagesort
    @Override
    public int getSentMessageCount(String interactionName, String lifelineName, MessageSort sort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyUmlInteraction myUmlInteraction = getInteraction(interactionName);
        return myUmlInteraction.getSentMessageCount(interactionName, lifelineName, sort);
    }
}
