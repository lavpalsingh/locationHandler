package location;

import excelHandler.ExcelReader;
import excelHandler.ExcelWriter;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;

class LocationData {

    private String approver;
    private static final String SUCCESS = "Success";
    private static final String FAILURE = "Failure";
    private static final String PARENT_NOT_FOUND = "Parent Not Found";
    private int current_row;
    private LocationEndpoint a;
    private HashMap<String, Long> city_cache = new HashMap<>();
    private String sheet_path;
    static final String LOCALITY = "sublocality_level_1";
    static final String CITY = "admin_level_3";
    static final String STATE = "admin_level_1";
    private String location_type ;
    private String operation_type;

    LocationData(String operation,String location_type,String approver,String x_auth_token,String env,String sheet_path){
        this.location_type = location_type;
        this.approver =approver;
        a = new LocationEndpoint(env,x_auth_token);
        this.sheet_path = sheet_path;
        this.operation_type = operation;
    }

    void processData(int row){
        switch(operation_type){
            case "add":
                addData(row);
                break;
            case "deprecate":
                deprecateData(row);
                break;
            case "update":
                updateData(row);
                break;
                default:
                    break;
        }
    }

   private void addData(int row) {
        ExcelReader er;
        int ptype =1;
        try {
            current_row = row;
            er = new ExcelReader(sheet_path, 0, 5);
            ArrayList<String> input = er.readRow(row);
            String city = input.get(0);
            String state = input.get(1);
            String locality = input.get(2);
            String lat = input.get(3);
            String lon = input.get(4);

            if(this.location_type.equalsIgnoreCase(CITY))
                ptype =0;

            long parentID = getParentID(city, state,ptype);
            System.out.println("parent " + parentID);
            if (parentID != -1) {
                int id = add_location(location_type, locality, lon, lat, parentID, approver);
                if (id != -1) {
                    write_comment(SUCCESS);
                    write_localityID(id);
                }
            } else
                write_comment(PARENT_NOT_FOUND);
            er.closeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateData(int row) {
        ExcelReader er;
        int ptype =1;
        try {
            current_row = row;
            er = new ExcelReader(sheet_path, 0, 7);
            ArrayList<String> input = er.readRow(row);
            String city = input.get(0);
            String state = input.get(1);
            String locality = input.get(2);
            String lat = input.get(3);
            String lon = input.get(4);
            String loc_id = input.get(6);
            if(this.location_type.equalsIgnoreCase(CITY))
                ptype =0;

            long parentID = getParentID(city, state,ptype);
            long loc_id_ = 0;

            System.out.println("parent " + parentID);
            if (parentID != -1) {
                try {
                    loc_id_ = Long.parseLong(loc_id);
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }
                int id = update_location(loc_id_, location_type, locality, lon, lat, parentID, approver);
                if (id != -1) {
                    write_comment(SUCCESS);
                }
            } else
                write_comment(PARENT_NOT_FOUND);
            er.closeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deprecateData(int row) {
        ExcelReader er;
        try {
            current_row = row;
            er = new ExcelReader(sheet_path, 0, 3);
            ArrayList<String> input = er.readRow(row);
            String loc_id = input.get(0);
            String supersededByLocationId = input.get(1);
            String observations = input.get(2);
            long loc_id_;
            if(loc_id!=null) {
                loc_id_ = Long.parseLong(loc_id);
                int id = deprecate_location(loc_id_, supersededByLocationId, observations, approver);
                if (id != -1) {
                    write_comment(SUCCESS);
                } else {
                    write_comment(FAILURE);
                }
            }else {
                write_comment(FAILURE);
            }

            er.closeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void write_localityID(int id) {
        ExcelWriter ew;
        try {
            ew = new ExcelWriter(sheet_path, 0);
            ew.writeRowVal(current_row, 7, id);
            ew.closeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void write_comment(String comment) {
        ExcelWriter ew;
        try {
            ew = new ExcelWriter(sheet_path, 0);
            ew.writeRowVal(current_row, 6, comment);
            ew.closeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private long getParentID(String city, String state,int parent_type) {
        if (city_cache.containsKey(city)) {
            return city_cache.get(city);
        } else {
            Response r = a.get_suggestion(city, state);
            ArrayList<HashMap<String, Object>> arr = r.path("data");
            if (arr.size() > 0) {
                HashMap<String, Object> obj = arr.get(0);
                String name = (String) obj.get("name");
                if (!name.equalsIgnoreCase(city)) {
                    return -1;
                } else {
                   String path ="data[0].addressComponents["+parent_type+"].name";
                    String state_name = r.path(path);
                    System.out.println("State fetched from response " + state_name);
                    if (state_name.equalsIgnoreCase(state)) {
                        long city_id = Long.parseLong(String.valueOf(obj.get("id")));
                        city_cache.put(city, city_id);
                        return city_id;
                    } else
                        return -1;
                }
            } else {

                return -1;
            }
        }
    }


    private int add_location(String location_type, String locality, String longitude, String latitude, long parentID, String approver) {
        Response r = a.add(location_type, locality, longitude, latitude, parentID, approver);
        return r.path("data.id");
    }

    private int update_location(long id, String location_type, String locality, String longitude, String latitude, long parentID, String approver) {
        Response r = a.update(id, location_type, locality, longitude, latitude, parentID, approver);
        return r.path("data.id");
    }

    private int deprecate_location(long id, String supersededByLocationId, String observations, String approverEmail) {
        Response r = a.deprecate(id, supersededByLocationId, observations, approverEmail);
        return r.path("data.id");
    }

}
