package restAssured;


import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * created by lavpal.bhatia on 30/08/2018
 */
public class RequestExecutor {
    private ByteArrayOutputStream byteArrayOutputStream;
    private String request;
    private String response;

    public RequestExecutor() {
    }

    public String getRequest() {
        return this.request;
    }

    private void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return this.response;
    }

    private void setResponse(String response) {
        this.response = response;
    }

    public Response executeRequest(Request request) {
        RequestSpecification requestSpecification = this.generateRequestSpec(request);
        Response response = RestAssured.given().spec(requestSpecification).when().log().all(false).request(request.getRequestType());
        this.setRequest(this.getStringFromByteArrayOutputStream());
        response = this.checkRedirection(response, request.getRequestType(), requestSpecification);
        response.then().log().ifError();
        this.setResponse(this.getStringFromByteArrayOutputStream());
        return response;
    }

    private RequestSpecification generateRequestSpec(Request request) {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        if (request.getApiPath() != null) {
            builder.setBasePath(request.getApiPath());
        }

        if (request.getCookie() != null) {
            builder.addCookie(request.getCookie());
        }

        if (request.getInputFile() != null) {
            MultiPartSpecification multipartSpecification = this.getMultipartSpecification(request);
            builder.addMultiPart(multipartSpecification);
        }

        if (request.getFormData() != null) {
            builder.addFormParams(request.getFormData());
        }

        if (request.getRequestBodyJson() != null) {
            builder.setBody(request.getRequestBodyJson());
        }

        if (request.getContentType() != null) {
            builder.setContentType(request.getContentType());
        }

        if (request.getHeaders() != null) {
            builder.addHeaders(request.getHeaders());
        }

        if (request.getQueryParameters() != null) {
            builder.addQueryParams(request.getQueryParameters());
        }

        builder.setConfig(this.getRequestLogConfig());
        builder.log(LogDetail.ALL);
        return builder.build();
    }

    private Response checkRedirection(Response response, Method requestType, RequestSpecification requestSpecification) {
        if (response.getStatusCode() == 301) {
            String redirectURL = response.getHeader("Location");
            return RestAssured.given().spec(requestSpecification).request(requestType, redirectURL, new Object[0]);
        } else {
            return response;
        }
    }

    private RestAssuredConfig getRequestLogConfig() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        return RestAssured.config().logConfig(new LogConfig(new PrintStream(this.byteArrayOutputStream), true));
    }

    private String getStringFromByteArrayOutputStream() {
        String content = new String(this.byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        this.byteArrayOutputStream.reset();
        return content;
    }

    private MultiPartSpecification getMultipartSpecification(Request request) {
        return (new MultiPartSpecBuilder(request.getInputFile())).controlName(request.getMultipartControlName()).mimeType(request.getMimeType()).build();
    }
}
