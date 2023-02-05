package net.hypnosis.util;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractIterator<T extends @Nullable Object> implements Iterator<T> {
    private enum State {
        /**
         * Compute the next element with {@link #computeNext()} and not yet returned.
         */
        READY,
        /**
         * Not computed yet, or we've already returned the element.
         */
        NOT_READY,
        /**
         * Finished computing elements, so {@link #hasNext()}
         * will always return false and {@link #next()} will always throw {@link NoSuchElementException}.
         */
        DONE,
        /**
         * Exception was thrown by the computation.
         */
        FAILED,
    }

    private State state = State.NOT_READY;
    private T next;

    protected abstract T computeNext();

    protected final T endOfData() {
        state = State.DONE;
        return null;
    }

    @Override
    public final boolean hasNext() {
        if (state == State.FAILED) {
            throw new IllegalStateException();
        }

        return switch (state) {
            case DONE -> false;
            case READY -> true;
            default -> tryToComputeNext();
        };
    }

    private boolean tryToComputeNext() {
        state = State.FAILED;
        next = computeNext();

        if (state != State.DONE) {
            state = State.READY;
            return true;
        }

        return false;
    }

    @Override
    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        state = State.NOT_READY;

        T result = next;
        next = null;

        return result;
    }

    public final T peek() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
