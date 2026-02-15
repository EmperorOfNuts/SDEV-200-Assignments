package M4A3;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class TwoWayLinkedList<E> implements MyList<E> {
    private Node<E> head, tail;
    private int size = 0;

    public TwoWayLinkedList() {} // Empty List

    public TwoWayLinkedList(E[] objects) {
        for (E object : objects) add(object); // Create List from Array of Objects
    }

    @Override
    public void add(E e) { addLast(e); }

    @Override
    public void add(int index, E e) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        if (index == 0) addFirst(e);
        else if (index == size) addLast(e);

        else {
            Node<E> current = getNode(index);
            Node<E> newNode = new Node<>(e);

            newNode.prev = current.prev;
            newNode.next = current;
            current.prev.next = newNode;
            current.prev = newNode;

            size++;
        }
    }

    public void addFirst(E e) {
        Node<E> newNode = new Node<>(e);

        if (head == null) head = tail = newNode; // addFirst when null
        else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }

        size++;
    }

    public void addLast(E e) {
        Node<E> newNode = new Node<>(e);

        if (tail == null) head = tail = newNode; // addLast when null
        else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return getNode(index).element;
    }

    public E getFirst() {
        if (size == 0) return null;
        return head.element;
    }

    public E getLast() {
        if (size == 0) return null;
        return tail.element;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        if (index == 0) return removeFirst();
        else if (index == size - 1) return removeLast();
        else {
            Node<E> current = getNode(index);

            // Remove current node
            current.prev.next = current.next;
            current.next.prev = current.prev;

            size--;
            return current.element;
        }
    }

    @Override
    public boolean remove(E e) {
        Node<E> current = head;

        while (current != null) {
            if ((e == null && current.element == null) || (e != null && e.equals(current.element))) {

                if (current == head) removeFirst();
                else if (current == tail) removeLast();
                else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    size--;
                }
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public E removeFirst() {
        if (size == 0) return null;

        Node<E> temp = head;
        head = head.next;

        if (head != null) head.prev = null;
        else tail = null;

        size--;
        return temp.element;
    }

    public E removeLast() {
        if (size == 0) return null;

        Node<E> temp = tail;
        tail = tail.prev;

        if (tail != null) tail.next = null;
        else head = null;

        size--;
        return temp.element;
    }

    @Override
    public int indexOf(E e) {
        Node<E> current = head;
        int index = 0;

        while (current != null) {
            if ((e == null && current.element == null) || (e != null && e.equals(current.element))) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(E e) {
        Node<E> current = tail;
        int index = size - 1;

        while (current != null) {
            if ((e == null && current.element == null) || (e != null && e.equals(current.element))) return index;
            current = current.prev;
            index--;
        }
        return -1;
    }

    @Override
    public E set(int index, E e) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        Node<E> node = getNode(index);
        E oldValue = node.element;
        node.element = e;
        return oldValue;
    }

    @Override
    public boolean contains(E e) { return indexOf(e) >= 0; }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<E> iterator() { return new LinkedListIterator(); }

    @Override
    public ListIterator<E> listIterator() { return new LinkedListIterator(); }

    @Override
    public ListIterator<E> listIterator(int index) { return new LinkedListIterator(index); }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<E> current = head;

        while (current != null) {
            result.append(current.element);
            current = current.next;
            if (current != null) result.append(", ");
        }

        result.append("]");
        return result.toString();
    }

    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    // Node Class
    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        public Node(E element) { this.element = element; }
    }

    // ListIterator Class
    private class LinkedListIterator implements ListIterator<E> {
        private Node<E> current;
        private Node<E> lastAccessed;
        private int index;
        private boolean canRemove;

        public LinkedListIterator() {
            this(0);
        }

        public LinkedListIterator(int index) {
            if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            this.index = index;
            this.current = (index == size) ? null : getNode(index);
            this.canRemove = false;
        }

        @Override
        public boolean hasNext() { return index < size; }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();

            lastAccessed = current;
            E element = current.element;
            current = current.next;
            index++;
            canRemove = true;
            return element;
        }

        @Override
        public boolean hasPrevious() { return index > 0;}

        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();

            if (current == null) current = tail;
            else current = current.prev;

            lastAccessed = current;
            index--;
            canRemove = true;
            return current.element;
        }

        @Override
        public int nextIndex() { return index; }

        @Override
        public int previousIndex() { return index - 1; }

        @Override
        public void remove() {
            if (!canRemove) throw new IllegalStateException();

            if (lastAccessed == head) removeFirst();
            else if (lastAccessed == tail) removeLast();
            else {
                lastAccessed.prev.next = lastAccessed.next;
                lastAccessed.next.prev = lastAccessed.prev;
                size--;
            }

            if (lastAccessed == current) current = current.next;
            else index--;

            lastAccessed = null;
            canRemove = false;
        }

        @Override
        public void set(E e) {
            if (!canRemove) throw new IllegalStateException();
            lastAccessed.element = e;
        }

        @Override
        public void add(E e) {
            if (current == null) addLast(e);
            else {
                Node<E> newNode = new Node<>(e);

                if (current.prev == null) {
                    // Insert at Beginning
                    newNode.next = head;
                    head.prev = newNode;
                    head = newNode;
                } else {
                    // Insert before Current
                    newNode.prev = current.prev;
                    newNode.next = current;
                    current.prev.next = newNode;
                    current.prev = newNode;
                }
                size++;
                index++;
            }
            canRemove = false;
        }
    }
}

interface MyList<E> extends Iterable<E> {
    void add(E e);
    void add(int index, E e);

    void clear();

    boolean contains(E e);

    E get(int index);

    int indexOf(E e);

    boolean isEmpty();

    int lastIndexOf(E e);

    boolean remove(E e);
    E remove(int index);

    E set(int index, E e);

    int size();

    ListIterator<E> listIterator();
    ListIterator<E> listIterator(int index);
}