package com.wordpress.nprogramming.serviceinfoviewer;

import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
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


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
