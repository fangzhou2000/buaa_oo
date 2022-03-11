public class Path implements Comparable<Path> {
    private int id;
    private int distance;

    public Path(int id, int distance) {
        this.id = id;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    //priorityQueue有comparator属性，comparator调用compare方法，compare调用compareTo
    @Override
    public int compareTo(Path o) {
        return Integer.compare(distance, o.distance);
    }
}
