import java.util.Iterator;

public class MyHashTable<K, V> implements Iterable<V> {

    private static class Node<K, V> { // node class to implement separate chaining
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Node<K, V>[] table;
    private int size;
    private static int initialCapacity = 11; // it should be prime number
    private static float loadFactor = 0.75f;


    public MyHashTable() { // constructor
        this.table = new Node[initialCapacity];
        this.size = 0;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length); // hash function

    }

    private void resize() {   // resize the table when l oad factor is exceeded
        if ((float) size / table.length < loadFactor) {
            return;
        }

        Node<K, V>[] myOldTable = table;
        table = new Node[myOldTable.length * 2];
        size = 0;
        for (Node<K, V> head : myOldTable) {
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }

    public void put(K key, V value) {
        int index = hash(key);

        Node<K, V> currentList = table[index];
        while (currentList != null) {
            if ((currentList.key == null && key == null) || (currentList.key != null && currentList.key.equals(key))) {
                currentList.value = value; // updating exist value
                return;
            }
            currentList = currentList.next;
        }

        // add new node to head of  list
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        resize();
    }

    public V get(K key) { // get value from table
        int index = hash(key);

        Node<K, V> currentList = table[index];
        while (currentList != null) {
            if ((currentList.key == null && key == null) || (currentList.key != null && currentList.key.equals(key))) {
                return currentList.value; // return value
            }
            currentList = currentList.next;
        }

        return null; // no such key
    }

    public void remove(K key) {
        int index = hash(key);

        Node<K, V> currentList = table[index];
        Node<K, V> prev = null;

        while (currentList != null) {
            if ((currentList.key == null && key == null) || (currentList.key != null && currentList.key.equals(key))) {
                if (prev == null) {
                    table[index] = currentList.next; // Remove head node
                } else {
                    prev.next = currentList.next; // Remove anotherd node
                }
                size--; // size decreases by 1
                return;
            }
            prev = currentList;
            currentList = currentList.next;
        }
    }

    public boolean containsKey(K key) {
        if(get(key) == null) {
            return false;
        }
        return true;
    }

    public Iterator<V> iterator() {
        return new HashTableIterator();
    }

    private class HashTableIterator implements Iterator<V> {
        private int currentIndex = 0;
        private Node<K, V> currentNode = null;

        public HashTableIterator() {
            findNextValidSlot();
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new IllegalStateException("no more elements hash table.");
            }

            V value = currentNode.value;
            currentNode = currentNode.next;

            if (currentNode == null) {
                currentIndex++;
                findNextValidSlot();
            }

            return value;
        }

        private void findNextValidSlot() {
            while (currentIndex < table.length && (table[currentIndex] == null)) {
                currentIndex++;
            }

            if (currentIndex < table.length) {
                currentNode = table[currentIndex];
            }
        }

    }

}
