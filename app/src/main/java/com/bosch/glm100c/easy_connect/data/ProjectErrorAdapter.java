package com.bosch.glm100c.easy_connect.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bosch.glm100c.easy_connect.R;

import java.util.List;
public class ProjectErrorAdapter extends BaseAdapter{
    List<ProjectError> projectErrorList;
    LayoutInflater inflter;
    public ProjectErrorAdapter(Context applicationContext, List<ProjectError> list) {
        this.projectErrorList = list;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return projectErrorList.size();
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public ProjectError getItem(int position){
        return projectErrorList.get(position);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.dialog_items2, null);
        TextView error_Address = view.findViewById(R.id.error_Address);
        error_Address.setText("Address: " +projectErrorList.get(i).Address);

        TextView error_City = view.findViewById(R.id.error_City);
        error_City.setText("City: " +projectErrorList.get(i).City);

        TextView error_Levels = view.findViewById(R.id.error_Levels);
        TextView error_Levels1 = view.findViewById(R.id.error_Levels1);
        String s1 = projectErrorList.get(i).EmptyLevels.trim();
        if(s1.length()>0) {
            error_Levels.setText(projectErrorList.get(i).EmptyLevels.substring(1).toLowerCase().trim());
        }
        else{
            error_Levels.setVisibility(View.GONE);
            error_Levels1.setVisibility(View.GONE);
        }

        TextView error_BK = view.findViewById(R.id.error_BK);
        TextView error_BK1 = view.findViewById(R.id.error_BK1);
        String s4 = projectErrorList.get(i).BK.trim();
        if(s4.length()>0){
            error_BK.setText(projectErrorList.get(i).BK.toLowerCase().trim());
        }
        else{
            error_BK.setVisibility(View.GONE);
            error_BK1.setVisibility(View.GONE);
        }


        TextView error_OutPics = view.findViewById(R.id.error_OutPics);
        TextView error_OutPics1 = view.findViewById(R.id.error_OutPics1);
        error_OutPics.setText(projectErrorList.get(i).MissingOutsidePics);
        if((projectErrorList.get(i).MissingOutsidePics.trim()).equals("No")){
            error_OutPics.setVisibility(View.GONE);
            error_OutPics1.setVisibility(View.GONE);
        }

        TextView error_Check = view.findViewById(R.id.error_Check);
        TextView error_Check1 = view.findViewById(R.id.error_Check1);
        error_Check.setText(projectErrorList.get(i).MissingCheck);
        if((projectErrorList.get(i).MissingCheck.trim()).equals("No")){
            error_Check.setVisibility(View.GONE);
            error_Check1.setVisibility(View.GONE);
        }

        String s2 = projectErrorList.get(i).MissingPicture.trim();
        TextView error_Pictures = view.findViewById(R.id.error_Pictures);
        TextView error_Pictures1 = view.findViewById(R.id.error_Pictures1);
        if(s2.length()>0) {
            error_Pictures.setText(projectErrorList.get(i).MissingPicture.substring(1).toLowerCase().trim());
        }
        else{
            error_Pictures.setVisibility(View.GONE);
            error_Pictures1.setVisibility(View.GONE);
        }

        TextView error_Measure = view.findViewById(R.id.error_Measure);
        TextView error_Measure1 = view.findViewById(R.id.error_Measure1);
        String s3 = projectErrorList.get(i).MissingMeasure.trim();
        if(s3.length()>0) {
            error_Measure.setText(projectErrorList.get(i).MissingMeasure.substring(1).toLowerCase().trim());
        }
        else{
            error_Measure.setVisibility(View.GONE);
            error_Measure1.setVisibility(View.GONE);
        }
        return view;
    }
}
