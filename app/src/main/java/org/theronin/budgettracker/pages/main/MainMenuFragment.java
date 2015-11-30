package org.theronin.budgettracker.pages.main;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.theronin.budgettracker.R;


public class MainMenuFragment extends Fragment {

    private LinearLayout organiseEntriesButton;
    private LinearLayout organiseCategoriesButton;
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__main_menu, container, false);
        organiseEntriesButton = (LinearLayout) rootView.findViewById(R.id.ll__menu_item_entries);
        organiseEntriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEntriesMenuItemClicked();
            }
        });

        organiseCategoriesButton = (LinearLayout) rootView.findViewById(R.id.ll__menu_item_categories);
        organiseCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCategoriesMenuItemClicked();
            }
        });

        return rootView;
    }

    public interface Listener {
        void onEntriesMenuItemClicked();

        void onCategoriesMenuItemClicked();
    }
}
