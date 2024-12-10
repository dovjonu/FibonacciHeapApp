package lt.dovydasjonuska.fibonacciheapapp.testing;

import lt.dovydasjonuska.fibonacciheapapp.utils.FibonacciHeap;

public class test {
    public static void main(String[] args) {
        FibonacciHeap<Integer> heap = new FibonacciHeap<>();
        for(int i = 0; i < 10; i++) {
            heap.insert(i);
        }
        heap.consolidate();
    }
}
