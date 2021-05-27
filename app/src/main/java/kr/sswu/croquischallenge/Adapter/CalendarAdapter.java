package kr.sswu.croquischallenge.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.sswu.croquischallenge.Model.DaysInMonthModel;
import kr.sswu.croquischallenge.R;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private Context ctx;

    private ArrayList<DaysInMonthModel> daysOfMonth;

    private BottomSheetDialog bottomSheetImage, bottomSheetDialog;

    public CalendarAdapter(Context ctx, ArrayList<DaysInMonthModel> daysOfMonth) { //, String monthYear) {
        this.ctx = ctx;
        this.daysOfMonth = daysOfMonth;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.calendar_item, parent, false);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1599);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        DaysInMonthModel item = daysOfMonth.get(position);

        String fid = item.getFid();
        String fImg = item.getImage();

        holder.dayOfMonth.setText(item.getDay());
        try {
            Picasso.get().load(item.getImage()).into(holder.dayOfMonthImage);
        } catch (Exception e) {

        }

        holder.dayOfMonthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getDay().equals("")) {
                    Toast.makeText(ctx, "Invalid", Toast.LENGTH_SHORT).show();
                } else {
                    if (item.getImage().equals("")) {
                        Toast.makeText(ctx, "No Image", Toast.LENGTH_SHORT).show();
                    } else {
                        bottomSheetDialog = new BottomSheetDialog(ctx, R.style.BottomSheetTheme);
                        View sheetView = LayoutInflater.from(ctx).inflate(R.layout.calendar_bottom_sheet,
                                null);

                        sheetView.findViewById(R.id.btn_showPhoto).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetImage = new BottomSheetDialog(ctx, R.style.BottomSheetTheme);
                                View ImageView = LayoutInflater.from(ctx).inflate(R.layout.calendar_item__bottom_sheet,
                                        null);

                                TextView date = ImageView.findViewById(R.id.cal_date);
                                ImageView imageView = ImageView.findViewById(R.id.iv_show_day_photo);
                                TextView caption = ImageView.findViewById(R.id.tv_memo);

                                if (item.getDay().length() < 2)
                                    date.setText(item.getMonthYear() + " 0" + item.getDay());
                                else
                                    date.setText(item.getMonthYear() + " " + item.getDay());

                                try {
                                    Picasso.get().load(item.getImage()).into(imageView);
                                } catch (Exception e) {

                                }

                                caption.setText(item.getDescription());

                                bottomSheetImage.setContentView(ImageView);
                                bottomSheetImage.show();

                                bottomSheetDialog.dismiss();
                            }
                        });

                        sheetView.findViewById(R.id.btn_deletePhoto).setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                                builder.setTitle("Delete Post?");
                                builder.setMessage("This action cannot be undone");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deletePost(fid, fImg);
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        bottomSheetDialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                                Button delete = dialog.getButton(Dialog.BUTTON_POSITIVE);
                                delete.setTextColor(Color.RED);

                                Button cancel = dialog.getButton(Dialog.BUTTON_NEGATIVE);
                                cancel.setTextColor(Color.BLACK);
                            }
                        });
                        bottomSheetDialog.setContentView(sheetView);
                        bottomSheetDialog.show();
                    }
                }
            }
        });
    }

    private void deletePost(String fid, String fImg) {

        ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Deleting..");

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(fImg);
        ref.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query query = FirebaseDatabase.getInstance().getReference("Calendars").orderByChild("fid").equalTo(fid);
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
        return daysOfMonth.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {

        private TextView dayOfMonth;
        private ImageView dayOfMonthImage;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.txt_cellDay);
            dayOfMonthImage = itemView.findViewById(R.id.preview_image);
        }
    }
}