package com.codepath.project.nytimessearch.fragments;

/**
 * Created by GANESH on 9/20/17.
 */

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.project.nytimessearch.Activites.SearchActivity;
import com.codepath.project.nytimessearch.R;

import java.util.Calendar;

// ...

public class EditNameDialogFragment extends DialogFragment  implements DatePickerDialog.OnDateSetListener /*implements OnEditorActionListener */ {


    String dateSelected;

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

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
        final EditText etDate = (EditText) view.findViewById(R.id.etDate);
        final Spinner filterOldest = (Spinner) view.findViewById(R.id.spinValues);
        final CheckBox filterArts = (CheckBox) view.findViewById(R.id.cbValues1);
        final CheckBox filterFashion = (CheckBox) view.findViewById(R.id.cbValues2);
        final CheckBox filterSports = (CheckBox) view.findViewById(R.id.cbValues3);

        String filterDateValue = getArguments().getString("date");
        String filterSortValue = getArguments().getString("sort");
        Boolean filterArtsValue = getArguments().getBoolean("arts");
        Boolean filterFashionValue = getArguments().getBoolean("fashion");
        Boolean filterSportsValue = getArguments().getBoolean("sports");

        etDate.setText(filterDateValue);

        if (filterSortValue.equals("oldest")) {
            filterOldest.setSelection(1);
        } else if (filterSortValue.equals("newest")) {
            filterOldest.setSelection(2);
        }
        else {
            filterOldest.setSelection(0);
        }

        if (filterArtsValue) {
            filterArts.setChecked(true);
        }

        if (filterFashionValue) {
            filterFashion.setChecked(true);
        }

        if (filterSportsValue) {
            filterSports.setChecked(true);
        }


        String title = getArguments().getString("title", "Enter Name");


        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.requestFocus();

        etDate.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("debug", "ondateclick");
                Log.d("debug", "on date click");
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setCallBack(ondate);
                newFragment.show(getFragmentManager(), "datePicker");
            }
            DatePickerDialog.OnDateSetListener ondate = new  DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // called after you selected the date and pressed ok
                    String yearValue = Integer.toString(year);
                    String monthValue;
                    String dayValue;

                    monthOfYear++;
                    if (monthOfYear < 10) {
                        monthValue = "0" + Integer.toString(monthOfYear);
                    } else {
                        monthValue = Integer.toString(monthOfYear);
                    }

                    if (dayOfMonth < 10) {
                        dayValue = "0" + Integer.toString(dayOfMonth);
                    } else {
                        dayValue =  Integer.toString(dayOfMonth);
                    }

                    String filterDate = yearValue+monthValue+dayValue;
                    etDate.setText(filterDate);
                    dateSelected = filterDate;
                }
            };
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "ondiaglogclick");

                // here is the place to find the right element

                boolean arts = false, fashion = false, sports = false;

                String date = etDate.getText().toString();

                if (filterArts.isChecked()) {
                    arts = true;
                }

                if (filterFashion.isChecked()) {
                    fashion = true;
                }

                if (filterSports.isChecked()) {
                    sports = true;
                }

                ((SearchActivity) getActivity()).getResult(date,
                        filterOldest.getSelectedItem().toString(), arts, fashion, sports);

                dismiss();
            }

            DatePickerDialog.OnDateSetListener ondate = (view1, year, monthOfYear, dayOfMonth) -> {
            };
        });



    }
}