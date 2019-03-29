package com.pivotaccess.kanefw.repository;

import com.pivotaccess.kanefw.domain.HardwareFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HardwareFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HardwareFileRepository extends JpaRepository<HardwareFile, Long> {

}
