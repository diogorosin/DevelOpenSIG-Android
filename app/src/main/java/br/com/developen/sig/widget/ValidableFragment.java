package br.com.developen.sig.widget;

import androidx.fragment.app.Fragment;

public interface ValidableFragment {

    void validate();

    interface Listener {

        void onValidationFailed(Fragment f);

        void onValidationSucceeded(Fragment f);

    }

}