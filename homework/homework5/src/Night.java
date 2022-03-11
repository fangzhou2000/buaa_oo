import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Night extends Strategy {

    public Night(WaitQueue waitQueue) {
        super(waitQueue);
    }

    @Override
    public PersonRequest getMainRequest() {
        ArrayList<PersonRequest> personRequests = getWaitQueue().getPersonRequests();
        PersonRequest highRequest = null;
        for (int i = 0; i < personRequests.size(); i++) {
            if (highRequest == null) {
                highRequest = personRequests.get(i);
            } else {
                if (highRequest.getFromFloor() < personRequests.get(i).getFromFloor()) {
                    highRequest = personRequests.get(i);
                }
            }
        }
        return highRequest;
    }
}
