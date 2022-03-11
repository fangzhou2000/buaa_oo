import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.OperationParamInformation;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    我的数据结构：把官方的Uml...当作基本类型元素，重新通过类来组织层次结构，并进行包装MyUml...通过组合实现对基本元素的管理
    MyUmlInteraction只负责解析和调用内层实现完成接口，具体实现过程分层次交给不同的类
    树形结构如下(省略了My)
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
                associationInterfaceList//似乎association应该放在Class类...

                UmlAttribute
                UmlOperation
                    UmlParameter
                UmlAssociationEnd
                    UmlAssociation

 */
public class MyUmlInteraction implements UmlInteraction {
    private int classCount;
    private HashMap<String, MyClass> myClassMap;                          // id, class or interface
    private HashMap<String, MyUmlOperation> myUmlOperationMap;            // operationId, operation
    private HashMap<String, ArrayList<MyUmlClass>> nameClassMap;          // name, classList
    private HashMap<String, MyUmlAssociationEnd> myUmlAssociationEndMap;  // endId, end

    public MyUmlInteraction(UmlElement[] elements) {
        this.classCount = 0;
        this.myClassMap = new HashMap<>();
        this.myUmlOperationMap = new HashMap<>();
        this.nameClassMap = new HashMap<>();
        this.myUmlAssociationEndMap = new HashMap<>();

        //解析类与接口
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

        //解析第二层element
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

        //解析依赖第二层的第三层element
        for (UmlElement e : elements2) {
            if (e.getElementType() == ElementType.UML_PARAMETER) {
                parseParameter((UmlParameter) e);
            } else if (e.getElementType() == ElementType.UML_ASSOCIATION) {
                parseAssociation((UmlAssociation) e);
            } else {
                //wrong
            }
        }
    }

    //解析类，维护classCount，加入myClassMap和nameClassMap
    private void parseClass(UmlClass umlClass) {
        classCount++;
        MyUmlClass myUmlClass = new MyUmlClass(umlClass);
        myClassMap.put(umlClass.getId(), myUmlClass);
        if (nameClassMap.containsKey(umlClass.getName())) {
            nameClassMap.get(umlClass.getName()).add(myUmlClass);
        } else {
            ArrayList<MyUmlClass> arrayList = new ArrayList<>();
            arrayList.add(myUmlClass);
            nameClassMap.put(umlClass.getName(), arrayList);
        }
    }

    //解析接口，加入myClassMap
    private void parseInterface(UmlInterface umlInterface) {
        MyUmlInterface myUmlInterface = new MyUmlInterface(umlInterface);
        myClassMap.put(umlInterface.getId(), myUmlInterface);
    }

    //解析属性，加入对应Class中的attributes
    private void parseAttribute(UmlAttribute umlAttribute) {
        MyUmlAttribute myUmlAttribute = new MyUmlAttribute(umlAttribute);
        MyClass myClass = myClassMap.get(umlAttribute.getParentId());
        myClass.addAttribute(myUmlAttribute);
    }

    //解析方法，加入对应Class中的operations，以及维护的operationMap
    private void parseOperation(UmlOperation umlOperation) {
        MyUmlOperation myUmlOperation = new MyUmlOperation(umlOperation);
        myUmlOperationMap.put(umlOperation.getId(), myUmlOperation);
        MyClass myClass = myClassMap.get(umlOperation.getParentId());
        myClass.addOperation(myUmlOperation);
    }

    //解析关联节点，加入维护的associationEndMap
    private void parseAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        MyUmlAssociationEnd myUmlAssociationEnd = new MyUmlAssociationEnd(umlAssociationEnd);
        myUmlAssociationEndMap.put(umlAssociationEnd.getId(), myUmlAssociationEnd);
    }

    //解析继承，加入对应类或接口的继承List中，注意仅允许类类或接口接口继承，并且类单继承，接口允许多继承
    private void parseGeneralization(UmlGeneralization umlGeneralization) {
        MyClass source = myClassMap.get(umlGeneralization.getSource());
        MyClass target = myClassMap.get(umlGeneralization.getTarget());
        if (source instanceof MyUmlClass && target instanceof MyUmlClass) {
            ((MyUmlClass) source).setExtendClass((MyUmlClass) target);
        } else if (source instanceof MyUmlInterface && target instanceof MyUmlInterface) {
            ((MyUmlInterface) source).addExtendInterfaces((MyUmlInterface) target);
        }
    }

    //解析接口实现，加入对于类的接口实现List中，注意仅允许类实现接口
    private void parseInterfaceRealization(UmlInterfaceRealization umlInterfaceRealization) {
        MyClass source = myClassMap.get(umlInterfaceRealization.getSource());
        MyClass target = myClassMap.get(umlInterfaceRealization.getTarget());
        if (source instanceof MyUmlClass && target instanceof MyUmlInterface) {
            ((MyUmlClass) source).addImplementInterface((MyUmlInterface) target);
        }
    }

    //解析参数，加入对应Operation
    private void parseParameter(UmlParameter umlParameter) {
        MyUmlParameter myUmlParameter = new MyUmlParameter(umlParameter);
        MyUmlOperation myUmlOperation = myUmlOperationMap.get(umlParameter.getParentId());
        myUmlOperation.addParameter(myUmlParameter);
    }

    //解析关联，根据End得到Class，加入对应Class的关联List中，注意双向关联
    private void parseAssociation(UmlAssociation umlAssociation) {
        MyUmlAssociationEnd myUmlAssociationEnd1 =
                myUmlAssociationEndMap.get(umlAssociation.getEnd1());
        MyUmlAssociationEnd myUmlAssociationEnd2 =
                myUmlAssociationEndMap.get(umlAssociation.getEnd2());
        MyClass reference1 =
                myClassMap.get(myUmlAssociationEnd1.getUmlAssociationEnd().getReference());
        MyClass reference2 =
                myClassMap.get(myUmlAssociationEnd2.getUmlAssociationEnd().getReference());
        if (reference1 instanceof MyUmlClass) {
            ((MyUmlClass) reference1).addAssociation(reference2);
        } else if (reference1 instanceof MyUmlInterface) {
            ((MyUmlInterface) reference1).addAssociation(reference2);
        }
        if (reference2 instanceof MyUmlClass) {
            ((MyUmlClass) reference2).addAssociation(reference1);
        } else if (reference2 instanceof MyUmlInterface) {
            ((MyUmlInterface) reference2).addAssociation(reference1);
        }
    }

    //通过name得到类，同时抛出两种异常
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

    //CLASS_COUNT 类的数量
    @Override
    public int getClassCount() {
        return classCount;
    }

    //CLASS_OPERATION_COUNT classname 某个类的方法数量
    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getOperationCount();
    }

    //CLASS_ATTR_COUNT classname 某个类的属性数量
    @Override
    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getAttributeCount();
    }

    //CLASS_OPERATION_VISIBILITY classname methodname 某个类某个名字方法可见性的统计
    //注意Map必须4个可进性都包含，没有则为0
    @Override
    public Map<Visibility, Integer>
        getClassOperationVisibility(String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getOperationVisibility(operationName);
    }

    //CLASS_ATTR_VISIBILITY classname attrname 某个类某个名字属性的可见性
    @Override
    public Visibility
        getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getAttributeVisibility(className, attributeName);
    }

    //CLASS_ATTR_TYPE classname attrname 某个类某个名字属性的类型
    @Override
    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getAttributeType(className, attributeName, myClassMap);
    }

    //CLASS_OPERATION_PARAM_TYPE classname methodname 某个类某个名字方法的参数类型，不重不漏
    //先抛出类型错误异常，再抛出重复异常
    @Override
    public List<OperationParamInformation>
        getClassOperationParamType(String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getOperationParamInformation(className, operationName, myClassMap);
    }

    //CLASS_ASSO_CLASS_LIST classname 某个类关联类的数量，不重不漏
    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getAssociationClassNameList(className);
    }

    //CLASS_TOP_BASE classname 顶级父类
    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getTopParentClassName();
    }

    //CLASS_IMPLEMENT_INTERFACE_LIST classname 实现的接口的名字，不重不漏
    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getImplementInterfaceNameList();
    }

    //CLASS_INFO_HIDDEN classname 类的非私有属性，可重
    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyUmlClass myUmlClass = getClass(className);
        return myUmlClass.getInformationNotHidden();
    }
}
