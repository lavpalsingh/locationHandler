package restAssured;


import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Method;

import java.io.File;
import java.util.Map;

/**
 * created by lavpal.bhatia on 30/08/2018
 */

public class Request {
    private Map<String, Object> queryParameters;
    private String apiPath;
    private Method requestType;
    private Map<String, String> headers;
    private String mimeType;
    private ContentType contentType;
    private String requestBodyJson;
    private String multipartControlName;
    private Map<String, Object> formData;
    private File inputFile;
    private Cookie cookie;

    private Request(String apiPath, Method requestType, Map<String, String> headers, String requestBodyJson, String multipartControlName, Map<String, Object> queryParameters, ContentType contentType, String mimeType, Map<String, Object> formData, File inputFile, Cookie cookie) {
        this.requestType = requestType;
        this.apiPath = apiPath;
        this.headers = headers;
        this.contentType = contentType;
        this.mimeType = mimeType;
        this.requestBodyJson = requestBodyJson;
        this.queryParameters = queryParameters;
        this.formData = formData;
        this.inputFile = inputFile;
        this.cookie = cookie;
        this.multipartControlName = multipartControlName;
    }

    public static Request.Builder builder() {
        return new Request.Builder();
    }

    Cookie getCookie() {
        return this.cookie;
    }

    File getInputFile() {
        return this.inputFile;
    }

    Map<String, Object> getFormData() {
        return this.formData;
    }

    Map<String, Object> getQueryParameters() {
        return this.queryParameters;
    }

    String getApiPath() {
        return this.apiPath;
    }

    Method getRequestType() {
        return this.requestType;
    }

    Map<String, String> getHeaders() {
        return this.headers;
    }

    ContentType getContentType() {
        return this.contentType;
    }

    String getMimeType() {
        return this.mimeType;
    }

    String getRequestBodyJson() {
        return this.requestBodyJson;
    }

    String getMultipartControlName() {
        return this.multipartControlName;
    }

    public static final class Builder {
        private Map<String, Object> nestedQueryParameters;
        private String nestedApiPath;
        private String nestedMultipartControlName;
        private Method nestedRequestType;
        private Map<String, String> nestedHeaders;
        private ContentType nestedContentType;
        private String nestedMimeType;
        private String nestedRequestBody;
        private Map<String, Object> nestedFormData;
        private File nestedFile;
        private Cookie nestedCookie;

        private Builder() {
        }

        public Request.Builder setCookie(Cookie cookie) {
            this.nestedCookie = cookie;
            return this;
        }

        public Request.Builder setInputFile(File file) {
            this.nestedFile = file;
            return this;
        }

        public Request.Builder setFormData(Map<String, Object> formData) {
            this.nestedFormData = formData;
            return this;
        }

        public Request.Builder setApiPath(String apiPath) {
            this.nestedApiPath = apiPath;
            return this;
        }

        public Request.Builder setRequestType(Method requestType) {
            this.nestedRequestType = requestType;
            return this;
        }

        public Request.Builder setQueryParameters(Map<String, Object> queryParameters) {
            this.nestedQueryParameters = queryParameters;
            return this;
        }

        public Request.Builder setHeaders(Map<String, String> headers) {
            this.nestedHeaders = headers;
            return this;
        }

        public Request.Builder setContentType(ContentType contentType) {
            this.nestedContentType = contentType;
            return this;
        }

        public Request.Builder setMimeType(String nestedMimeType) {
            this.nestedMimeType = nestedMimeType;
            return this;
        }

        public Request.Builder setRequestBody(String requestBody) {
            this.nestedRequestBody = requestBody;
            return this;
        }

        public Request.Builder setMultipartControlName(String multipartControlName) {
            this.nestedMultipartControlName = multipartControlName;
            return this;
        }

        public Request build() {
            return new Request(this.nestedApiPath, this.nestedRequestType, this.nestedHeaders, this.nestedRequestBody, this.nestedMultipartControlName, this.nestedQueryParameters, this.nestedContentType, this.nestedMimeType, this.nestedFormData, this.nestedFile, this.nestedCookie);
        }
    }
}
