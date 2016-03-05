package dsl

import org.springframework.context.ApplicationContext
/**
 * Created by john on 27/02/16.
 */
class Analysis {
    def _id
    def _ctx
    def k
    def gui
    def model

    Analysis(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')
        gui = _ctx.getBean('gui')
        model = []
    }
}
