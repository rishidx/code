package in.rajarshi.file.controller;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import in.rajarshi.file.model.FileRecords;
import in.rajarshi.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/**
 * The Class FileController.
 *
 * @author Rajarshi Chakrabarty
 */

/** The Constant log. */
@Slf4j
@RestController
public class FileController {

	/** The file repository. */
	@Autowired
	private FileRepository fileRepository;

	/**
	 * Hello user.
	 *
	 * @return the response entity
	 */
	@GetMapping("/")
	public ResponseEntity<String> helloUser() {
		return ResponseEntity.ok("Hello User !!! This is project to download docx file");
	}

	/**
	 * Upload file.
	 *
	 * @param file
	 *            the file
	 * @return the response entity
	 */
	@PostMapping("/file/upload")
	public ResponseEntity<FileRecords> uploadFile(@RequestParam("file") MultipartFile file) {

		FileRecords fileRecords = null;
		FileRecords response = null;
		ResponseEntity<FileRecords> serviceResponse = null;

		try {
			fileRecords = new FileRecords();
			fileRecords.setFileName(file.getOriginalFilename());
			fileRecords.setContentType(file.getContentType());
			fileRecords.setFileContent(file.getBytes());
			fileRecords.setCreateDt(new Date());
			fileRepository.save(fileRecords);

			response = new FileRecords();
			response.setId(fileRecords.getId());
		} catch (Exception ex) {
			log.error("uploadFile :: Faled to execute upload file", ex);
		}

		if (response != null) {
			serviceResponse = new ResponseEntity<FileRecords>(response, HttpStatus.OK);
		} else {
			serviceResponse = new ResponseEntity<FileRecords>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return serviceResponse;
	}

	/**
	 * Upload file.
	 *
	 * @param files
	 *            the files
	 * @return the response entity
	 */
	@PostMapping("/file/upload-files")
	public ResponseEntity<FileRecords> uploadFile(@RequestParam("file") MultipartFile[] files) {

		FileRecords fileRecords = null;
		FileRecords response = null;
		List<FileRecords> responseFiles = null;
		ResponseEntity<FileRecords> serviceResponse = null;

		try {
			if (files != null) {
				responseFiles = new ArrayList<>();
				for (MultipartFile file : files) {
					fileRecords = new FileRecords();
					fileRecords.setFileName(file.getOriginalFilename());
					fileRecords.setContentType(file.getContentType());
					fileRecords.setFileContent(file.getBytes());
					fileRecords.setCreateDt(new Date());
					fileRepository.save(fileRecords);

					response = new FileRecords();
					response.setId(fileRecords.getId());
					responseFiles.add(response);
				}
			}
		} catch (Exception ex) {
			log.error("uploadFile :: Faled to execute upload file", ex);
		}

		if (response != null) {
			serviceResponse = new ResponseEntity<FileRecords>(response, HttpStatus.OK);
		} else {
			serviceResponse = new ResponseEntity<FileRecords>(HttpStatus.NOT_ACCEPTABLE);
		}

		return serviceResponse;
	}

	/**
	 * Download file.
	 *
	 * @param id
	 *            the id
	 * @param httpServletResponse
	 *            the http servlet response
	 */
	@GetMapping("/file/download/{id}")
	public void downloadFile(@PathVariable("id") Long id, HttpServletResponse httpServletResponse) {

		Optional<FileRecords> optional = null;
		OutputStream outputStream = null;

		try {
			optional = fileRepository.findById(id);
			if (optional.isPresent()) {
				httpServletResponse.setHeader("Content-Disposition",
						"inline;filename=\"" + optional.get().getFileName() + "\"");
				httpServletResponse.setContentType(optional.get().getContentType());
				outputStream = httpServletResponse.getOutputStream();
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(optional.get().getFileContent());
				IOUtils.copy(byteArrayInputStream, outputStream);
				httpServletResponse.flushBuffer();

			} else {
				log.warn("downloadFile :: Invalid or wrong input");
			}
		} catch (Exception ex) {
			log.error("downloadFile :: Failed to create download stream", ex);
		}
		return;
	}

	/**
	 * Download multiple file.
	 *
	 * @param ids
	 *            the ids
	 * @param httpServletResponse
	 *            the http servlet response
	 */
	@GetMapping(value = "/file/download/all", produces = "application/zip")
	public void downloadMultipleFile(@RequestParam("id") List<Long> ids, HttpServletResponse httpServletResponse) {

		Optional<FileRecords> optional = null;
		ByteArrayInputStream byteArrayInputStream = null;

		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream(httpServletResponse.getOutputStream());
			if (ids != null) {
				for (Long id : ids) {
					optional = fileRepository.findById(id);
					if (optional.isPresent()) {

						ZipEntry zipEntry = new ZipEntry(optional.get().getFileName());
						zipEntry.setSize(optional.get().getFileContent().length);
						zipOutputStream.putNextEntry(zipEntry);
						byteArrayInputStream = new ByteArrayInputStream(optional.get().getFileContent());
						StreamUtils.copy(byteArrayInputStream, zipOutputStream);
						zipOutputStream.closeEntry();
					} else {
						log.warn("downloadFile :: Invalid or wrong input");
					}
				}
				zipOutputStream.finish();
				zipOutputStream.close();
				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
				httpServletResponse.setHeader("Content-Disposition", "inline;filename=\"download.zip\"");

			}
		} catch (Exception ex) {
			log.error("downloadMultipleFile :: Failed to create download stream", ex);
		}
		return;
	}

	/**
	 * Download file 2.
	 *
	 * @param id
	 *            the id
	 * @return the response entity
	 */
	@GetMapping("/file/download")
	public ResponseEntity<StreamingResponseBody> downloadFile2(@RequestParam("id") Long id) {

		Optional<FileRecords> optional = null;
		StreamingResponseBody streamingResponseBody = null;
		HttpHeaders httpHeaders = null;
		final FileRecords fileRecords;
		try {
			optional = fileRepository.findById(id);
			if (optional.isPresent()) {
				fileRecords = optional.get();
				httpHeaders = new HttpHeaders();
				httpHeaders.set("Content-Disposition", "inline;filename=\"" + fileRecords.getFileName() + "\"");
				httpHeaders.setContentType(MediaType.parseMediaType(fileRecords.getContentType()));

				streamingResponseBody = (outputStream) -> {
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileRecords.getFileContent());
					IOUtils.copy(byteArrayInputStream, outputStream);
				};

			} else {
				log.warn("downloadFile2 :: Invalid or wrong input");
			}
		} catch (Exception ex) {
			log.error("downloadFile2 :: Failed to create download stream", ex);
		}
		return new ResponseEntity<StreamingResponseBody>(streamingResponseBody, httpHeaders, HttpStatus.OK);
	}

	/**
	 * Convert and download docx files to pdf using Aspose API. Aspose is NOT
	 * opensource
	 * 
	 * Upload docx file using /file/upload
	 * 
	 * {@link https://blog.aspose.com/2020/02/20/convert-word-doc-docx-to-pdf-in-java-programmatically/}
	 *
	 * @param id
	 *            the id
	 * @return the response entity
	 */
	@GetMapping("/file/convert/aspose")
	public ResponseEntity<StreamingResponseBody> convertAndDownloadFile(@RequestParam("id") Long id) {

		StreamingResponseBody streamingResponseBody = null;
		HttpHeaders httpHeaders = null;
		Optional<FileRecords> optional = null;
		final FileRecords fileRecords;
		try {

			optional = fileRepository.findById(id);
			if (optional.isPresent()) {
				fileRecords = optional.get();

				ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
				fileRecords.setFileName(fileRecords.getFileName().replace("docx", "pdf"));
				Document loadedFromBytes = new Document(new ByteArrayInputStream(fileRecords.getFileContent()));
				loadedFromBytes.save(pdfOutputStream, SaveFormat.PDF);

				httpHeaders = new HttpHeaders();
				httpHeaders.set("Content-Disposition", "inline;filename=\"" + fileRecords.getFileName() + "\"");
				httpHeaders.setContentType(MediaType.parseMediaType(fileRecords.getContentType()));

				streamingResponseBody = (outputStream) -> {
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
					IOUtils.copy(byteArrayInputStream, outputStream);
				};

			} else {
				log.warn("convertAndDownloadFile :: Invalid or wrong input");
			}

			// File file = ResourceUtils.getFile("classpath:Employee.docx");
			// InputStream inputStream = new FileInputStream(file);
			// ByteArrayOutputStream outputStream1 = new
			// ByteArrayOutputStream();
			//
			// Document document = new Document(inputStream);
			// document.save(outputStream1, SaveFormat.DOCX);
			// byte[] docx = outputStream1.toByteArray();
			// System.out.println(docx.length);
			// outputStream1.close();
			//
			// ByteArrayOutputStream outputStream2 = new
			// ByteArrayOutputStream();
			// Document loadedFromBytes = new Document(new
			// ByteArrayInputStream(docx));
			// loadedFromBytes.save(outputStream2, SaveFormat.PDF);
			//
			// httpHeaders = new HttpHeaders();
			// httpHeaders.set("Content-Disposition", "inline;filename=\"" +
			// "Employee.docx" + "\"");
			//
			// streamingResponseBody = (outputStream) -> {
			// ByteArrayInputStream byteArrayInputStream = new
			// ByteArrayInputStream(outputStream2.toByteArray());
			// IOUtils.copy(byteArrayInputStream, outputStream);
			// };
		} catch (Exception ex) {
			log.error("convertAndDownloadFile :: Failed to create download stream", ex);
		}
		return new ResponseEntity<StreamingResponseBody>(streamingResponseBody, httpHeaders, HttpStatus.OK);
	}

	/**
	 * Convert and download docx files to pdf using
	 * org.apache.poi.xwpf.converter.pdf of fr.opensagres.xdocreport api
	 * 
	 * Upload docx file using /file/upload
	 *
	 * {@link https://github.com/opensagres/xdocreport/issues/332}
	 *
	 * @param id
	 *            the id
	 * @return the response entity
	 */
	@GetMapping("/file/convert/opensagres")
	public ResponseEntity<StreamingResponseBody> convertAndDownloadFile2(@RequestParam("id") Long id) {

		StreamingResponseBody streamingResponseBody = null;
		HttpHeaders httpHeaders = null;
		Optional<FileRecords> optional = null;
		final FileRecords fileRecords;
		try {

			optional = fileRepository.findById(id);
			if (optional.isPresent()) {
				fileRecords = optional.get();

				ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
				fileRecords.setFileName(fileRecords.getFileName().replace("docx", "pdf"));
				XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(fileRecords.getFileContent()));
				PdfOptions pdfOptions = PdfOptions.create();
				PdfConverter.getInstance().convert(document, pdfOutputStream, pdfOptions);

				httpHeaders = new HttpHeaders();
				httpHeaders.set("Content-Disposition", "inline;filename=\"" + fileRecords.getFileName() + "\"");
				httpHeaders.setContentType(MediaType.parseMediaType(fileRecords.getContentType()));

				streamingResponseBody = (outputStream) -> {
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
					IOUtils.copy(byteArrayInputStream, outputStream);
				};

			} else {
				log.warn("convertAndDownloadFile :: Invalid or wrong input");
			}
		} catch (Exception ex) {
			log.error("downloadFile2 :: Failed to create download stream", ex);
		}
		return new ResponseEntity<StreamingResponseBody>(streamingResponseBody, httpHeaders, HttpStatus.OK);
	}

	/**
	 * Convert and download docx files to pdf using docx4j api
	 * 
	 * Upload docx file using /file/upload
	 *
	 * {@link https://converter-eval.plutext.com/client_java.html}
	 * {@link https://converter-eval.plutext.com/}
	 * {@link https://github.com/plutext/PDF-Converter-Java}
	 *
	 *
	 * @param id
	 *            the id
	 * @return the response entity
	 */
	@GetMapping("/file/convert/docx4j")
	public ResponseEntity<StreamingResponseBody> convertAndDownloadFile3(@RequestParam("id") Long id) {

		StreamingResponseBody streamingResponseBody = null;
		HttpHeaders httpHeaders = null;
		Optional<FileRecords> optional = null;
		final FileRecords fileRecords;
		try {

			optional = fileRepository.findById(id);
			if (optional.isPresent()) {
				fileRecords = optional.get();

				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileRecords.getFileContent());
				ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
				fileRecords.setFileName(fileRecords.getFileName().replace("docx", "pdf"));
				Docx4jProperties.setProperty("com.plutext.converter.URL",
						"https://converter-eval.plutext.com:443/v1/00000000-0000-0000-0000-000000000000/convert");
				WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(byteArrayInputStream);
				Docx4J.toPDF(wordprocessingMLPackage, pdfOutputStream);

				httpHeaders = new HttpHeaders();
				httpHeaders.set("Content-Disposition", "inline;filename=\"" + fileRecords.getFileName() + "\"");
				httpHeaders.setContentType(MediaType.parseMediaType(fileRecords.getContentType()));

				streamingResponseBody = (outputStream) -> {
					ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
					IOUtils.copy(pdfInputStream, outputStream);
				};

			} else {
				log.warn("convertAndDownloadFile :: Invalid or wrong input");
			}

		} catch (Exception ex) {
			log.error("convertAndDownloadFile3 :: Failed to create download stream", ex);
		}
		return new ResponseEntity<StreamingResponseBody>(streamingResponseBody, httpHeaders, HttpStatus.OK);
	}
}
