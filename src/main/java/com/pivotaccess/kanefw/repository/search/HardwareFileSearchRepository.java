package com.pivotaccess.kanefw.repository.search;

import com.pivotaccess.kanefw.domain.HardwareFile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the HardwareFile entity.
 */
public interface HardwareFileSearchRepository extends ElasticsearchRepository<HardwareFile, Long> {
}
