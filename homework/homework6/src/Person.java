import com.oocourse.elevator2.PersonRequest;

public class Person {
    private PersonRequest personRequest;
    private int status;

    public Person(PersonRequest personRequest) {
        this.personRequest = personRequest;
        this.status = 0;
    }

    public PersonRequest getPersonRequest() {
        return personRequest;
    }

    public void setStatus(int status) {
        //表明已被选为主请求，其他电梯无法选择
        this.status = status;
    }

    public boolean isAvailable() {
        return status != 1;
    }
}
