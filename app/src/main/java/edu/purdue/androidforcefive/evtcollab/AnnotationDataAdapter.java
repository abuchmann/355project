package edu.purdue.androidforcefive.evtcollab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Annotation;

/**
 * Created by abuchmann on 01.12.2015.
 */
public class AnnotationDataAdapter extends ArrayAdapter<Annotation> {
    private final Context context;
    private Annotation[] annotations;

    public AnnotationDataAdapter(Context context, Annotation[] annotations) {
        super(context, -1, annotations);
        this.context = context;
        this.annotations = annotations;
    }

    public AnnotationDataAdapter(Context context) {
        super(context, -1);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.annotations_list, parent, false);
        TextView aAuthor = (TextView) rowView.findViewById(R.id.annotationAuthor);
        TextView aMessage = (TextView) rowView.findViewById(R.id.annotationmessage);
        TextView aDate = (TextView) rowView.findViewById(R.id.annotationTime);
        aAuthor.setText(annotations[position].getAuthor());
        aMessage.setText(annotations[position].getMessage().toString());
        aDate.setText(annotations[position].getCreatedAt().toString());
        // change the icon for Windows and iPhone
        Annotation e = annotations[position];
        //imageView.setImageResource(R.drawable.ok);

        return rowView;
        //return super.getView(position, convertView, parent);
    }

    public void addAnnotations(Annotation[] items) {
        super.addAll(items);
        annotations = items;
    }
}
