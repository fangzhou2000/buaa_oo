import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.models.elements.UmlStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyUmlStateMachine {
    private UmlStateMachine umlStateMachine;
    private HashMap<String, MyUmlAllState> myUmlAllStateMap;
    private int stateCount;
    private HashMap<String, ArrayList<MyUmlAllState>> nameAllStateMap;
    private HashMap<String, MyUmlTransition> myUmlTransitionMap;
    //缓存
    private HashMap<String, HashMap<String, List<String>>> eventListMap = new HashMap<>();

    public MyUmlStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
        this.myUmlAllStateMap = new HashMap<>();
        this.stateCount = 0;
        this.nameAllStateMap = new HashMap<>();
        this.myUmlTransitionMap = new HashMap<>();
    }

    public UmlStateMachine getUmlStateMachine() {
        return umlStateMachine;
    }

    //添加状态
    public void addState(MyUmlAllState myUmlAllState) {
        stateCount++;
        myUmlAllStateMap.put(myUmlAllState.getUmlElement().getId(), myUmlAllState);
        String name = myUmlAllState.getUmlElement().getName();
        if (nameAllStateMap.containsKey(name)) {
            nameAllStateMap.get(name).add(myUmlAllState);
        } else {
            ArrayList<MyUmlAllState> arrayList = new ArrayList<>();
            arrayList.add(myUmlAllState);
            nameAllStateMap.put(name, arrayList);
        }
    }

    //添加转移
    public void addTransition(MyUmlTransition myUmlTransition) {
        myUmlTransitionMap.put(myUmlTransition.getUmlTransition().getId(), myUmlTransition);
        HashMap<String, List<String>> hashMap;
        if (eventListMap.containsKey(myUmlTransition.getUmlTransition().getSource())) {
            hashMap = eventListMap.get(myUmlTransition.getUmlTransition().getSource());
            if (!hashMap.containsKey(myUmlTransition.getUmlTransition().getTarget())) {
                List<String> list = new ArrayList<>();
                hashMap.put(myUmlTransition.getUmlTransition().getTarget(), list);
            }
        } else {
            hashMap = new HashMap<>();
            List<String> list = new ArrayList<>();
            hashMap.put(myUmlTransition.getUmlTransition().getTarget(), list);
            eventListMap.put(myUmlTransition.getUmlTransition().getSource(), hashMap);
        }
    }

    //添加事件
    public void addEvent(MyUmlEvent myUmlEvent) {
        if (myUmlTransitionMap.containsKey(myUmlEvent.getUmlEvent().getParentId())) {
            MyUmlTransition myUmlTransition =
                    myUmlTransitionMap.get(myUmlEvent.getUmlEvent().getParentId());
            if (myUmlAllStateMap.containsKey(myUmlTransition.getUmlTransition().getSource()) &&
                    myUmlAllStateMap.containsKey(myUmlTransition.getUmlTransition().getTarget())) {
                MyUmlAllState source =
                        myUmlAllStateMap.get(myUmlTransition.getUmlTransition().getSource());
                MyUmlAllState target =
                        myUmlAllStateMap.get(myUmlTransition.getUmlTransition().getTarget());
                eventListMap.get(source.getUmlElement().getId())
                        .get(target.getUmlElement().getId())
                        .add(myUmlEvent.getUmlEvent().getName());
            }
        }

    }

    /*给定状态机模型中一共有多少个状态
    输入指令格式：STATE_COUNT statemachine_name
    举例：STATE_COUNT complex_sm
    输出：
    Ok，state count of statemachine "complex_sm" is x. x为应状态机模型complex_sm的状态总数.
            Failed, statemachine "complex_sm" not found. 未找到状态机模型complex_sm
    Failed, duplicated statemachine "complex_sm". 存在多个状态机模型complex_sm
    说明：
    Initial State 和 Final State均算作状态。
     */
    public int getStateCount() {
        return stateCount;
    }

    //检查状态
    private MyUmlAllState getState(String stateMachineName, String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        if (!nameAllStateMap.containsKey(stateName)) {
            throw new StateNotFoundException(stateMachineName, stateName);
        } else {
            ArrayList<MyUmlAllState> arrayList = nameAllStateMap.get(stateName);
            if (arrayList.size() > 1) {
                throw new StateDuplicatedException(stateMachineName, stateName);
            } else {
                return arrayList.get(0);
            }
        }
    }

    //SUBSEQUENT_STATE_COUNT statemachine_name statename
    public int getSubsequenceStateCount(String stateMachineName, String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        MyUmlAllState myUmlAllState = getState(stateMachineName, stateName);
        return myUmlAllState.getSubsequenceStateCount();
    }

    /*给定状态机模型中和其中两个状态，引起状态迁移的所有触发事件
    输入指令格式：TRANSITION_TRIGGER statemachine_name statename1 statename2
    举例：TRANSITION_TRIGGER door_sm open close
    输出：
    Ok，triggers of transition from state "open" to state "close" in statemachine "door_sm" are (A, B, C). A、B、C为应状态机模型door_sm中引起状态open迁移到状态close的触发事件，其中
    传出列表时可以乱序，官方接口会自动进行排序（但是需要编写者自行保证不重不漏）
    保证所有的触发事件名称都不相同，且不为空
    Failed, statemachine "door_sm" not found. 未找到状态机模型door_sm
    Failed, duplicated statemachine "door_sm". 存在多个状态机模型door_sm
    Failed, state "open" in statemachine "door_sm" not found.在状态机模型door_sm中未找到状态open
    Failed, duplicated state "open" in statemachine "door_sm".在状态机模型door_sm中存在多个open状态
    Failed, transition from state "open" to state "close" in statemachine "door_sm" not found.在状态机模型door_sm中未找到从状态open到状态close的迁移
    说明：
    该询问考虑的迁移为状态间的直接迁移。
    检测状态与迁移异常时，先检测起点状态是否存在异常，再检测终点状态是否存在异常，最后检查是否存在相应的迁移。
    若存在相应迁移，但无触发事件，应输出空列表。
    */
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        MyUmlAllState source = getState(stateMachineName, sourceStateName);
        MyUmlAllState target = getState(stateMachineName, targetStateName);
        if (eventListMap.containsKey(source.getUmlElement().getId())) {
            HashMap<String, List<String>> hashMap =
                    eventListMap.get(source.getUmlElement().getId());
            if (hashMap.containsKey(target.getUmlElement().getId())) {
                return hashMap.get(target.getUmlElement().getId());
            } else {
                throw new TransitionNotFoundException(
                        stateMachineName, sourceStateName, targetStateName);
            }
        } else {
            throw new TransitionNotFoundException(
                    stateMachineName, sourceStateName, targetStateName);
        }
    }
}
