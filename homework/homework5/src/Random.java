import com.oocourse.elevator1.PersonRequest;

public class Random extends Strategy {
    public Random(WaitQueue waitQueue) {
        super(waitQueue);
    }

    public PersonRequest getMainRequest() {
        PersonRequest targetRequest = null;
        int maxTakenNum = 0;
        for (int i = 0; i < getWaitQueue().getPersonRequests().size(); i++) {
            targetRequest = getWaitQueue().getPersonRequests().get(i);
            break;
            //不要冒险，这个优化是基于数据的，不是100%能优于ALS
            /*if (i == 0) {
                targetRequest = getWaitQueue().getPersonRequests().get(i);
            }
            if (getTakenNum(getWaitQueue().getPersonRequests().get(i)) > maxTakenNum) {
                targetRequest = getWaitQueue().getPersonRequests().get(i);
                maxTakenNum = getTakenNum(targetRequest);
            }*/
        }
        return targetRequest;
    }

    private int getTakenNum(PersonRequest p) {
        int takenNum = 0;
        for (int i = 0; i < getWaitQueue().getPersonRequests().size(); i++) {
            PersonRequest pi = getWaitQueue().getPersonRequests().get(i);
            if (((p.getToFloor() - p.getFromFloor()) *
                    (pi.getToFloor() - pi.getFromFloor())) >= 0
                    &&
                    ((p.getToFloor() - pi.getFromFloor()) *
                            (pi.getFromFloor() - p.getFromFloor())) >= 0) {
                takenNum++;
            }
        }
        return takenNum;
    }

    public PersonRequest getThisFloor(int thisFloor) {
        PersonRequest targetRequest = null;
        for (int i = 0; i < getWaitQueue().getPersonRequests().size(); i++) {
            if (getWaitQueue().getPersonRequests().get(i).getFromFloor() == thisFloor) {
                targetRequest = getWaitQueue().getPersonRequests().get(i);
                break;
            }
        }
        return targetRequest;
    }
}
