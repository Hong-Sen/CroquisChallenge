package kr.sswu.croquischallenge.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

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
        String fImg = feedList.get(position).getImage();
        String fTitle = feedList.get(position).getTitle();

        holder.fTitle.setText(fTitle);

        try{
            Picasso.get().load(fImg).into(holder.fImage);
        } catch (Exception e) {

        }

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "More" , Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView fImage;
        TextView fTitle, fTime;
        ImageButton moreBtn;

        public FeedViewHolder(View itemView) {
            super(itemView);

            fImage = itemView.findViewById(R.id.fImage);
            fTitle = itemView.findViewById(R.id.fTitle);
            fTime = itemView.findViewById(R.id.fTime);
            moreBtn = itemView.findViewById(R.id.moreBtn);
        }
    }

}

