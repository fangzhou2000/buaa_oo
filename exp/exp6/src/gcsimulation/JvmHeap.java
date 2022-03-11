package gcsimulation;

import java.util.List;

public class JvmHeap extends MyHeap<MyObject> {

    JvmHeap(int capacity) {
        super(capacity);
    }

    /*@ public normal_behavior
      @ requires objectId != null;
      @ assignable elementData;
      @ ensures size == \old(size);
      @ ensures (\forall int i; 1 <= i && i <= size; elementData[i] == \old(elementData[i]));
      @ [1]; 要求：定义后置条件，表示调用此方法前后，elementData中下标大于等于1且小于等于size的各位置元素始终为同一对象引用指向的对象，
      @  不可使用原子表达式\not_assigned与\not_modified
      @ ensures (\forall int i; 1 <= i && i <= size;
      @          (\forall int j; 0 <= j && j < objectId.length; objectId[j] != elementData[i]) ==>
      @           elementData[i].getReferenced() == \old(elementData[i].getReferenced()));
      @ ensures (\forall int i; 1 <= i && i <= size;
      @          (\exists int j; 0 <= j && i < objectId.length; objectId[j] == elementData[i]) ==>
      @           elementData[i].getReferenced() == false);
      @ ensures (\forall int i; 1 <= i && i <= size;
      @          elementData[i].getId() == \old(elementData[i].getId()));
      @ ensures (\forall int i; 1 <= i && i <= size;
      @          elementData[i].getAge() == \old(elementData[i].getAge()));
      @*/
    public void setUnreferencedId(List<Integer> objectId) {
        for (int id : objectId) {
            for (int i = 1; i <= this.getSize(); i++) {
                MyObject myObject = (MyObject) this.getElementData()[i];
                if (myObject.getId() == id) {
                    myObject.setReferenced(false);
                    this.setElementData(i, myObject);
                }
            }
        }
    }

    /*@ public normal_behavior
      @ assignable elementData, size;
      @ ensures size == (\sum int i; 1 <= i && i <= \old(size) &&
      @                              \old(elementData[i].getReferenced()) == true; 1);
      @ ensures (\forall int i; 1 <= i && i <= \old(size);
      @          \old(elementData[i].getReferenced()) == true ==>
      @           (\exist int j; 1 <= j && j <= size; elementData[j].equals(\old(elementData[i]))))
      @ ensures (\forall int i; 1 <= i && i <= \old(size);
      @          \old(elementData[i].getReferenced()) == false ==>
      @           (\forall int j; 1 <= j && j <= size;
      @           !elementData[j].equals(\old(elementData[i]))))
      @ ensures (\forall int i; 1 <= i && i <= size;
      @          (\exists int j; 1 <= j && j <= \old(size);
      @          elementData[i].equals(\old(elementData[j]))));
      @*/
    public void removeUnreferenced() {
        // TODO
        Object[] oldElementData = getElementData().clone();
        int oldSize = getSize();
        int size = 0;
        clear();
        for (int i = 1; i <= oldSize; i++ ) {
            if (((MyObject)oldElementData[i]).getReferenced() == true) {
                getElementData()[++size] = oldElementData[i];
            }
        }
        setSize(size);
    }

    /*@ public normal_behavior
      @ requires size > 0;
      @ ensures (\exist int i; 1 <= i && i <= size;
      @          (\forall int j; 1 <= j && j <= size && j != i;
      @            elementData[i].compareTo(elementData[j]) == -1) &&
      @           \result == elementData[i]);
      @ also
      @ public normal_behavior
      @ requires size == 0;
      @ ensures \result == null;
      @*/
    public /*@pure@*/ MyObject getYoungestOne() {
        // TODO
        if (getSize() == 0) {
            return null;
        } else if (getSize() > 0) {
            MyObject youngestOne = (MyObject)getElementData()[1];
            for (int i = 1; i < getSize(); i++) {
                if ( ((MyObject)getElementData()[i]).compareTo(youngestOne) == -1) {
                    youngestOne = (MyObject)getElementData()[i];
                }
            }
            return youngestOne;
        }
        return null;
    }
}