package com.x.sentinel.utils;

import java.util.List;

public final class SimulatedUserContext {

    private static final ThreadLocal<SimulatedUser> currentUser = new ThreadLocal<>();

    private SimulatedUserContext() {}

    public static void setCurrentUser(SimulatedUser user) {
        currentUser.set(user);
    }

    public static SimulatedUser getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }

    public record SimulatedUser(String username, List<String> roles) {}
}
