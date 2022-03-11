import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.OperationParamInformation;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private MyUmlClassModelInteraction myUmlClassModelInteraction;
    private MyUmlCollaborationInteraction myUmlCollaborationInteraction;
    private MyUmlStateChartInteraction myUmlStateChartInteraction;

    public MyUmlGeneralInteraction(UmlElement[] elements) {
        this.myUmlClassModelInteraction = new MyUmlClassModelInteraction(elements);
        this.myUmlCollaborationInteraction = new MyUmlCollaborationInteraction(elements);
        this.myUmlStateChartInteraction = new MyUmlStateChartInteraction(elements);
    }

    @Override
    public int getClassCount() {
        return myUmlClassModelInteraction.getClassCount();
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassOperationCount(className);
    }

    @Override
    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassAttributeCount(className);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassOperationVisibility(className, operationName);
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return myUmlClassModelInteraction.getClassAttributeVisibility(className, attributeName);
    }

    @Override
    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        return myUmlClassModelInteraction.getClassAttributeType(className, attributeName);
    }

    @Override
    public List<OperationParamInformation> getClassOperationParamType(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return myUmlClassModelInteraction.getClassOperationParamType(className, operationName);
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getClassAssociatedClassList(className);
    }

    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getTopParentClass(className);
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getImplementInterfaceList(className);
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myUmlClassModelInteraction.getInformationNotHidden(className);
    }

    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return myUmlCollaborationInteraction.getParticipantCount(interactionName);
    }

    @Override
    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return myUmlCollaborationInteraction.getIncomingMessageCount(
                interactionName, lifelineName);
    }

    @Override
    public int getSentMessageCount(String interactionName, String lifelineName, MessageSort sort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return myUmlCollaborationInteraction.getSentMessageCount(
                interactionName, lifelineName, sort);
    }

    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return myUmlStateChartInteraction.getStateCount(stateMachineName);
    }

    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return myUmlStateChartInteraction.getSubsequentStateCount(stateMachineName, stateName);
    }

    @Override
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return myUmlStateChartInteraction.getTransitionTrigger(
                stateMachineName, sourceStateName, targetStateName);
    }
}
