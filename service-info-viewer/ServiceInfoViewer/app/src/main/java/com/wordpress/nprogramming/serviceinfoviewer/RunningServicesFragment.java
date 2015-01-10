package com.wordpress.nprogramming.serviceinfoviewer;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wordpress.nprogramming.utilitylibrary.RunningServiceWrapper;

import java.util.List;

public class RunningServicesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayAdapter<RunningServiceWrapper> services;
    private ListView serviceList;

    public RunningServicesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        services = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        serviceList = (ListView) rootView.findViewById(R.id.service_list);
        serviceList.setAdapter(services);
        serviceList.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getActivity().getApplicationContext();
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices =
                activityManager.getRunningServices(Integer.MAX_VALUE);
        services.clear();

        for (ActivityManager.RunningServiceInfo currentService : runningServices) {
            RunningServiceWrapper runningServiceWrapper =
                    new RunningServiceWrapper(currentService);
            services.add(runningServiceWrapper);
        }

        services.notifyDataSetChanged();

        Toast.makeText(
                getActivity(),
                "Found " + services.getCount() + " services",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RunningServiceWrapper currentItem = services.getItem(position);
        ServiceDetailFragment serviceDetailFragment =
                ServiceDetailFragment.newInstance(currentItem.getInfo());

        FragmentManager fragmentManager =
                getActivity().getFragmentManager();

        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, serviceDetailFragment);
        fragmentTransaction.addToBackStack(
                serviceDetailFragment.getClass().getSimpleName());

        fragmentTransaction.commit();
    }
}
