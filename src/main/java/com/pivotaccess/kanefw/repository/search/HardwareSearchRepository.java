package com.pivotaccess.kanefw.repository.search;

import com.pivotaccess.kanefw.domain.Hardware;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Hardware entity.
 */
public interface HardwareSearchRepository extends ElasticsearchRepository<Hardware, Long> {
}
