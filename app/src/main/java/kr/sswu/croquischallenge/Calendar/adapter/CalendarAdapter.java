package kr.sswu.croquischallenge.Calendar.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Calendar;
import java.util.List;

import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.Calendar.model.CalendarHeader;
import kr.sswu.croquischallenge.Calendar.model.Day;
import kr.sswu.croquischallenge.Calendar.model.EmptyDay;

public class CalendarAdapter extends RecyclerView.Adapter {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;

    public CalendarAdapter(List<Object> calendarList) {
        mCalendarList = calendarList;
    }

    public void setCalendarList(List<Object> calendarList) {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE; // month type
        } else if (item instanceof String) {
            return EMPTY_TYPE; // empty day type
        } else {
            return DAY_TYPE; // day type

        }
    }


    // viewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // month type
        if (viewType == HEADER_TYPE) {

            HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.calendar_header_item, parent, false));

            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true);
            viewHolder.itemView.setLayoutParams(params);

            return viewHolder;

        // empty day type
        } else if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.calendar_day_empty_item, parent, false));

        }
        // day type
        else {
            return new DayViewHolder(inflater.inflate(R.layout.calendar_day_item, parent, false));

        }

    }

    // 데이터 넣기
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        /** month 꾸미기*/
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면 model에 데이터들어감
                model.setHeader((Long) item);
            }
            // view에 표시하기
            holder.bind(model);
        }
        /** empty day type 꾸미기 */
        else if (viewType == EMPTY_TYPE) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            EmptyDay model = new EmptyDay();
            holder.bind(model);
        }
        /** day type 꾸미기 */
        else if (viewType == DAY_TYPE) {
            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            Day model = new Day();
            if (item instanceof Calendar) {

                // Model에 Calendar 값을 넣어서 몇 일인지 데이터 넣기
                model.setCalendar((Calendar) item);
            }
            // Model의 데이터를 View에 표현하기
            holder.bind(model);
        }
    }

    // 개수구하기
    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }
}