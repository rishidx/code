package in.rajarshi.filedownload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.rajarshi.filedownload.model.FileRecords;

@Repository
public interface FileRepository extends JpaRepository<FileRecords, Long>{

}
