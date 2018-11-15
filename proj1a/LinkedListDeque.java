public class LinkedListDeque<T> {
    private class LLNode {
        public T item;
        public LLNode previous;
        public LLNode next;

        public LLNode(T item, LLNode previous, LLNode next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
    }

    private LLNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new LLNode(null, null, null);
        size = 0;
    }

    public void addFirst(T item) {
        LLNode first = new LLNode(item, sentinel, null);
        if (size == 0) {
            first.next = sentinel;
            sentinel.previous = first;
        } else {
            first.next = sentinel.next;
            sentinel.next.previous = first;
        }
        sentinel.next = first;
        size += 1;
    }

    public void addLast(T item) {
        LLNode last = new LLNode(item, null, sentinel);
        if (size == 0) {
            last.previous = sentinel;
            sentinel.next = last;
        } else {
            last.previous = sentinel.previous;
            sentinel.previous.next = last;
        }
        sentinel.previous = last;
        size += 1;

    }

    public boolean isEmpty() {
        return sentinel.previous == null && sentinel.next == null;
    }

    public int size() {
        return size;

    }

    public void printDeque() {
        LLNode current = sentinel.next;
        for (int i = 0; i < size; i += 1) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println();

    }

    public T removeFirst() {
        if (size == 0) {
            System.out.println("The size of the deque is 0. There is nothing to remove.");
            return null;
        } else {
            LLNode first = sentinel.next;
            T item0 = first.item;
            if (size > 1) {
                LLNode newFirst = sentinel.next.next;
                sentinel.next = newFirst;
                newFirst.previous = sentinel;
            } else {
                sentinel.previous = null;
                sentinel.next = null;
            }
            first.item = null;
            first.previous = null;
            first.next = null;
            size -= 1;
            return item0;

        }
    }

    public T removeLast() {
        if (size == 0) {
            System.out.println("This is an empty deque. There is nothing to remove.");
            return null;
        } else {
            LLNode last = sentinel.previous;
            T item0 = last.item;
            if (size > 1) {
                LLNode newLast = sentinel.previous.previous;
                sentinel.previous = newLast;
                newLast.next = sentinel;
            } else {
                sentinel.previous = null;
                sentinel.next = null;
            }
            last.item = null;
            last.previous = null;
            last.next = null;
            size -= 1;
            return item0;

        }
    }

    public T get(int index) {
        if (index >= size) {
            System.out.println("Index out of range.");
            return null;
        }
        LLNode current = sentinel.next;
        for (int i = 0; i < size; i += 1) {
            current = current.next;
        }
        return current.item;

    }
    private LLNode getLLNode(LLNode A, int index){
        if (index == 0){
            return A;
        }
        else{
            return getLLNode(A.next, index - 1);
        }
    }



    public T getRecursive(int index) {
        if (index >= size) {
            System.out.println("Index out of range.");
            return null;
        }
        LLNode indexNode = getLLNode(sentinel.next, index);
        return indexNode.item;

    }
}

