package gcsimulation;

public class MyHeap<T extends Comparable<T>> {
    //@ public instance model non_null T[] elementData;
    //@ invariant (\forall int i; 2 <= i && i <= size; elementData[i / 2].compareTo(elementData[i]) == -1);
    //@ [2]; 要求：书写不变式，描述小顶堆的性质，即任意父节点均小于其子节点，必须使用推理操作符"==>"；
    //   描述时可直接引入T类的compareTo方法，T类的对象A小于对象B等价于A.compareTo(B) == -1；关于小顶堆的样例见指导书

    private /*spec_public@*/ Object[] elementData;
    private /*spec_public@*/ int capacity;
    private /*spec_public@*/ int size;

    MyHeap(int capacity) {
        elementData = new Object[capacity + 1];
        this.capacity = capacity;
        this.size = 0;
    }

    //@ ensures \result == size;
    public /*@pure@*/ int getSize() {
        return size;
    }

    //@ ensures \result == elementData
    public /*@pure@*/ Object[] getElementData() {
        return elementData;
    }

    /*@ public normal_behavior
      @ requires index >= 1 && index <= getSize();
      @ assignable elementData;
      @ ensures (\forall int i; 1 <= i && i <= getSize() && i != index;
      @          \not_modified(elementData[i]));
      @ ensures elementData[index] == element;
      @*/
    public void setElementData(int index, T element) {
        this.elementData[index] = element;
    }

    /*@ public normal_behavior
      @ assignable size;
      @ ensures size == 0;
      @*/
    public void clear() {
        this.size = 0;
    }

    /*@ public normal_behavior
      @ requires newSize >= 0;
      @ assignable size;
      @ ensures size == newSize;
      @*/
    public void setSize(int newSize) {
        this.size = newSize;
    }

    /*@ public normal_behavior
      @ requires indexA >= 1 && indexA <= getSize() && indexB >= 1 && indexB <= getSize();
      @ assignable elementData;
      @ ensures (\forall int i; 1 <= i && i <= getSize() && i != indexA && i != indexB;
      @          \not_modified(elementData[i]));
      @ ensures elementData[indexA] == \old(elementData[indexB]);
      @ ensures elementData[indexB] == \old(elementData[indexA]);
      @*/
    private void swap(int indexA, int indexB) {
        T temp = (T) elementData[indexA];
        elementData[indexA] = elementData[indexB];
        elementData[indexB] = temp;
    }

    public void add(T newElement) {
        if (size == capacity) {
            Object[] oldElementData = elementData.clone();
            capacity = capacity << 1;
            elementData = new Object[capacity + 1];
            for (int i = 1; i <= size; i++) {
                elementData[i] = oldElementData[i];
            }
        }
        elementData[++size] = newElement;
        int tempIndex = size;
        while (tempIndex / 2 != 0 &&
                ((T) elementData[tempIndex]).compareTo((T) elementData[tempIndex / 2]) < 0) {
            swap(tempIndex, tempIndex / 2);
            tempIndex /= 2;
        }
    }

    public void removeFirst() {
        if (size == 0) {
            System.err.println("No element found in list.");
            return;
        }
        elementData[1] = elementData[size--];
        int index = 1;
        while (true) {
            if (index > size) {
                break;
            }
            if (index * 2 + 1 <= size) {
                if (((T) elementData[index]).compareTo((T) elementData[index * 2]) > 0 ||
                        ((T) elementData[index]).compareTo((T) elementData[index * 2 + 1]) > 0) {
                    if (((T) elementData[index * 2 + 1]).compareTo(
                            (T) elementData[index * 2]) > 0) {
                        swap(index * 2, index);
                        index = 2 * index;
                    } else {
                        swap(index * 2 + 1, index);
                        index = 2 * index + 1;
                    }
                }
            } else if (index * 2 <= size) {
                if (((T) elementData[index]).compareTo((T) elementData[index * 2]) > 0) {
                    swap(index * 2, index);
                    index = 2 * index;
                }
            } else {
                break;
            }
        }
    }
}