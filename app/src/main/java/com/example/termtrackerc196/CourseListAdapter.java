package com.example.termtrackerc196;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtrackerc196.db.Entities.Course;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    private List<Course> courseList;

    public CourseListAdapter(Context context, List<Course> courseList, RecyclerViewInterface recyclerViewInterface) {

        this.context = context;
        this.courseList = courseList;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public CourseListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.course_recycler_row, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListAdapter.MyViewHolder holder, int position) {
        holder.tvCourseTitle.setText(this.courseList.get(position).getCourseTitle());
        holder.tvCourseInstructorName.setText(this.courseList.get(position).getCourseInstructorName());
//        holder.tvCourseInstructorEmail.setText(this.courseList.get(position).getCourseInstructorEmail());
//        holder.tvCourseInstructorPhone.setText(this.courseList.get(position).getCourseInstructorPhone());
        holder.tvCourseStatus.setText(this.courseList.get(position).getCourseStatus());
//        holder.tvCourseStartDate.setText(this.courseList.get(position).getCourseStartDate());
//        holder.tvCourseEndDate.setText(this.courseList.get(position).getCourseEndDate());
//        holder.tvCourseNote.setText(this.courseList.get(position).getCourseNote());
    }

    @Override
    public int getItemCount() {  return this.courseList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCourseTitle;
        TextView tvCourseInstructorName;
        TextView tvCourseInstructorEmail;
        TextView tvCourseInstructorPhone;
        TextView tvCourseStatus;
        TextView tvCourseStartDate;
        TextView tvCourseEndDate;
        TextView tvCourseNote;

        public MyViewHolder(View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            tvCourseTitle = view.findViewById(R.id.tvCourseTitle);
            tvCourseInstructorName = view.findViewById(R.id.tvCourseInstructorName);
//            tvCourseInstructorEmail = view.findViewById(R.id.tvCourseInstructorEmail);
//            tvCourseInstructorPhone = view.findViewById(R.id.tvCourseInstructorPhone);
            tvCourseStatus = view.findViewById(R.id.tvCourseStatus);
//            tvCourseStartDate = view.findViewById(R.id.tvCourseStartDate);
//            tvCourseEndDate = view.findViewById(R.id.tvCourseEndDate);
//            tvCourseNote = view.findViewById(R.id.courseNote);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CourseListAdapter.this.recyclerViewInterface != null) {
                        int pos = getAbsoluteAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            CourseListAdapter.this.recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }


    }
}
