package com.vp.config;

import org.springframework.http.HttpStatus;

public class APIAccessPermission {
    public enum APIStatus {
        INVALID_KEY(HttpStatus.BAD_REQUEST), ACTIVE(HttpStatus.OK), SUSPEDED(HttpStatus.TOO_MANY_REQUESTS);
        
        private HttpStatus httpStatus;

        APIStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;

        }

        public HttpStatus getHttpStatus() {
            return this.httpStatus;
        }

    }

    private boolean allowed;
    private APIStatus apiStatus;

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public APIStatus getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(APIStatus apiStatus) {
        this.apiStatus = apiStatus;
    }

}
