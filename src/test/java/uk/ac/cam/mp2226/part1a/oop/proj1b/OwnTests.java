package uk.ac.cam.mp2226.part1a.oop.proj1b;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class OwnTests {
    @Test
    void test() {
        testListMethodsSame(new WorkingDeque<>(), new ArrayDeque61B<>(),
                toFn(a -> a.addFirst(2)),
                toFn(a -> a.addLast(7)),
                a -> a.get(1),
                toFn(a -> a.addFirst(4)),
                a -> a.get(2),
                toFn(a -> a.addLast(1)),
                a -> a.get(2),
                a -> a.getRecursive(2),
                a -> a.get(-1),
                a -> a.getRecursive(-1),
                a -> a.get(4),
                a -> a.getRecursive(4),
                Deque61B::size,
                Deque61B::removeFirst,
                Deque61B::removeLast,
                Deque61B::removeLast,
                Deque61B::removeFirst,
                Deque61B::size,
                Deque61B::removeLast,
                Deque61B::removeFirst
        );
        ArrayDeque61B<Integer> arr = new ArrayDeque61B<>();
        arr.addFirst(2);
        arr.set(-1, 7);
        arr.set(1, 8);
        assertIterableEquals(List.of(2), arr.toList());
    }

    /// Test that some actions applied to 2 lists return the same output
    /// and result in the same list
    @SafeVarargs
    final <T> void testListMethodsSame(Deque61B<T> workingList, Deque61B<T> testList,
                                       Function<? super Deque61B<T>, ?>... actions) {
        // We must assume consistent starting state
        assertIterableEquals(testList.toList(), workingList.toList());
        int i = 0;
        for(Function<? super Deque61B<T>, ?> a : actions) {
            FunctionResult.fromExecution(() -> (Object) a.apply(workingList))
                    .testExecution(() -> a.apply(testList),  "Action " + i);
            assertIterableEquals(workingList.toList(), testList.toList(), "Action " + i);
            i += 1;
        }
    }

    // Required because `void foo(T arg)` is not a `Function<T, void>` in Java
    // because, once again, Java is annoying and there is no unit type!!!
    private static <T> Function<T, VoidV> toFn(Consumer<? super T> action) {
        return t -> {action.accept(t); return VoidV.INST;};
    }

    // Rust-style enum (tagged union):
    private sealed interface FunctionResult<R, E extends Throwable> {
        static <R, E extends Throwable> FunctionResult<R, E> fromExecution(
                ThrowingSupplier<? extends R, ? extends E> execution,
                Class<E> errorType) {
            return Util.tryCatchElse(execution, errorType, FuncError::new, FuncReturn::new);
        }
        static <R> FunctionResult<R, RuntimeException> fromExecution(
                ThrowingSupplier<? extends R, ? extends RuntimeException> execution) {
            return fromExecution(execution, RuntimeException.class);
        }

        void testExecution(ThrowingSupplier<? extends R, ? extends E> execution, String msg);
    }
    private static final class FuncReturn<R, E extends Throwable>
            implements FunctionResult<R, E> {
        R value;

        public FuncReturn(R value_) {
            value = value_;
        }

        @Override
        public String toString() {
            return "FuncReturn{" + value + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof FuncReturn<?, ?> that)) return false;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public void testExecution(
                ThrowingSupplier<? extends R, ? extends E> execution, String msg) {
            assertEquals(value, assertDoesNotThrow(execution::get, msg), msg);
        }
    }
    private static final class FuncError<R, E extends Throwable>
            implements FunctionResult<R, E> {
        E error;

        public FuncError(E error_) {
            error = error_;
        }

        @Override
        public String toString() {
            return "FuncError{" + error + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof FuncError<?, ?> funcError)) return false;
            return Objects.equals(error, funcError.error);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(error);
        }

        @Override
        public void testExecution(
                ThrowingSupplier<? extends R, ? extends E> execution, String msg) {
            assertThrows(error.getClass(), execution::get, msg);
        }
    }
}

class WorkingDeque<T> extends ArrayList<T> implements Deque61B<T> {
    @Override
    public List<T> toList() {
        return this;
    }

    @Override
    public T getRecursive(int index) {
        return get(index);
    }

    @Override
    public T get(int index) {
        return Util.excGuard(() -> super.get(index), IndexOutOfBoundsException.class, null);
    }

    @Override
    public T removeFirst() {
        return Util.excGuard(super::removeFirst, NoSuchElementException.class, null);
    }

    @Override
    public T removeLast() {
        return Util.excGuard(super::removeLast, NoSuchElementException.class, null);
    }
}
