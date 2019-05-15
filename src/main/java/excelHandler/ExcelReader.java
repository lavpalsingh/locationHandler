package excelHandler;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExcelReader extends ExcelUtil {

    private static int MAX_COL = 48;

    public ExcelReader(String filepath, int sheet_index, int max_col) throws IOException {

        super(filepath, sheet_index);
        if (max_col != 0) {
            MAX_COL = max_col;
        }
    }


    public Sheet getSheet() {
        return sheet;
    }

    public Row getRow(int row_index) {
        return sheet.getRow(row_index);
    }

    public ArrayList<String> readRow(int row_index) throws Exception {
        row_index--;


        ArrayList<String> result_row = new ArrayList<>();
        Row row = sheet.getRow(row_index);

        int count = 0;
        while (count < MAX_COL) {
            Cell col = row.getCell(count);
            Object o = null;
            count++;
            if (col != null) {
                switch (col.getCellTypeEnum()) {
                    case FORMULA:
                        o = col.getCellFormula();

                        break;
                    case NUMERIC:

                        if (HSSFDateUtil.isCellDateFormatted(col)) {

                            String pattern = "yyyy-MM-dd";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                            o = simpleDateFormat.format(HSSFDateUtil.getJavaDate(col.getNumericCellValue()));

                        } else {
                            double number = col.getNumericCellValue();
                            if (Double.isInfinite(number) || Double.isNaN(number) || Math.floor(number) != number)
                                o = Double.toString(number);
                            else
                                o = String.valueOf(new Double(number).intValue());
                        }
                        break;
                    case BOOLEAN:
                        o = Boolean.toString(col.getBooleanCellValue());

                        break;
                    case STRING:
                        o = col.getStringCellValue();

                        break;
                    case ERROR:
                        o = col.getErrorCellValue();
                        break;

                    default:
                        break;
                }
                if (o != null)
                    result_row.add(o.toString());
                else
                    result_row.add(null);

            } else {
                result_row.add(null);
            }
        }
        return result_row;
    }

    public Object readRowVal(int row_index, int col_index) throws Exception {
        Object o = null;
        row_index--;
        col_index--;
        Row row = sheet.getRow(row_index);
        Cell col = row.getCell(col_index);
        switch (col.getCellTypeEnum()) {

            case NUMERIC:
                o = col.getNumericCellValue();
                break;
            case BOOLEAN:
                o = col.getBooleanCellValue();
                break;
            case STRING:
                o = col.getStringCellValue();
                break;
            case ERROR:
                o = col.getErrorCellValue();
                break;

            default:

                break;
        }

        return o;
    }

}
