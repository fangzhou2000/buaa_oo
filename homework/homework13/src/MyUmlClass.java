import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyUmlClass extends MyClass {
    private UmlClass umlClass;                                      //UmlClass
    private MyUmlClass extendClass;                                 //UmlGeneralization
    private ArrayList<MyClass> associationList;
    private ArrayList<MyUmlClass> associationClassList;             //UmlAssociation
    private ArrayList<MyUmlInterface> associationInterfaceList;
    private ArrayList<MyUmlInterface> implementInterfaceList;       //UmlInterfaceRealization

    public MyUmlClass(UmlClass umlClass) {
        super();
        this.umlClass = umlClass;
        this.extendClass = null;
        this.associationList = new ArrayList<>();
        this.associationClassList = new ArrayList<>();
        this.associationInterfaceList = new ArrayList<>();
        this.implementInterfaceList = new ArrayList<>();
    }

    public MyUmlClass getExtendClass() {
        return extendClass;
    }

    public ArrayList<MyUmlClass> getAssociationClassList() {
        return associationClassList;
    }

    public ArrayList<MyUmlInterface> getImplementInterfaceList() {
        return implementInterfaceList;
    }

    public UmlClass getUmlClass() {
        return umlClass;
    }

    public String getClassName() {
        return umlClass.getName();
    }

    public void setExtendClass(MyUmlClass myUmlClass) {
        extendClass = myUmlClass;
    }

    public void addImplementInterface(MyUmlInterface myUmlInterface) {
        implementInterfaceList.add(myUmlInterface);
    }

    public void addAssociation(MyClass myClass) {
        associationList.add(myClass);
        if (myClass instanceof MyUmlClass) {
            associationClassList.add((MyUmlClass) myClass);
        } else if (myClass instanceof MyUmlInterface) {
            associationInterfaceList.add((MyUmlInterface) myClass);
        }
    }

    /*???????????????????????????
    ?????????????????????CLASS_ATTR_COUNT classname
    ?????????CLASS_ATTR_COUNT Elevator
    ?????????
    Ok, attribute count of class "classname" is x. x????????????????????????
    Failed, class "classname" not found. ????????????
    Failed, duplicated class "classname". ???????????????
    ?????????
    ???????????????????????????????????????????????????????????????????????????????????????????????????*/
    public int getAttributeCount() {
        int attributeCount = getMyUmlAttributes().size();
        MyUmlClass myUmlClass = extendClass;
        while (myUmlClass != null) {
            attributeCount += myUmlClass.getMyUmlAttributes().size();
            myUmlClass = myUmlClass.getExtendClass();
        }
        return attributeCount;
    }

    /*?????????????????????
    ?????????????????????CLASS_ATTR_VISIBILITY classname attrname
    ?????????CLASS_ATTR_VISIBILITY Taxi id
    ?????????
    Ok, attribute "attrname" in class "classname"'s
    visibility is public/protected/private/package-private. ???????????????????????????
    Failed, class "classname" not found. ????????????
    Failed, duplicated class "classname". ???????????????
    Failed, attribute "attrname" not found in class "classname". ?????????????????????
    Failed, duplicated attribute "attrname" in class "classname". ??????????????????????????????
    ?????????
    ?????????????????????????????????????????????????????????
    ????????????????????????????????????????????????????????????????????????duplicated attribute??????*/
    public Visibility getAttributeVisibility(String className, String attributeName) throws
            AttributeNotFoundException, AttributeDuplicatedException {
        ArrayList<MyUmlAttribute> arrayList = new ArrayList<>();
        arrayList.addAll(getMyUmlAttributes());
        MyUmlClass myUmlClass = extendClass;
        while (myUmlClass != null) {
            arrayList.addAll(myUmlClass.getMyUmlAttributes());
            myUmlClass = myUmlClass.getExtendClass();
        }
        int flag = 0;
        Visibility visibility = null;
        for (MyUmlAttribute myUmlAttribute : arrayList) {
            if (myUmlAttribute.getUmlAttribute().getName().equals(attributeName)) {
                flag++;
                if (flag > 1) {
                    throw new AttributeDuplicatedException(className, attributeName);
                }
                visibility = myUmlAttribute.getUmlAttribute().getVisibility();
            }
        }
        if (flag == 0) {
            throw new AttributeNotFoundException(className, attributeName);
        }
        return visibility;
    }

    public boolean checkParameterType(String string) {
        if (!string.equals("byte") && !string.equals("short")
                && !string.equals("int") && !string.equals("long")) {
            if (!string.equals("float") && !string.equals("double")
                    && !string.equals("char") && !string.equals("boolean")
                    && !string.equals("String")) {
                return false;
            }
        }
        return true;
    }

    /*??????????????????
    ?????????????????????CLASS_ATTR_TYPE classname attrname
    ?????????CLASS_ATTR_TYPE Taxi id
    ?????????
    Ok, the type of attribute "attrname" in class "classname" is typeA. ??????????????????
    Failed, class "classname" not found. ????????????
    Failed, duplicated class "classname". ???????????????
    Failed, attribute "attrname" not found in class "classname". ?????????????????????
    Failed, duplicated attribute "attrname" in class "classname". ??????????????????????????????
    Failed, wrong type of attribute "attrname" in class "classname". ??????????????????
    ?????????
    ??????type????????????????????????????????????ReferenceType???NamedType???????????????????????????
    NamedType?????????????????????????????? JAVA ????????????????????????(byte, short, int, long, float,
    double, char, boolean)???String????????????????????????????????????
    ReferenceType???????????????????????????????????????????????????????????????????????????????????????????????????
    ?????????????????????????????????????????????????????????
    ????????????????????????????????????????????????????????????????????????duplicated attribute?????????
    ?????????????????????????????????????????????*/
    public String getAttributeType(String className, String attributeName,
                                   HashMap<String, MyClass> myClassMap) throws
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        ArrayList<MyUmlAttribute> arrayList = new ArrayList<>();
        arrayList.addAll(getMyUmlAttributes());
        MyUmlClass myUmlClass = extendClass;
        while (myUmlClass != null) {
            arrayList.addAll(myUmlClass.getMyUmlAttributes());
            myUmlClass = myUmlClass.getExtendClass();
        }
        int flag = 0;
        NameableType type = null;
        for (MyUmlAttribute myUmlAttribute : arrayList) {
            if (myUmlAttribute.getUmlAttribute().getName().equals(attributeName)) {
                flag++;
                if (flag > 1) {
                    throw new AttributeDuplicatedException(className, attributeName);
                }
                type = myUmlAttribute.getUmlAttribute().getType();
            }
        }
        if (flag == 0) {
            throw new AttributeNotFoundException(className, attributeName);
        }
        if (type instanceof NamedType) {
            String name = ((NamedType) type).getName();
            if (!checkParameterType(name)) {
                throw new AttributeWrongTypeException(className, attributeName);
            } else {
                return name;
            }
        } else if (type instanceof ReferenceType) {
            String id = ((ReferenceType) type).getReferenceId();
            if (!myClassMap.containsKey(id)) {
                throw new AttributeWrongTypeException(className, attributeName);
            } else {
                MyClass myClass = myClassMap.get(id);
                if (myClass instanceof MyUmlClass) {
                    return ((MyUmlClass) myClass).getClassName();
                } else if (myClass instanceof MyUmlInterface) {
                    return ((MyUmlInterface) myClass).getClassName();
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    /*?????????????????????????????????
    ?????????????????????CLASS_ASSO_CLASS_LIST classname
    ?????????CLASS_ASSO_CLASS_LIST Elevator
    ?????????
    Ok, associated classes of class "classname" are (A, B, C). A???B???C?????????????????????????????????????????????
    ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    ???????????????????????????id??????????????????????????????????????????????????????????????????????????????????????????????????????
    ??????????????????????????????????????????????????????????????????
    Failed, class "classname" not found. ????????????
    Failed, duplicated class "classname". ???????????????
    ?????????
    Association????????????????????????????????????????????????XX????????????YY????????????YY???Association?????????????????????XX???
    ?????????????????????????????????????????????????????????*/
    public List<String> getAssociationClassNameList(String className) {
        //HashSet????????????????????????
        HashSet<MyUmlClass> hashSet = new HashSet<>();
        hashSet.addAll(associationClassList);
        MyUmlClass myUmlClass = extendClass;
        while (myUmlClass != null) {
            hashSet.addAll(myUmlClass.getAssociationClassList());
            myUmlClass = myUmlClass.getExtendClass();
        }
        List<String> associationClassList = new ArrayList<>();
        for (MyUmlClass muc : hashSet) {
            associationClassList.add(muc.getClassName());
        }
        return associationClassList;
    }

    /*??????????????????
    ?????????????????????CLASS_TOP_BASE classname
    ?????????CLASS_TOP_BASE AdvancedTaxi
    ?????????
    Ok, top base class of class "classname" is top_classname. top_classname???????????????
    Failed, class "classname" not found. ????????????
    Failed, duplicated class "classname". ???????????????
    ?????????
    ????????????????????????XX?????????YY????????????????????????????????????
    XX???YY?????????????????????????????????XX??????XX????????????
    ????????????ZZ?????????YY???ZZ?????????*/
    public String getTopParentClassName() {
        MyUmlClass myUmlClass = this;
        while (myUmlClass.getExtendClass() != null) {
            myUmlClass = myUmlClass.getExtendClass();
        }
        return myUmlClass.getClassName();
    }

    /*????????????????????????
    ?????????????????????CLASS_IMPLEMENT_INTERFACE_LIST classname
    ?????????CLASS_IMPLEMENT_INTERFACE_LIST Taxi
    ?????????
    Ok, implement interfaces of class "classname" are (A, B, C). A???B???C????????????????????????
    ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    Failed, class "classname" not found. ????????????
    Failed, duplicated class "classname". ???????????????*/
    public List<String> getImplementInterfaceNameList() {
        HashSet<MyUmlInterface> hashSet = new HashSet<>();
        //???????????????????????????
        HashSet<MyUmlInterface> implementHashSet = new HashSet<>();
        implementHashSet.addAll(implementInterfaceList);
        MyUmlClass myUmlClass = extendClass;
        while (myUmlClass != null) {
            implementHashSet.addAll(myUmlClass.getImplementInterfaceList());
            myUmlClass = myUmlClass.getExtendClass();
        }
        //??????????????????????????????
        HashSet<MyUmlInterface> extendHashSet = new HashSet<>();
        //Set????????????????????????????????????remove???????????????????????????
        //ArrayList??????
        ArrayList<MyUmlInterface> arrayList = new ArrayList<>();
        arrayList.addAll(implementHashSet);
        while (!arrayList.isEmpty()) {
            MyUmlInterface myUmlInterface = arrayList.remove(0);
            //??????????????????????????????hashSet???????????????????????????????????????
            if (!hashSet.contains(myUmlInterface)) {
                hashSet.add(myUmlInterface);
                arrayList.addAll(myUmlInterface.getExtendInterfaceList());
            }
        }

        List<String> implementInterfaceNameList = new ArrayList<>();
        for (MyUmlInterface mui : hashSet) {
            implementInterfaceNameList.add(mui.getClassName());
        }
        return implementInterfaceNameList;
    }

    /*?????????????????????????????????
    ?????????????????????CLASS_INFO_HIDDEN classname
    ?????????CLASS_INFO_HIDDEN Taxi
    ?????????
    Yes, information of class "classname" is hidden. ???????????????????????????
    No, attribute xxx in xxx, xxx in xxx, are not hidden. ??????????????????????????????
    Failed, class "classname" not found. ????????????
    Failed, duplicated class "classname". ???????????????
    ?????????
    ????????????????????????????????????????????????????????????????????????private??????????????????????????????
    ?????????????????????????????????????????????????????????????????????????????????????????????????????????
    ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????*/
    public List<AttributeClassInformation> getInformationNotHidden() {
        List<AttributeClassInformation> attributeClassInformations = new ArrayList<>();
        for (MyUmlAttribute myUmlAttribute : getMyUmlAttributes()) {
            if (myUmlAttribute.getUmlAttribute().getVisibility() != Visibility.PRIVATE) {
                AttributeClassInformation attributeClassInformation =
                        new AttributeClassInformation(myUmlAttribute.getUmlAttribute().getName(),
                                getClassName());
                attributeClassInformations.add(attributeClassInformation);
            }
        }
        MyUmlClass myUmlClass = extendClass;
        while (myUmlClass != null) {
            for (MyUmlAttribute myUmlAttribute : myUmlClass.getMyUmlAttributes()) {
                if (myUmlAttribute.getUmlAttribute().getVisibility() != Visibility.PRIVATE) {
                    AttributeClassInformation attributeClassInformation =
                            new AttributeClassInformation(
                                    myUmlAttribute.getUmlAttribute().getName(),
                                    myUmlClass.getClassName());
                    attributeClassInformations.add(attributeClassInformation);
                }
            }
            myUmlClass = myUmlClass.getExtendClass();
        }
        return attributeClassInformations;
    }
}
