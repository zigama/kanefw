package com.pivotaccess.kanefw.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of HardwareFileSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class HardwareFileSearchRepositoryMockConfiguration {

    @MockBean
    private HardwareFileSearchRepository mockHardwareFileSearchRepository;

}
