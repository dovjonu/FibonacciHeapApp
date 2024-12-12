package lt.dovydasjonuska.fibonacciheapapp.testing;

import lt.dovydasjonuska.fibonacciheapapp.utils.FibonacciHeap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class Benchmark {

    private FibonacciHeap<Integer> heap;

    @Param({"1000", "10000", "50000"})
    public int elementCount;

    @Setup(Level.Iteration)
    public void setupHeap() {
        heap = new FibonacciHeap<>();
        for (int i = 0; i < elementCount; i++) {
            heap.insert((int) (Math.random() * elementCount));
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

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}