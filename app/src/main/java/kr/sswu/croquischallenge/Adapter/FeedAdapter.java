package kr.sswu.croquischallenge.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import kr.sswu.croquischallenge.Model.FeedModel;
import kr.sswu.croquischallenge.R;

public class FeedAdapter extends FirestoreRecyclerAdapter<FeedModel, FeedAdapter.FeedHolder> {

    public FeedAdapter(@NonNull FirestoreRecyclerOptions<FeedModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FeedHolder holder, int position, @NonNull FeedModel model) {
        holder.imageView.setImageURI(Uri.parse(model.getImageUrl()));
     //   holder.category.setText(model.getCategory());
    }

    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new FeedHolder(v);
    }


    class FeedHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
     //   TextView category;

        public FeedHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.f_image);
        //    category = itemView.findViewById(R.id.f_category);
        }
    }

}

