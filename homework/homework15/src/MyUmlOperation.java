import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.elements.UmlOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyUmlOperation {
    private UmlOperation umlOperation;
    private ArrayList<MyUmlParameter> myUmlParameters;
    private MyUmlParameter myUmlReturnParameter;

    public MyUmlOperation(UmlOperation umlOperation) {
        this.umlOperation = umlOperation;
        this.myUmlParameters = new ArrayList<>();
        this.myUmlReturnParameter = null;
    }

    public void addParameter(MyUmlParameter myUmlParameter) {
        if (myUmlParameter.getUmlParameter().getDirection().equals(Direction.RETURN)) {
            myUmlReturnParameter = myUmlParameter;
        } else {
            myUmlParameters.add(myUmlParameter);
        }
    }

    public UmlOperation getUmlOperation() {
        return umlOperation;
    }

    public boolean checkForUml005() {
        if (umlOperation.getName() == null) {
            return false;
        }
        for (MyUmlParameter myUmlParameter : myUmlParameters) {
            if (myUmlParameter.getUmlParameter().getName() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isOkParameterType(String string) {
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

    public boolean isOkReturnParameterType(String string) {
        if (!string.equals("byte") && !string.equals("short")
                && !string.equals("int") && !string.equals("long")) {
            if (!string.equals("float") && !string.equals("double")
                    && !string.equals("char") && !string.equals("boolean")
                    && !string.equals("String") && !string.equals("void")) {
                return false;
            }
        }
        return true;
    }

    public List<String> getParaMeterTypes(String className, String operationName,
                                          HashMap<String, MyClass> myClassMap)
            throws MethodWrongTypeException {
        List<String> parameterTypes = new ArrayList<>();
        for (MyUmlParameter myUmlParameter : myUmlParameters) {
            NameableType type = myUmlParameter.getUmlParameter().getType();
            if (type instanceof NamedType) {
                String name = ((NamedType) type).getName();
                if (!isOkParameterType(name)) {
                    throw new MethodWrongTypeException(className, operationName);
                } else {
                    parameterTypes.add(name);
                }
            } else if (type instanceof ReferenceType) {
                String id = ((ReferenceType) type).getReferenceId();
                if (!myClassMap.containsKey(id)) {
                    throw new MethodWrongTypeException(className, operationName);
                } else {
                    MyClass myClass = myClassMap.get(id);
                    if (myClass instanceof MyUmlClass) {
                        parameterTypes.add(((MyUmlClass) myClass).getClassName());
                    } else if (myClass instanceof MyUmlInterface) {
                        parameterTypes.add(((MyUmlInterface) myClass).getClassName());
                    }
                }
            }
        }
        return parameterTypes;
    }

    public String getMyUmlReturnParameter(String className, String operationName,
                                          HashMap<String, MyClass> myClassMap)
            throws MethodWrongTypeException {
        if (myUmlReturnParameter != null) {
            NameableType type = myUmlReturnParameter.getUmlParameter().getType();
            if (type instanceof NamedType) {
                String name = ((NamedType) type).getName();
                if (!isOkReturnParameterType(name)) {
                    throw new MethodWrongTypeException(className, operationName);
                } else {
                    return name;
                }
            } else if (type instanceof ReferenceType) {
                String id = ((ReferenceType) type).getReferenceId();
                if (!myClassMap.containsKey(id)) {
                    throw new MethodWrongTypeException(className, operationName);
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
        } else {
            return null;
        }
    }
}
