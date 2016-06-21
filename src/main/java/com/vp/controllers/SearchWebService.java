package com.vp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vp.config.APIAccessPermission;
import com.vp.config.APIKEYSetting;
import com.vp.data.Hotel;
import com.vp.data.SearchCriteria;
import com.vp.service.SearchService;

@RestController
@ComponentScan(basePackages = { "com.vp.config" })
@SpringBootApplication
public class SearchWebService {

	private APIKEYSetting apikeySetting;
	private SearchService searchService;
	private static Log log = LogFactory.getLog(SearchWebService.class);

	@RequestMapping("/")
	public ResponseEntity<Map<String, String>> greet(@RequestHeader(value = "x-api-key") String apiKey) {
		Map<String, String> response = new HashMap<String, String>();
		APIAccessPermission permission = apikeySetting.apiPermission(apiKey);
		if (permission.isAllowed()) {
			response.put("msg", "Service is running..");
		} else {
			response.put("error", permission.getApiStatus().name());
		}

		ResponseEntity<Map<String, String>> x = new ResponseEntity<Map<String, String>>(response,
				permission.getApiStatus().getHttpStatus());
		return x;
	}

	@RequestMapping("/search")
	public ResponseEntity<Map<String, Object>> search(@RequestHeader(value = "x-api-key") String apiKey,
			@RequestParam(value = "city") String city,
			@RequestParam(value = "sort", defaultValue = "ASC") String sort) {

		//Checking permission for apikey
		APIAccessPermission permission = apikeySetting.apiPermission(apiKey);

		Map<String, Object> response = new HashMap<String, Object>();
		if (permission.isAllowed()) {
			log.info("searching for city " + city);
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setCity(city);
			searchCriteria.setSort(sort);
			List<Hotel> hotels = searchService.search(searchCriteria);
			response.put("hotels", hotels);
		} else {
			response.put("error", permission.getApiStatus().name());
		}
		ResponseEntity<Map<String, Object>> httpResponse = new ResponseEntity<Map<String, Object>>(response,
				permission.getApiStatus().getHttpStatus());
		return httpResponse;
	}

	public static void main(String[] args) {
		log.info("Starting up..");
		SpringApplication.run(SearchWebService.class, args);
	}

	@Autowired
	public void setApikeySetting(APIKEYSetting apikeySetting) {
		this.apikeySetting = apikeySetting;
	}

	@Autowired
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
}
