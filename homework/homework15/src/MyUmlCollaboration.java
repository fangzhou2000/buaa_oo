import com.oocourse.uml3.models.elements.UmlCollaboration;

import java.util.HashMap;

public class MyUmlCollaboration {
    private UmlCollaboration umlCollaboration;
    private HashMap<String, MyUmlAttribute> myUmlAttributeMap;

    public MyUmlCollaboration(UmlCollaboration umlCollaboration) {
        this.umlCollaboration = umlCollaboration;
        this.myUmlAttributeMap = new HashMap<>();
    }

    public void addAttribute(MyUmlAttribute myUmlAttribute) {
        this.myUmlAttributeMap.put(myUmlAttribute.getUmlAttribute().getId(), myUmlAttribute);
    }

    public HashMap<String, MyUmlAttribute> getMyUmlAttributeMap() {
        return myUmlAttributeMap;
    }
}
