package kr.sswu.croquischallenge.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import kr.sswu.croquischallenge.Model.FeedModel;
import kr.sswu.croquischallenge.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private Context ctx;
    private List<FeedModel> feedList;
    private String mUid;

    private DatabaseReference likeRef;
    private DatabaseReference feedRef;

    private boolean processLike = false;

    public FeedAdapter(Context ctx, List<FeedModel> feedList) {
        this.ctx = ctx;
        this.feedList = feedList;

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        feedRef = FirebaseDatabase.getInstance().getReference().child("Feeds");
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

        String uid = tmp.getEmail();
        String fid = tmp.getFid();
        String uName = tmp.getName();
        String fTitle = tmp.getTitle();
        String fDate = tmp.getDate();
        String fImg = tmp.getImage();
        String fDescription = tmp.getDescription();
        String fLikes = tmp.getLikes();
        String ref = tmp.getRef();

        int likes = Integer.parseInt(fLikes);

        if (likes == 0)
            holder.txtLike.setText("");
        else if (likes == 1)
            holder.txtLike.setText(fLikes + " like");
        else if (likes > 1)
            holder.txtLike.setText(fLikes + " likes");

        try {
            Picasso.get().load(fImg).into(holder.fImage);
        } catch (Exception e) {

        }

        setLikes(holder, fid);

        holder.fImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ImageView img = new ImageView(ctx);
                img.setMinimumWidth(1000);
                img.setMinimumHeight(1000);
                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                try {
                    Picasso.get().load(fImg).into(img);
                } catch (Exception e) {

                }
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setView(img);
                builder.setPositiveButton("close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();

                return false;
            }
        });

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.info_dialog, null);
                ImageView img;
                TextView name, title, date, description;
                img = dialogView.findViewById(R.id.fImg);
                name = dialogView.findViewById(R.id.uName);
                title = dialogView.findViewById(R.id.fTitle);
                date = dialogView.findViewById(R.id.fDate);
                description = dialogView.findViewById(R.id.fDescription);


                try {
                    Picasso.get().load(fImg).into(img);
                } catch (Exception e) {
                }

                if (uName.equals(""))
                    name.setText(uid);
                else
                    name.setText(uName);

                title.setText(fTitle);

                if (fDate.equals("Date"))
                    date.setText("");
                else
                    date.setText(fDate);

                description.setText(fDescription);


                builder.setView(dialogView);
                builder.setPositiveButton("close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setCancelable(true);
                builder.show();
            }
        });

        holder.btnSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ref.equals("")) {
                    ImageView img = new ImageView(ctx);
                    try {
                        Picasso.get().load(ref).into(img);
                    } catch (Exception e) {

                    }
                    img.setPadding(10, 50, 10, 10);
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("References");
                    builder.setView(img);
                    builder.setPositiveButton("close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.setCancelable(true);
                    builder.show();
                } else
                    Toast.makeText(ctx, "No References" , Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int likes = Integer.parseInt(tmp.getLikes());
                processLike = true;
                String fid = tmp.getFid();
                likeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (processLike) {
                            if (snapshot.child(mUid).hasChild(fid)) {
                                feedRef.child(fid).child("likes").setValue(likes - 1);
                                likeRef.child(mUid).child(fid).removeValue();
                                tmp.setLikes(Integer.toString(likes - 1));
                            } else {
                                feedRef.child(fid).child("likes").setValue(likes + 1);
                                likeRef.child(mUid).child(fid).setValue("liked");
                                tmp.setLikes(Integer.toString(likes + 1));
                            }

                            processLike = false;

                            if (likes == 0)
                                holder.txtLike.setText("");
                            else if (likes == 1)
                                holder.txtLike.setText(fLikes + " like");
                            else if (likes > 1)
                                holder.txtLike.setText(fLikes + " likes");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void setLikes(FeedAdapter.FeedViewHolder holder, String fid) {
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(mUid).hasChild(fid)) {
                    holder.btnLike.setImageResource(R.drawable.ic_favorite);
                } else {
                    holder.btnLike.setImageResource(R.drawable.ic_favorite_border);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView fImage, btnSrc, btnInfo;
        TextView uName, fTitle, fDate, fDescription, txtLike;
        ImageButton btnLike;

        public FeedViewHolder(View itemView) {
            super(itemView);

            fImage = itemView.findViewById(R.id.imageView);
            uName = itemView.findViewById(R.id.uName);
            fTitle = itemView.findViewById(R.id.fTitle);
            fDate = itemView.findViewById(R.id.fDate);
            fDescription = itemView.findViewById(R.id.fDescription);
            txtLike = itemView.findViewById(R.id.txt_like);
            btnInfo = itemView.findViewById(R.id.btn_info);
            btnSrc = itemView.findViewById(R.id.btn_src);
            btnLike = itemView.findViewById(R.id.like);
        }
    }
}