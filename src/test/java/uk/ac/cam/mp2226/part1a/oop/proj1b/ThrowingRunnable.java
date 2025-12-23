package uk.ac.cam.mp2226.part1a.oop.proj1b;

@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {
    void run() throws E;

    default ThrowingSupplier<VoidV, E> toSupplier() {
        return () -> {
            run();
            return VoidV.INST;
        };
    }
}
