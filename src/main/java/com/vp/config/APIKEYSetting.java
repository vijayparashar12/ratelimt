package com.vp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vp.config.APIAccessPermission.APIStatus;
import com.vp.ratelimit.RateLimmiter;

/**
 * @author vijay
 * This class reads the apikeys.properties file and keeps a in memory copy of setting and
 * rateLimemmire object for individual apikey. 
 */
public class APIKEYSetting {
    public static final int SUSPENSION_PERIOD = 60 * 5 * 1000;
	private static final String WINDOW_SIZE_SUFFIX = ".window.sec";
    private static final String REQUEST_THRESHOLD_SUFFIX = ".request.threshold";
    private static final int FALLBACK_WINDOW_SIZE = 10;
    private static final int FALLBACK_REQUEST_THRESHOLD = 1;
    
    private Map<String, List<Settings>> settings;
    private Map<String, RateLimmiter> apiKeyRateLimmiter;
    private Map<String, Long> suspendedKeys;
    private APIKEYStore apikeyStore;
    
    private static final Log log = LogFactory.getLog(APIKEYSetting.class);

    public APIKEYSetting() {
        settings = new HashMap<String, List<Settings>>();
        apiKeyRateLimmiter = new HashMap<String, RateLimmiter>();
        suspendedKeys = new HashMap<String, Long>();
    }

    public void init() {
        Set<String> apikeys = apikeyStore.getStore();
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("apikeys.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            for (String apikey : apikeys) {
                String threshold = properties.getProperty(getRequestThresholdKey(apikey));
                int thresholdValue = threshold != null ? Integer.parseInt(threshold) : getGlobalRequestThreshold(properties);

                String windowSize = properties.getProperty(getWindowSizeKey(apikey));
                int windowSizeValue = windowSize != null ? Integer.parseInt(windowSize) : getGlobalWindowSize(properties);

                RateLimtSettings rateLimitSettings = new RateLimtSettings(thresholdValue, windowSizeValue);
                RateLimmiter rateLimmiter = new RateLimmiter(rateLimitSettings);
                apiKeyRateLimmiter.put(apikey, rateLimmiter);
                applySettings(apikey, rateLimitSettings);
            }
        } catch (IOException e) {
            log.error("Error while reading apikeys.properties, applying global fallback for all");
            Settings globalFallBackSettings = new RateLimtSettings(FALLBACK_REQUEST_THRESHOLD, FALLBACK_WINDOW_SIZE);
            for (String apikey : apikeys) {
                apiKeyRateLimmiter.put(apikey, new RateLimmiter((RateLimtSettings) globalFallBackSettings));
                applySettings(apikey, globalFallBackSettings);
            }
        }
    }

    private String getRequestThresholdKey(String apikey) {
        return getPropertyKey(apikey, REQUEST_THRESHOLD_SUFFIX);
    }

    private String getPropertyKey(String apikey, String suffix) {
        StringBuilder apiKeyPropertyBuilder = new StringBuilder();
        apiKeyPropertyBuilder.append(apikey).append(suffix);
        return apiKeyPropertyBuilder.toString();
    }

    private String getWindowSizeKey(String apikey) {
        return getPropertyKey(apikey, WINDOW_SIZE_SUFFIX);
    }

    private int getGlobalWindowSize(Properties properties) {
        String value = properties.getProperty("global.window.sec");
        return value == null ? FALLBACK_WINDOW_SIZE : Integer.parseInt(value);
    }

    private int getGlobalRequestThreshold(Properties properties) {
        String value = properties.getProperty("global.request.limit");
        return value == null ? FALLBACK_REQUEST_THRESHOLD : Integer.parseInt(value);
    }

    private void applySettings(String apikey, Settings globalFallBackSettings) {
        List<Settings> apiKeySettings = null;
        if (settings.containsKey(apikey)) {
            apiKeySettings = settings.get(apikey);
        } else {
            apiKeySettings = new ArrayList<Settings>();
            settings.put(apikey, apiKeySettings);
        }
        apiKeySettings.add(globalFallBackSettings);
    }

    @Autowired
    public void setApikeyStore(APIKEYStore apikeyStore) {
        this.apikeyStore = apikeyStore;
    }

    public APIAccessPermission apiPermission(String apiKey) {
        APIAccessPermission permission = new APIAccessPermission();
        if (apikeyStore.validKey(apiKey)) {
            if (isKeyInSuspenstion(apiKey)) {
                permission.setApiStatus(APIStatus.SUSPEDED);
            } else {
                if (canAccessNow(apiKey)) {
                    permission.setAllowed(true);
                    permission.setApiStatus(APIStatus.ACTIVE);
                } else {
                    suspendKey(apiKey);
                    permission.setApiStatus(APIStatus.SUSPEDED);
                }

            }
        } else {
            permission.setApiStatus(APIStatus.INVALID_KEY);
        }
        return permission;
    }

    private void suspendKey(String apiKey) {
        long fiveMinutes = System.currentTimeMillis() + SUSPENSION_PERIOD;
        suspendedKeys.put(apiKey, fiveMinutes);
    }

    public boolean isKeyInSuspenstion(String apiKey) {
        boolean suspended = false;
        if (suspendedKeys.containsKey(apiKey)) {
            if (System.currentTimeMillis() < suspendedKeys.get(apiKey)) {
                suspended = true;
            } else {
                suspendedKeys.remove(apiKey);
            }
        }
        return suspended;
    }

    private boolean canAccessNow(String apiKey) {
        RateLimmiter rateLimmiter = apiKeyRateLimmiter.get(apiKey);
        return rateLimmiter.access();
    }
}
