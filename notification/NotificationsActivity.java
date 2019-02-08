package com.roc.android.roc.activities.notification;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roc.android.roc.R;
import com.roc.android.roc.base.NavigationActivity;
import com.roc.android.roc.pojo.CommonResponse;
import com.roc.android.roc.pojo.Data;
import com.roc.android.roc.pojo.DialogType;
import com.roc.android.roc.pojo.GetAddedStoresResponse;
import com.roc.android.roc.pojo.NotificationModel;
import com.roc.android.roc.services.ApiClient;
import com.roc.android.roc.services.ApiConstants;
import com.roc.android.roc.services.ApiInterface;
import com.roc.android.roc.services.MakeCalls;
import com.roc.android.roc.utils.AppStringConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Class is used for Notification screen
 * <p>
 * created by Rajneesh Shukla on 20/12/18
 */
public class NotificationsActivity extends NavigationActivity implements MakeCalls.CallListener{
    private NotificationsPagerAdapter mNotificationsPagerAdapter;


    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
//        setUpNavigation();
        //TODO:  Change the tab icon as per design
        //Initialise the Pager Adapter
        mNotificationsPagerAdapter = new NotificationsPagerAdapter(getSupportFragmentManager(), this);

        mNotificationsPagerAdapter.addFragment(AllNotificationFragment.newInstance(getApplicationContext()), getString(R.string.all), R.drawable.ic_contact);
        mNotificationsPagerAdapter.addFragment(OpeningsFragment.newInstance(getApplicationContext()), getString(R.string.openings), R.drawable.ic_contact);
        mNotificationsPagerAdapter.addFragment(ClosingsFragment.newInstance(getApplicationContext()), getString(R.string.closings), R.drawable.ic_contact);
        int limit = (mNotificationsPagerAdapter.getCount() > 1 ? mNotificationsPagerAdapter.getCount() - 1 : 1);


        mViewPager.setAdapter(mNotificationsPagerAdapter);
        mViewPager.setOffscreenPageLimit(limit);
        mNotificationsPagerAdapter.notifyDataSetChanged();
        //Set the Adapter to View Pager
       // mViewPager.setOffscreenPageLimit(3);
        //Tie View Pager to Tab Layout
        mTabLayout.setupWithViewPager(mViewPager);

        //Initialise first Tab
        highLightCurrentTab(0);
        makeGetAddedStoresBackendCall();

        //Tab Change Listener
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }




    private void highLightCurrentTab(int position) {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mNotificationsPagerAdapter.getTabView(i));
        }
        TabLayout.Tab tab = mTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mNotificationsPagerAdapter.getSelectedTabView(position));
    }


    /**
     * Handle event when top black arrow is presses
     */
    @OnClick(R.id.ic_back)
    public void onBackArrowPressed() {
        finish();
    }

    private void makeGetAddedStoresBackendCall() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("scope", AppStringConstants.ROC);
        params.put("action", AppStringConstants.GET_RECENT_NOTIFICATION);

        if (isInternetConnected(this)) {
            showLoader(this);
            ApiInterface apiInterface = ApiClient.createService(ApiInterface.class);
            Call<NotificationModel> call = apiInterface.allNotificationActivity(params);
            MakeCalls.commonCall(call, this, ApiConstants.GET_RECENT_NOTIFICATION_KEY);
        } else {
            showDialog(getString(R.string.check_internet), getString(R.string.app_name), new DialogType(DialogType.DIALOG_MESSAGE));
        }
    }

    @Override
    public void onCallSuccess(@NonNull Object result, String key) {
        hideLoader(this);
        super.onCallSuccess(result, key);
        Log.e("response", result+ "");
        if (key.equals(ApiConstants.GET_RECENT_NOTIFICATION_KEY)) {
            NotificationModel response = (NotificationModel) result;
            //Log.e('key', response);
            Log.e("response", response+ "");
            if(response.getData().size() > 0){
                AllNotificationFragment.addNotificationData(response.getData().get(0).getSlides().get(0).getRetailers(),response.getData().get(0).getSlides().get(0).getStore_name());
                // Toast.makeText(getApplicationContext(),response.getData().get(0).getSlides().get(0).getAddress()+"", Toast.LENGTH_LONG).show();
                //mAddedByMeFragment.getRecentlyAddedStores(mAddedByMeStores);
                OpeningsFragment.addNotificationData(response.getData().get(0).getSlides().get(0).getRetailers(),response.getData().get(0).getSlides().get(0).getStore_name());
                ClosingsFragment.addNotificationData(response.getData().get(0).getSlides().get(0).getRetailers(),response.getData().get(0).getSlides().get(0).getStore_name());

            }
           // Toast.makeText(getApplicationContext(),response+"inside response", Toast.LENGTH_LONG).show();

            //mAddedByOthersFragment.getRecentlyAddedStores(mAddedByOthers);
        }
    }


    @Override
    public void onCallFailure(@NonNull Object result, String key) {
        hideLoader(this);
        showDialog(result+"", "", new DialogType(DialogType.DIALOG_MESSAGE));
    }


//    /**
//     * Open drawer
//     */
//    @OnClick(R.id.hamburger_icon)
//    public void openNavDrawer() {
//        openDrawer();
//    }

    /**
     * Pager Adapter Class
     */
    public static class NotificationsPagerAdapter extends FragmentPagerAdapter {
        private Context context;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Integer> mFragmentIconList = new ArrayList<>();

        NotificationsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title, int tabIcon) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mFragmentIconList.add(tabIcon);
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView tabTextView = view.findViewById(R.id.tabTextView);
            tabTextView.setText(mFragmentTitleList.get(position));
            ImageView tabImageView = view.findViewById(R.id.tabImageView);
            tabImageView.setImageResource(mFragmentIconList.get(position));
            return view;
        }

        public View getSelectedTabView(int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView tabTextView = view.findViewById(R.id.tabTextView);
            tabTextView.setText(mFragmentTitleList.get(position));
            tabTextView.setTextColor(ContextCompat.getColor(context, R.color.blue_3));
            ImageView tabImageView = view.findViewById(R.id.tabImageView);
            tabImageView.setImageResource(mFragmentIconList.get(position));
            tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.blue_3), PorterDuff.Mode.SRC_ATOP);
            return view;
        }
    }
}

