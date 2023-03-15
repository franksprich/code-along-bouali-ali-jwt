package edu.security.jwt.user;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {DESCRIPTION}
 *
 * @author Frank Sprich
 */
@Getter
public enum Role {

    USER("USER"),
    ADMIN("ADMIN");

    private final String label;

    private static final Map<String, Role> lookup;

    static {
        lookup = Arrays.stream(Role.values()).collect(Collectors.toMap(Role::getLabel, Function.identity()));
    }

    public static Role lookup(String label) {
        return lookup.get(label);
    }

    Role(String label) {
        this.label = label;
    }
}
