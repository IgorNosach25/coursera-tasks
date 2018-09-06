package stacks_and_queues;/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    public Deque() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (first == null) {
            first = new Node<>();
            first.value = item;
            last = first;
        }
        else {
            Node<Item> oldFirst = first;
            Node<Item> node = new Node<>();
            node.value = item;
            first = node;
            node.next = oldFirst;
            oldFirst.previous = node;
        }
        size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (last == null) {
            last = new Node<>();
            last.value = item;
            first = last;
        }
        else {
            Node<Item> oldLast = last;
            Node<Item> node = new Node<>();
            node.value = item;
            last = node;
            oldLast.next = node;
            last.previous = oldLast;
        }
        size++;
    }

    public Item removeFirst() {
        if (first == null) throw new NoSuchElementException();
        if (size == 1) return removeLastElement();
        Node<Item> oldFirst = first;
        Node<Item> nextFirst = first.next;
        nextFirst.previous = null;
        first = nextFirst;
        size--;
        return oldFirst.value;
    }

    public Item removeLast() {
        if (last == null) throw new NoSuchElementException();
        if (size == 1) return removeLastElement();
        else {
            Node<Item> oldLast = last;
            last = last.previous;
            size--;
            return oldLast.value;
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node<Item> itemNode = first;
            @Override
            public boolean hasNext() {
                return itemNode != null;
            }
            @Override
            public Item next() {
                if (itemNode == null) throw new NoSuchElementException();
                Item value = itemNode.value;
                itemNode = itemNode.next;
                return value;
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static class Node<Item> {
        Node<Item> next;
        Node<Item> previous;
        Item value;
    }

    private Item removeLastElement() {
        Node<Item> itemNode = first;
        first = null;
        last = null;
        size = 0;
        return itemNode.value;
    }
}
