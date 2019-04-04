package com.pivotaccess.kanefw.repository;

import com.pivotaccess.kanefw.domain.DeviceHealth;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DeviceHealth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceHealthRepository extends JpaRepository<DeviceHealth, Long> {

}
