package com.smartplace.alerta.tips;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.smartplace.alerta.Constants;
import com.smartplace.alerta.R;
import com.smartplace.alerta.Utilities.Miscellaneous;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link TipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TipsFragment extends Fragment {

    private SwingBottomInAnimationAdapter mAnimTipsListAdapter;
    private ArrayList mCoachingItems;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TipsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TipsFragment newInstance() {
        TipsFragment fragment = new TipsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public TipsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_tips, container, false);
        ListView tipsList = (ListView)v.findViewById(R.id.list_tips_items);

        mCoachingItems = new ArrayList();

        TipsItem tipsItem = new TipsItem();
        tipsItem.setVideoTitle("Incendios");
        tipsItem.setVideoContent("Este video explica de manera gráfica qué hacer en caso de algún incendio.");
        tipsItem.setVideoID("A6ihUPDwNX4");

        try {
            Miscellaneous.extractYoutubeId("http://www.youtube.com/watch?v=A6ihUPDwNX4");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            tipsItem.setVideoID(Miscellaneous.extractYoutubeId("http://www.youtube.com/watch?v=A6ihUPDwNX4"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mCoachingItems.add(tipsItem);
        tipsItem = new TipsItem();
        tipsItem.setVideoTitle("Sismos");
        tipsItem.setVideoContent("Este video explica de manera gráfica qué hace en caso de algún sismo.");
        try {
            tipsItem.setVideoID(Miscellaneous.extractYoutubeId("http://www.youtube.com/watch?v=Ns4HBwprpgk"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mCoachingItems.add(tipsItem);
        tipsItem = new TipsItem();
        tipsItem.setVideoTitle("Huracánes");
        tipsItem.setVideoContent("Este video explica de manera gráfica qué hacer en caso de algún huracán.");

        try {
            tipsItem.setVideoID(Miscellaneous.extractYoutubeId("http://www.youtube.com/watch?v=QZD-ahhWAME"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mCoachingItems.add(tipsItem);
        TipsListAdapter tipsListAdapter = new TipsListAdapter(getActivity(),mCoachingItems);
        mAnimTipsListAdapter =  new SwingBottomInAnimationAdapter(tipsListAdapter);
        mAnimTipsListAdapter.setAbsListView(tipsList);
        tipsList.setAdapter(mAnimTipsListAdapter);
        tipsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TipsItem tipsItem =(TipsItem)mCoachingItems.get(position);
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                        Constants.DEVELOPER_YOUTUBE_KEY, tipsItem.getVideoID());

                startActivity(intent);
            }
        });

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
