package com.vp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class APIKEYStore {

    private static final Log log = LogFactory.getLog(APIKEYStore.class);
    private Set<String> store;

    public boolean validKey(String apiKey) {
        return apiKey == null ? false : store.contains(apiKey.trim());
    }

    public void loadAllKeys() {
        log.info("loading all api keys");
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("apikeys.store");
        store = new HashSet<String>();
        try {
            String values = IOUtils.toString(inputStream);
            String[] keys = values.split(",");
            for (String key : keys) {
                log.info("Api key added " + key.trim());
                store.add(key.trim());
            }
        } catch (IOException e) {
        }
    }

    public Set<String> getStore() {
        return store;
    }
}
