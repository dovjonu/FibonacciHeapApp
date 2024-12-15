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
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Threads(10)
@Fork(0)
public class Benchmark {

    /// Setup methods

    private FibonacciHeap<Integer> heap;

    private FibonacciHeap<Integer> consolidatedHeap;

    private FibonacciHeap<Integer> withoutHolesHeap;

    private FibonacciHeap<Integer> consolidatedWithoutHolesHeap;

    @Param({"1000", "5000", "10000", "50000", "100000"})
    public int elementCount;

    @Setup(Level.Iteration)
    public void setupHeap() {
        heap = new FibonacciHeap<>();
        for (int i = 0; i < elementCount; i++) {
            heap.insert((int) (Math.random() * elementCount));
        }
    }

    @Setup(Level.Iteration)
    public void setupConsolidatedHeap() {
        consolidatedHeap = new FibonacciHeap<>();
        for (int i = 0; i < elementCount; i++) {
            consolidatedHeap.insert((int) (Math.random() * elementCount));
        }
        consolidatedHeap.consolidate();
    }

    @Setup(Level.Iteration)
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

    @Setup(Level.Iteration)
    public void setupConsolidatedWithoutHolesHeap() {
        consolidatedWithoutHolesHeap = new FibonacciHeap<>();
        java.util.List<Integer> elements = new java.util.ArrayList<>();
        for (int i = 0; i < elementCount; i++) {
            elements.add(i);
        }
        java.util.Collections.shuffle(elements);
        for (int value : elements) {
            consolidatedWithoutHolesHeap.insert(value);
        }
        consolidatedWithoutHolesHeap.consolidate();
    }

    /// Benchmark methods

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkInsert() {
        heap.insert((int) (Math.random() * elementCount));
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkExtractMin() {
        heap.insert(heap.extractMin());
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkExtractMinFromConsolidated() {
        consolidatedHeap.insert(consolidatedHeap.extractMin());
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
    public void benchmarkUnion() {
        FibonacciHeap<Integer> unionHeap = new FibonacciHeap<>();
        for (int i = 0; i < 100; i++) {
            unionHeap.insert((int) (Math.random() * elementCount));
        }
        unionHeap.union(heap);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkDecreaseKey() {
        RandomGenerator random = RandomGenerator.getDefault();
        int oldKey = random.nextInt(100, elementCount);
        int newKey = (int) random.nextInt(0, oldKey);
        withoutHolesHeap.decreaseKey(oldKey, newKey);
        withoutHolesHeap.insert(oldKey);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkDecreaseKeyFromConsolidated() {
        RandomGenerator random = RandomGenerator.getDefault();
        int oldKey = random.nextInt(100, elementCount);
        int newKey = (int) random.nextInt(0, oldKey);
        consolidatedWithoutHolesHeap.decreaseKey(oldKey, newKey);
        consolidatedWithoutHolesHeap.insert(oldKey);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkRemove() {
        RandomGenerator random = RandomGenerator.getDefault();
        int key = random.nextInt(0, elementCount);
        withoutHolesHeap.remove(key);
        withoutHolesHeap.insert(key);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}