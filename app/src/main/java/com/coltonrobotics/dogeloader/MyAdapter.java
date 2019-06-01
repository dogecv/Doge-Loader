package com.coltonrobotics.dogeloader;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<FileParser.LibEntry> mDataset;
    public static final String LIB_CLASS = "com.coltonrobotics.dogeloader.LIB_CLASS";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textTitle, textAuthor, textDesc;
        public FileParser.LibEntry entry;
        public MyViewHolder(final View v) {
            super(v);
            textTitle = v.findViewById(R.id.text_name);
            textAuthor = v.findViewById(R.id.text_author);
            textDesc = v.findViewById(R.id.text_desc);

            v.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DownloadActivity.class);
                    intent.putExtra(LIB_CLASS, entry);
                    view.getContext().startActivity(intent);

                }
            });
        }

        public void update(){
            if(FileUtil.isInstalled(entry.title)){
                ((CardView)itemView.findViewById(R.id.card_view)).setBackgroundResource(R.color.success);
            }else{
                ((CardView)itemView.findViewById(R.id.card_view)).setBackgroundResource(R.color.failed);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<FileParser.LibEntry> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.my_test_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textTitle.setText(mDataset.get(position).name);
        holder.textAuthor.setText(mDataset.get(position).author);
        holder.textDesc.setText(mDataset.get(position).desc );
        holder.entry = mDataset.get(position);
        holder.update();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateData(List<FileParser.LibEntry> myDataset) {
        mDataset = myDataset;
        notifyDataSetChanged();
    }
}