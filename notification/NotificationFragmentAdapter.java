package com.roc.android.roc.activities.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roc.android.roc.R;
import com.roc.android.roc.pojo.CustomRocsModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationFragmentAdapter extends RecyclerView.Adapter<NotificationFragmentAdapter.MyViewHolder> {

    private List<CustomRocsModel> retailers;
    private String storeName;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mBannerNameTv;
        private TextView mDistanceTv;
        private TextView mOpeningsTv;
        private TextView mDateEffectiveTv;
        private TextView mStoreNameTv;
        private TextView mPostedTimeTv;
       // private ImageView mMarkSeenImage;
        private ImageView mCalendarImage;
        private ImageView mMarkerImage;
        private View mSideView;

        public MyViewHolder(View view) {
            super(view);

            mBannerNameTv = view.findViewById(R.id.banner_name);
            mDistanceTv = view.findViewById(R.id.distance);
            mOpeningsTv = view.findViewById(R.id.opening);
            mDateEffectiveTv = view.findViewById(R.id.date_effective);
            mStoreNameTv = view.findViewById(R.id.store_name);
            mPostedTimeTv = view.findViewById(R.id.posted_time);
            mMarkerImage = view.findViewById(R.id.marker_image);
            mCalendarImage = view.findViewById(R.id.calendar);
         //   mMarkSeenImage = view.findViewById(R.id.mark_seen_image);
            mSideView = view.findViewById(R.id.side_view);
        }
    }

    public NotificationFragmentAdapter(Context context, List<CustomRocsModel>  retailers, String storeName) {
        this.retailers = retailers;
        this.storeName = storeName;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String postedTime = getDurationTimeStamp(retailers.get(position).getDateCreated());
        holder.mStoreNameTv.setText(storeName);
        holder.mBannerNameTv.setText(retailers.get(position).getBannerName());
        holder.mDistanceTv.setText(retailers.get(position).getDistance());
        holder.mOpeningsTv.setText(retailers.get(position).getOpening());
        holder.mDateEffectiveTv.setText(retailers.get(position).getDateEffective());
        holder.mPostedTimeTv.setText(postedTime);
        if(retailers.get(position).getOpening() == "true"){
            holder.mOpeningsTv.setText("Opening");
            holder.mCalendarImage.setImageResource(R.drawable.ic_calender_plus_blue);
            holder.mOpeningsTv.setTextColor(mContext.getResources().getColor(R.color.blue_2));
            holder.mDateEffectiveTv.setTextColor(mContext.getResources().getColor(R.color.blue_2));
            holder.mSideView.setBackgroundColor(mContext.getResources().getColor(R.color.blue_2));

        } else {
            holder.mOpeningsTv.setText("Closing");
            holder.mCalendarImage.setImageResource(R.drawable.ic_calender_minus_red);
            holder.mOpeningsTv.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.mDateEffectiveTv.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.mSideView.setBackgroundColor(mContext.getResources().getColor(R.color.red));


        }
//        if(retailers.get(position).getIsSeen() == "true"){
//            holder.mMarkSeenImage.setImageResource(R.drawable.ic_right_icon);
//
//        }
        String url = retailers.get(position).getSubsidiaryId().substring(0, retailers.get(position).getSubsidiaryId().length() - 1);
        String subsidary = url.substring(0, url.length() - 1);

        Picasso.get()
                .load("https://www.creditntell.com/member/reit/markers/specific/marker_" + subsidary + "_c_fo.png")
                .placeholder(R.drawable.ic_location_big)
                .error(R.drawable.ic_marker_blue)
                // To fit image into imageView
                .fit()
                // To prevent fade animation
                .noFade()
                .into(holder.mMarkerImage);


    }

    /**
     * Method will take Date in "MMMM, dd yyyy HH:mm:s" format and return time difference like added: 3 min ago
     *
     * @param date : date in "MMMM, dd yyyy HH:mm:s" format
     * @return : time difference
     */
    private String getDurationTimeStamp(String date) {
        String timeDifference = "";

        //date formatter as per the coder need
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd yyyy HH:mm:s");

        Date startDate = null;
        try {
            startDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //end date will be the current system time to calculate the lapse time difference
        Date endDate = new Date();

        //get the time difference in milliseconds
        long duration = endDate.getTime() - startDate.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

        if (diffInDays > 365) {
            int year = (int) (diffInDays / 365);
            timeDifference = year + mContext.getString(R.string.year_ago);
        } else if (diffInDays > 30) {
            int month = (int) (diffInDays / 30);
            timeDifference = month + mContext.getString(R.string.month_ago);
        }
        //if days are not enough to create year then get the days
        else if (diffInDays > 1) {
            timeDifference = diffInDays + mContext.getString(R.string.day_ago);
        }
        //if days value<1 then get the hours
        else if (diffInHours > 1) {
            timeDifference = diffInHours + mContext.getString(R.string.hour_ago);
        }
        //if hours value<1 then get the minutes
        else if (diffInMinutes > 1) {
            timeDifference = diffInMinutes + mContext.getString(R.string.min_ago);
        }
        //if minutes value<1 then get the seconds
        else if (diffInSeconds > 1) {
            timeDifference = diffInSeconds + mContext.getString(R.string.sec_ago);
        }

        return mContext.getString(R.string.posted) + " " + timeDifference;
    }


    @Override
    public int getItemCount() {
        return retailers.size();
    }
}
