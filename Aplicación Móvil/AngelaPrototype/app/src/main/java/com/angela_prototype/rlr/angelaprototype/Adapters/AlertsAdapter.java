package com.angela_prototype.rlr.angelaprototype.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.angela_prototype.rlr.angelaprototype.Pojos.Alert;
import com.angela_prototype.rlr.angelaprototype.R;

import java.util.ArrayList;

/**
 * Created by Raúl on 22/06/2017.
 */

public class AlertsAdapter extends ArrayAdapter<Alert> {

    private ArrayList<Alert> data;
    public AlertsAdapter(Context context, ArrayList<Alert> data) {
        super(context, R.layout.listitem_alert, data);
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_alert, null);

        TextView type = (TextView) item.findViewById(R.id.type);
        TextView problem = (TextView) item.findViewById(R.id.problem);
        TextView lectures = (TextView) item.findViewById(R.id.lectures);
        TextView date_time = (TextView) item.findViewById(R.id.date_time);

        type.setText(data.get(position).getType());
        problem.setText(data.get(position).getProblem());
        lectures.setText(data.get(position).getLectures());
        date_time.setText(data.get(position).getDate() + " " + data.get(position).getTime());

        return(item);
    }
}
