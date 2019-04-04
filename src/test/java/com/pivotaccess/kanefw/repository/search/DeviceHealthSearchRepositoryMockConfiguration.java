package com.pivotaccess.kanefw.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DeviceHealthSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DeviceHealthSearchRepositoryMockConfiguration {

    @MockBean
    private DeviceHealthSearchRepository mockDeviceHealthSearchRepository;

}
