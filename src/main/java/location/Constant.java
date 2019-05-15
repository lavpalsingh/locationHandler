package location;

class Constant {

     static final String STAGING = "staging";
     static final String PRODUCTION = "production";
     static final String BASE_URL_STAGING = "https://polly-staging.platform.olx.berlin";
     static final String BASE_URL_PRODUCTION = "https://polly.platform.olx.berlin";
     static final String BASE_URL_VYOM_INTERNAL = "https://api.olx.in/v1/real-estate/business/internal";
     private static final String API_PATH_LOCATION = "/api/internal/datasets/in/changes/";
     static final String CONTENT_TYPE = "Content-Type";
     static final String CONTENT_TYPE_JSON = "application/json";
     static final String X_AUTH_TOKEN ="X-Auth-Token";
     static final String API_PATH_LOCATION_ADDITION = API_PATH_LOCATION + "location_addition";
     static final String API_PATH_LOCATION_UPDATE = API_PATH_LOCATION + "location_update";
     static final String API_PATH_LOCATION_DEPRECATE = API_PATH_LOCATION + "location_deprecation";
     static final String API_PATH_LOCATION_AUTOCOMPLETE = "/location/autocomplete";
}
