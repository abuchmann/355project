package edu.purdue.androidforcefive.evtcollab.DataAccess;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.purdue.androidforcefive.evtcollab.Controller.LogonController;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Enums.RestMethod;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Interfaces.IAsyncResponse;

/**
 * Created by abuchmann on 20.11.2015.
 */
public class AsyncApiAccess extends AsyncTask<RestCommand, Void, Void> {
    //private URL url;
    private IAsyncResponse delegate;
    private RestCommand restCommand;

    public AsyncApiAccess(IAsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(RestCommand... params) {
        try {
            restCommand = params[0];
            URL url = new URL(params[0].getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.addRequestProperty("Authorization", "Token token=" + LogonController.getInstance().getToken());

            if(restCommand.getRestMethod() == RestMethod.UPDATE) {
                connection.setDoOutput(true);
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(restCommand.getData().getBytes());
                outputStream.flush();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + connection.getResponseCode());
                }

            }

            if(restCommand.getRestMethod() == RestMethod.DELETE) {
                connection.setRequestMethod("DELETE");
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + connection.getResponseCode());
                }
            }
            if (restCommand.getRestMethod() == RestMethod.CREATE) {
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(restCommand.getData().getBytes());
                outputStream.flush();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + connection.getResponseCode());
                }
            }

            if(restCommand.getRestMethod() == RestMethod.CREATE ||
                    restCommand.getRestMethod() == RestMethod.INDEX ||
                    restCommand.getRestMethod() == RestMethod.UPDATE ) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String output, jsonString = "";
                //System.out.println("Output from Server .... \n");
                while ((output = bufferedReader.readLine()) != null) {
                    //System.out.println(output);
                    jsonString += output;
                }

                restCommand.setResult(jsonString.toString());
            }

            //JSONObject result = new JSONObject(jsonString);
            //MainActivity.order.setOrderId(result.getInt("id"));
            restCommand.setStatusCode(connection.getResponseCode());
            connection.disconnect();


        } catch (IOException e) {
            restCommand.setException(e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void nothing) {
        delegate.onTaskCompleted(restCommand);
    }
}
