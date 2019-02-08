package com.roc.android.roc.activities.notification;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roc.android.roc.R;
import com.roc.android.roc.pojo.CustomRocsModel;
import com.roc.android.roc.pojo.Retailers;
import com.roc.android.roc.pojo.Rocs;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * class is a fragment used in All notification tab of notification screen
 * created by Rajneesh Shukla on 20/12/18
 */
public class AllNotificationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private static List<CustomRocsModel> rocsModels;

    private static RecyclerView mAllNotificationFragmentRecyclerView;
    private static NotificationFragmentAdapter mAllNotificationFragmentAdapter;
    private static Context mContext;
    private static List<Retailers> mRetailers;
    private static String mStoreName;
    private static List<CustomRocsModel> rocsModels;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AllNotificationFragment() {
        // Required empty public constructor
    }

    public static AllNotificationFragment newInstance(String param1, String param2) {
        AllNotificationFragment fragment = new AllNotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AllNotificationFragment newInstance(Context context) {
        mContext = context;
       // mNotificationModel = response;
        return new AllNotificationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //prepareMovieData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_notification, container, false);
        ButterKnife.bind(this, view);
        mAllNotificationFragmentRecyclerView = view.findViewById((R.id.recycler_view));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mAllNotificationFragmentRecyclerView.setLayoutManager(mLayoutManager);
        mAllNotificationFragmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mAllNotificationFragmentAdapter = new AllNotificationFragmentAdapter(mContext);
//        mAllNotificationFragmentRecyclerView.setAdapter(mAllNotificationFragmentAdapter);
        if(mAllNotificationFragmentAdapter != null){
            mAllNotificationFragmentAdapter = new NotificationFragmentAdapter(mContext,rocsModels, mStoreName);
            mAllNotificationFragmentRecyclerView.setAdapter(mAllNotificationFragmentAdapter);
            mAllNotificationFragmentAdapter.notifyDataSetChanged();
        }

        return view;
    }


    public static void addNotificationData(List<Retailers> retailers, String storeName) {
        mRetailers = retailers;
        mStoreName = storeName;
        rocsModels = new ArrayList<>();;
        for(int i=0; i<mRetailers.size(); i++) {
            CustomRocsModel customRocsModel = new CustomRocsModel();
            List<Rocs> rocs = mRetailers.get(i).getRocs();
            for(int j = 0; j<rocs.size(); j++){
                customRocsModel.setDistance(rocs.get(j).getDistance());
                customRocsModel.setOpening(rocs.get(j).getOpening());
                customRocsModel.setIsSeen(rocs.get(j).getIs_seen());
                customRocsModel.setDateEffective(rocs.get(j).getDate_effective());
                customRocsModel.setDateCreated(rocs.get(j).getDate_created());
                customRocsModel.setMarkerFile(mRetailers.get(i).getMarker_file());
                customRocsModel.setSubsidiaryId(mRetailers.get(i).getSubsidiary_id());
                customRocsModel.setBannerName(mRetailers.get(i).getBanner_name());
                rocsModels.add(customRocsModel);
             //   Toast.makeText(mContext,mRetailers.get(1).getSubsidiary_id()+ "hj", Toast.LENGTH_LONG).show();


            }


        }
        mAllNotificationFragmentAdapter = new NotificationFragmentAdapter(mContext,rocsModels, mStoreName);
        mAllNotificationFragmentRecyclerView.setAdapter(mAllNotificationFragmentAdapter);
        //Toast.makeText(mContext,mRetailers.getData().get(0).getSlides().get(0).getAddress()+"",Toast.LENGTH_LONG).show();

        //mAllNotificationFragmentAdapter.notifyDataSetChanged();
    }

}
