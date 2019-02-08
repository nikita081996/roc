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
 *  class is a fragment used in openings notifications tab of notification screen
 * A simple {@link Fragment} subclass.
 *
 * created by Rajneesh Shukla on 20/12/18
 */

public class OpeningsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static RecyclerView mOpeningsFragmentRecyclerView;
    private static NotificationFragmentAdapter mOpeningsFragmentAdapter;
    private static Context mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static List<CustomRocsModel> rocsModels;


    private static List<Retailers> mRetailers;
    private static String mStoreName;


    public OpeningsFragment() {
        // Required empty public constructor
    }

    public static OpeningsFragment newInstance(String param1, String param2) {
        OpeningsFragment fragment = new OpeningsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static OpeningsFragment newInstance(Context context) {
        mContext = context;
        return new OpeningsFragment();
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
        View view = inflater.inflate(R.layout.fragment_openings, container, false);
        ButterKnife.bind(this, view);

        mOpeningsFragmentRecyclerView = view.findViewById((R.id.recycler_view_openings));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mOpeningsFragmentRecyclerView.setLayoutManager(mLayoutManager);
        mOpeningsFragmentRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if(mOpeningsFragmentAdapter != null){
            mOpeningsFragmentAdapter = new NotificationFragmentAdapter(mContext,rocsModels, mStoreName);
            mOpeningsFragmentRecyclerView.setAdapter(mOpeningsFragmentAdapter);
            mOpeningsFragmentAdapter.notifyDataSetChanged();
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
                if(rocs.get(j).getOpening() == "true"){
                    customRocsModel.setDistance(rocs.get(j).getDistance());
                    customRocsModel.setOpening(rocs.get(j).getOpening());
                    customRocsModel.setIsSeen(rocs.get(j).getIs_seen());
                    customRocsModel.setDateEffective(rocs.get(j).getDate_effective());
                    customRocsModel.setDateCreated(rocs.get(j).getDate_created());
                    customRocsModel.setMarkerFile(mRetailers.get(i).getMarker_file());
                    customRocsModel.setSubsidiaryId(mRetailers.get(i).getSubsidiary_id());
                    customRocsModel.setBannerName(mRetailers.get(i).getBanner_name());
                    rocsModels.add(customRocsModel);
                }

                //   Toast.makeText(mContext,mRetailers.get(1).getSubsidiary_id()+ "hj", Toast.LENGTH_LONG).show();


            }


        }
        mOpeningsFragmentAdapter = new NotificationFragmentAdapter(mContext,rocsModels, mStoreName);
        mOpeningsFragmentRecyclerView.setAdapter(mOpeningsFragmentAdapter);
        //Toast.makeText(mContext,mRetailers.getData().get(0).getSlides().get(0).getAddress()+"",Toast.LENGTH_LONG).show();

        //mAllNotificationFragmentAdapter.notifyDataSetChanged();
    }


}
