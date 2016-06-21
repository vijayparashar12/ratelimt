package com.vp.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.vp.config.APIAccessPermission.APIStatus;

public class TestRateLimiting {
	private APIKEYSetting setting;

	@Before
	public void init() {
		APIKEYStore apikeyStore = new APIKEYStore();
		apikeyStore.loadAllKeys();
		setting = new APIKEYSetting();
		setting.setApikeyStore(apikeyStore);
		setting.init();
	}

	@Test
	public void rateLimitBehaviourTest() {
		String apiKey = "c144ce49-a6fe-4781-ad40-78fc62f51ee9";
		for (int i = 0; i < 10; i++) {
			assertTrue(setting.apiPermission(apiKey).isAllowed());
		}
		assertFalse("Failing on 11th call within 30 sec", setting.apiPermission(apiKey).isAllowed());
		assertTrue(setting.isKeyInSuspenstion(apiKey));

	}

	@Test
	public void keySusspensionTest() throws Exception {
		String apiKey = "c144ce49-a6fe-4781-ad40-78fc62f51ee9";
		for (int i = 0; i < 11; i++) {
			setting.apiPermission(apiKey).isAllowed();
		}
		assertTrue(setting.isKeyInSuspenstion(apiKey));
	}

	@Test
	public void invalidKeyTest() throws Exception {
		String apiKey = "abc123";
		assertEquals(APIStatus.INVALID_KEY, setting.apiPermission(apiKey).getApiStatus());
	}

	@Test
	@Ignore //this test runs for five minutes.
	public void keySusspensionRevokeTest() throws Exception {
		String apiKey = "c144ce49-a6fe-4781-ad40-78fc62f51ee9";
		for (int i = 0; i < 11; i++) {
			setting.apiPermission(apiKey).isAllowed();
		}
		Thread.sleep(APIKEYSetting.SUSPENSION_PERIOD);
		assertFalse(setting.isKeyInSuspenstion(apiKey));
		assertTrue(setting.apiPermission(apiKey).isAllowed());
	}

}
