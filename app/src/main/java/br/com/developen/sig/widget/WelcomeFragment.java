package br.com.developen.sig.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.developen.sig.R;

public class WelcomeFragment extends Fragment {


    public WelcomeFragment() {}


    public static WelcomeFragment newInstance() {

        WelcomeFragment fragment = new WelcomeFragment();

        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;

    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_welcome, container, false);

    }


}
