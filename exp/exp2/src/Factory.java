import java.util.List;

public class Factory {
    public static Vehicle getNew(List<String> ops) {
        String type = ops.get(1);
        if ("Car".equals(type)) {
            // TODO
            String id = ops.get(2);
            String price = ops.get(3);
            String max_speed = ops.get(4);
            return new Car(Integer.parseInt(id), Integer.parseInt(price),
                    Integer.parseInt(max_speed));
        } else if ("Sprinkler".equals(type)) {
            // TODO
            String id = ops.get(2);
            String price = ops.get(3);
            String volume = ops.get(4);
            return new Sprinkler(Integer.parseInt(id), Integer.parseInt(price),
                    Integer.parseInt(volume));
        } else {
            // TODO
            String id = ops.get(2);
            String price = ops.get(3);
            String volume = ops.get(4);
            return new Bus(Integer.parseInt(id), Integer.parseInt(price),
                    Integer.parseInt(volume));
        }
    }
}
