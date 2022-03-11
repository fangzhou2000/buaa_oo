import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyUmlCollaborationInteraction implements UmlCollaborationInteraction {
    private HashMap<String, ArrayList<MyUmlInteraction>> nameInteractionMap;
    private HashMap<String, MyUmlInteraction> myUmlInteractionMap;
    private HashMap<String, MyUmlLifeLine> myUmlLifeLineMap;

    public MyUmlCollaborationInteraction(UmlElement[] elements) {
        this.nameInteractionMap = new HashMap<>();
        this.myUmlInteractionMap = new HashMap<>();
        this.myUmlLifeLineMap = new HashMap<>();

        ArrayList<UmlElement> elements1 = new ArrayList<>();
        for (UmlElement e : elements) {
            if (e.getElementType() == ElementType.UML_INTERACTION) {
                parseInteraction((UmlInteraction) e);
            } else if (e.getElementType() == ElementType.UML_LIFELINE) {
                elements1.add(e);
            } else if (e.getElementType() == ElementType.UML_MESSAGE) {
                elements1.add(e);
            }
        }

        ArrayList<UmlElement> elements2 = new ArrayList<>();
        for (UmlElement e : elements1) {
            if (e.getElementType() == ElementType.UML_LIFELINE) {
                parseLifeLine((UmlLifeline) e);
            } else if (e.getElementType() == ElementType.UML_MESSAGE) {
                elements2.add(e);
            }
        }

        for (UmlElement e : elements2) {
            if (e.getElementType() == ElementType.UML_MESSAGE) {
                parseMessage((UmlMessage) e);
            }
        }

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
