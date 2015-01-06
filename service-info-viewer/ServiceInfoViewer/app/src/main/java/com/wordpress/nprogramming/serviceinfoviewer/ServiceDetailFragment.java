package com.wordpress.nprogramming.serviceinfoviewer;


import android.app.ActivityManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceDetailFragment extends Fragment {

    private static final String ARG_SRV_INFO = "srv_info";

    private ActivityManager.RunningServiceInfo serviceInfo;

    public static ServiceDetailFragment newInstance(ActivityManager.RunningServiceInfo info) {
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SRV_INFO, info);
        fragment.setArguments(args);
        return fragment;
    }

    public ServiceDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceInfo = getArguments().getParcelable(ARG_SRV_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.frag_srv_detail, container, false);

        prepareServiceInfoRow(
                rootView,
                R.id.component_name,
                R.string.component_name_title,
                serviceInfo.service.flattenToShortString()
        );

        prepareServiceInfoRow(
                rootView,
                R.id.process_name,
                R.string.process_name_title,
                serviceInfo.process
        );

        prepareServiceInfoRow(
                rootView,
                R.id.pid,
                R.string.pid_title,
                Integer.toString(serviceInfo.pid)
        );

        prepareServiceInfoRow(
                rootView,
                R.id.uid,
                R.string.uid_title,
                Integer.toString(serviceInfo.uid)
        );

        prepareServiceInfoRow(
                rootView,
                R.id.connected_clients,
                R.string.connected_clients_title,
                Integer.toString(serviceInfo.clientCount)
        );

        prepareServiceInfoRow(
                rootView,
                R.id.active_time,
                R.string.active_time_title,
                Long.toString(SystemClock.elapsedRealtime() -
                    serviceInfo.lastActivityTime)
        );

        return rootView;
    }

    private void prepareServiceInfoRow(
            View rootView, int componentId, int titleResourceId, String text) {

        View serviceInfoRow = rootView.findViewById(componentId);

        TextView title = (TextView) serviceInfoRow.findViewById(R.id.title);
        title.setText(titleResourceId);

        TextView data = (TextView) serviceInfoRow.findViewById(R.id.data);
        data.setText(text);
    }


}
