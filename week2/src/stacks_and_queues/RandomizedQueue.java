package stacks_and_queues;/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Node<Item> top;
    private int size;

    public RandomizedQueue() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (top == null) {
            top = new Node<>();
            top.value = item;
        }
        else {
            Node<Item> oldFirst = top;
            Node<Item> node = new Node<>();
            node.value = item;
            top = node;
            node.next = oldFirst;
            oldFirst.previous = node;
        }
        size++;
    }

    public Item dequeue() {
        if (top == null) throw new NoSuchElementException();
        Item item;
        Node<Item> itemNode = top;
        if (size == 1) {
            top = null;
            size = 0;
            item = itemNode.value;
        }
        else {
            int random = StdRandom.uniform(1, (size / 2) + 1);
            while (random >= 1) {
                itemNode = itemNode.next;
                random--;
            }
            item = itemNode.value;
            deleteItem(itemNode);
            size--;
        }
        return item;
    }

    public Item sample() {
        if (top == null) throw new NoSuchElementException();
        Item item;
        Node<Item> itemNode = top;
        if (size == 1) {
            return top.value;
        }
        else {
            int random = StdRandom.uniform(1, (size / 2) + 1);
            while (itemNode.next != null && random > 1) {
                itemNode = itemNode.next;
                random--;
            }
            item = itemNode.value;
            return item;

        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node<Item> itemNode = top;
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

    private void deleteItem(Node<Item> itemNode) {
        if (itemNode.previous != null) {
            itemNode.previous.next = itemNode.next;
        }
        if (itemNode.next != null) {
            itemNode.next.previous = itemNode.previous;
        }
    }

    private static class Node<Item> {
        Node<Item> next;
        Node<Item> previous;
        Item value;
    }
}

