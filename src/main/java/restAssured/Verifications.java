package restAssured;


import io.restassured.response.Response;

/**
 * created by lavpal.bhatia on 30/08/2018
 */
public interface Verifications {

    void verifyStatusCode(Response var1, int var2) throws AssertionError;

    void verifyJsonSchema(Response var1, String var2) throws AssertionError;

    void verifyStatusMessage(Response var1, Status var2) throws AssertionError;
}
