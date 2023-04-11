package com.example.termtrackerc196;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtrackerc196.db.Entities.Term;

import java.util.List;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.MyViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private List<Term> termList;
    public TermListAdapter(Context context, List<Term> termList, RecyclerViewInterface recyclerViewInterface) {

        this.context = context;
        this.termList = termList;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public TermListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.term_recycler_row, parent, false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TermListAdapter.MyViewHolder holder, int position) {
        holder.tvTermTitle.setText(this.termList.get(position).getTermTitle());
        holder.tvTermStartDate.setText(this.termList.get(position).getTermStartDate());
        holder.tvTermEndDate.setText(this.termList.get(position).getTermEndDate());
    }

    @Override
    public int getItemCount() {
        return this.termList.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTermTitle;
        TextView tvTermStartDate;
        TextView tvTermEndDate;

        public MyViewHolder(View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            tvTermTitle = view.findViewById(R.id.tvTermTitle);
            tvTermStartDate = view.findViewById(R.id.tvTermStartDate);
            tvTermEndDate = view.findViewById(R.id.tvTermEndDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TermListAdapter.this.recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            TermListAdapter.this.recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
