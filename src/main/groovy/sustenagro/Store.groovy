/**
 * Created by dilvan on 6/18/15.
 */

package sustenagro

import com.tinkerpop.blueprints.impls.sail.impls.MemoryStoreSailGraph
import com.tinkerpop.gremlin.groovy.Gremlin

/**
 * Created by dilvan on 6/18/15.
 */
class Store {
    private static MemoryStoreSailGraph store;
    static{
        Gremlin.load()
        store = new MemoryStoreSailGraph()
        //g.addNamespace('foaf','http://xmlns.com/foaf/0.1/')
        //g.addNamespace('owl','http://www.w3.org/2002/07/owl#')
        g.addNamespace('sustenagro','http://www.biomac.icmc.usp.br:8080/sustenagro#')

        g.loadRDF(new FileInputStream('ontology/SustenAgroOntology.rdf'), 'http://www.biomac.icmc.usp.br:8080/sustenagro#', 'rdf-xml', null)

        store.loadRDF(new FileInputStream(
                '/Applications/Bigdata/BIGDATA_RELEASE_1_5_0/ant-build/gremlin-groovy-2.5.0/data/graph-example-1.ntriple'),
                'http://tinkerpop.com#', 'n-triples', null)
    }

    static MemoryStoreSailGraph getG() {store}
}
