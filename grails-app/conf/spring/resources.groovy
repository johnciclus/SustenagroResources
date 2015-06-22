import sustenagro.MemStore

// Place your Spring DSL code here
beans = {
    memStore(MemStore){
    }
    println memStore
}
