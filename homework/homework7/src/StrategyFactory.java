public class StrategyFactory {
    public static Strategy getNew(WaitQueue waitQueue, String type) {
        switch (waitQueue.getArrivePattern()) {
            case "Night": {
                return new Night(waitQueue, type);
            }
            case "Morning": {
                return new Morning(waitQueue, type);
            }
            default: {
                return new Random(waitQueue, type);
            }
        }
    }
}
