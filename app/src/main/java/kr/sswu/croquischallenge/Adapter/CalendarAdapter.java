package kr.sswu.croquischallenge.Adapter;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.util.ArrayList;

import kr.sswu.croquischallenge.AddPhotoCalendarActivity;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.ShowPhotoCalendarActivity;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private Context ctx;
    private ArrayList<String> daysOfMonth;
    private BottomSheetDialog bottomSheetDialog;

    private String uid, monthYear;

    public CalendarAdapter(Context ctx, ArrayList<String> daysOfMonth, String monthYear) {
        this.ctx = ctx;
        this.daysOfMonth = daysOfMonth;
        this.monthYear = monthYear;

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        layoutParams.height = (int) (parent.getHeight() * 0.1599);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.dayOfMonth.setText(daysOfMonth.get(position));

        String item = daysOfMonth.get(position);

        holder.dayOfMonthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.equals(""))
                    Toast.makeText(ctx, "Invalid", Toast.LENGTH_SHORT).show();
                else {
                    bottomSheetDialog = new BottomSheetDialog(ctx, R.style.BottomSheetTheme);

                    View sheetView = LayoutInflater.from(ctx).inflate(R.layout.calendar_bottom_sheet,
                            null);
                    sheetView.findViewById(R.id.btn_addPhoto).setOnClickListener(it -> {
                        Intent intent = new Intent(ctx, AddPhotoCalendarActivity.class);
                        intent.putExtra("date", monthYear + item);
                        ctx.startActivity(intent);
                        bottomSheetDialog.dismiss();
                        // save
                        // uri(photo) -> uri.tostring() -> file:///data/user/0/kr.sswu.croquischallenge/cache/cropped7692266798634543379.jpg(path)
                        // path -> sharedpreference[]

                        // restore
                        // sharedpreference[] -> read -> file:///data/user/0/kr.sswu.croquischallenge/cache/cropped7692266798634543379.jpg
                        // imageview -> show


                    });
                    sheetView.findViewById(R.id.btn_showPhoto).setOnClickListener(it -> {
                        Intent intent = new Intent(ctx, ShowPhotoCalendarActivity.class);
                        intent.putExtra("date", monthYear + item);
                        ctx.startActivity(intent);
                        bottomSheetDialog.dismiss();

                    });

                    bottomSheetDialog.setContentView(sheetView);
                    bottomSheetDialog.show();
                }
            }
        });

        SharedPreferences settings = ctx.getSharedPreferences("calendar", 0);
        holder.dayOfMonthImage.setImageURI(Uri.parse(settings.getString(uid + monthYear + item + "image", "")));
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