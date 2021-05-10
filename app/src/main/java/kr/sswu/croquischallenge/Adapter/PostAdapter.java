package kr.sswu.croquischallenge.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kr.sswu.croquischallenge.FeedActivity;
import kr.sswu.croquischallenge.Model.FeedModel;
import kr.sswu.croquischallenge.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context ctx;
    List<FeedModel> feedList;

    public PostAdapter(Context ctx, List<FeedModel> feedList) {
        this.ctx = ctx;
        this.feedList = feedList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        FeedModel tmp = feedList.get(position);

        String fImg = tmp.getImage();
        String fTitle = tmp.getTitle();
        String fDate = tmp.getDate();
        String fDescription = tmp.getDescription();

        if (fTitle.equals(""))
            holder.fTitle.setVisibility(View.GONE);
        else
            holder.fTitle.setText(fTitle);

        if (fDescription.equals(""))
            holder.fDescription.setVisibility(View.GONE);
        else
            holder.fDescription.setText(fDescription);

        if (fDate.contentEquals("Date"))
            holder.fDate.setVisibility(View.GONE);
        else
            holder.fDate.setText(fDate);

        try {
            Picasso.get().load(fImg).into(holder.fImage);
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView fImage;
        TextView fTitle, fDate, fDescription;

        public PostViewHolder(View itemView) {
            super(itemView);

            fImage = itemView.findViewById(R.id.imageView);
            fTitle = itemView.findViewById(R.id.fTitle);
        //    tLike = itemView.findViewById(R.id.txt_like);
            fDate = itemView.findViewById(R.id.fDate);
            fDescription = itemView.findViewById(R.id.fDescription);
        }
    }
}