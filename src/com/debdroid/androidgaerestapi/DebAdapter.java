package com.debdroid.androidgaerestapi;

import java.util.List;

import com.debdroid.apiwithjdo.dao.employeeendpoint.model.Employee;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Admin on 04/03/2015.
 */
public class DebAdapter extends RecyclerView.Adapter<DebAdapter.ViewHolder> {
	List<Employee> employees;
	Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mEidView;
        TextView mNameView;
        TextView mSalaryView;
        TextView mDegView;
        public ViewHolder(View view) {
            super(view);
            Log.d("DEB"," ViewHolder constructor is called");
            mEidView = (TextView) view.findViewById(R.id.listtextView1);
            mNameView = (TextView) view.findViewById(R.id.listtextView2);
            mSalaryView = (TextView) view.findViewById(R.id.listtextView3);
            mDegView = (TextView) view.findViewById(R.id.listtextView4);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DebAdapter(List<Employee> employees, Context context) {
        Log.d("DEB"," MyAdapter constructor is called");
        this.employees = employees;
        this.context = context;
    }

    @Override
    public DebAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("DEB","onCreateViewHolder is called");
        View mView = LayoutInflater.from(context).inflate(R.layout.sample_custom_layout,parent,false);
        ViewHolder mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("DEB","onBindViewHolder is called");
        Employee emp = employees.get(position);
        holder.mEidView.setText(Long.toString(emp.getEid()));
        holder.mNameView.setText(emp.getEname());
        holder.mSalaryView.setText(Double.toString(emp.getSalary()));
        holder.mDegView.setText(emp.getDeg());
    }

    @Override
    public int getItemCount() {
        // Return the size of your dataset (invoked by the layout manager)
        Log.d("DEB","getItemCount is called");
        return employees.size();
    }
}
