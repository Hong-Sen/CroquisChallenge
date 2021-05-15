package kr.sswu.croquischallenge.Fragment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import kr.sswu.croquischallenge.Adapter.CalendarAdapter;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.YearMonthPickerDialog;

public class CalendarFragment extends Fragment {

    private ImageView back, forward;
    private TextView monthYear;
    private RecyclerView recyclerView;
    private LocalDate selectedDate;
    public CalendarAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        back = (ImageView) view.findViewById(R.id.btn_back);
        monthYear = (TextView) view.findViewById(R.id.txt_monthYear);
        forward = (ImageView) view.findViewById(R.id.btn_forward);
        recyclerView = (RecyclerView) view.findViewById(R.id.calendarRecyclerView);

        selectedDate = LocalDate.now();

        monthYear.setOnClickListener(new View.OnClickListener(){
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

        setMonthView();

        return view;
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            Log.d("YearMonthPickerTest", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
            selectedDate = selectedDate.withYear(year);
            selectedDate = selectedDate.withMonth(monthOfYear);
            setMonthView();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy  MM");
        monthYear.setText(selectedDate.format(formatter));

        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add("");
            else
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
        }

        adapter = new CalendarAdapter(getContext(), daysInMonthArray, selectedDate.format(DateTimeFormatter.ofPattern("yyyyMM")));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}