package com.kurzdigital.keycloak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ResponseHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseHelper.class);

    public static void checkCreateResponse(String entity, Response response) {
        int status = response.getStatus();
        if (status != Response.Status.CREATED.getStatusCode()) {
            throw new RuntimeException("Could not create " + entity + ". Status Code " + status);
        }
    }

    public static void checkGetResponse(String entity, Response response) {
        int status = response.getStatus();
        if (status != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Could not get " + entity + ". Status Code " + status);
        }
    }

    public static String getIdFromLocation(Response response) {
        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf("/") + 1);
    }

    public static <R> R retryWithException(SimpleReturnFunction<R> function) {
        try {
            return function.apply();
        } catch (Exception ignore) {
            LOG.info("Retrying because of exception " + ignore);
            try {
                return function.apply();
            } catch (NotFoundException nfe) {
                throw nfe;
            } catch (Exception e) {
                throw new WebApplicationException("Could not execute.", e);
            }
        }
    }

    public static Response retryOnWrongStatusCode(SimpleReturnFunction<Response> function) {
        Response response = function.apply();
        if(response.getStatus() == Response.Status.FORBIDDEN.getStatusCode() || response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
            LOG.info("Retrying because of status code " + response.getStatus());
            return function.apply();
        }
        return response;
    }

    public interface SimpleReturnFunction<R> {
        R apply();
    }
}
