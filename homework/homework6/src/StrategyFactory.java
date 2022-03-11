public class StrategyFactory {
    public static Strategy getNew(WaitQueue waitQueue) {
        switch (waitQueue.getArrivePattern()) {
            case "Night": {
                return new Night(waitQueue);
            }
            case "Morning": {
                return new Morning(waitQueue);
            }
            default: {
                return new Random(waitQueue);
            }
        }
    }
}
