package com.farjad.savaari.settings;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.farjad.savaari.R;
import com.farjad.savaari.ride.RideActivity;
import com.farjad.savaari.utility.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThemeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThemeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_THEME_SYNCED = "syncTheme";
    private static final String PARAM_AUTO_DARK_THEME = "autoDarkTheme";

    private boolean syncTheme;
    private boolean autoDarkTheme;

    private SharedPreferences preferences = null;

    private ImageView image1, image2, image3, image4, image5, prevImage;
    private int previousThemeVar = 1;

    private static Bitmap takeScreenshot(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    public ThemeFragment() {
        // Required empty public constructor
    }

    private void initializeSelectableThemes() {
        switch(preferences.getInt(getString(R.string.preference_theme_var), 1)) {
            case(0):
                image1.setVisibility(View.VISIBLE);
                prevImage = image1;
                break;
            case(1):
                image2.setVisibility(View.VISIBLE);
                prevImage = image2;
                break;
            case(2):
                image3.setVisibility(View.VISIBLE);
                prevImage = image3;
                break;
            case(3):
                image4.setVisibility(View.VISIBLE);
                prevImage = image4;
                break;
            case(4):
                image5.setVisibility(View.VISIBLE);
                prevImage = image5;
                break;
        }
    }


    // TODO: Rename and change types and number of parameters
    public static ThemeFragment newInstance(boolean syncTheme, boolean autoDarkTheme) {
        ThemeFragment fragment = new ThemeFragment();
        Bundle args = new Bundle();
        args.putBoolean(PARAM_THEME_SYNCED, syncTheme);
        args.putBoolean(PARAM_AUTO_DARK_THEME, autoDarkTheme);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            setSyncTheme(getArguments().getBoolean(PARAM_THEME_SYNCED));
            setAutoDarkTheme(getArguments().getBoolean(PARAM_AUTO_DARK_THEME));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        preferences = null;

        if (getActivity() != null)
             preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        final CheckBox SyncThemeCheckbox = view.findViewById(R.id.sync_theme),
                AutoDarkThemeCheckbox = view.findViewById(R.id.auto_dark_theme);

        assert getArguments() != null;
        SyncThemeCheckbox.setChecked(getArguments().getBoolean(PARAM_THEME_SYNCED));
        AutoDarkThemeCheckbox.setChecked(getArguments().getBoolean(PARAM_AUTO_DARK_THEME));

        CompoundButton.OnCheckedChangeListener checkboxCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.getId() == R.id.sync_theme) {
                    setSyncTheme(isChecked);

                    /*
                    databaseReference.child("users").child(Objects.requireNonNull(mAuth.getUid()))
                            .child("settings").child("syncTheme").setValue(isChecked);

                    databaseReference.child("users").child(Objects.requireNonNull(mAuth.getUid()))
                            .child("settings").child("themeVar").setValue(preferences.getInt(getString(R.string.preference_theme_var), 1));*/
                }
                else if (buttonView.getId() == R.id.auto_dark_theme){

                    setAutoDarkTheme(isChecked);

                    /*databaseReference.child("users").child(Objects.requireNonNull(mAuth.getUid()))
                            .child("settings").child("autoDarkTheme").setValue(isChecked);*/
                }
            }
        };

        SyncThemeCheckbox.setOnCheckedChangeListener(checkboxCheckedChangeListener);
        AutoDarkThemeCheckbox.setOnCheckedChangeListener(checkboxCheckedChangeListener);


        View theme1 = view.findViewById(R.id.theme1),
                theme1Primary = theme1.findViewById(R.id.theme_primary),
                theme1Secondary = theme1.findViewById(R.id.theme_secondary),

                theme2 = view.findViewById(R.id.theme2),
                theme2Primary = theme2.findViewById(R.id.theme_primary),
                theme2Secondary = theme2.findViewById(R.id.theme_secondary),

                theme3 = view.findViewById(R.id.theme3),
                theme3Primary = theme3.findViewById(R.id.theme_primary),
                theme3Secondary = theme3.findViewById(R.id.theme_secondary),

                theme4 = view.findViewById(R.id.theme4),
                theme4Primary = theme4.findViewById(R.id.theme_primary),
                theme4Secondary = theme4.findViewById(R.id.theme_secondary),

                theme5 = view.findViewById(R.id.theme5),
                theme5Primary = theme5.findViewById(R.id.theme_primary),
                theme5Secondary = theme5.findViewById(R.id.theme_secondary);

        TextView text1 = theme1.findViewById(R.id.theme_name),
                text2 = theme2.findViewById(R.id.theme_name),
                text3 = theme3.findViewById(R.id.theme_name),
                text4 = theme4.findViewById(R.id.theme_name),
                text5 = theme5.findViewById(R.id.theme_name);

        image1 = theme1.findViewById(R.id.selectedIcon);
        image2 = theme2.findViewById(R.id.selectedIcon);
        image3 = theme3.findViewById(R.id.selectedIcon);
        image4 = theme4.findViewById(R.id.selectedIcon);
        image5 = theme5.findViewById(R.id.selectedIcon);

        initializeSelectableThemes();



        text1.setText(R.string.black_theme);
        theme1Primary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.BlackThemePrimary));
        theme1Primary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);
        theme1Secondary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.BlackThemeSecondary));
        theme1Secondary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);

        text2.setText(R.string.red_theme);
        theme2Primary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.RedThemePrimary));
        theme2Primary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);
        theme2Secondary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.RedThemeSecondary));
        theme2Secondary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);

        text3.setText(R.string.dark_red_theme);
        theme3Primary.setBackgroundColor(getResources().getColor(R.color.DarkRedThemePrimary));
        theme3Primary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);
        theme3Secondary.setBackgroundColor(getResources().getColor(R.color.DarkRedThemeSecondary));
        theme3Secondary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);

        text4.setText(R.string.blue_theme);
        theme4Primary.setBackgroundColor(getResources().getColor(R.color.BlueThemePrimary));
        theme4Primary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);
        theme4Secondary.setBackgroundColor(getResources().getColor(R.color.BlueThemeSecondary));
        theme4Secondary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);

        text5.setText(R.string.dark_blue_theme);
        theme5Primary.setBackgroundColor(getResources().getColor(R.color.DarkBlueThemePrimary));
        theme5Primary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);
        theme5Secondary.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.DarkBlueThemeSecondary));
        theme5Secondary.setBackgroundTintMode(PorterDuff.Mode.SCREEN);

        theme1.setOnClickListener(this);
        theme2.setOnClickListener(this);
        theme3.setOnClickListener(this);
        theme4.setOnClickListener(this);
        theme5.setOnClickListener(this);

        return view;
    }

    public boolean isSyncTheme() {
        return syncTheme;
    }

    public void setSyncTheme(boolean syncTheme) {
        this.syncTheme = syncTheme;
    }

    public boolean isAutoDarkTheme() {
        return autoDarkTheme;
    }

    public void setAutoDarkTheme(boolean autoDarkTheme) {
        this.autoDarkTheme = autoDarkTheme;
    }

    @Override
    public void onClick(View v) {

        int themeSelected = 1;

        prevImage.setVisibility(View.INVISIBLE);

        switch (v.getId()) {
            case(R.id.theme1):
                themeSelected = 0;
                prevImage = image1;
                break;
            case(R.id.theme2):
                themeSelected = 4;
                prevImage = image2;
                break;
            case(R.id.theme3):
                themeSelected = 2;
                prevImage = image3;
                break;
            case(R.id.theme4):
                themeSelected = 1;
                prevImage = image4;
                break;
            case(R.id.theme5):
                themeSelected = 4;
                prevImage = image5;
                break;
        }

        prevImage.setVisibility(View.VISIBLE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(getString(R.string.preference_theme_var), themeSelected);
        editor.commit();

        Util.getInstance().setThemeID(themeSelected);
        //if (isSyncTheme())

        //TODO: Implement this for new database
        //databaseReference.child("users").child(mAuth.getUid()).child("settings").child("themeVar").setValue(themeSelected);

        //Bitmap screenshot = takeScreenshot(requireView().getRootView());

        Intent i = getActivity().getIntent();
        i.putExtra("themeChange", true);

        TaskStackBuilder.create(getActivity())
                .addNextIntent(new Intent(getActivity(), RideActivity.class))
                .addNextIntent(i)
                .startActivities();
    }
}