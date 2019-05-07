package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;


public class ImportCSV {

	public ImportCSV() {};
	
	public void readCsv(String file)
    {
		String jdbcURL = "jdbc:mysql://localhost:3306/INTERVIEW_ASSESSMENT?userSSL=false&serverTimezone=UTC";
		String userName = "root";
		String password = "password";
		String driver = "com.mysql.cj.jdbc.Driver";
 
        try (@SuppressWarnings("deprecation")
		CSVReader reader = new CSVReader(new FileReader(file), ','); )
                    
        {
			System.out.println("Connecting to database : " + jdbcURL);
			Class.forName(driver);
			
			Connection conn = DriverManager.getConnection(jdbcURL, userName, password);
			
			System.out.println("Connection Successful!!!");
			String insertQuery = "Insert into X (A, B, C, D, E, F, G, H, I, J) values (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            String[] rowData = null;
            int i = 0;
            
            readLines(file);

            while((rowData = reader.readNext()) != null)
            {
            	
                for (String data : rowData)
                {

                    pstmt.setString((i % 10) + 1, data);
 
                    if (++i % 10 == 0)
                       pstmt.addBatch();// add batch
                    System.out.println(data);
 
                    if (i % 30 == 0)// insert when the batch size is 10
                    pstmt.executeBatch();
                }
        }		
        System.out.println("Data Successfully Uploaded");
        }
        catch (Exception e)
        {
                e.printStackTrace();
        }
 
    }
	
	public boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
	
	public void readLines(String file) {
        Scanner s = null;
        try {
            s = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        while (s.hasNextLine()) {
            String[] line = s.nextLine().split(",");
            for (String element : line) {
                if (!isBlank(element))
                    System.out.println(element);
            }

        }
	}

}
