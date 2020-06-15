package com.ashtray.movie360.entities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class MyFragment extends Fragment {

    protected MyFragmentCallBacks myFragmentCallBacks;

    public MyFragment(){
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.setMyFragmentCallBacks(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myFragmentCallBacks = null;
    }

    public abstract boolean handleBackButtonPressed();

    private void setMyFragmentCallBacks(Context context){
        if (context instanceof MyFragmentCallBacks) {
            myFragmentCallBacks = (MyFragmentCallBacks) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MyFragmentCallBacks");
        }
    }

    public interface MyFragmentCallBacks {
        void showFragment(MyFragmentNames fragmentName);
        void showToastMessage(String message, boolean forShortTime);
        void setActivityTitle(String title);
        void setBackButtonEnabled(boolean enabled);
    }

    public enum MyFragmentNames {
        HOME,
        ADMIN_LOGIN,
        ADMIN_POST_CREATE,
        ADMIN_POST_VIEW
    }

}

// public empty constructor
// override onCreateView (initialize layout and widgets)
// override onActivityCreated (change activity title + activity menus)
// override onCreate (if your project has menu in any fragment)
// override onCreateOptionsMenu (if your project has menu in any fragment)