package util.list;

/**
 *
 * @author german
 */
public class List<I> {

    private Item<I> start;
    private Item<I> last;
    private Item<I> reverse;
    private int size = 0;

    public void insert(I n) {
        if (start == null) {
            Item<I> i = new Item<>(n);
            start = i;
            last = i;
        } else {
            Item<I> i = new Item<>(n);
            i.setPrevious(last);
            last.setNext(i);
            last = i;
        }
        size++;
    }

    public void remove(int j) {
        if (j < 0 || j >= size) {
            throw new IndexOutOfBoundsException("Index " + j + " out of range: " + "[0 - " + size + "]");
        }
        Item<I> i = start;
        int c = 0;
        while (i != null) {
            if (c == j) {
                if (i.getPrevious() != null) {
                    i.getPrevious().setNext(i.getNext());
                } else {
                    start = i.getNext();
                    if (start == null) {
                        last = null;
                        reverse = null;
                        size--;
                        return;
                    }
                }
                if (i.getNext() != null) {
                    i.getNext().setPrevious(i.getPrevious());
                } else {
                    last = i.getPrevious();
                    last.setNext(null);
                }
                i.setPrevious(null);
                i.setNext(null);
                size--;
                break;
            }
            i = i.getNext();
            c++;
        }
    }

    public I get(int i) {
        int j = 0;
        Item<I> b = start;

        while (b != null) {
            if (i == j) {
                return b.get();
            } else {
                j++;
                b = b.getNext();
            }
        }

        throw new IndexOutOfBoundsException(i);
    }

    public Item<I> reverse() {
        if (reverse == null) {
            Item<I> i = last;
            reverse = last.getPrevious();
            return i;
        } else {
            Item<I> i = reverse;
            reverse = reverse.getPrevious();
            return i;
        }
    }

    public int getSize() {
        return size;
    }
}
