package com.vp.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vp.service.SearchService;

/**
 * @author vijay
 * Spring bean configurations.
 */
@Configuration
public class BeanConfig {

    private static final Log log = LogFactory.getLog(BeanConfig.class);

    @Autowired
    private APIKEYStore apikeyStore;

    @Bean(name = "apikeyStore")
    public APIKEYStore loadAPIKEYStore() {
        log.info("Creating APIKEY Store");
        APIKEYStore apikeyStore = new APIKEYStore();
        apikeyStore.loadAllKeys();
        return apikeyStore;
    }

    @Bean(name = "apikeySetting")
    public APIKEYSetting loadApikeySetting() {
        log.info("Creating APIKEYSettings");
        APIKEYSetting apikeySetting = new APIKEYSetting();
        apikeySetting.setApikeyStore(apikeyStore);
        apikeySetting.init();
        return apikeySetting;
    }

    @Bean
    public SearchService searchBean() {
        log.info("Creating SearchService");
        SearchService searchService = new SearchService();
        return searchService;
    }

}
