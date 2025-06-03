package util.list;

import java.io.Serializable;

/**
 *
 * @author german
 */
public class Item<I> implements Serializable {

    private I n;
    private Item<I> next;
    private Item<I> prev;

    public Item(I n) {
        this.n = n;
    }

    public I get() {
        return n;
    }

    public Item<I> getNext() {
        return next;
    }
    
    public void setNext(Item<I> next) {
        this.next = next;
    }
    
    public Item<I> getPrevious() {
        return prev;
    }
    
    public void setPrevious(Item<I> prev) {
        this.prev = prev;
    }
}
