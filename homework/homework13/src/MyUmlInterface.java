import com.oocourse.uml1.models.elements.UmlInterface;

import java.util.ArrayList;

public class MyUmlInterface extends MyClass {
    private UmlInterface umlInterface;
    private ArrayList<MyUmlInterface> extendInterfaceList;
    private ArrayList<MyClass> associationList;
    private ArrayList<MyUmlInterface> associationInterfaceList;
    private ArrayList<MyUmlClass> associationClassList;

    public MyUmlInterface(UmlInterface umlInterface) {
        super();
        this.umlInterface = umlInterface;
        this.extendInterfaceList = new ArrayList<>();
        this.associationList = new ArrayList<>();
        this.associationInterfaceList = new ArrayList<>();
        this.associationClassList = new ArrayList<>();
    }

    public UmlInterface getUmlInterface() {
        return umlInterface;
    }

    public String getClassName() {
        return umlInterface.getName();
    }

    public ArrayList<MyUmlInterface> getExtendInterfaceList() {
        return extendInterfaceList;
    }

    public void addExtendInterfaces(MyUmlInterface myUmlInterface) {
        extendInterfaceList.add(myUmlInterface);
    }

    public void addAssociation(MyClass myClass) {
        associationList.add(myClass);
        if (myClass instanceof MyUmlClass) {
            associationClassList.add((MyUmlClass) myClass);
        } else if (myClass instanceof MyUmlInterface) {
            associationInterfaceList.add((MyUmlInterface) myClass);
        }
    }
}
