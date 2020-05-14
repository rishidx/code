package in.rajarshi.filedownload.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.ResourceUtils;

import in.rajarshi.filedownload.model.FileRecords;
import in.rajarshi.filedownload.repository.FileRepository;

/**
 * The Class FileDownloadApp.
 *
 * @author Rajarshi Chakrabarty
 * @since 14-05-2020
 */
@SpringBootApplication
@ComponentScan("in.rajarshi.filedownload")
@EnableJpaRepositories("in.rajarshi.filedownload.repository")
@EntityScan("in.rajarshi.filedownload.model")
public class FileDownloadApp implements CommandLineRunner {

	/** The employee repository. */
	@Autowired
	private FileRepository employeeRepository;
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FileDownloadApp.class, args);
	}
	
	/*
	 * Store a sample docx file in database
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws IOException {
		
		// Read file from classpath
		File file = ResourceUtils.getFile("classpath:Employee.docx");
		InputStream inputStream = new FileInputStream(file);
		FileRecords docxRecord = new FileRecords();
		docxRecord.setCreateDt(new Date());
		docxRecord.setFileContent(IOUtils.toByteArray(inputStream));
		docxRecord.setFileName(file.getName());
		employeeRepository.save(docxRecord);

		file = ResourceUtils.getFile("classpath:Employee.pdf");
		inputStream = new FileInputStream(file);
		FileRecords pdfRecord = new FileRecords();
		pdfRecord.setCreateDt(new Date());
		pdfRecord.setFileContent(IOUtils.toByteArray(inputStream));
		pdfRecord.setFileName(file.getName());
		employeeRepository.save(pdfRecord);
		
		inputStream.close();
	}
}
