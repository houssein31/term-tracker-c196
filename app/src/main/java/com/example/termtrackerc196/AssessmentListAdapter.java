package com.example.termtrackerc196;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtrackerc196.db.Entities.Assessment;

import java.util.List;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private List<Assessment> assessmentList;

    public AssessmentListAdapter(Context context, List<Assessment> assessmentList, RecyclerViewInterface recyclerViewInterface) {

        this.context = context;
        this.assessmentList = assessmentList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AssessmentListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.assessment_recycler_row, parent, false);

        return new AssessmentListAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentListAdapter.MyViewHolder holder, int position) {
        holder.tvAssessmentTitle.setText(this.assessmentList.get(position).getAssessmentTitle());
        holder.tvAssessmentStatus.setText(this.assessmentList.get(position).getAssessmentStatus());
        holder.tvAssessmentEndDate.setText(this.assessmentList.get(position).getAssessmentEndDate());
//        holder.tvCourseNote.setText(this.courseList.get(position).getCourseNote());
    }

    @Override
    public int getItemCount() {  return this.assessmentList.size(); }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAssessmentTitle;
        TextView tvAssessmentStatus;
        TextView tvAssessmentEndDate;


        public MyViewHolder(View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            tvAssessmentTitle = view.findViewById(R.id.tvAssessmentTitle);
            tvAssessmentEndDate = view.findViewById(R.id.tvAssessmentEndDate);
            tvAssessmentStatus = view.findViewById(R.id.tvAssessmentStatus);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AssessmentListAdapter.this.recyclerViewInterface != null) {
                        int pos = getAbsoluteAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            AssessmentListAdapter.this.recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }


    }
}
