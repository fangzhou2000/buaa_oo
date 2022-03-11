import com.oocourse.elevator3.PersonRequest;

public class Person {
    private PersonRequest personRequest;
    private int mainPersonFlag;

    public Person(PersonRequest personRequest) {
        this.personRequest = personRequest;
        this.mainPersonFlag = 0;
    }

    public PersonRequest getPersonRequest() {
        return personRequest;
    }

    public void setPersonRequest(PersonRequest personRequest) {
        this.personRequest = personRequest;
    }

    public void setMainPersonFlag(int mainPersonFlag) {
        //表明已被选为主请求，其他电梯无法选择
        this.mainPersonFlag = mainPersonFlag;
    }

    public boolean isAvailableForElevator(String type) {
        if (mainPersonFlag != 0) {
            return false;
        } else {
            if (type.equals("A")) {
                return true;
            } else if (type.equals("B")) {
                if (isArriveForB(personRequest.getFromFloor()) &&
                        isArriveForB(personRequest.getToFloor())) {
                    return true;
                } else if (isArriveForB(personRequest.getFromFloor()) &&
                        isChangeForB()) {
                    return true;
                } else {
                    return false;
                }
            } else if (type.equals("C")) {
                if (isArriveForC(personRequest.getFromFloor()) &&
                        isArriveForC(personRequest.getToFloor())) {
                    return true;
                } else if (isArriveForC(personRequest.getFromFloor()) &&
                        isChangeForC()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                //System.out.println("type wrong");
                return false;
            }
        }
    }

    public boolean isChangeForB() {
        if (!isArriveForB(personRequest.getToFloor())) {
            if (personRequest.getToFloor() + 1 != personRequest.getFromFloor() &&
                    personRequest.getToFloor() - 1 != personRequest.getFromFloor()) {
                return true;
            }
        }
        return false;
    }

    public boolean isChangeForC() {
        if (!isArriveForC(personRequest.getToFloor())) {
            if (personRequest.getFromFloor() <= 3) {
                if (personRequest.getToFloor() >= 15) {
                    return true;
                } else if (personRequest.getFromFloor() != 3) {
                    return true;
                } else {
                    return false;
                }
            } else if (personRequest.getFromFloor() >= 18) {
                if (personRequest.getToFloor() <= 6) {
                    return true;
                } else if (personRequest.getFromFloor() == 20) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isArriveForB(int floor) {
        return floor % 2 == 1;
    }

    public boolean isArriveForC(int floor) {
        if (1 <= floor && floor <= 3) {
            return true;
        } else if (18 <= floor && floor <= 20) {
            return true;
        } else {
            return false;
        }
    }
}
