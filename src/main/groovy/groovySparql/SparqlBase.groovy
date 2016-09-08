/*
 * Copyright (c) 2015-2016 Dilvan Moreira.
 * Copyright (c) 2015-2016 John Garavito.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package groovySparql

import groovy.sparql.Sparql
import groovy.util.logging.*

import org.apache.jena.query.Query
import org.apache.jena.query.QueryExecution
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.query.QuerySolution
import org.apache.jena.query.ResultSet
import org.apache.jena.query.Syntax
import org.apache.jena.update.UpdateExecutionFactory
import org.apache.jena.update.UpdateFactory
import org.apache.jena.update.UpdateRequest
import org.apache.jena.update.UpdateProcessor
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.RDFNode
import org.apache.jena.rdf.model.Literal

/**
 * SparqlBase
 *
 * Primary class for working with a SPARQL endpoint
 *
 * Can be initialized with an endpoint, an update endpoint (for SPARQL 1.1 Update),
 * or an Apache Jena model
 *
 * @author Dilvan Moreira
 * @author John Garavito
 *
 */
@Slf4j
class SparqlBase extends Sparql{

    /**
     * Static Factory
     * @param url sparql endpoint URL
     * @return instance of Sparql
     */
    static SparqlBase newInstance(String url) {
        new SparqlBase(endpoint:url)
    }

    /**
     * Static factory
     * @param model Apache Jena model
     * @return instance of Sparql
     */
    static SparqlBase newInstance(Model model) {
        new SparqlBase(model:model)
    }

    /**
     * Constructor
     * Construct the Sparql object with an Apache Jena model
     * @param model
     */
    SparqlBase(Model model) { super(model);}

    /**
     * Constructor
     * Endpoint can be a query endpoint
     * If updateEndpoint is not set, this parameter will be used for SPARQL update
     * @param endpoint
     */
    SparqlBase(String endpoint) { super(endpoint)}

    /**
     * Constructor
     *
     * Add configuration via a map, used for credentials for the HTTP Layer
     *
     * @param model
     * @param config
     */
    SparqlBase(Model model, Map config) { super(model, config)}

    /**
     * Constructor
     * @param endpoint
     * @param config
     */
    SparqlBase(String endpoint, Map config) { super(endpoint, config)}

    /**
     * Empty constructor
     * The properties of the Sparql class are public so this can be used if property injection happens eleswhere
     * This allows this class to be easliy used in dependency injection frameworks, where you can either —
     * constructor injection or property injection post-construction
     */
    SparqlBase() { super()}

    /**
     * <code>update</code>
     * @param query - SPARQL Update query
     *
     * This method will attempt to use the updateEndpoint, and default to endpoint
     *
     */
    void update(String sparql) {
        try {
            UpdateRequest request = UpdateFactory.create(sparql, Syntax.syntaxARQ)
            UpdateProcessor up = UpdateExecutionFactory.createRemoteForm(request, endpoint);
            up.execute();
        } catch (Exception e) {
            System.err.println("Error $endpoint $sparql ${e.getMessage()}");
            e.printStackTrace();
        }
        //Context ctx = new Context()
        //println "Sparql encode"
        //println URLEncoder.encode(sparql)

        //ctx.set(Symbol.create('update'), URLEncoder.encode(sparql))
        //UpdateProcessRemoteForm processor = new UpdateProcessRemoteForm(request, endpoint, ctx)

        //UpdateProcessor up = UpdateExecutionFactory.createRemote(request, endpoint)
        //up.execute()
        /*try {
            println up.execute()
        }
        catch (JenaException e) {
            log.error "Error executing update with ${sparql}", e
        }/*

        /*try {
            HttpContext httpContext = new BasicHttpContext()
            CredentialsProvider provider = new BasicCredentialsProvider()
            //provider.setCredentials(new AuthScope(AuthScope.ANY_HOST,
            //        AuthScope.ANY_PORT), new UsernamePasswordCredentials(user, pass))
            //httpContext.setAttribute(ClientContext.CREDS_PROVIDER, provider)

            UpdateRequest request = UpdateFactory.create()

            request.add(sparql)

            def ep = (updateEndpoint != null) ? updateEndpoint: endpoint

            UpdateProcessor processor = UpdateExecutionFactory
                    .createRemoteForm(request, ep)
            ((UpdateProcessRemoteForm)processor).setHttpContext(httpContext)
            processor.execute()

        } catch (JenaException e) {
            log.error "Error executing update with ${sparql}", e
            throw new SparqlException(e)
        }*/
    }

    def query(String sparql, String lang) {
        def res = []

        Query query
        QueryExecution qe = null

        try{
            query = QueryFactory.create(sparql, Syntax.syntaxARQ)
            qe = QueryExecutionFactory.sparqlService(endpoint, query)
        }
        catch (all){
            println 'Exception: '+all
        }

        /*
         * Some explanation here - ARQ can provide a QE based on a pure
         * SPARQL service endpoint, or a Jena model, plus you can still
         * do remote queries with the model using the in-SPARQL "service"
         * keyword.
         */

            /*if (model) {
            qe = QueryExecutionFactory.create(query, model)
        } else {
            if (!endpoint) {
                return
            }
            qe = QueryExecutionFactory.sparqlService(endpoint, query)
            if (config.timeout) {
                ((QueryEngineHTTP) qe).addParam(timeoutParam, config.timeout as String)
            }
            if (user) {
                ((QueryEngineHTTP) qe).setBasicAuthentication(user, pass?.toCharArray())
            }
        }*/
        try{
            QuerySolution sol
            Map<String, Object> row, last = [:]
            boolean add
            String varName
            RDFNode varNode
            Literal literal
            boolean existingRow

            for (ResultSet rs = qe.execSelect(); rs.hasNext();) {
                sol = rs.nextSolution()
                row = [:]
                add = true

                for (Iterator<String> varNames = sol.varNames(); varNames.hasNext();) {
                    varName = varNames.next()
                    varNode = sol.get(varName)

                    //println varName + ": " +varNode + (varNode.isLiteral())? 'Literal' : 'Not Literal'


                    if (varNode.isLiteral())
                        literal = varNode.asLiteral()

                    if (varName != 'label' || literal.language == lang)
                        row.put(varName, (varNode.isLiteral() ? literal.value : varNode.toString()))

                    else if (lang == '*' && varNode.isLiteral() && literal.getLanguage()) {
                        row.put(varName + '@' + literal.getLanguage(), literal.getString())
                    }

                    //println varNode.isLiteral()
                    //println (varNode.isLiteral() ? literal.value : varNode.toString())

                    if (lang != '' &&
                        varNode.isLiteral() &&
                        literal.language != null &&
                        literal.language.size() > 1 && literal.language != lang) add = false
                }

                if (lang == '*') {
                    existingRow = true
                    row.each { key, value ->
                        if (!key.startsWith('label')) {
                            existingRow = existingRow && (last[key] == value)
                            if (!existingRow) return true
                        }
                    }

                    if (existingRow) {
                        row.each { key, value ->
                            if (key.startsWith('label')) {
                                last.put(key, value)
                            }
                        }
                        add = false
                    } else {
                        add = true
                    }
                    last = row
                }

                if (add)
                    res.push(row)
                //closure.delegate = row
                //closure.call()
            }
        }
        catch (all){
            println 'Exception: '+all
        }
        finally {
            if(qe)
                qe.close()
        }
        return res
    }
}