package com.bosch.glm100c.easy_connect.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.pano.R;

import com.bosch.glm100c.easy_connect.DataService;
import com.bosch.glm100c.easy_connect.OpenProjectActivity;
import com.bosch.glm100c.easy_connect.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter  extends ArrayAdapter<Project> {
    private static List<Project> projectList = new ArrayList<Project>();
    private static OpenProjectActivity main;
    private static ProjectAdapter adapter;
    private static DataService helper;
    public ProjectAdapter(Context context, int resource, int textViewResourceId, List<Project> list, DataService _helper) {
        super(context, resource, textViewResourceId, list);
        projectList.clear();
        projectList= list;
        main = (OpenProjectActivity) context;
        adapter = this;
        helper = _helper;
    }
    @Override
    public int getCount() {
        return projectList.size();
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public Project getItem(int position) {
        return projectList.get(position);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        Project item = getItem(i);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_projects, null);
            holder = new ViewHolder((ImageView) view.findViewById(R.id.checkbox_completed10),
                    (TextView) view.findViewById(R.id.contact_Address10),
                    (TextView) view.findViewById(R.id.contact_City10),
                    (TextView) view.findViewById(R.id.contact_State10),
                    (TextView) view.findViewById(R.id.project_Status10),
                    (TextView) view.findViewById(R.id.notes10), i);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.contact_Address.setText(" Address: " + projectList.get(i).Address);
        //TextView City = (TextView)view.findViewById(R.id.contact_City);
        holder.contact_City.setText(" City: " + projectList.get(i).City);
        //TextView State = (TextView)view.findViewById(R.id.contact_State);
        holder.contact_State.setText(" State: " + projectList.get(i).State + ", " + projectList.get(i).ZIPCode);
        //TextView Status = (TextView)view.findViewById(R.id.project_Status);
        holder.project_Status.setText(" Status: " + projectList.get(i).Status2);
        //TextView notes = (TextView)view.findViewById(R.id.notes);
        holder.notes.setText(" Note: " + projectList.get(i).Notes);


        if (projectList.get(i).Completed.equals("Yes")) {
            holder.checkbox.setImageResource(R.drawable.ic_check_active);
           // view.setBackgroundResource(R.drawable.project_item_border1);
        }
        else {
            holder.checkbox.setImageResource(R.drawable.ic_check_normal);
        }

        ProjectError complete = helper.projectCompleted(projectList.get(i));
        if(complete.ReturnType == 1){
        }
        else if(complete.ReturnType == 2){
            view.setBackgroundResource(R.drawable.project_item_border);
        }
        else if(complete.ReturnType == 3 && (projectList.get(i).Completed.equals("Yes")==false)){
            view.setBackgroundResource(R.drawable.project_item_border);
        }
        else if(complete.ReturnType == 3){
            view.setBackgroundResource(R.drawable.project_item_border1);
        }
        return view;
    }
    private static class ViewHolder {
        public ImageView checkbox;
        public TextView contact_Address;
        public TextView contact_City;
        public TextView contact_State;
        public TextView project_Status;
        public TextView notes;
        public int index;
        public ViewHolder(ImageView checkbox, TextView _contact_Address,
                          TextView _contact_City, TextView _contact_State,
                          TextView _project_Status, TextView _notes, int i) {
            super();
            this.checkbox = checkbox;
            this.contact_Address = _contact_Address;
            this.contact_City = _contact_City;
            this.contact_State = _contact_State;
            this.project_Status = _project_Status;
            this.notes = _notes;
            index = i;
            this.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectError pState = helper.projectCompleted(projectList.get(index));
                    if (pState.ReturnType == 2) {
                        main.alertIncomplete(1);
                       return;
                    }
                    if (pState.ReturnType == 1) {
                        main.alertIncomplete(2);
                        return;
                    }
                    if (projectList.get(index).Completed.equals("Yes")) {
                        projectList.get(index).Completed = "No";
                    }
                    else {
                        projectList.get(index).Completed = "Yes";
                    }
                    helper.UpdateProject(projectList.get(index));
                    adapter.notifyDataSetChanged();
                    main.reLoad();
                }
            });
        }
    }
}
