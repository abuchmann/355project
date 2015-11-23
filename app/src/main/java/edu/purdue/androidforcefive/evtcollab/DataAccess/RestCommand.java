package edu.purdue.androidforcefive.evtcollab.DataAccess;

import edu.purdue.androidforcefive.evtcollab.DataAccess.Enums.RestMethod;

/**
 * Created by abuchmann on 21.11.2015.
 */
public class RestCommand {
    private RestMethod restMethod;
    private String url;
    private String data;
    private Object result;
    private int statusCode;

    public RestCommand(RestMethod restMethod, String url, String data) {
        this.restMethod = restMethod;
        this.url = url;
        this.data = data;
    }

    public RestCommand(RestMethod restMethod, String url) {
        this.restMethod = restMethod;
        this.url = url;
    }

    public RestMethod getRestMethod() {
        return restMethod;
    }

    public String getUrl() {
        return url;
    }

    public String getData() {
        return data;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
