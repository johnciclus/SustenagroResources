package dsl

/**
 * Created by john on 27/02/16.
 */
class Unity {
    def _id
    def _k

    Unity(String id, k){
        _id = id
        _k = k
    }

    def feature(Map args = [:], String clsName, String prop = ''){
        def featureId = _k.shortURI(clsName)
        def dataType = _k[clsName].getRange()
        def widget = _k.DSL['gui']['dataTypeToWidget'][dataType]

        args['id'] = featureId
        return ['id': featureId,
                'widget': widget.toLowerCase(),
                'request': (prop?.trim()) ? [prop, clsName] : [],
                'args': args]
    }
}
