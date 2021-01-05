package com.farjad.savaari.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.farjad.savaari.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsFragment extends ListFragment {

    String[] textPrimary = new String[] {
      "Account",
      "General",
      "Theme",
      "Productivity",
      "Reminders",
      "Notifications",
      "Support",
            "Log out"
    };

    int[] icons = new int[] {
            R.drawable.ic_account,
            R.drawable.ic_general,
            R.drawable.ic_theme,
            R.drawable.ic_productivity,
            R.drawable.ic_reminders,
            R.drawable.ic_notifications,
            R.drawable.ic_support,
            R.drawable.ic_logout
    };

    private SettingsClickListener settingsClickListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public SettingsFragment(SettingsClickListener settingsClickListener) {
        this.settingsClickListener = settingsClickListener;
    }

    public static SettingsFragment newInstance(SettingsClickListener settingsClickListener) {
        return new SettingsFragment(settingsClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<8;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("textPrimary", textPrimary[i]);
            hm.put("icon", Integer.toString(icons[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "textPrimary", "icon"};

        // Ids of views in listview_layout
        int[] to = { R.id.textPrimary,R.id.icon};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.settings_card, from, to);

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        switch(position) {
            case(0):
                settingsClickListener.onAccountClicked();
                break;
            case(1):
                settingsClickListener.onGeneralClicked();
                break;
            case(2):
                settingsClickListener.onThemeClicked();
                break;
            case(3):
                settingsClickListener.onProductivityClicked();
                break;
            case(4):
                settingsClickListener.onRemindersClicked();
                break;
            case(5):
                settingsClickListener.onNotificationsClicked();
                break;
            case(6):
                settingsClickListener.onSupportClicked();
                break;
            case(7):
                settingsClickListener.onLogoutClicked();
                break;
        }
    }
}
