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

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    Context ctx;
    List<FeedModel> feedList;

    public FeedAdapter(Context ctx, List<FeedModel> feedList) {
        this.ctx = ctx;
        this.feedList = feedList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.feed_item, parent, false);
        return new FeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedModel tmp = feedList.get(position);

        String fImg = tmp.getImage();
        String fTitle = tmp.getTitle();

        if (fTitle.equals(""))
            holder.fTitle.setVisibility(View.GONE);
        else
            holder.fTitle.setText(fTitle);

        try {
            Picasso.get().load(fImg).into(holder.fImage);
        } catch (Exception e) {

        }

        holder.fImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, FeedActivity.class);
                intent.putExtra("image", fImg);
                intent.putExtra("name", tmp.getName());
                intent.putExtra("email", tmp.getEmail());
                intent.putExtra("title", fTitle);
                intent.putExtra("description", tmp.getDescription());
                intent.putExtra("category", tmp.getCategory());
                intent.putExtra("uTime", tmp.getUpload_time());
                intent.putExtra("date", tmp.getDate());

                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView fImage;
        TextView fTitle;

        public FeedViewHolder(View itemView) {
            super(itemView);

            fImage = itemView.findViewById(R.id.fImage);
            fTitle = itemView.findViewById(R.id.fTitle);
        }
    }
}