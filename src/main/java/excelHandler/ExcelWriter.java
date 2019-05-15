package excelHandler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.util.ArrayList;

public class ExcelWriter extends ExcelUtil {

	public ExcelWriter(String filepath, int sheet_index) throws IOException {
		super(filepath, sheet_index);
	}

	
	void writeRow(int row_index, ArrayList<Object> input_data) throws Exception {
		int col_index = 0;
		row_index--;

		Row row = sheet.createRow(row_index);
		for (Object o : input_data) {
			Object data;
			
			if (o != null) {
				if(o.equals("null"))data = "";
				else if(o.equals("TRUE()")||o.equals("true"))data =true;
				else if(o.equals("FALSE()")||o.equals("false"))data =false;
				else data = o.toString().trim();
			}
			else
				data = "";
			cellWriter(data, row, col_index);
			col_index++;
		}
		writeToSheet();
	}

	private void cellWriter(Object o, Row row, int col_index) {
		if (o != null) {
			if (o instanceof String) {
				Cell cell = row.createCell(col_index, CellType.STRING);
				cell.setCellValue((String) o);
			} else if (o instanceof Integer) {
				Cell cell = row.createCell(col_index, CellType.NUMERIC);
				cell.setCellValue((Integer) o);
			} else if (o instanceof Boolean) {
				Cell cell = row.createCell(col_index, CellType.BOOLEAN);
				cell.setCellValue((Boolean) o);
			} else if (o instanceof Float) {
				Cell cell = row.createCell(col_index, CellType.NUMERIC);
				cell.setCellValue((Float) o);
			} else if (o instanceof Double) {
				Cell cell = row.createCell(col_index, CellType.NUMERIC);
				cell.setCellValue((Double) o);
			} else if (o instanceof Long) {
				Cell cell = row.createCell(col_index, CellType.NUMERIC);
				cell.setCellValue((Long) o);
			}
		} else {
			row.createCell(col_index, CellType.BLANK);

		}

	}

	public void writeRowVal(int row_index, int col_index, Object o) throws Exception {

		row_index--;
		col_index--;
		Row row;
		try {
		row = sheet.getRow(row_index);
		}
		catch(Exception e) {
	    row = sheet.createRow(row_index);
		}

		cellWriter(o, row, col_index);
		writeToSheet();
	}

}
