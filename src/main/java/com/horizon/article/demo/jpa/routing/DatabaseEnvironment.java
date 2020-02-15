package com.horizon.article.demo.jpa.routing;

/**
 * Thread shared context to point to the datasource which should be used. This
 * enables context switches between different environments.
 *
 * Created by linyanbin on 2020/2/13
 */
public enum DatabaseEnvironment {
    DEVELOPMENT, TESTING, PRODUCTION
}
