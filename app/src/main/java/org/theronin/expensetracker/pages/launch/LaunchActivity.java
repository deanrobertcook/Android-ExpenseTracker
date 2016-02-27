package org.theronin.expensetracker.pages.launch;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.theronin.expensetracker.BuildConfig;
import org.theronin.expensetracker.CustomApplication;
import org.theronin.expensetracker.R;
import org.theronin.expensetracker.model.user.UserManager;
import org.theronin.expensetracker.pages.main.DebugActivity;
import org.theronin.expensetracker.pages.main.MainActivity;
import org.theronin.expensetracker.utils.Prefs;
import org.theronin.expensetracker.utils.TrackingUtils;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    private LaunchFragment currentFragment;

    private Button positiveButton;
    private Button negativeButton;
    private Button tertiaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity__launch);
        positiveButton = (Button) findViewById(R.id.button_positive);
        negativeButton = (Button) findViewById(R.id.button_negative);
        tertiaryButton = (Button) findViewById(R.id.button_tertiary);
        setPage(getFirstPageToDisplay());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * Set the current launch page. Setting a null page will trigger the main portion of the app.
     * @param page the page to display, or null to enter the app.
     */
    public void setPage(LaunchPage page) {
        if (startApplication(page)) {
            finish();
            return;
        }
        currentFragment = page.fragment;
        getFragmentManager().beginTransaction()
                .replace(R.id.welcome_content, currentFragment)
                .commit();
    }

    private boolean startApplication(LaunchPage page) {
        if (page == LaunchPage.ENTER_APP) {
            //Create or Set the database for the user:
            TrackingUtils.setUserDetails(UserManager.getUser(this));
            ((CustomApplication) getApplication()).setDatabase();

            Intent startAppIntent = new Intent(this, BuildConfig.DEBUG ? DebugActivity.class : MainActivity.class);
            startActivity(startAppIntent);
            return true;
        }
        return false;
    }

    private LaunchPage getFirstPageToDisplay() {
        if (!Prefs.hasWelcomeScreenBeenShown(this)) {
            return LaunchPage.WELCOME;
        }
        if (!UserManager.signedIn(this)) {
            return LaunchPage.SKIP_ACCOUNT;
        }
        return LaunchPage.ENTER_APP;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        wireUpButtons();
    }

    private void wireUpButtons() {
        //TODO hunt for NPEs around here
        //TODO https://github.com/deanrobertcook/Android-ExpenseTracker/issues/33
        positiveButton.setText(currentFragment.getPositiveButtonText());

        String negativeButtonText = currentFragment.getNegativeButtonText();
        if (negativeButtonText == null) {
            negativeButton.setVisibility(View.GONE);
        } else {
            negativeButton.setVisibility(View.VISIBLE);
            negativeButton.setText(negativeButtonText);
        }

        String tertiaryButtonText = currentFragment.getTertiaryButtonText();
        if (tertiaryButtonText == null) {
            tertiaryButton.setVisibility(View.GONE);
        } else {
            tertiaryButton.setVisibility(View.VISIBLE);
            tertiaryButton.setText(tertiaryButtonText);
        }

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        tertiaryButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_positive:
                currentFragment.onPositiveButtonClicked();
                break;
            case R.id.button_negative:
                currentFragment.onNegativeButtonClicked();
                break;
            case R.id.button_tertiary:
                currentFragment.onTertiaryButtonClicked();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        currentFragment.onBackPressed();
    }
}
