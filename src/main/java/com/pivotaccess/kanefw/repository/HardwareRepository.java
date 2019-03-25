package com.pivotaccess.kanefw.repository;

import com.pivotaccess.kanefw.domain.Hardware;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Hardware entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {

}
