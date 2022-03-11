import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashSet;

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

    //R002：不能有循环继承(UML008)
    public boolean checkForUml002() {
        ArrayList<MyUmlInterface> arrayList = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>();
        arrayList.addAll(extendInterfaceList);
        while (!arrayList.isEmpty()) {
            MyUmlInterface myUmlInterface = arrayList.remove(0);
            if (myUmlInterface.getUmlInterface().getId().equals(umlInterface.getId())) {
                return false;
            } else if (!hashSet.contains(myUmlInterface.getUmlInterface().getId())) {
                hashSet.add(myUmlInterface.getUmlInterface().getId());
                arrayList.addAll(myUmlInterface.getExtendInterfaceList());
            }
        }
        return true;
    }

    //R003：任何一个类或接口不能重复继承另外一个类或接口(UML007)
    public boolean checkForUml003() {
        ArrayList<MyUmlInterface> arrayList = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>();
        arrayList.addAll(extendInterfaceList);
        while (!arrayList.isEmpty()) {
            MyUmlInterface myUmlInterface = arrayList.remove(0);
            if (hashSet.contains(myUmlInterface.getUmlInterface().getId())) {
                return false;
            } else {
                hashSet.add(myUmlInterface.getUmlInterface().getId());
                arrayList.addAll(myUmlInterface.getExtendInterfaceList());
            }
        }
        return true;
    }

    //R005: 类图元素名字不能为空(UML 001)
    public boolean checkForUml005() {
        if (umlInterface.getName() == null) {
            return false;
        }
        for (MyUmlAttribute myUmlAttribute : getMyUmlAttributeList()) {
            if (myUmlAttribute.getUmlAttribute().getName() == null) {
                return false;
            }
        }
        for (MyUmlOperation myUmlOperation : getMyUmlOperationList()) {
            if (!myUmlOperation.checkForUml005()) {
                return false;
            }
        }
        return true;
    }

    //R006: 接口的所有属性均需要为public(UML 011)
    public boolean checkForUml006() {
        for (MyUmlAttribute myUmlAttribute : getMyUmlAttributeList()) {
            if (myUmlAttribute.getUmlAttribute().getVisibility() != Visibility.PUBLIC) {
                return false;
            }
        }
        return true;
    }
}
