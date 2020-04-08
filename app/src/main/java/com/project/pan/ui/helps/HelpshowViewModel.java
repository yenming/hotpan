package com.project.pan.ui.helps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelpshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HelpshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is helps fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}