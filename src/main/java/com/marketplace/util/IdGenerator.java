package com.marketplace.util;
import java.util.concurrent.atomic.AtomicLong;
/** Monotonic, thread-safe listing ID generator starting at 100001. */
public final class IdGenerator {
    private final AtomicLong next = new AtomicLong(100001L);
    public long nextId() { return next.getAndIncrement(); }
}
