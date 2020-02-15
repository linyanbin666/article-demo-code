package com.horizon.article.demo.jpa.routing;

/**
 * Created by linyanbin on 2020/2/13
 */
public class DatabaseContextHolder {

    private static final ThreadLocal<DatabaseEnvironment> CONTEXT =
        new ThreadLocal<>();

    public static void set(DatabaseEnvironment databaseEnvironment) {
        CONTEXT.set(databaseEnvironment);
    }

    public static DatabaseEnvironment getEnvironment() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
