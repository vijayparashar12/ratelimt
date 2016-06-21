# Ratelimit Implementation For Hotel Search Webservice.

This is a spring boot application.

For running the demo   

<b>mvn spring-boot:run</b>

Server will listen at port 3000.

this can be changed via src/main/resources/application.properties

#API Access

curl -i -H "x-api-key:c144ce49-a6fe-4781-ad40-78fc6esc"ee9" "http://localhost:3000/search?city=Bangkok&sort=desc"

#API Keys

For adding an api key,add a entry to src/main/resources/apikeys.store

Newly added key will get global rate limit settings 

	global.window.sec=10
	global.request.threshold=1

To override these setting add a entry to src/main/resources/apikeys.properties in following format.

	${apikey}.window.sec=${some value for rate limit window}
	${apikey}.request.threshold=${some request threshold for given window}


