package in.rajarshi.file.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(includeFieldNames = true)
@JsonInclude(Include.NON_NULL)
public class FileRecords {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;
	
	private String contentType;

//	@Lob
//	@Column(columnDefinition = "BLOB")
//	private byte[] fileContent;
	
	@Lob
	@Column(columnDefinition = "BLOB")
	private byte[] fileContent;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDt;
}
