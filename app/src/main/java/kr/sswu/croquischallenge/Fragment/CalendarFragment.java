package kr.sswu.croquischallenge.Fragment;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import kr.sswu.croquischallenge.Adapter.CalendarAdapter;
import kr.sswu.croquischallenge.Model.CalendarModel;
import kr.sswu.croquischallenge.Model.DaysInMonthModel;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.YearMonthPickerDialog;

public class CalendarFragment extends Fragment {

    private String curUid;
    private LocalDate selectedDate;

    private ImageView back, forward;
    private TextView monthYear;
    private RecyclerView recyclerView;

    private ProgressBar progressBar;
    private TextView txt_progress, txt_totalDay;

    public CalendarAdapter adapter;
    private ArrayList<CalendarModel> calList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        curUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        back = (ImageView) view.findViewById(R.id.btn_back);
        monthYear = (TextView) view.findViewById(R.id.txt_monthYear);
        forward = (ImageView) view.findViewById(R.id.btn_forward);

        progressBar = (ProgressBar) view.findViewById(R.id.cal_progressBar);
        txt_progress = (TextView) view.findViewById(R.id.txt_progress);
        txt_totalDay = (TextView) view.findViewById(R.id.txt_totalDay);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        recyclerView = (RecyclerView) view.findViewById(R.id.calendarRecyclerView);
        recyclerView.setLayoutManager(layoutManager);

        calList = new ArrayList<>();
        selectedDate = LocalDate.now();
        loadCalendar();

        monthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YearMonthPickerDialog pd = new YearMonthPickerDialog();
                pd.setListener(d);
                pd.show(getActivity().getSupportFragmentManager(), "YearMonthPickerTest");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });

        return view;
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.d("YearMonthPickerTest", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
            selectedDate = selectedDate.withYear(year);
            selectedDate = selectedDate.withMonth(monthOfYear);

            setMonthView();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadCalendar() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Calendars");
        ref.orderByChild("uid").equalTo(curUid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                calList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String uid = ds.child("uid").getValue().toString();
                    String fid = ds.child("fid").getValue().toString();
                    String monthYear = ds.child("monthYear").getValue().toString();
                    String day = ds.child("day").getValue().toString();
                    String date = ds.child("date").getValue().toString();
                    String img = ds.child("image").getValue().toString();
                    String description = ds.child("description").getValue().toString();

                    CalendarModel item = new CalendarModel(uid, fid, monthYear, day, date, img, description);

                    calList.add(item);
                }
                setMonthView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        int countAdd = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM");
        monthYear.setText(selectedDate.format(formatter));

        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        ArrayList<DaysInMonthModel> daysInMonthArray = new ArrayList<>();
        for (int i = 1; i <= 42; i++) {
            DaysInMonthModel model = new DaysInMonthModel();
            model.setMonthYear(selectedDate.format(formatter));

            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                model.setDay("");
            } else {
                model.setDay(String.valueOf(i - dayOfWeek));
                for (int j = 0; j < calList.size(); j++) {
                    CalendarModel calendarModel = calList.get(j);
                    if (calendarModel.getMonthYear().equals(model.getMonthYear())) {
                        if (i - dayOfWeek < 10) {
                            if (calendarModel.getDay().equals("0" + String.valueOf(i - dayOfWeek))) {
                                model.setFid(calList.get(j).getFid());
                                model.setImage(calList.get(j).getImage());
                                model.setDescription(calList.get(j).getDescription());
                                countAdd++;
                                break;
                            }
                        } else {
                            if (calendarModel.getDay().equals(String.valueOf(i - dayOfWeek))) {
                                model.setFid(calList.get(j).getFid());
                                model.setImage(calList.get(j).getImage());
                                model.setDescription(calList.get(j).getDescription());
                                countAdd++;
                                break;
                            }
                        }
                    }
                }
            }

            if (model.getImage() == null) {
                model.setFid("");
                model.setImage("");
                model.setDescription("");
            }

            txt_totalDay.setText(" / " + String.valueOf(daysInMonth));
            txt_progress.setText(String.valueOf(countAdd));
            progressBar.setMax(daysInMonth);
            progressBar.setProgress(countAdd);
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));

            daysInMonthArray.add(model);
            adapter = new CalendarAdapter(getContext(), daysInMonthArray);
            recyclerView.setAdapter(adapter);
        }
    }
}