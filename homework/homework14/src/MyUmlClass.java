import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.common.NamedType;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlClass;

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
    //缓存
    private int attributeCount = -1;
    private HashMap<String, Visibility> attributeVisibility = new HashMap<>();
    private HashMap<String, String> attributeType = new HashMap<>();
    private List<String> associationClassNameList = null;
    private String topParentClassName = null;
    private List<String> implementInterfaceNameList = null;
    private List<AttributeClassInformation> informationNotHidden = null;

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

    /*类中的属性有多少个
    输入指令格式：CLASS_ATTR_COUNT classname
    举例：CLASS_ATTR_COUNT Elevator
    输出：
    Ok, attribute count of class "classname" is x. x为类中属性的个数
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    说明：
    本指令的查询均需要考虑属性的继承关系，即需包括各级父类定义的属性。*/
    public int getAttributeCount() {
        if (attributeCount == -1) {
            attributeCount = getMyUmlAttributeList().size();
            MyUmlClass myUmlClass = extendClass;
            if (myUmlClass != null) {
                attributeCount = attributeCount + myUmlClass.getAttributeCount();
            }
        }
        return attributeCount;
    }

    /*类的属性可见性
    输入指令格式：CLASS_ATTR_VISIBILITY classname attrname
    举例：CLASS_ATTR_VISIBILITY Taxi id
    输出：
    Ok, attribute "attrname" in class "classname"'s
    visibility is public/protected/private/package-private. 该属性的实际可见性
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    Failed, attribute "attrname" not found in class "classname". 类中没有该属性
    Failed, duplicated attribute "attrname" in class "classname". 类中属性存在多个同名
    说明：
    本指令的查询均需要考虑属性的继承关系。
    其中对于父类和子类均存在此名称的属性时，需要按照duplicated attribute处理*/
    public Visibility getAttributeVisibility(String className, String attributeName) throws
            AttributeNotFoundException, AttributeDuplicatedException {
        if (!attributeVisibility.containsKey(attributeName)) {
            ArrayList<MyUmlAttribute> arrayList = new ArrayList<>();
            arrayList.addAll(getMyUmlAttributeList());
            MyUmlClass myUmlClass = extendClass;
            while (myUmlClass != null) {
                arrayList.addAll(myUmlClass.getMyUmlAttributeList());
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
            attributeVisibility.put(attributeName, visibility);
            return visibility;
        } else {
            return attributeVisibility.get(attributeName);
        }
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

    /*类的属性类型
    输入指令格式：CLASS_ATTR_TYPE classname attrname
    举例：CLASS_ATTR_TYPE Taxi id
    输出：
    Ok, the type of attribute "attrname" in class "classname" is typeA. 该属性的类型
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    Failed, attribute "attrname" not found in class "classname". 类中没有该属性
    Failed, duplicated attribute "attrname" in class "classname". 类中属性存在多个同名
    Failed, wrong type of attribute "attrname" in class "classname". 属性类型错误
    说明：
    类型type的数据类型有两种，分别为ReferenceType和NamedType。对于这两种情形，
    NamedType为命名型类别，只考虑 JAVA 语言八大基本类型(byte, short, int, long, float,
    double, char, boolean)和String，其余一律视为错误类型。
    ReferenceType为依赖型类别，其指向已定义的类或接口，类型名为对应的类名或接口名。
    本指令的查询均需要考虑属性的继承关系。
    其中对于父类和子类均存在此名称的属性时，需要按照duplicated attribute处理。
    先处理重复异常，再处理错误异常*/
    public String getAttributeType(String className, String attributeName,
                                   HashMap<String, MyClass> myClassMap) throws
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        if (!attributeType.containsKey(attributeName)) {
            ArrayList<MyUmlAttribute> arrayList = new ArrayList<>();
            arrayList.addAll(getMyUmlAttributeList());
            MyUmlClass myUmlClass = extendClass;
            while (myUmlClass != null) {
                arrayList.addAll(myUmlClass.getMyUmlAttributeList());
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
                    attributeType.put(attributeName, name);
                    return name;
                }
            } else if (type instanceof ReferenceType) {
                String id = ((ReferenceType) type).getReferenceId();
                if (!myClassMap.containsKey(id)) {
                    throw new AttributeWrongTypeException(className, attributeName);
                } else {
                    MyClass myClass = myClassMap.get(id);
                    if (myClass instanceof MyUmlClass) {
                        attributeType.put(attributeName,((MyUmlClass) myClass).getClassName());
                        return ((MyUmlClass) myClass).getClassName();
                    } else if (myClass instanceof MyUmlInterface) {
                        attributeType.put(attributeName, ((MyUmlInterface) myClass).getClassName());
                        return ((MyUmlInterface) myClass).getClassName();
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } else {
            return attributeType.get(attributeName);
        }
    }

    /*类的关联的对端是哪些类
    输入指令格式：CLASS_ASSO_CLASS_LIST classname
    举例：CLASS_ASSO_CLASS_LIST Elevator
    输出：
    Ok, associated classes of class "classname" are (A, B, C). A、B、C为类所有关联的对端的类名，其中
    传出列表时可以乱序，官方接口会自动进行排序（但是需要编写者自行保证不重不漏；
    特别的，对于同名但id不同的类，如果结果同时包含多个的话，需要在列表中返回对应数量个类名）
    如果出现自关联的话，那么自身类也需要加入输出
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    注意：
    Association关系需要考虑父类的继承。即，假如XX类继承了YY类，那么YY的Association对端节点也属于XX。
    不考虑由属性和操作参数类型引起的关联。*/
    public List<String> getAssociationClassNameList(String className) {
        //HashSet可以合并相同元素
        if (associationClassNameList == null) {
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
            associationClassNameList = associationClassList;
        }
        return associationClassNameList;
    }

    /*类的顶级父类
    输入指令格式：CLASS_TOP_BASE classname
    举例：CLASS_TOP_BASE AdvancedTaxi
    输出：
    Ok, top base class of class "classname" is top_classname. top_classname为顶级父类
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    说明：
    具体来说，对于类XX，如果YY为其顶级父类的话，则满足
    XX是YY的子类（此处特别定义，XX也是XX的子类）
    不存在类ZZ，使得YY是ZZ的子类*/
    public String getTopParentClassName() {
        if (topParentClassName == null) {
            topParentClassName = this.getClassName();
            MyUmlClass myUmlClass = extendClass;
            if (myUmlClass != null) {
                topParentClassName = myUmlClass.getTopParentClassName();
            }
        }
        return topParentClassName;
    }

    /*类实现的全部接口
    输入指令格式：CLASS_IMPLEMENT_INTERFACE_LIST classname
    举例：CLASS_IMPLEMENT_INTERFACE_LIST Taxi
    输出：
    Ok, implement interfaces of class "classname" are (A, B, C). A、B、C为继承的各个接口
    传出列表时可以乱序，官方接口会自动进行排序（但是需要编写者自行保证不重不漏）
    特别值得注意的是，无论是直接实现还是通过父类或者接口继承等方式间接实现，都算做实现了接口
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个*/
    public List<String> getImplementInterfaceNameList() {
        if (implementInterfaceNameList == null) {
            HashSet<MyUmlInterface> hashSet = new HashSet<>();
            //类和父类实现的接口
            HashSet<MyUmlInterface> implementHashSet = new HashSet<>();
            implementHashSet.addAll(implementInterfaceList);
            MyUmlClass myUmlClass = extendClass;
            while (myUmlClass != null) {
                implementHashSet.addAll(myUmlClass.getImplementInterfaceList());
                myUmlClass = myUmlClass.getExtendClass();
            }
            //实现的接口继承的接口
            HashSet<MyUmlInterface> extendHashSet = new HashSet<>();
            //Set有个缺点，他是无序的，边remove边添加可能会出问题
            //ArrayList有序
            ArrayList<MyUmlInterface> arrayList = new ArrayList<>();
            arrayList.addAll(implementHashSet);
            while (!arrayList.isEmpty()) {
                MyUmlInterface myUmlInterface = arrayList.remove(0);
                //好像判断也可以，因为hashSet不允许重，加入判断减少循环
                if (!hashSet.contains(myUmlInterface)) {
                    hashSet.add(myUmlInterface);
                    arrayList.addAll(myUmlInterface.getExtendInterfaceList());
                }
            }
            implementInterfaceNameList = new ArrayList<>();
            for (MyUmlInterface mui : hashSet) {
                implementInterfaceNameList.add(mui.getClassName());
            }
            return implementInterfaceNameList;
        } else {
            return implementInterfaceNameList;
        }
    }

    /*类是否违背信息隐藏原则
    输入指令格式：CLASS_INFO_HIDDEN classname
    举例：CLASS_INFO_HIDDEN Taxi
    输出：
    Yes, information of class "classname" is hidden. 满足信息隐藏原则。
    No, attribute xxx in xxx, xxx in xxx, are not hidden. 不满足信息隐藏原则。
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    说明：
    信息隐藏原则，指的是在类属性的定义中，不允许使用private以外的任何可见性修饰
    本指令中需要列出全部的非隐藏属性，同时也需要考虑继承自父类的非隐藏属性
    值得注意的是，父类和子类中，是可以定义同名属性的（甚至还可以不同类型，不同可见性，感兴趣的话可以自己尝试尝试），
    然而父类中定义的和子类中定义的实际上并不是同一个属性，需要在输出时进行分别处理
    同样的，返回的列表可以乱序，官方接口会进行自动排序（但是依然需要编写者保证不重不漏）*/
    public List<AttributeClassInformation> getInformationNotHidden() {
        if (informationNotHidden == null) {
            List<AttributeClassInformation> attributeClassInformations = new ArrayList<>();
            for (MyUmlAttribute myUmlAttribute : getMyUmlAttributeList()) {
                if (myUmlAttribute.getUmlAttribute().getVisibility() != Visibility.PRIVATE) {
                    AttributeClassInformation attributeClassInformation =
                            new AttributeClassInformation(
                                    myUmlAttribute.getUmlAttribute().getName(), getClassName());
                    attributeClassInformations.add(attributeClassInformation);
                }
            }
            MyUmlClass myUmlClass = extendClass;
            while (myUmlClass != null) {
                for (MyUmlAttribute myUmlAttribute : myUmlClass.getMyUmlAttributeList()) {
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
            informationNotHidden = attributeClassInformations;
            return attributeClassInformations;
        } else {
            return informationNotHidden;
        }
    }
}
