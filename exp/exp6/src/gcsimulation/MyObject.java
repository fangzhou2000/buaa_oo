package gcsimulation;

import java.util.Objects;

public class MyObject implements Comparable<MyObject> {
    private static int totalId = 0;
    private /*@spec_public@*/ int id;
    private /*@spec_public@*/ boolean referenced;
    private /*@spec_public@*/ int age;

    MyObject() {
        id = totalId;
        totalId++;
        referenced = true;
        age = 0;
    }

    /*@ public normal_behavior
      @ assignable age;
      @ ensures age == newAge;
      @*/
    public void setAge(int newAge) {
        this.age = newAge;
    }

    //@ ensures \result == age;
    public /*@pure@*/ int getAge() {
        return age;
    }

    //@ ensures \result == id;
    public /*@pure@*/ int getId() {
        return id;
    }

    /*@ public normal_behavior
      @ assignable referenced;
      @ ensures referenced == newReferenced;
      @*/
    public void setReferenced(boolean newReferenced) {
        this.referenced = newReferenced;
    }

    //@ ensures \result == referenced;
    public /*@pure@*/ boolean getReferenced() {
        return referenced;
    }

    /*@ public normal_behavior
      @ requires this == o;
      @ ensures \result == true;
      @ also
      @ requires this != o && (o == null || !(o instanceof MyObject));
      @ ensures \result == false;
      @ also
      @ requires this != o && o != null && o instanceof MyObject;
      @ ensures \result == (id == (MyObject) o.getId() &&
      @         referenced == (MyObject) o.getReferenced() &&
      @         age == (MyObject) o.getAge());
      @*/
    @Override
    public /*@pure@*/ boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyObject)) {
            return false;
        }
        MyObject myObject = (MyObject) o;
        return id == myObject.getId() && referenced == myObject.getReferenced()
                && age == myObject.getAge();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referenced, age);
    }

    /*@ public normal_behavior
      @ requires object != null;
      @ ensures ((age < object.age) || (age == object.age && id < object.id)) ==> (\result == -1);
      @ ensures ((age > object.age) || (age == object.age && id >= object.id)) ==> (\result == 1);
      @ also
      @ public exceptional_behavior
      @ requires object == null;
      @ signals (NullPointerException e) object == null;
      @*/
    public /*@pure@*/ int compareTo(MyObject object) {
        // TODO
        if (object != null) {
            if (age < object.age || (age == object.age && id < object.id)) {
                return  -1;
            }
            if (age > object.age || (age == object.age && id >= object.id)) {
                return 1;
            }
        }
        throw new NullPointerException();
    }
}