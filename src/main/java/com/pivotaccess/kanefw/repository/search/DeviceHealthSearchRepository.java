package com.pivotaccess.kanefw.repository.search;

import com.pivotaccess.kanefw.domain.DeviceHealth;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DeviceHealth entity.
 */
public interface DeviceHealthSearchRepository extends ElasticsearchRepository<DeviceHealth, Long> {
}
