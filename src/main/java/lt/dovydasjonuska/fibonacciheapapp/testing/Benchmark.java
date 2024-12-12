package lt.dovydasjonuska.fibonacciheapapp.testing;

import lt.dovydasjonuska.fibonacciheapapp.utils.FibonacciHeap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class Benchmark {

    private FibonacciHeap<Integer> heap;

    private FibonacciHeap<Integer> withoutHolesHeap;

    @Param({"1000", "10000", "50000"})
    public int elementCount;

    @Setup(Level.Invocation)
    public void setupHeap() {
        heap = new FibonacciHeap<>();
        for (int i = 0; i < elementCount; i++) {
            heap.insert((int) (Math.random() * elementCount));
        }
    }

    @Setup(Level.Invocation)
    public void setupWithoutHolesHeap() {
        withoutHolesHeap = new FibonacciHeap<>();
        java.util.List<Integer> elements = new java.util.ArrayList<>();
        for (int i = 0; i < elementCount; i++) {
            elements.add(i);
        }
        java.util.Collections.shuffle(elements);
        for (int value : elements) {
            withoutHolesHeap.insert(value);
        }
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkInsert() {
        heap.insert((int) (Math.random() * elementCount));
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkExtractMin() {
        heap.extractMin();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkFindMin() {
        heap.findMin();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkConsolidate() {
        heap.consolidate();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkDecreaseKey() {
        RandomGenerator random = RandomGenerator.getDefault();
        int oldKey = random.nextInt(100, elementCount);
        int newKey = (int) random.nextInt(0, oldKey);
        withoutHolesHeap.decreaseKey(oldKey, newKey);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkRemove() {
        RandomGenerator random = RandomGenerator.getDefault();
        int key = random.nextInt(0, elementCount);
        withoutHolesHeap.remove(key);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}