package com.foreverrafs.numericals.activities;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.foreverrafs.numericals.R;
import com.foreverrafs.numericals.dialog.OperationListDialog;
import com.foreverrafs.numericals.fragments.FragmentMainMenu;
import com.foreverrafs.numericals.utils.Utilities;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {


    static InputMethodManager imm;


    public static void hideKeyboard(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        loadMenuFragment();
        initControls();
    }

    private void loadMenuFragment() {
        FragmentMainMenu fragment = new FragmentMainMenu();
        Utilities.loadFragment(fragment, getSupportFragmentManager(), R.id.fragmentContainer);
    }

    private void initControls() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView toolbarAppTitle = findViewById(R.id.toolbarAppTitle);
        toolbarAppTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/philosopher_bold.ttf"));

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onBackPressed() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        Log.i(Utilities.LOG_TAG, f.toString() + " is the active fragment now");

        if (!(f instanceof FragmentMainMenu)) {
            //transition between the open fragment and mainmenu
            Utilities.replaceFragment(new FragmentMainMenu(), getSupportFragmentManager(), R.id.fragmentContainer, true);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        System.out.println("key input detected");
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public void onGoToOperation(MenuItem item) {
        OperationListDialog dialog = new OperationListDialog();
        dialog.show(getFragmentManager(), "Fragment dialog");

    }

    public void onAbout(MenuItem item) {
        //Fragment fragment = new About();
       // Utilities.replaceFragment(fragment, getSupportFragmentManager(), R.id.fragmentContainer, false);
    }
}
