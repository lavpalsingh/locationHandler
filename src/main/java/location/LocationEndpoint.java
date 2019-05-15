package location;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import restAssured.Request;
import restAssured.RequestExecutor;

import java.util.HashMap;
import java.util.Map;
import static location.Constant.*;


class LocationEndpoint {

    private final String env;
    private String X_AUTH_TOKEN_VAL;

    LocationEndpoint(String env, String x_auth_token) {
       this.env=env;
        this.X_AUTH_TOKEN_VAL = x_auth_token;
    }

    private void setBaseURL(){
        if (env.equalsIgnoreCase(STAGING)) RestAssured.baseURI = BASE_URL_STAGING;
        else if (env.equalsIgnoreCase(PRODUCTION)) RestAssured.baseURI = BASE_URL_PRODUCTION;
        else RestAssured.baseURI = BASE_URL_STAGING;
    }
    private Request buildRequest(String locationType, String location_name, String longitude, String latitude, long parentId, String approverEmail)  {
        setBaseURL();
        return Request.builder().setApiPath(API_PATH_LOCATION_ADDITION).setHeaders(getHeaders()).setRequestBody(getRequestBody(locationType, location_name, longitude, latitude, parentId, approverEmail)).setRequestType(Method.POST).build();
    }

    private Request buildRequest(long id, String locationType, String location_name, String longitude, String latitude, long parentId, String approverEmail)  {
        setBaseURL();
        return Request.builder().setApiPath(API_PATH_LOCATION_UPDATE).setHeaders(getHeaders()).setRequestBody(getRequestBody(id, locationType, location_name, longitude, latitude, parentId, approverEmail)).setRequestType(Method.POST).build();
    }

    private Request buildRequest(long id, String supersededByLocationId, String observations, String approverEmail)  {
        setBaseURL();
        return Request.builder().setApiPath(API_PATH_LOCATION_DEPRECATE).setHeaders(getHeaders()).setRequestBody(getRequestBody(id, supersededByLocationId, observations, approverEmail)).setRequestType(Method.POST).build();
    }

    private Request buildRequest(String key1, String key2)  {
        RestAssured.baseURI = BASE_URL_VYOM_INTERNAL;

        Map<String, Object> query = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(key1);
        if (key2 != null) {
            sb.append(",");
            sb.append(key2);
        }
        query.put("input", sb.toString());
        return Request.builder().setApiPath(API_PATH_LOCATION_AUTOCOMPLETE).setHeaders(getHeaders()).setQueryParameters(query).setRequestType(Method.GET).build();
    }

    Response add(String locationType, String location_name, String longitude, String latitude, long parentId, String approverEmail) {
        try {
            Request request = buildRequest(locationType, location_name, longitude, latitude, parentId, approverEmail);
            RequestExecutor executor = new RequestExecutor();
            Response response = executor.executeRequest(request);
            System.out.println("Response : " + response.asString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Response update(long id, String locationType, String location_name, String longitude, String latitude, long parentId, String approverEmail) {
        try {
            Request request = buildRequest(id, locationType, location_name, longitude, latitude, parentId, approverEmail);
            RequestExecutor executor = new RequestExecutor();
            Response response = executor.executeRequest(request);
            System.out.println("Response : " + response.asString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Response deprecate(long id, String supersededByLocationId, String observations, String approverEmail) {
        try {
            Request request = buildRequest(id, supersededByLocationId, observations, approverEmail);
            RequestExecutor executor = new RequestExecutor();
            Response response = executor.executeRequest(request);
            System.out.println("Response : " + response.asString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Response get_suggestion(String key1, String key2) {
        try {
            Request request = buildRequest(key1, key2);
            RequestExecutor executor = new RequestExecutor();
            Response response = executor.executeRequest(request);
            System.out.println("Response : " + response.asString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getRequestBody(String locationType, String location_name, String longitude, String latitude, long parentId, String approverEmail) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("dataset", "in");
        if (locationType != null && !locationType.isEmpty()) requestParams.put("locationType", locationType);
        JSONArray names_array = new JSONArray();
        JSONObject name_hi = new JSONObject();
        if (location_name != null && !location_name.isEmpty()) name_hi.put("name", location_name);
        name_hi.put("lang", "hi");
        name_hi.put("primary", true);

        JSONObject name_en = new JSONObject();
        if (location_name != null && !location_name.isEmpty()) name_en.put("name", location_name);
        name_en.put("lang", "en");
        name_en.put("primary", true);

        names_array.put(name_en);
        names_array.put(name_hi);

        requestParams.put("names", names_array);
        requestParams.put("longitude", longitude);
        requestParams.put("latitude", latitude);
        requestParams.put("approverEmail", approverEmail);

        requestParams.put("parentId", parentId);
        requestParams.put("postcode", "");

        System.out.println(requestParams.toString());
        return requestParams.toString();
    }

    private String getRequestBody(long id, String locationType, String location_name, String longitude, String latitude, long parentId, String approverEmail) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("dataset", "in");
        requestParams.put("locationId", id);
        if (locationType != null && !locationType.isEmpty()) requestParams.put("locationType", locationType);
        JSONArray names_array = new JSONArray();
        JSONObject name_hi = new JSONObject();
        if (location_name != null && !location_name.isEmpty()) name_hi.put("name", location_name);
        name_hi.put("lang", "hi");
        name_hi.put("primary", true);

        JSONObject name_en = new JSONObject();
        if (location_name != null && !location_name.isEmpty()) name_en.put("name", location_name);
        name_en.put("lang", "en");
        name_en.put("primary", true);

        names_array.put(name_en);
        names_array.put(name_hi);

        requestParams.put("names", names_array);
        requestParams.put("longitude", longitude);
        requestParams.put("latitude", latitude);
        requestParams.put("approverEmail", approverEmail);

        requestParams.put("parentId", parentId);
        requestParams.put("postcode", "");

        System.out.println(requestParams.toString());
        return requestParams.toString();
    }

    private String getRequestBody(long id, String supersededByLocationId, String observations, String approverEmail) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("dataset", "in");
        requestParams.put("locationId", id);
        requestParams.put("supersededByLocationId", supersededByLocationId);
        requestParams.put("observations", observations);
        requestParams.put("approverEmail", approverEmail);

        System.out.println(requestParams.toString());
        return requestParams.toString();
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.putIfAbsent(CONTENT_TYPE, CONTENT_TYPE_JSON);
        headers.putIfAbsent(X_AUTH_TOKEN, X_AUTH_TOKEN_VAL);
        return headers;
    }




}
