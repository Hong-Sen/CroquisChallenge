package kr.sswu.croquischallenge.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kr.sswu.croquischallenge.Model.DaysInMonthModel;
import kr.sswu.croquischallenge.R;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private Context ctx;
    private ArrayList<DaysInMonthModel> daysOfMonth;

    private BottomSheetDialog bottomSheetDialog;

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
                        View sheetView = LayoutInflater.from(ctx).inflate(R.layout.calendar_item__bottom_sheet,
                                null);

                        TextView date = sheetView.findViewById(R.id.cal_date);
                        ImageView imageView = sheetView.findViewById(R.id.iv_show_day_photo);
                        TextView caption = sheetView.findViewById(R.id.tv_memo);

                        if (item.getDay().length() < 2)
                            date.setText(item.getMonthYear() + " 0" + item.getDay());
                        else
                            date.setText(item.getMonthYear() + " " + item.getDay());

                        try {
                            Picasso.get().load(item.getImage()).into(imageView);
                        } catch (Exception e) {

                        }

                        bottomSheetDialog.setContentView(sheetView);
                        bottomSheetDialog.show();
                    }
                }
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