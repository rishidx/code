package in.rajarshi.filedownload.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import in.rajarshi.filedownload.model.FileRecords;
import in.rajarshi.filedownload.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;

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
	 * Download file.
	 *
	 * @param id the id
	 * @param httpServletResponse the http servlet response
	 */
	@GetMapping("/file/{id}")
	public void downloadFile(@PathVariable("id") Long id, HttpServletResponse httpServletResponse) {

		Optional<FileRecords> optional = null;
		OutputStream outputStream = null;

		try {
			optional = fileRepository.findById(id);
			if (optional.isPresent()) {
				httpServletResponse.setHeader("Content-Disposition",
						"inline;filename=\"" + optional.get().getFileName() + "\"");
				outputStream = httpServletResponse.getOutputStream();
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(optional.get().getFileContent());
				IOUtils.copy(byteArrayInputStream, outputStream);
				httpServletResponse.flushBuffer();

			} else {
				log.warn("downloadDocxFile :: Invalid or wrong input");
			}
		} catch (Exception ex) {
			log.error("downloadDocxFile :: Failed to create download stream", ex);
		}
		return;
	}
	
	/**
	 * Download file 2.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@GetMapping("/file")
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
				httpHeaders.set("Content-Disposition",
						"inline;filename=\"" + fileRecords.getFileName() + "\"");
				streamingResponseBody = new StreamingResponseBody() {
					
					@Override
					public void writeTo(OutputStream outputStream) throws IOException {
						ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileRecords.getFileContent());
						IOUtils.copy(byteArrayInputStream, outputStream);
					}
				};
			} else {
				log.warn("downloadDocxFile :: Invalid or wrong input");
			}
		} catch (Exception ex) {
			log.error("downloadDocxFile :: Failed to create download stream", ex);
		}
		return new ResponseEntity<StreamingResponseBody>(streamingResponseBody, httpHeaders, HttpStatus.OK);
	}
	
	
}
