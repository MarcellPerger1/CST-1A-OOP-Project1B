package uk.ac.cam.mp2226.part1a.oop.proj1b;

import java.util.List;

public class ArrayDeque61B<T> implements Deque61B<T> {
    T[] arr;
    int start;
    int len;

    // In reality, I'd make this bigger but this makes it easier to test
    private static final int INITIAL_CAPACITY = 1;

    public ArrayDeque61B() {
        arr = createArray(INITIAL_CAPACITY);
        start = 0;
        len = 0;
    }

    @Override
    public int size() {
        return len;
    }
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public T get(int index) {
        if(!validIndex(index)) return null;
        return arr[getArrIndex(index)];
    }
    @Override
    public T getRecursive(int index) {
        return get(index);  // Just to satisfy their weird interface
    }
    public void set(int index, T value) {
        if(validIndex(index)) arr[getArrIndex(index)] = value;
    }

    @Override
    public List<T> toList() {
        return List.of(linearizedArray(len));
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) return null;
        T result = arr[start];
        start = wrapIdx(start + 1);
        len -= 1;
        return result;
    }
    @Override
    public T removeLast() {
        if(isEmpty()) return null;
        T result = get(size() - 1);
        len -= 1;
        return result;
    }

    @Override
    public void addFirst(T x) {
        growIfNeeded();
        start = wrapIdx(start - 1);
        arr[start] = x;
        len += 1;
    }
    @Override
    public void addLast(T x) {
        growIfNeeded();
        len += 1;
        set(size() - 1, x);
    }

    protected void growIfNeeded() {
        if(!isFull()) return;
        int newCapacity = capacity() * 2;
        arr = linearizedArray(newCapacity);
        start = 0;
    }

    protected T[] linearizedArray(int capacity) {
        T[] newArr = createArray(capacity);
        // Now copy everything over. I will use arraycopy for performance.
        // I do not trust the 61B's opinion without my own verification.
        int firstLength = min(len, arr.length - start);  // Only copy up to end of array
        arrayCopy(arr, start, newArr, 0, firstLength);
        if(start + len > arr.length) {  // Wraps round, so copy extra
            arrayCopy(arr, 0, newArr, firstLength, len - firstLength);
        }
        return newArr;
    }

    protected int getArrIndex(int index) {
        return wrapIdx(index + start);
    }
    protected int wrapIdx(int index) {
        return Math.floorMod(index, capacity());
    }

    protected int capacity() {
        return arr.length;
    }
    protected boolean isFull() {
        return capacity() == len;
    }

    protected T[] createArray(int size) {
        // SAFETY: Object[] can store any T so this works
        //noinspection unchecked
        return (T[])new Object[size];
    }

    private boolean validIndex(int i) {
        return i >= 0 && i < size();
    }

    /// Same as `System.arraycopy` but with actually intelligent types
    private static <T> void arrayCopy(T[] src, int srcPos,
                                      T[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }
    /// We can't use java.util so here's my min
    private static int min(int a, int b) {
        //noinspection ManualMinMaxCalculation
        return a < b ? a : b;
    }
}
