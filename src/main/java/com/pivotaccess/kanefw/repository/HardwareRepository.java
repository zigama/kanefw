package com.pivotaccess.kanefw.repository;

import com.pivotaccess.kanefw.domain.Hardware;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Hardware entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {

	@Query("SELECT hardware FROM Hardware hardware WHERE hardware.model=:model")
	Hardware findByModel( @Param("model") String model);

	@Query("SELECT hardware FROM Hardware hardware WHERE hardware.model=:model"
													+ " AND hardware.serie=:serie")
	Hardware findByModelAndSerie( @Param("model") String model,  @Param("serie") String serie);

}
