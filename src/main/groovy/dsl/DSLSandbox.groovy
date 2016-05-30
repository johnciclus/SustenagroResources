package dsl

import org.kohsuke.groovy.sandbox.GroovyValueFilter
import semantics.DataReader

/**
 * Created by john on 09/09/15.
 */
class DSLSandbox extends GroovyValueFilter{
    @Override
    Object filter(Object o) {
        //println "#[" + o.getClass() + "] " + o.toString()

        if (o==null || ALLOWED_TYPES.contains(o.getClass()))
            return o;
        if (o instanceof Script || o instanceof Closure)
            return o; // access to properties of compiled groovy script
        throw new SecurityException("Oops, unexpected type: "+o.getClass());
    }

    private static final Set<Class> ALLOWED_TYPES = [
            Class,
            Boolean,
            Integer,
            Float,
            Double,
            String,
            Binding,
            ArrayList,
            Object[],
            LinkedHashMap,
            DataReader,
            EvaluationObject,
            Feature
    ] as Set
}
