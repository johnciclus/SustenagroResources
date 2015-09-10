package dsl

import org.kohsuke.groovy.sandbox.GroovyValueFilter

/**
 * Created by john on 09/09/15.
 */
class DSLSandbox extends GroovyValueFilter{
    @Override
    Object filter(Object o) {
        println "#:("+o.class+") " + o
        if (o==null || ALLOWED_TYPES.contains(o.class))
            return o;
        if (o instanceof Script || o instanceof Closure)
            return o; // access to properties of compiled groovy script
        throw new SecurityException("Oops, unexpected type: "+o.class);
    }

    private static final Set<Class> ALLOWED_TYPES = [
            Class,
            DSL,
            String,
            Integer,
            Boolean
            // all the primitive types should be OK, but I'm too lazy

            // I'm not adding Class, which rules out all the static method calls
    ] as Set
}
