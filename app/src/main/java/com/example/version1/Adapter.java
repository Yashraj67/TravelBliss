package com.example.version1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final rv_interface rvInterface;
    private List<TripData> data;
    Context context;
    public Adapter(List<TripData> data, Context context,rv_interface rvInterface){
        this.data = data;
        this.context = context;
        this.rvInterface = rvInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design,parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String src_img = data.get(position).getImg();
        String title = data.get(position).getTitle();
        String rating = data.get(position).getRating();
        String des = data.get(position).getDescrip();
        String dis = data.get(position).getDistance();

        holder.setData(src_img,title,rating,des,dis);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView src;
        private TextView title_;
        private TextView rating_;
        private TextView dis_;
        private TextView descrip_;

        public  ViewHolder(@NonNull View itemView) {
            super(itemView);

            src = itemView.findViewById(R.id.ivImg);
            title_ = itemView.findViewById(R.id.tvTitle);
            rating_ = itemView.findViewById(R.id.tvRating);
            dis_ = itemView.findViewById(R.id.tvDistance);
            descrip_ = itemView.findViewById(R.id.tvDescrip);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rvInterface!=null){
                        int pos= getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            rvInterface.OnItemClicked(pos);
                        }
                    }
                }
            });

        }

        public void setData(String src_img, String title, String rating, String des, String dis) {
            Picasso.get().load(src_img).into(src);

            title_.setText(title);
            rating_.setText(rating);
            descrip_.setText(des);
            dis_.setText(dis);
        }
    }
}

