import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.OperationParamInformation;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.format.UmlClassModelInteraction;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
    ?????????????????????????????????Uml...?????????????????????????????????????????????????????????????????????????????????MyUml...??????????????????????????????????????????
    MyUmlInteraction????????????????????????????????????????????????????????????????????????????????????????????????
    ??????????????????(?????????My)
        Class
            attributions
            operations
            UmlClass
                extendClass
                associationList
                associationClassList
                associationInterfaceList
                implementedInterfaceList
            UmlInterface
                extendInterfaceList
                associationList
                associationClassList
                associationInterfaceList//??????association????????????Class???...

                UmlAttribute
                UmlOperation
                    UmlParameter
                UmlAssociationEnd
                    UmlAssociation

 */
public class MyUmlClassModelInteraction implements UmlClassModelInteraction {
    private int classCount;
    private HashMap<String, MyClass> myClassMap;                          // id, class or interface
    private HashMap<String, MyUmlInterface> myInterfaceMap;
    private HashMap<String, MyUmlClass> myUmlClassMap;
    private HashMap<String, MyUmlOperation> myUmlOperationMap;            // operationId, operation
    private HashMap<String, ArrayList<MyUmlClass>> nameClassMap;          // name, classList
    private HashMap<String, MyUmlAssociationEnd> myUmlAssociationEndMap;  // endId, end

    public MyUmlClassModelInteraction(UmlElement[] elements) {
        this.classCount = 0;
        this.myClassMap = new HashMap<>();
        this.myInterfaceMap = new HashMap<>();
        this.myUmlClassMap = new HashMap<>();
        this.myUmlOperationMap = new HashMap<>();
        this.nameClassMap = new HashMap<>();
        this.myUmlAssociationEndMap = new HashMap<>();

        //??????????????????
        ArrayList<UmlElement> elements1 = new ArrayList<>();
        for (UmlElement e : elements) {
            if (e.getElementType() == ElementType.UML_CLASS) {
                parseClass((UmlClass) e);
            } else if (e.getElementType() == ElementType.UML_INTERFACE) {
                parseInterface((UmlInterface) e);
            } else {
                elements1.add(e);
            }
        }

        //???????????????element
        ArrayList<UmlElement> elements2 = new ArrayList<>();
        for (UmlElement e : elements1) {
            if (e.getElementType() == ElementType.UML_ATTRIBUTE) {
                parseAttribute((UmlAttribute) e);
            } else if (e.getElementType() == ElementType.UML_OPERATION) {
                parseOperation((UmlOperation) e);
            } else if (e.getElementType() == ElementType.UML_ASSOCIATION_END) {
                parseAssociationEnd((UmlAssociationEnd) e);
            } else if (e.getElementType() == ElementType.UML_GENERALIZATION) {
                parseGeneralization((UmlGeneralization) e);
            } else if (e.getElementType() == ElementType.UML_INTERFACE_REALIZATION) {
                parseInterfaceRealization((UmlInterfaceRealization) e);
            } else {
                elements2.add(e);
            }
        }

        //?????????????????????????????????element
        for (UmlElement e : elements2) {
            if (e.getElementType() == ElementType.UML_PARAMETER) {
                parseParameter((UmlParameter) e);
            } else if (e.getElementType() == ElementType.UML_ASSOCIATION) {
                parseAssociation((UmlAssociation) e);
            }
        }
    }

    //??????????????????classCount?????????myClassMap???nameClassMap
    private void parseClass(UmlClass umlClass) {
        classCount++;
        MyUmlClass myUmlClass = new MyUmlClass(umlClass);
        myClassMap.put(umlClass.getId(), myUmlClass);
        myUmlClassMap.put(umlClass.getId(), myUmlClass);
        if (nameClassMap.containsKey(umlClass.getName())) {
            nameClassMap.get(umlClass.getName()).add(myUmlClass);
        } else {
            ArrayList<MyUmlClass> arrayList = new ArrayList<>();
            arrayList.add(myUmlClass);
            nameClassMap.put(umlClass.getName(), arrayList);
        }
    }

    //?????????????????????myClassMap
    private void parseInterface(UmlInterface umlInterface) {
        MyUmlInterface myUmlInterface = new MyUmlInterface(umlInterface);
        myClassMap.put(umlInterface.getId(), myUmlInterface);
        myInterfaceMap.put(umlInterface.getId(), myUmlInterface);

    }

    //???????????????????????????Class??????attributes
    private void parseAttribute(UmlAttribute umlAttribute) {
        MyUmlAttribute myUmlAttribute = new MyUmlAttribute(umlAttribute);
        if (myClassMap.containsKey(umlAttribute.getParentId())) {
            MyClass myClass = myClassMap.get(umlAttribute.getParentId());
            myClass.addAttribute(myUmlAttribute);
            if (myClass instanceof MyUmlClass) {
                ((MyUmlClass) myClass).addAttrAssoEnd(umlAttribute);
            }
        }
    }

    //???????????????????????????Class??????operations??????????????????operationMap
    private void parseOperation(UmlOperation umlOperation) {
        MyUmlOperation myUmlOperation = new MyUmlOperation(umlOperation);
        myUmlOperationMap.put(umlOperation.getId(), myUmlOperation);
        if (myClassMap.containsKey(umlOperation.getParentId())) {
            MyClass myClass = myClassMap.get(umlOperation.getParentId());
            myClass.addOperation(myUmlOperation);
        }
    }

    //????????????????????????????????????associationEndMap
    private void parseAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        MyUmlAssociationEnd myUmlAssociationEnd = new MyUmlAssociationEnd(umlAssociationEnd);
        myUmlAssociationEndMap.put(umlAssociationEnd.getId(), myUmlAssociationEnd);
    }

    //????????????????????????????????????????????????List?????????????????????????????????????????????????????????????????????????????????????????????
    private void parseGeneralization(UmlGeneralization umlGeneralization) {
        if (myClassMap.containsKey(umlGeneralization.getSource()) &&
                myClassMap.containsKey(umlGeneralization.getTarget())) {
            MyClass source = myClassMap.get(umlGeneralization.getSource());
            MyClass target = myClassMap.get(umlGeneralization.getTarget());
            if (source instanceof MyUmlClass && target instanceof MyUmlClass) {
                ((MyUmlClass) source).setExtendClass((MyUmlClass) target);
            } else if (source instanceof MyUmlInterface && target instanceof MyUmlInterface) {
                ((MyUmlInterface) source).addExtendInterfaces((MyUmlInterface) target);
            }
        }
    }

    //???????????????????????????????????????????????????List????????????????????????????????????
    private void parseInterfaceRealization(UmlInterfaceRealization umlInterfaceRealization) {
        if (myClassMap.containsKey(umlInterfaceRealization.getSource()) &&
                myClassMap.containsKey(umlInterfaceRealization.getTarget())) {
            MyClass source = myClassMap.get(umlInterfaceRealization.getSource());
            MyClass target = myClassMap.get(umlInterfaceRealization.getTarget());
            if (source instanceof MyUmlClass && target instanceof MyUmlInterface) {
                ((MyUmlClass) source).addImplementInterface((MyUmlInterface) target);
            }
        }
    }

    //???????????????????????????Operation
    private void parseParameter(UmlParameter umlParameter) {
        MyUmlParameter myUmlParameter = new MyUmlParameter(umlParameter);
        if (myUmlOperationMap.containsKey(umlParameter.getParentId())) {
            MyUmlOperation myUmlOperation = myUmlOperationMap.get(umlParameter.getParentId());
            myUmlOperation.addParameter(myUmlParameter);
        }
    }

    //?????????????????????End??????Class???????????????Class?????????List????????????????????????
    private void parseAssociation(UmlAssociation umlAssociation) {
        if (myUmlAssociationEndMap.containsKey(umlAssociation.getEnd1()) &&
                myUmlAssociationEndMap.containsKey(umlAssociation.getEnd2())) {
            MyUmlAssociationEnd myUmlAssociationEnd1 =
                    myUmlAssociationEndMap.get(umlAssociation.getEnd1());
            MyUmlAssociationEnd myUmlAssociationEnd2 =
                    myUmlAssociationEndMap.get(umlAssociation.getEnd2());
            if (myClassMap.containsKey(myUmlAssociationEnd1.getUmlAssociationEnd().getReference())
                    && myClassMap.containsKey(
                    myUmlAssociationEnd2.getUmlAssociationEnd().getReference())) {
                MyClass reference1 =
                        myClassMap.get(myUmlAssociationEnd1.getUmlAssociationEnd().getReference());
                MyClass reference2 =
                        myClassMap.get(myUmlAssociationEnd2.getUmlAssociationEnd().getReference());
                if (reference1 instanceof MyUmlClass) {
                    ((MyUmlClass) reference1).addAssociation(reference2);
                    ((MyUmlClass) reference1).
                            addAttrAssoEnd(myUmlAssociationEnd2.getUmlAssociationEnd());
                } else if (reference1 instanceof MyUmlInterface) {
                    ((MyUmlInterface) reference1).addAssociation(reference2);
                }
                if (reference2 instanceof MyUmlClass) {
                    ((MyUmlClass) reference2).addAssociation(reference1);
                    ((MyUmlClass) reference2).
                            addAttrAssoEnd(myUmlAssociationEnd1.getUmlAssociationEnd());
                } else if (reference2 instanceof MyUmlInterface) {
                    ((MyUmlInterface) reference2).addAssociation(reference1);
                }
            }
        }
    }

    //R001????????????????????????????????????????????????????????????????????????(UML002)
    public void checkForUml001() throws UmlRule001Exception {
        Set<AttributeClassInformation> set = new HashSet<>();
        for (MyUmlClass myUmlClass : myUmlClassMap.values()) {
            set.addAll(myUmlClass.checkForUml001());
        }
        if (!set.isEmpty()) {
            throw new UmlRule001Exception(set);
        }
    }

    //R002????????????????????????(UML008)
    public void checkForUml002() throws UmlRule002Exception {
        Set<UmlClassOrInterface> set = new HashSet<>();
        for (MyClass myClass : myClassMap.values()) {
            if (myClass instanceof MyUmlClass) {
                if (!((MyUmlClass) myClass).checkForUml002()) {
                    UmlClassOrInterface umlClassOrInterface =
                            ((MyUmlClass) myClass).getUmlClass();
                    set.add(umlClassOrInterface);
                }
            } else if (myClass instanceof MyUmlInterface) {
                if (!((MyUmlInterface) myClass).checkForUml002()) {
                    UmlClassOrInterface umlClassOrInterface =
                            ((MyUmlInterface) myClass).getUmlInterface();
                    set.add(umlClassOrInterface);
                }
            }
        }
        if (!set.isEmpty()) {
            throw new UmlRule002Exception(set);
        }
    }

    //R003?????????????????????????????????????????????????????????????????????(UML007)
    //?????????????????????????????????????????????????????????
    public void checkForUml003() throws UmlRule003Exception {
        Set<UmlClassOrInterface> set = new HashSet<>();
        for (MyUmlInterface myUmlInterface : myInterfaceMap.values()) {
            if (!myUmlInterface.checkForUml003()) {
                set.add(myUmlInterface.getUmlInterface());
            }
        }
        if (!set.isEmpty()) {
            throw new UmlRule003Exception(set);
        }
    }

    //R004???????????????????????????????????????????????????(UML009)
    public void checkForUml004() throws UmlRule004Exception {
        Set<UmlClass> set = new HashSet<>();
        for (MyUmlClass myUmlClass : myUmlClassMap.values()) {
            if (!myUmlClass.checkForUml004()) {
                set.add(myUmlClass.getUmlClass());
            }
        }
        if (!set.isEmpty()) {
            throw new UmlRule004Exception(set);
        }
    }

    //R005: ??????????????????????????????(UML 001)
    public void checkForUml005() throws UmlRule005Exception {
        for (MyClass myClass : myClassMap.values()) {
            if (myClass instanceof MyUmlClass) {
                if (!((MyUmlClass) myClass).checkForUml005()) {
                    throw new UmlRule005Exception();
                }
            } else if (myClass instanceof MyUmlInterface) {
                if (!((MyUmlInterface) myClass).checkForUml005()) {
                    throw new UmlRule005Exception();
                }
            }
        }
    }

    //R006: ?????????????????????????????????public(UML 011)
    public void checkForUml006() throws UmlRule006Exception {
        for (MyUmlInterface myUmlInterface : myInterfaceMap.values()) {
            if (!myUmlInterface.checkForUml006()) {
                throw new UmlRule006Exception();
            }
        }
    }

    //??????name????????????????????????????????????
    private MyUmlClass getClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!nameClassMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else {
            ArrayList<MyUmlClass> arrayList = nameClassMap.get(className);
            if (arrayList.size() > 1) {
                throw new ClassDuplicatedException(className);
            } else {
                return arrayList.get(0);
            }
        }
    }

    //CLASS_COUNT ????????????
    @Override
    public int getClassCount() {
        return classCount;
    }

    //CLASS_OPERATION_COUNT classname ????????????????????????
    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getOperationCount();
    }

    //CLASS_ATTR_COUNT classname ????????????????????????
    @Override
    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getAttributeCount();
    }

    //CLASS_OPERATION_VISIBILITY classname methodname ?????????????????????????????????????????????
    //??????Map??????4????????????????????????????????????0
    @Override
    public Map<Visibility, Integer>
        getClassOperationVisibility(String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getOperationVisibility(operationName);
    }

    //CLASS_ATTR_VISIBILITY classname attrname ???????????????????????????????????????
    @Override
    public Visibility
        getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getAttributeVisibility(className, attributeName);
    }

    //CLASS_ATTR_TYPE classname attrname ????????????????????????????????????
    @Override
    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getAttributeType(className, attributeName, myClassMap);
    }

    //CLASS_OPERATION_PARAM_TYPE classname methodname ?????????????????????????????????????????????????????????
    //???????????????????????????????????????????????????
    @Override
    public List<OperationParamInformation>
        getClassOperationParamType(String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getOperationParamInformation(className, operationName, myClassMap);
    }

    //CLASS_ASSO_CLASS_LIST classname ??????????????????????????????????????????
    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getAssociationClassNameList(className);
    }

    //CLASS_TOP_BASE classname ????????????
    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getTopParentClassName();
    }

    //CLASS_IMPLEMENT_INTERFACE_LIST classname ???????????????????????????????????????
    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getImplementInterfaceNameList();
    }

    //CLASS_INFO_HIDDEN classname ??????????????????????????????
    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getInformationNotHidden();
    }
}
