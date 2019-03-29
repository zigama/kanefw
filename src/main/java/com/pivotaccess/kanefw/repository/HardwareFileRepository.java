package com.pivotaccess.kanefw.repository;

import com.pivotaccess.kanefw.domain.Hardware;
import com.pivotaccess.kanefw.domain.HardwareFile;
import com.pivotaccess.kanefw.domain.enumeration.FileCategory;
import com.pivotaccess.kanefw.domain.enumeration.FileStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HardwareFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HardwareFileRepository extends JpaRepository<HardwareFile, Long> {
	
	@Query("SELECT hardwareFile FROM HardwareFile hardwareFile"
			+ " WHERE hardwareFile.hardware =:hardware AND hardwareFile.category =:category"
			+ " ORDER BY hardwareFile.version, hardwareFile.dateUploaded DESC")
	HardwareFile findOneByHardwareAndCategoryOrderByVersionDateUploadedDesc( Hardware hardware, FileCategory category);

}
