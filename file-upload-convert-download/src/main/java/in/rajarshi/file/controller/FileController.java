package in.rajarshi.file.controller;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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

import in.rajarshi.file.model.FileRecords;
import in.rajarshi.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class FileController.
 *
 * @author Rajarshi Chakrabarty
 */

/** The Constant log. */

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
	 * @param file the file
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

}
