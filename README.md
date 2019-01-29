# Ratelimit Implementation For Hotel Search Webservice.

This is a spring boot application.

<h3>For running the demo</h3>   

	mvn spring-boot:run

Server will listen at port 3000.

this can be changed via src/main/resources/application.properties

# API Access

	curl -i -H "x-api-key:c144ce49-a6fe-4781-ad40-78fc62f51ee9" "http://localhost:3000/search?city=Bangkok&sort=desc"

# API Keys

For adding an api key,add a entry to src/main/resources/apikeys.store

Newly added key will get global rate limit settings 

	global.window.sec=10
	global.request.threshold=1

To override these setting add a entry to src/main/resources/apikeys.properties in following format.

	${apikey}.window.sec=#{some value for rate limit window}
	${apikey}.request.threshold=#{some request threshold for given window}
	
	
# Implementation Overview

* Application start point is <strong>com.vp.controllers.SearchWebService</strong>

* API implementation is done via search method in above class.
 
* For RateLimiting functionality please check <strong>com.vp.config.APIKEYSetting.apiPermission(String)</strong> , core logic of rate liming is present in <strong>com.vp.ratelimit.RateLimmiter </strong> Implementation is done using atomic counter and synchronized block for thread safety.

* For any other clarification please email vijayparashar.eca@gmail.com
 


