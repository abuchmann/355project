package edu.purdue.androidforcefive.evtcollab.DataCollections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Annotation;
import edu.purdue.androidforcefive.evtcollab.DataAccess.AsyncApiAccess;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Enums.RestMethod;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Interfaces.IAsyncResponse;
import edu.purdue.androidforcefive.evtcollab.DataAccess.RestCommand;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;

/**
 * Created by abuchmann on 21.11.2015.
 */
public class AnnotationCollection implements IAsyncResponse<RestCommand> {
    private static AnnotationCollection instance;

    private List<Annotation> annotations;
    private List<IDataCollectionChanged> delegates = new ArrayList<>();
    // private final AsyncApiAccess asyncRestAccess;
    private RestCommand currentCommand;


    private String ApiUrl = "http://192.168.109.128:3000/api/";
    private int ApiVersion = 1;

    public static AnnotationCollection getInstance() {
        if (instance == null) {
            instance = new AnnotationCollection();
        }
        return instance;
    }

    /**
     * Private constructor as this class is a singleton
     */
    private AnnotationCollection() {
        //asyncRestAccess = new AsyncApiAccess(this);
    }

    public List<Annotation> getAnnotations() {
        //System.out.println(annotations.size());
        return annotations;

    }

    public Annotation getAnnotation(int id) {
        for (Annotation annotation : annotations) {
            if (annotation.getId() == id) {
                return annotation;
            }
        }
        return null;
    }

    public void addAnnotation(Annotation annotation) {
        AsyncApiAccess asyncApiAccess1 = new AsyncApiAccess(this);
        asyncApiAccess1.execute(new RestCommand(RestMethod.CREATE, ApiUrl + "v" + ApiVersion + "/annotations", annotation.getItemAsJsonObject().toString()));
    }


    public void initializeAnnotations() {
        annotations = new ArrayList<>();
        refreshAnnotations();
    }

    public void refreshAnnotations() {
        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        asyncApiAccess.execute(new RestCommand(RestMethod.INDEX, ApiUrl + "v" + ApiVersion + "/annotations"));
    }

    public void destroyAnnotation(Annotation annotation) {
        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        RestCommand restCommand = new RestCommand(RestMethod.DELETE, ApiUrl + "v" + ApiVersion + "/annotations/" + annotation.getId());
        restCommand.setResult(annotation);
        asyncApiAccess.execute(restCommand);
    }

    public void updateAnnotation(Annotation annotation) {

        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        RestCommand restCommand = new RestCommand(RestMethod.UPDATE, ApiUrl + "v" + ApiVersion + "/annotations/" + annotation.getId(),  annotation.getItemAsJsonObject().toString());
        asyncApiAccess.execute(restCommand);
    }

    public void addListener(IDataCollectionChanged delegate) {
        delegates.add(delegate);
    }

    private void notifyListeners() {
        for (IDataCollectionChanged delegate : delegates) {
            delegate.onCollectionChanged();
        }
    }

    @Override
    public void onTaskCompleted(RestCommand restCommand) {
        switch (restCommand.getRestMethod()) {
            case INDEX:
                try {
                    JSONArray annotationsJson = new JSONArray((String) restCommand.getResult());
                    for (int i = 0; i < annotationsJson.length(); i++) {
                        JSONObject e = annotationsJson.getJSONObject(i);
                        Annotation itemToAdd = new Annotation();
                        itemToAdd.applyJsonObject(e);
                        annotations.add(itemToAdd);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case CREATE:
                try {
                    JSONObject newAnnotationJson = new JSONObject((String) restCommand.getResult());
                    Annotation itemToAdd = new Annotation();
                    itemToAdd.applyJsonObject(newAnnotationJson);
                    annotations.add(itemToAdd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("Annotation added!");
                break;
            case DELETE:
                if (restCommand.getStatusCode() == 204) {
                    annotations.remove(restCommand.getResult());
                }
                break;
            case UPDATE:
                try {
                    JSONObject updatedAnnotationJson = new JSONObject((String) restCommand.getResult());

                    for(Annotation annotation : annotations) {
                        if(annotation.getId() == updatedAnnotationJson.getInt("id")) {
                            annotation.applyJsonObject(updatedAnnotationJson);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
        notifyListeners();

    }



}
