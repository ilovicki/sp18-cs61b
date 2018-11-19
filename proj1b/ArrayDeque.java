public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int front;
    private int back;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        front = 0;
        back = 0;
    }

    private void resize(int cap, int start) {
        T[] A = (T[]) new Object[cap];
        if (front < back) {
            System.arraycopy(items, front, A, start, size);
        } else if (front > back) {
            int nBack = items.length - front;
            int nFront = size + front - items.length;
            System.arraycopy(items, front, A, start, nBack);
            System.arraycopy(items, 0, A, start + items.length - front, nFront);
        } else {
            System.out.println("There must be some errors.");
        }
        items = A;
        front = start;
        back = start + size - 1;
    }

    @Override
    public void addFirst(T item) {
        if (size > 0) {
            if ((double) size / items.length < 0.25 && items.length >= 16) {
                resize(items.length / 2, 1);
            }
            if (size == items.length) {
                resize(size * 2, 1);
            }
            front -= 1;
            if (front < 0) {
                front += items.length;
            }
        }
        items[front] = item;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size > 0) {
            if ((double) size / items.length < 0.25 && items.length >= 16) {
                resize(items.length / 2, 0);
            }
            if (size == items.length) {
                resize(size * 2, 0);
            }
            back += 1;
            if (back == items.length) {
                back -= items.length;
            }
        }
        items[back] = item;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;

    }

    @Override
    public int size() {
        return size;

    }

    @Override
    public void printDeque() {
        if (front <= back) {
            for (int i = front; i < front + size; i++) {
                System.out.print(items[i] + " ");
            }
        } else {
            for (int i = front; i < items.length; i++) {
                System.out.print(items[i] + " ");
            }
            for (int j = 0; j < back + 1; j++) {
                System.out.print(items[j] + " ");
            }
        }
        System.out.println();

    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            System.out.println("This is an empty deque. There is nothing to remove.");
            return null;
        } else {
            T first = items[front];
            items[front] = null;
            if (size == 1) {
                front = 0;
                back = 0;
            } else {
                front += 1;
                if (front == items.length) {
                    front -= items.length;
                }
            }
            size -= 1;
            if ((double) size / items.length < 0.25 && items.length >= 16) {
                resize(items.length / 2, 0);
            }
            return first;
        }
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            System.out.println("This is an empty deque. There is nothing to remove.");
            return null;
        } else {
            T last = items[back];
            items[back] = null;
            if (size == 1) {
                front = 0;
                back = 0;
            } else {
                back -= 1;
                if (back < 0) {
                    back += items.length;
                }
            }
            size -= 1;
            if ((double) size / items.length < 0.25 && items.length >= 16) {
                resize(items.length / 2, 0);
            }
            return last;
        }
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            System.out.println("Index out of range.");
        }
        if (front <= back) {
            return items[front + index];
        } else {
            if (index < items.length - front) {
                return items[index + front];
            } else {
                return items[index + front - items.length];
            }
        }

    }
}
