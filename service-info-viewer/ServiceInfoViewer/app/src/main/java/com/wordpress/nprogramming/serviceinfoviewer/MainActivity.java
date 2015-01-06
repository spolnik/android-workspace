package com.wordpress.nprogramming.serviceinfoviewer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements AdapterView.OnItemClickListener {

        private ArrayAdapter<RunningServiceWrapper> services;
        private ListView serviceList;

        public PlaceholderFragment() {
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

        private class RunningServiceWrapper {

            private final ActivityManager.RunningServiceInfo info;

            public RunningServiceWrapper(ActivityManager.RunningServiceInfo info) {
                this.info = info;
            }

            public ActivityManager.RunningServiceInfo getInfo() {
                return info;
            }

            @Override
            public String toString() {
                return info.service.flattenToShortString();
            }
        }
    }
}
