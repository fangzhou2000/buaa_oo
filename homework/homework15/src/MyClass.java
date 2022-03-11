import com.oocourse.uml3.interact.common.OperationParamInformation;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.models.common.Visibility;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.EnumMap;
import java.util.Collections;

public class MyClass {
    private ArrayList<MyUmlAttribute> myUmlAttributeList;
    private HashMap<String, ArrayList<MyUmlAttribute>> nameAttributeMap; // name, attribute
    private int operationCount;
    private ArrayList<MyUmlOperation> myUmlOperationList;
    private HashMap<String, ArrayList<MyUmlOperation>> nameOperationMap;//name, operation
    //缓存
    private HashMap<String, Map<Visibility, Integer>> operationVisibility = new HashMap<>();
    private HashMap<String, List<OperationParamInformation>> operationParamInformation =
            new HashMap<>();

    public MyClass() {
        this.myUmlAttributeList = new ArrayList<>();
        this.nameAttributeMap = new HashMap<>();
        this.operationCount = 0;
        this.myUmlOperationList = new ArrayList<>();
        this.nameOperationMap = new HashMap<>();
    }

    public ArrayList<MyUmlAttribute> getMyUmlAttributeList() {
        return myUmlAttributeList;
    }

    public HashMap<String, ArrayList<MyUmlAttribute>> getNameAttributeMap() {
        return nameAttributeMap;
    }

    public ArrayList<MyUmlOperation> getMyUmlOperationList() {
        return myUmlOperationList;
    }

    public void addAttribute(MyUmlAttribute myUmlAttribute) {
        myUmlAttributeList.add(myUmlAttribute);
        String name = myUmlAttribute.getUmlAttribute().getName();
        if (nameAttributeMap.containsKey(name)) {
            nameAttributeMap.get(name).add(myUmlAttribute);
        } else {
            ArrayList<MyUmlAttribute> arrayList = new ArrayList<>();
            arrayList.add(myUmlAttribute);
            nameAttributeMap.put(name, arrayList);
        }
    }

    public void addOperation(MyUmlOperation myUmlOperation) {
        operationCount++;
        myUmlOperationList.add(myUmlOperation);
        String name = myUmlOperation.getUmlOperation().getName();
        if (nameOperationMap.containsKey(name)) {
            nameOperationMap.get(name).add(myUmlOperation);
        } else {
            ArrayList<MyUmlOperation> arrayList = new ArrayList<>();
            arrayList.add(myUmlOperation);
            nameOperationMap.put(name, arrayList);
        }
    }

    /*
    类中的操作有多少个
    输入指令格式：CLASS_OPERATION_COUNT classname
    举例：CLASS_OPERATION_COUNT Elevator
    输出：
    Ok, operation count of class "classname" is x. x为类中的操作个数
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    说明：
    本指令中统计的一律为此类自己定义的操作，不包含继承自其各级父类所定义的操作
    本指令不检查操作的合法性，所有操作均计入总数。如有重复操作分别计入总数。
     */
    public int getOperationCount() {
        return operationCount;
    }

    /*类的操作可见性
    输入指令格式：CLASS_OPERATION_VISIBILITY classname methodname
    举例：CLASS_OPERATION_VISIBILITY Taxi setStatus
    输出：
    Ok, operation visibility of method "methodname" in class "classname"
    is public: xxx, protected: xxx, private: xxx, package-private: xxx. 该操作的实际可见性统计
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    说明：
    本指令中统计的一律为此类自己定义的操作，不包含其各级父类所定义的操作
    在上一条的前提下，需要统计出全部的名为methodname的方法的可见性信息*/
    public Map<Visibility, Integer> getOperationVisibility(String operationName) {
        if (!operationVisibility.containsKey(operationName)) {
            Map<Visibility, Integer> map = new EnumMap<Visibility, Integer>(Visibility.class);
            map.put(Visibility.PUBLIC, 0);
            map.put(Visibility.PRIVATE, 0);
            map.put(Visibility.PROTECTED, 0);
            map.put(Visibility.PACKAGE, 0);
            if (nameOperationMap.containsKey(operationName)) {
                for (MyUmlOperation myUmlOperation : nameOperationMap.get(operationName)) {
                    Visibility visibility = myUmlOperation.getUmlOperation().getVisibility();
                    Integer old = map.get(visibility);
                    map.put(visibility, old + 1);
                }
            }
            operationVisibility.put(operationName, map);
            return map;
        } else {
            return operationVisibility.get(operationName);
        }
    }

    /*类的操作的参数类型
    输入指令格式：CLASS_OPERATION_PARAM_TYPE classname methodname
    举例：CLASS_OPERATION_PARAM_TYPE Taxi setStatus
    输出：
    Ok, method "methodname" in class "classname" has parameter tables and return value:
    (type1, return: type2), (type3, return: type4), (type5, type6, no return).
    该操作有三种参数表和返回值的搭配，其中
    传出列表时可以乱序，内部参数类型也可以乱序，官方接口会自动进行排序（但是需要编写者自行保证不重不漏）
    Failed, class "classname" not found. 类不存在
    Failed, duplicated class "classname". 类存在多个
    Failed, wrong type of parameters or return value in method
    "methodname" of class "classname". 存在错误类型
    Failed, duplicated method "methodname" in class "classname". 存在重复操作
    说明：
    对于参数类型
    NamedType只考虑JAVA 语言八大基本类型(byte, short, int, long, float,
    double, char, boolean)和String，其余一律视为错误类型。
    ReferenceType指向已定义的类或接口，类型名为对应的类名或接口名。
    对于返回值类型
    NamedType只考虑JAVA 语言八大基本类型(byte, short, int, long, float, double,
     char, boolean)和String，void，其余一律视为错误类型。（实际上，void也算是一种类型，
     C/C++/Java对于这件事也都是这样的定义）。void不等同于无返回值。
    ReferenceType指向已定义的类或接口，类型名为对应的类名或接口名。
    参数之间不分次序，即op(int,int,double)和op(double,int,int)视为具有相同参数类型，
    但参数和返回值之间是有区别的，且保证最多只有一个返回值，无返回值时相应位置返回null。
    如果两个操作的操作名相同，且参数和返回值的类型也相同，视为重复操作。
    如果同时存在错误类型和重复操作两种异常，按错误类型异常处理。
    本指令中统计的一律为此类自己定义的操作，不包含继承自其各级父类所定义的操作。
    先处理错误类型再处理重复操作*/
    public List<OperationParamInformation>
        getOperationParamInformation(String className, String operationName,
                                 HashMap<String, MyClass> myClassMap)
            throws MethodWrongTypeException, MethodDuplicatedException {
        if (!operationParamInformation.containsKey(operationName)) {
            List<OperationParamInformation> operationParamInformations = new ArrayList<>();
            int duplicatedFlag = 0;
            if (nameOperationMap.containsKey(operationName)) {
                for (MyUmlOperation myUmlOperation : nameOperationMap.get(operationName)) {
                    List<String> paraMeterTypes = new ArrayList<>();
                    String returnType = null;
                    paraMeterTypes = myUmlOperation.getParaMeterTypes(className, operationName,
                            myClassMap);
                    Collections.sort(paraMeterTypes);
                    returnType = myUmlOperation.getMyUmlReturnParameter(className, operationName,
                            myClassMap);
                    OperationParamInformation operationParamInformation =
                            new OperationParamInformation(paraMeterTypes, returnType);
                    for (OperationParamInformation opi : operationParamInformations) {
                        if (opi.equals(operationParamInformation)) {
                            duplicatedFlag = 1;
                        }
                    }
                    operationParamInformations.add(operationParamInformation);
                }
                if (duplicatedFlag == 1) {
                    throw new MethodDuplicatedException(className, operationName);
                }
            }
            //如果没有这个名字的返回一个空数组
            operationParamInformation.put(operationName, operationParamInformations);
            return operationParamInformations;
        } else {
            return operationParamInformation.get(operationName);
        }
    }
}
