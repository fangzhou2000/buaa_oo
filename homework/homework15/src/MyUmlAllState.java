import com.oocourse.uml3.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashSet;

public class MyUmlAllState {
    private UmlElement umlElement;
    private ArrayList<MyUmlAllState> subsequenceStateList;
    private ArrayList<MyUmlTransition> myUmlOutTransitionList; //迁出
    //缓存
    private int subsequenceStateCount = -1;

    public MyUmlAllState(UmlElement umlElement) {
        this.umlElement = umlElement;
        this.subsequenceStateList = new ArrayList<>();
        this.myUmlOutTransitionList = new ArrayList<>();
    }

    public UmlElement getUmlElement() {
        return umlElement;
    }

    public void addOutTransition(MyUmlTransition myUmlTransition) {
        myUmlOutTransitionList.add(myUmlTransition);
    }

    public ArrayList<MyUmlTransition> getMyUmlOutTransitionList() {
        return myUmlOutTransitionList;
    }

    public void addSubsequenceState(MyUmlAllState myUmlAllState) {
        subsequenceStateList.add(myUmlAllState);
    }

    public ArrayList<MyUmlAllState> getSubsequenceStateList() {
        return subsequenceStateList;
    }

    /*给定状态机模型和其中的一个状态，有多少个不同的可达状态
    输入指令格式：SUBSEQUENT_STATE_COUNT statemachine_name statename
    举例：SUBSEQUENT_STATE_COUNT complex_sm openned
    输出：
    Ok, subsequent state count from state "openned" in statemachine "complex_sm"
    is x. x为状态机模型complex_sm中从openned状态可达的不同状态个数
    Failed, statemachine "complex_sm" not found. 未找到状态机模型complex_sm
    Failed, duplicated statemachine "complex_sm". 存在多个状态机模型complex_sm
    Failed, state "openned" in statemachine "complex_sm" not found.在状态机模型complex_sm中未找到状态openned
    Failed, duplicated state "openned" in statemachine "complex_sm".在状态机模型complex_sm中存在多个openned状态
    说明：
    Initial State 和 Final State均算作状态。
    可达状态不仅包括直接可达状态，也包括间接可达状态。
     */
    public int getSubsequenceStateCount() {
        if (subsequenceStateCount == -1) {
            HashSet<MyUmlAllState> hashSet = new HashSet<>();
            ArrayList<MyUmlAllState> arrayList = new ArrayList<>();
            arrayList.addAll(subsequenceStateList);
            while (!arrayList.isEmpty()) {
                MyUmlAllState ms = arrayList.remove(0);
                if (!hashSet.contains(ms)) {
                    hashSet.add(ms);
                    arrayList.addAll(ms.getSubsequenceStateList());
                }
            }
            subsequenceStateCount = hashSet.size();
        }
        return subsequenceStateCount;
    }
}
