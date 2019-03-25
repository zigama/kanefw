package com.pivotaccess.kanefw.repository.search;

import com.pivotaccess.kanefw.domain.Device;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Device entity.
 */
public interface DeviceSearchRepository extends ElasticsearchRepository<Device, Long> {
}
