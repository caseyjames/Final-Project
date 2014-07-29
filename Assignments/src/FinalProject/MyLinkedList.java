package FinalProject;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a generic doubly linked list.
 *
 * @param <E> - the type of elements contained in the linked list
 * @author Paymon Saebi
 * @author Guy Watson
 * @author Tom Pridham
 *         <p/>
 *         Description:
 */

@SuppressWarnings("UnusedDeclaration")
public class MyLinkedList<E> {
    //Instance variables
    int size;
    Node head;
    Node tail;

    /**
     * Constructor.  Creates a blank linked list.
     */
    public MyLinkedList() {
        size = 0;
        head = null;
        tail = null;
    }

    /**
     * The basic node structure
     */
    private class Node {
        E data;
        Node next;
        Node prev;

        public Node(E element) {
            data = element;
        }
    }

    /**
     * @param element - The element to add at the beginning of the list.
     *                <p/>
     *                Inserts the specified element at the beginning of the list.
     *                O(1) for a doubly-linked list.
     */
    public void addFirst(E element) {
        Node nodeToAdd = new Node(element);
        if (size == 0) {
            head = nodeToAdd;
            tail = nodeToAdd;
            size++;
            return;
        }
        Node temp = head;
        head = nodeToAdd;
        nodeToAdd.next = temp;
        temp.prev = nodeToAdd;
        size++;

    }

    /**
     * @param element - The element to add at the end of the list.
     *                <p/>
     *                Inserts the specified element at the end of the list.
     *                O(1) for a doubly-linked list.
     */
    public void addLast(E element) {
        Node nodeToAdd = new Node(element);
        if (size == 0) {
            head = nodeToAdd;
            tail = nodeToAdd;
            size++;
            return;
        }
        Node temp = tail;
        tail = nodeToAdd;
        nodeToAdd.prev = temp;
        temp.next = nodeToAdd;
        size++;

    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Throws IndexOutOfBoundsException if index is out of range.
     * O(N) for a doubly-linked list.
     */
    public void add(int index, E element) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        Node toInsert = new Node(element);
        Node temp, currentItem;

        //gets the item right before the index
        if (index == 0) {
            addFirst(element);
            return;
        }
        if (index == size - 1) {
            addLast(element);
            return;
        }

        if (index < (size - 1) / 2) {
            //start from head
            currentItem = head;
            for (int i = 1; i < index; i++) {
                currentItem = currentItem.next;
            }
            temp = currentItem.next;

        } else {
            //start from tail
            temp = tail;
            for (int i = size - 1; i > index; i--) {
                temp = temp.prev;
            }
            currentItem = temp.prev;
        }

        currentItem.next = toInsert;
        toInsert.prev = currentItem;
        toInsert.next = temp;
        temp.prev = toInsert;
        size++;

    }

    /**
     * Returns the first element in the list.
     * Throws NoSuchElementException if the list is empty.
     * O(1) for a doubly-linked list.
     */
    public E getFirst() throws NoSuchElementException {
        if (size == 0)
            throw new NoSuchElementException();

        return head.data;
    }

    /**
     * Returns the last element in the list.
     * Throws NoSuchElementException if the list is empty.
     * O(1) for a doubly-linked list.
     */
    public E getLast() throws NoSuchElementException {
        if (size == 0)
            throw new NoSuchElementException();

        return tail.data;
    }

    /**
     * Returns the element at the specified position in the list.
     * Throws IndexOutOfBoundsException if index is out of range.
     * O(N) for a doubly-linked list.
     */
    public E get(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        Node temp;
        if (index < (size - 1) / 2) {
            //start from head
            temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }

        } else {
            //start from tail
            temp = tail;
            for (int i = size - 1; i > index; i--) {
                temp = temp.prev;
            }
        }
        return temp.data;
    }

    /**
     * Removes and returns the first element from the list.
     * Throws NoSuchElementException if the list is empty.
     * O(1) for a doubly-linked list.
     */
    public E removeFirst() throws NoSuchElementException {
        if (size == 0)
            throw new NoSuchElementException();

        Node itemToReturn = head;
        if (size > 1) {
            Node temp = head.next;
            temp.prev = null;
            head = temp;
        } else {
            head = null;
            tail = null;
        }
        size--;
        return itemToReturn.data;
    }

    /**
     * Removes and returns the last element from the list.
     * Throws NoSuchElementException if the list is empty.
     * O(1) for a doubly-linked list.
     */
    public E removeLast() throws NoSuchElementException {
        if (size == 0)
            throw new NoSuchElementException();

        Node itemToReturn = tail;
        if (size > 1) {
            Node temp = tail.prev;
            temp.next = null;
            tail = temp;
        } else {
            head = null;
            tail = null;
        }
        size--;
        return itemToReturn.data;
    }

    /**
     * Removes and returns the element at the specified position in the list.
     * Throws IndexOutOfBoundsException if index is out of range.
     * O(N) for a doubly-linked list.
     */
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 1 || index == 0) {
            return removeFirst();
        } else if (index == size - 1)
            return removeLast();

        Node toRemove;


        if (index < (size - 1) / 2) {
            //start from head
            toRemove = head;
            for (int i = 0; i < index; i++) {
                toRemove = toRemove.next;
            }

        } else {
            //start from tail
            toRemove = tail;
            for (int i = size - 1; i > index; i--) {
                toRemove = toRemove.prev;
            }
        }

        toRemove.prev.next = toRemove.next;
        toRemove.next.prev = toRemove.prev;
        size--;
        return toRemove.data;
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present
     * Returns true if the element was found and removed, false otherwise
     * O(N) for a doubly-linked list.
     */
    public boolean remove(E element) {
        Node currentItem = head;
        for (int i = 0; i < size; i++) {
            if (currentItem.data.equals(element)) {
                //remove the element
                currentItem.prev.next = currentItem.next;
                currentItem.next.prev = currentItem.prev;
                size--;
                return true;
            }
            currentItem = currentItem.next;
        }
        return false;
    }

    /**
     * Returns true if this list contains the specified element
     * or false if this list does not contain the element.
     * O(N) for a doubly-linked list.
     */
    public boolean contains(E element) {
        Node currentItem = head;
        for (int i = 0; i < size; i++) {
            if (currentItem.data.equals(element))
                return true;
            currentItem = currentItem.next;
        }
        return false;
    }

    /**
     * Returns the index of the first occurrence of the specified element in the list,
     * or -1 if this list does not contain the element.
     * O(N) for a doubly-linked list.
     */
    public int indexOf(E element) {
        Node currentItem = head;
        for (int i = 0; i < size; i++) {
            if (currentItem.data.equals(element))
                return i;
            currentItem = currentItem.next;
        }
        return -1;
    }

    /**
     * Returns the index of the last occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     * O(N) for a doubly-linked list.
     */
    public int lastIndexOf(E element) {
        Node currentItem = tail;
        for (int i = size - 1; i >= 0; i--) {
            if (currentItem.data.equals(element))
                return i;
            currentItem = currentItem.prev;
        }
        return -1;
    }

    /**
     * Returns the number of elements in this list.
     * O(1) for a doubly-linked list.
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this collection contains no elements.
     * O(1) for a doubly-linked list.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all of the elements from this list.
     * O(1) for a doubly-linked list.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence
     * (from first to last element).
     * O(N) for a doubly-linked list.
     */
    public Object[] toArray() {
        Object[] arrayToReturn = new Object[size];
        Node currentItem = head;
        for (int i = 0; i < size; i++) {
            arrayToReturn[i] = currentItem.data;
            currentItem = currentItem.next;
        }
        return arrayToReturn;
    }

    private class LinkedListIterator implements Iterator<Node> {
        int index = 0;
        boolean gotNext = false;
        Node current;

        /**
         * Constructor
         */
        public LinkedListIterator() {
            index = 0;
            gotNext = false;
            current = head;
        }

        /**
         * Checks whether this has more elements to look at
         */
        public boolean hasNext() {
            // if there are more items to look at that aren't null
            if (current.next != tail) {
                gotNext = true;
                return true;
            }
            gotNext = false;
            return false;
        }

        /**
         * Returns the next elements and updates the index.
         */
        public Node next() {
            // Checks that there are more elements.
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Node temp = current;
            current = current.next;
            return temp;
        }
    }
}
