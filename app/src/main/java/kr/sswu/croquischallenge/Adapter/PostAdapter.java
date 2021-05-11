package kr.sswu.croquischallenge.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import kr.sswu.croquischallenge.Model.FeedModel;
import kr.sswu.croquischallenge.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context ctx;
    List<FeedModel> feedList;

    String mEmail;

    public PostAdapter(Context ctx, List<FeedModel> feedList) {
        this.ctx = ctx;
        this.feedList = feedList;
        mEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
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

        String fid = tmp.getFid();
        String email = tmp.getEmail();
        String fImg = tmp.getImage();
        String fTitle = tmp.getTitle();
        String fDate = tmp.getDate();
        String fDescription = tmp.getDescription();

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                showMoreOption(holder.btnMore, email, mEmail, fid, fImg);
            }
        });

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMoreOption(ImageView btnMore, String email, String mEmail, String fid, String fImg) {

        PopupMenu popupMenu = new PopupMenu(ctx, btnMore, Gravity.END);

        if (email.equals(mEmail)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == 0) {
                    deletePost(fid, fImg);
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void deletePost(String fid, String fImg) {

        ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Deleting..");

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(fImg);
        ref.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query query = FirebaseDatabase.getInstance().getReference("Feeds").orderByChild("fid").equalTo(fid);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    ds.getRef().removeValue();
                                }
                                Toast.makeText(ctx, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView btnMore, fImage;
        TextView fTitle, fDate, fDescription;

        public PostViewHolder(View itemView) {
            super(itemView);

            btnMore = itemView.findViewById(R.id.btn_more);
            fImage = itemView.findViewById(R.id.imageView);
            fTitle = itemView.findViewById(R.id.fTitle);
            //    tLike = itemView.findViewById(R.id.txt_like);
            fDate = itemView.findViewById(R.id.fDate);
            fDescription = itemView.findViewById(R.id.fDescription);
        }
    }
}