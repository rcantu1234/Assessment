package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileUploadController {

	public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";
	
	@RequestMapping("/")
	public String UploadPage(Model model) {	
		return "uploadView";
	}
	
	@RequestMapping("/upload")
	public String upload(Model model, @RequestParam("files") String[] files) {
		
		StringBuilder fileNames = new StringBuilder();
		ImportCSV csv = new ImportCSV();
		
		Path fileNameAndPath = Paths.get(uploadDirectory, files.toString());
		
		csv.readCsv(fileNames.toString());

		for(String file : files) {
			 fileNameAndPath = Paths.get(uploadDirectory, file.toString());
			
			try {
				Files.write(fileNameAndPath, file.getBytes());
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		model.addAttribute("msg", "Successfully uploaded files " + fileNames.toString());
		
		return "uploadStatusView";
	}
	
}