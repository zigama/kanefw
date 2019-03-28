package com.pivotaccess.kanefw.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.pivotaccess.kanefw.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Hardware.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Hardware.class.getName() + ".hardwareFiles", jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.HardwareFile.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Content.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Device.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Device.class.getName() + ".deviceHealths", jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Device.class.getName() + ".transactions", jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.DeviceHealth.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Customer.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Customer.class.getName() + ".transactions", jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Transaction.class.getName(), jcacheConfiguration);
            cm.createCache(com.pivotaccess.kanefw.domain.Hardware.class.getName() + ".devices", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
