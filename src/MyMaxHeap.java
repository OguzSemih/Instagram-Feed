import java.util.Iterator;

public class MyMaxHeap<T extends Comparable<T>>  implements Iterable<T> {
    private T[] heap;
    private int size;
    private static int initialCapacity = 10; // initial capacity
    
    public MyMaxHeap() { // constructor
        this.size = 0;
        this.heap = (T[]) new Comparable[initialCapacity];
    }

    // methods to calculate parent and child places in array
    private int parent(int index) {
        int parentIndex = (index - 1) / 2;
        return parentIndex;
    }

    private int leftChild(int index) {
        int leftChildIndex = 2 * index + 1;
        return leftChildIndex;
    }

    private int rightChild(int index) {
        int rightChildIndex = 2 * index + 2;
        return rightChildIndex;
    }


    private void resize() { // it resizes the heap
        T[] newHeap = (T[]) new Comparable[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    // insert a new value into the heap
    public void insert(T value) {
        if (size == heap.length) {
            resize(); // increase the capacity dynamically
        }
        heap[size] = value;
        int current = size;
        size++;

        // percolate up
        while (current != 0 && heap[parent(current)].compareTo(heap[current]) < 0) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public T extractMax() { // this gives maximum element and delete it
        if (size <= 0) {
            return null;
        }
        else if (size == 1) {
            size--;
            return heap[0];
        }

        T root = heap[0];
        heap[0] = heap[size - 1];
        size--;
        maxHeapify(0);
        return root;
    }
    private void maxHeapify(int i) { //this ensures maintaining maxheap property
        int left = leftChild(i);
        int right = rightChild(i);
        int largest = i;

        if (left < size && heap[left].compareTo(heap[largest]) > 0)
            largest = left;

        if (right < size && heap[right].compareTo(heap[largest]) > 0)
            largest = right;

        if (largest != i) {
            swap(i, largest);
            maxHeapify(largest);
        }
    }
    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    
    


    public int getSize() {
        return size;
    }

    public Iterator<T> iterator() {
        return new MaxHeapIterator();
    }

    private class MaxHeapIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements in heap.");
            }
            return heap[currentIndex++];
        }
    }

}
