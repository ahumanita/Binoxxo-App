package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatSpinner;

public class SpinnerPlus extends AppCompatSpinner {

    public SpinnerPlus(Context context)
    { super(context); }

    public SpinnerPlus(Context context, AttributeSet attrs)
    { super(context, attrs); }

    public SpinnerPlus(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }

    @Override public void
    setSelection(int position, boolean animate)
    {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override public void
    setSelection(int position)
    {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }
}

//public class SpinnerPlus extends AppCompatSpinner {
//    AdapterView.OnItemSelectedListener listener;
//
//    public SpinnerPlus(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public void setSelection(int position) {
//        super.setSelection(position);
//        if (listener != null)
//            listener.onItemSelected(this, getSelectedView(), position, 0);
//    }
//
//
//
//    public void setOnItemSelectedEvenIfUnchangedListener(
//            AdapterView.OnItemSelectedListener listener) {
//        this.listener = listener;
//    }
//}