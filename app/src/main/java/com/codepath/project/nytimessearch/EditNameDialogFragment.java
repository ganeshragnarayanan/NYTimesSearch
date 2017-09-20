package com.codepath.project.nytimessearch;

/**
 * Created by GANESH on 9/20/17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.project.nytimessearch.Activites.SearchActivity;

// ...



public class EditNameDialogFragment extends DialogFragment /*implements OnEditorActionListener */ {

    public interface EditNameDialogListener {

        void onFinishEditDialog(String inputText);

    }
    private EditText mEditText;

    public EditNameDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }



    public static EditNameDialogFragment newInstance(String title) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }



    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //mEditText.setOnEditorActionListener(this);


        Button btnExample = (Button) view.findViewById(R.id.btnSubmit);
        final EditText editText = (EditText) view.findViewById(R.id.txt_your_name);
        btnExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "ondiaglogclick");
                String searchText = editText.getText().toString();
                ((SearchActivity) getActivity()).getResult(searchText);
                dismiss();
            }
        });

    }




}