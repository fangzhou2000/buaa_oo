import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyUmlStateChartInteraction implements UmlStateChartInteraction {
    private HashMap<String, ArrayList<MyUmlStateMachine>> nameStateMachineMap;
    private HashMap<String, MyUmlStateMachine> myUmlStateMachineMap;
    private HashMap<String, MyUmlRegion> myUmlRegionMap;
    private HashMap<String, MyUmlAllState> myUmlAllStateMap;
    private HashMap<String, MyUmlTransition> myUmlTransitionMap;

    public MyUmlStateChartInteraction(UmlElement[] elements) {
        this.nameStateMachineMap = new HashMap<>();
        this.myUmlStateMachineMap = new HashMap<>();
        this.myUmlRegionMap = new HashMap<>();
        this.myUmlAllStateMap = new HashMap<>();
        this.myUmlTransitionMap = new HashMap<>();
        ArrayList<UmlElement> elements1 = new ArrayList<>();
        for (UmlElement e : elements) {
            if (e.getElementType() == ElementType.UML_STATE_MACHINE) {
                parseStateMachine((UmlStateMachine) e);
            } else if (e.getElementType() == ElementType.UML_STATE ||
                    e.getElementType() == ElementType.UML_PSEUDOSTATE ||
                    e.getElementType() == ElementType.UML_FINAL_STATE) {
                elements1.add(e);
            } else if (e.getElementType() == ElementType.UML_TRANSITION ||
                    e.getElementType() == ElementType.UML_EVENT) {
                elements1.add(e);
            } else if (e.getElementType() == ElementType.UML_REGION) {
                elements1.add(e);
            }
        }
        ArrayList<UmlElement> elements2 = new ArrayList<>();
        for (UmlElement e : elements1) {
            if (e.getElementType() == ElementType.UML_REGION) {
                parseRegion((UmlRegion) e);
            } else if (e.getElementType() == ElementType.UML_STATE ||
                    e.getElementType() == ElementType.UML_PSEUDOSTATE ||
                    e.getElementType() == ElementType.UML_FINAL_STATE) {
                elements2.add(e);
            } else if (e.getElementType() == ElementType.UML_TRANSITION ||
                    e.getElementType() == ElementType.UML_EVENT) {
                elements2.add(e);
            }
        }
        ArrayList<UmlElement> elements3 = new ArrayList<>();
        for (UmlElement e : elements2) {
            if (e.getElementType() == ElementType.UML_STATE ||
                    e.getElementType() == ElementType.UML_PSEUDOSTATE ||
                    e.getElementType() == ElementType.UML_FINAL_STATE) {
                parseAllState(e);
            } else if (e.getElementType() == ElementType.UML_TRANSITION ||
                    e.getElementType() == ElementType.UML_EVENT) {
                elements3.add(e);
            }
        }
        ArrayList<UmlElement> elements4 = new ArrayList<>();
        for (UmlElement e : elements3) {
            if (e.getElementType() == ElementType.UML_TRANSITION) {
                parseTransition((UmlTransition) e);
            } else if (e.getElementType() == ElementType.UML_EVENT) {
                elements4.add(e);
            }
        }
        for (UmlElement e : elements4) {
            if (e.getElementType() == ElementType.UML_EVENT) {
                parseEvent((UmlEvent) e);
            }
        }
    }

    //解析状态机
    private void parseStateMachine(UmlStateMachine umlStateMachine) {
        MyUmlStateMachine myUmlStateMachine = new MyUmlStateMachine(umlStateMachine);
        myUmlStateMachineMap.put(umlStateMachine.getId(), myUmlStateMachine);
        if (nameStateMachineMap.containsKey(umlStateMachine.getName())) {
            nameStateMachineMap.get(umlStateMachine.getName()).add(myUmlStateMachine);
        } else {
            ArrayList<MyUmlStateMachine> arrayList = new ArrayList<>();
            arrayList.add(myUmlStateMachine);
            nameStateMachineMap.put(umlStateMachine.getName(), arrayList);
        }
    }

    //解析Region
    private void parseRegion(UmlRegion umlRegion) {
        MyUmlRegion myUmlRegion = new MyUmlRegion(umlRegion);
        myUmlRegionMap.put(umlRegion.getId(), myUmlRegion);
    }

    //解析状态
    private void parseAllState(UmlElement umlElement) {
        MyUmlAllState myUmlAllState = new MyUmlAllState(umlElement);
        myUmlAllStateMap.put(umlElement.getId(), myUmlAllState);
        if (myUmlRegionMap.containsKey(umlElement.getParentId())) {
            MyUmlRegion myUmlRegion = myUmlRegionMap.get(umlElement.getParentId());
            if (myUmlStateMachineMap.containsKey(myUmlRegion.getUmlRegion().getParentId())) {
                MyUmlStateMachine myUmlStateMachine =
                        myUmlStateMachineMap.get(myUmlRegion.getUmlRegion().getParentId());
                myUmlStateMachine.addState(myUmlAllState);
            }
        }
    }

    //解析转移
    private void parseTransition(UmlTransition umlTransition) {
        MyUmlTransition myUmlTransition = new MyUmlTransition(umlTransition);
        myUmlTransitionMap.put(umlTransition.getId(), myUmlTransition);
        if (myUmlAllStateMap.containsKey(umlTransition.getSource()) &&
                myUmlAllStateMap.containsKey(umlTransition.getTarget())) {
            MyUmlAllState source = myUmlAllStateMap.get(umlTransition.getSource());
            MyUmlAllState target = myUmlAllStateMap.get(umlTransition.getTarget());
            source.addSubsequenceState(target);
        }
        if (myUmlRegionMap.containsKey(umlTransition.getParentId())) {
            MyUmlRegion myUmlRegion = myUmlRegionMap.get(umlTransition.getParentId());
            if (myUmlStateMachineMap.containsKey(myUmlRegion.getUmlRegion().getParentId())) {
                MyUmlStateMachine myUmlStateMachine =
                        myUmlStateMachineMap.get(myUmlRegion.getUmlRegion().getParentId());
                myUmlStateMachine.addTransition(myUmlTransition);
            }
        }
    }

    //解析事件
    private void parseEvent(UmlEvent umlEvent) {
        MyUmlEvent myUmlEvent = new MyUmlEvent(umlEvent);
        if (myUmlTransitionMap.containsKey(umlEvent.getParentId())) {
            MyUmlTransition myUmlTransition = myUmlTransitionMap.get(umlEvent.getParentId());
            if (myUmlRegionMap.containsKey(myUmlTransition.getUmlTransition().getParentId())) {
                MyUmlRegion myUmlRegion =
                        myUmlRegionMap.get(myUmlTransition.getUmlTransition().getParentId());
                if (myUmlStateMachineMap.containsKey(myUmlRegion.getUmlRegion().getParentId())) {
                    MyUmlStateMachine myUmlStateMachine =
                            myUmlStateMachineMap.get(myUmlRegion.getUmlRegion().getParentId());
                    myUmlStateMachine.addEvent(myUmlEvent);
                }
            }
        }
    }

    //状态机检查
    private MyUmlStateMachine getStateMachine(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!nameStateMachineMap.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else {
            ArrayList<MyUmlStateMachine> arrayList =
                    nameStateMachineMap.get(stateMachineName);
            if (arrayList.size() > 1) {
                throw new StateMachineDuplicatedException(stateMachineName);
            } else {
                return arrayList.get(0);
            }
        }
    }

    //STATE_COUNT statemachine_name
    @Override
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyUmlStateMachine myUmlStateMachine = getStateMachine(stateMachineName);
        return myUmlStateMachine.getStateCount();
    }

    //SUBSEQUENT_STATE_COUNT statemachine_name statename
    @Override
    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MyUmlStateMachine myUmlStateMachine = getStateMachine(stateMachineName);
        return myUmlStateMachine.getSubsequenceStateCount(stateMachineName, stateName);
    }

    //TRANSITION_TRIGGER statemachine_name statename1 statename2
    @Override
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        MyUmlStateMachine myUmlStateMachine = getStateMachine(stateMachineName);
        return myUmlStateMachine.getTransitionTrigger(
                stateMachineName, sourceStateName, targetStateName);
    }
}
