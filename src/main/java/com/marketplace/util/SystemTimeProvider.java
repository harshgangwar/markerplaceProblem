package com.marketplace.util;
import java.time.Instant;
public final class SystemTimeProvider implements TimeProvider { public Instant now() { return Instant.now(); } }
