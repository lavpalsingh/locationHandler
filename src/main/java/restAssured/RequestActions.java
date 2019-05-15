package restAssured;


import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

/**
 * created by lavpal.bhatia on 30/08/2018
 */
public interface RequestActions<T> {
    String getJsonSchemaDocument() throws Exception;

    Map<String, String> getHeaders() throws Exception ;

    Map<String, Object> getQueryParameters() throws Exception ;

    Object fromResponseToObject(Response var1, Class<T> var2) throws Exception ;

    Response executeRequestAndGetResponse(Request var1) throws Exception ;

    Request buildRequest() throws Exception ;

    Request buildRequest(String var1) throws Exception;

    Request buildRequest(Map<String, Object> var1) throws Exception ;
    
    Request buildRequest(File file) throws Exception;

    String constructRequestBody() throws Exception ;

    Map<String, Object> constructFormData() throws Exception ;

	Request buildRequest(String s1, String s2) throws  Exception;
}
