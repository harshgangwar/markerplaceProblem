package com.marketplace.cli;

import java.util.*;

/** Parses whitespace-separated tokens while preserving single-quoted multi-word values. */
public final class CommandParser {
    public List<String> parse(String line) {
        List<String> tokens = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\'') { quoted = !quoted; continue; }
            if (Character.isWhitespace(c) && !quoted) {
                if (current.length() > 0) { tokens.add(current.toString()); current.setLength(0); }
            } else current.append(c);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed quote");
        if (current.length() > 0) tokens.add(current.toString());
        return tokens;
    }
}
