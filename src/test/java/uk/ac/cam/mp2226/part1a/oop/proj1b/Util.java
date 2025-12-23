package uk.ac.cam.mp2226.part1a.oop.proj1b;

import java.util.function.Function;

public class Util {
    private Util() {}

    // Kinda like OCaml Result.fold if the ThrowingSupplier is `unit -> ('t, 'e) result`
    public static <T, E extends Throwable, R> R tryCatchElse(
            ThrowingSupplier<? extends T, ? extends E> try_,
            Class<E> runtimeClass, Function<? super E, ? extends R> catch_,
            Function<? super T, ? extends R> else_) {
        T result;
        try {
            result = try_.get();
        } catch (Throwable exc) {
            if (runtimeClass.isInstance(exc)) {
                // SAFETY: We check above that exc is assignment compatible to E
                //noinspection unchecked
                return catch_.apply((E) exc);
            }
            // Here, exc will not an instance of E but since E is the only
            // checked exception type, the exception must be an unchecked
            // exception here but Java is too stupid to realise
            else if (exc instanceof RuntimeException e) {
                throw e;
            } else if (exc instanceof Error e) {
                throw e;
            } else {
                assert false : "Only checked exceptions should reach here";
                throw new RuntimeException(exc);  // if assertions off
            }
        }
        return else_.apply(result);
    }

    public static <R, E extends Throwable> R excGuard(
            ThrowingSupplier<R, E> fn, Class<E> excType,
            R defaultValue) {
        return tryCatchElse(fn, excType, e -> defaultValue, r -> r);
    }

    public static <E extends Throwable> void excGuard(
            ThrowingRunnable<E> fn, Class<E> excType) {
        excGuard(fn.toSupplier(), excType, VoidV.INST);
    }
}
