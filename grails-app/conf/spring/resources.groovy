import sustenagro.SustenAgroMemStore

// Place your Spring DSL code here
beans = {
    memStore(SustenAgroMemStore){
    }
    println memStore
}
