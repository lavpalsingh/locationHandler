package location;


import location.exception.*;

public class Driver {
    public static void main(String[] args) {
        try {
            Driver driver = new Driver();
            driver.runScript();
        } catch (BadLocalityTypeException | InvalidBoundException | InvalidOperationTypeException | InvalidApproverException | InvalidAuthTokenException | InvalidEnvironmentException | InvalidSheetPathException e) {
            e.printStackTrace();
        }

    }
    /*
     *
     * Environment Variables that must be set before the script execution
     *
     * ltype - locality, city or state
     * operation - add, update or deprecate
     * approver- email id of the approver is required
     * x-auth-token- session token must be provided after login in polly dashboard
     * env - staging, production
     * sheet_path - excel sheet path with data in proper format must be provided
     * start - start row number ( row starting from 1)
     * end - end row number
     *
     * */

    private void runScript() throws BadLocalityTypeException, InvalidBoundException, InvalidOperationTypeException, InvalidApproverException, InvalidAuthTokenException, InvalidEnvironmentException, InvalidSheetPathException {
        String location_type;
        int start = 2, end;

        location_type = System.getProperty("ltype");
        System.out.println("location type: "+location_type);
        if (location_type != null) {
            switch (location_type) {
                case "locality":
                    location_type = LocationData.LOCALITY;
                    break;
                case "city":
                    location_type = LocationData.CITY;
                    break;
                case "state":
                    location_type = LocationData.STATE;
                    break;
                default:
                    throw new BadLocalityTypeException("Invalid Locality Type");
            }
        } else {
            throw new BadLocalityTypeException("Locality Type is required");
        }


        String operation = System.getProperty("operation");
        System.out.println("operation type: "+operation);
        if (operation == null) {
            throw new InvalidOperationTypeException("Operation Type is required");
        }

        String approver = System.getProperty("approver");
        System.out.println("approver: "+approver);
        if (approver == null) {
            throw new InvalidApproverException("Approver Email ID is required");
        }

        String x_auth_token = System.getProperty("x-auth-token");
        System.out.println("x_auth_token: "+x_auth_token);
        if (x_auth_token == null) {
            throw new InvalidAuthTokenException("X-AUTH-TOKEN is required");
        }

        String env = System.getProperty("env");
        System.out.println("env: "+env);
        if (env == null) {
            throw new InvalidEnvironmentException("Environment is required");
        }

        String sheet_path = System.getProperty("sheet_path");
        System.out.println("sheet_path: "+sheet_path);
        if (sheet_path == null) {
            throw new InvalidSheetPathException("Excel Sheet path is required");
        }

        String start_row = System.getProperty("start");
        System.out.println("start_row: "+start_row);
        String end_row = System.getProperty("end");
        System.out.println("end_row: "+end_row);
        if (start_row != null)
            start = Integer.parseInt(start_row);
        if (end_row != null)
            end = Integer.parseInt(end_row);
        else
            throw new InvalidBoundException("Invalid row end value");

        LocationData d = new LocationData(operation, location_type, approver, x_auth_token, env, sheet_path);
        for (int i = start; i <= end; i++)
            d.processData(i);
    }

}
