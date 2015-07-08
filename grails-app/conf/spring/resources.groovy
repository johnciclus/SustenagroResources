import sustenagro.MemStore
import sustenagro.SlurpRDF

// Place your Spring DSL code here
beans = {
    g(MemStore)
    s(SlurpRDF,g)
}
