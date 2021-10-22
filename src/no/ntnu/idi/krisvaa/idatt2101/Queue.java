package no.ntnu.idi.krisvaa.idatt2101;

public class Queue<T> {

    private T[] tab;
    private int start;
    private int end;
    private int count;

    public Queue(int size) {
        tab = (T[]) new Object[size];
    }

    public boolean empty() {
        return  count == 0;
    }

    public boolean full() {
        return count == tab.length;
    }

    public void addToQueue(T e) {
        if(full()) return;

        tab[end] = e;

        end = (end + 1) % tab.length;
        count++;
    }

    public T getNextInQueue() {
        if(empty()) return null;

        T  e = tab[start];
        start = (start + 1) % tab.length;
        count--;
        return e;
    }

    public T peekQueue() {
        if(empty()) return null;
        else return tab[start];
    }

}
