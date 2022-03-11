import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<Integer, Bus> idToBus = new HashMap<>();
        Map<Integer, Person> idToPerson = new HashMap<>();
        List<List<Person>> res = new ArrayList<>();
        int personId;
        int busId;
        Bus bus;
        Person person;
        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNext()) {
                String[] cmd = input.nextLine().split("\\s+");
                switch (cmd[0]) {
                    case "1":
                        personId = Integer.parseInt(cmd[1]);
                        busId = Integer.parseInt(cmd[2]);
                        bus = idToBus.get(busId);
                        person = idToPerson.get(personId);
                        if (person != null) {
                            person.setBoardTime(cmd[3]);
                        } else {
                            person = new Person(personId, cmd[3]);
                            idToPerson.put(personId, person);
                        }
                        if (bus != null) {
                            bus.addPerson(person);
                        } else {
                            bus = new Bus();
                            bus.addPerson(person);
                            idToBus.put(busId, bus);
                        }
                        break;
                    case "2":
                        bus = idToBus.get(Integer.parseInt(cmd[2]));
                        person = new Person(Integer.parseInt(cmd[1]));
                        if (bus != null) {
                            bus.removePerson(person);
                        } else {
                            throw new Exception("no such bus");
                        }
                        break;
                    case "3":
                        bus = idToBus.get(Integer.parseInt(cmd[1]));
                        if (bus != null) {
                            res.add(bus.getPersonList());
                        } else {
                            throw new Exception("no such bus");
                        }
                        break;
                    default:
                        throw new Exception("wrong command");
                }
            }
        }
        for (List<Person> personList : res) {
            if (personList.isEmpty()) {
                System.out.println("empty bus");
            } else {
                personList.sort(Comparator.comparing(Person::getPersonId));
                for (Person per : personList) {
                    System.out.print(per);
                }
                System.out.println();
            }
        }
    }
}
