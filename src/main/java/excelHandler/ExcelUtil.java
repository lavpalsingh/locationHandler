package excelHandler;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtil {
	private Workbook workbook;
	Sheet sheet;
	private FileInputStream fis;
	private String filepath ;

	ExcelUtil(String filepath, int sheet_index) throws IOException {

		this.filepath = filepath;
		readWorkbook(filepath);

		readSheet(sheet_index);
	}

	private void readWorkbook(String fileName) throws IOException {
		fis = new FileInputStream(fileName);

		if (fileName.toLowerCase().endsWith("xlsx")) {
			workbook = new XSSFWorkbook(fis);
		} else if (fileName.toLowerCase().endsWith("xls")) {
			workbook = new HSSFWorkbook(fis);
		}

	}

	private void readSheet(int index) {
		sheet = workbook.getSheetAt(index);
	}

	public void closeWorkbook() throws IOException {
		fis.close();
	}
	

	void writeToSheet() throws IOException {
		 FileOutputStream fileOut = new FileOutputStream(filepath);
	        workbook.write(fileOut);
	        fileOut.close();
	        closeWorkbook();
	}
}
