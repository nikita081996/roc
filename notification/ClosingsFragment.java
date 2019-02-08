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

import com.roc.android.roc.R;

import com.roc.android.roc.pojo.CustomRocsModel;
import com.roc.android.roc.pojo.Retailers;
import com.roc.android.roc.pojo.Rocs;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 *  class is a fragment used in closing notification tab of notification screen
 * created by Rajneesh Shukla on 20/12/18
 */

public class ClosingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static RecyclerView mClosingsFragmentRecyclerView;
    private static NotificationFragmentAdapter mClosingsFragmentAdapter;
    private static Context mContext;
    private static List<CustomRocsModel> rocsModels;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static View view;

    private static List<Retailers> mRetailers;
    private static String mStoreName;


    public ClosingsFragment() {
        // Required empty public constructor
    }

    public static ClosingsFragment newInstance(String param1, String param2) {
        ClosingsFragment fragment = new ClosingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ClosingsFragment newInstance(Context context) {
        mContext = context;
        return new ClosingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       // prepareMovieData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_closings, container, false);
        ButterKnife.bind(this, view);

        mClosingsFragmentRecyclerView = view.findViewById((R.id.recycler_view_closings));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mClosingsFragmentRecyclerView.setLayoutManager(mLayoutManager);
        mClosingsFragmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if(mClosingsFragmentAdapter != null){
            mClosingsFragmentAdapter = new NotificationFragmentAdapter(mContext,rocsModels, mStoreName);
            mClosingsFragmentRecyclerView.setAdapter(mClosingsFragmentAdapter);
            mClosingsFragmentAdapter.notifyDataSetChanged();
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
                if(rocs.get(j).getOpening() == "false") {
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


        }
        mClosingsFragmentAdapter = new NotificationFragmentAdapter(mContext,rocsModels, mStoreName);
        if(view !=null){
            if(mClosingsFragmentRecyclerView == null) {
                mClosingsFragmentRecyclerView = view.findViewById((R.id.recycler_view_closings));

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mClosingsFragmentRecyclerView.setLayoutManager(mLayoutManager);
                mClosingsFragmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
            mClosingsFragmentRecyclerView.setAdapter(mClosingsFragmentAdapter);
        }

        //Toast.makeText(mContext,mRetailers.getData().get(0).getSlides().get(0).getAddress()+"",Toast.LENGTH_LONG).show();

        //mAllNotificationFragmentAdapter.notifyDataSetChanged();
    }
}
