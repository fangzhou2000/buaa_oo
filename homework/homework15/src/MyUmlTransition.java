import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.HashMap;

public class MyUmlTransition {
    private UmlTransition umlTransition;
    private HashMap<String, MyUmlEvent> myUmlEventMap;

    public MyUmlTransition(UmlTransition umlTransition) {
        this.umlTransition = umlTransition;
        this.myUmlEventMap = new HashMap<>();
    }

    public UmlTransition getUmlTransition() {
        return umlTransition;
    }

    public void addEvent(MyUmlEvent myUmlEvent) {
        this.myUmlEventMap.put(myUmlEvent.getUmlEvent().getId(), myUmlEvent);
    }

    public HashMap<String, MyUmlEvent> getMyUmlEventMap() {
        return myUmlEventMap;
    }
}
