package com.redleefstudios.ptocounter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeSetupFragment extends Fragment {


    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_BG_COLOR = "bg_color";
    private static final String ARG_TITLE_COLOR = "title_color";
    private static final String ARG_DESC_COLOR = "desc_color";
    private static final String ARG_CATEGORY = "pto_category";



    public static TimeSetupFragment newInstance(CharSequence title, CharSequence description, int bgColor, Category category) {
        return newInstance(title, description, bgColor, 0, 0, category);
    }

    public static TimeSetupFragment newInstance(CharSequence title, CharSequence description, int bgColor, int titleColor, int descColor, Category category) {
        TimeSetupFragment sampleSlide = new TimeSetupFragment();

        Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_DESC, description);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        args.putString(ARG_CATEGORY, category.name());
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    private int bgColor, titleColor, descColor;
    private CharSequence title, description;
    private Category category;

    private int vacationTotal, sickTotal, otherTotal;

    public TimeSetupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().size() != 0) {
            title = getArguments().getCharSequence(ARG_TITLE);
            description = getArguments().getCharSequence(ARG_DESC);
            bgColor = getArguments().getInt(ARG_BG_COLOR);
            titleColor = getArguments().containsKey(ARG_TITLE_COLOR) ? getArguments().getInt(ARG_TITLE_COLOR) : 0;
            descColor = getArguments().containsKey(ARG_DESC_COLOR) ? getArguments().getInt(ARG_DESC_COLOR) : 0;
            category = Category.valueOf(getArguments().containsKey(ARG_CATEGORY) ? getArguments().getString(ARG_CATEGORY) : "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_fragment, container, false);
        TextView t = (TextView) v.findViewById(R.id.title);
        TextView d = (TextView) v.findViewById(R.id.description);
        EditText ptoNumber = (EditText) v.findViewById(R.id.ptoNumber);
        LinearLayout m = (LinearLayout) v.findViewById(R.id.main);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        vacationTotal = prefs.getInt(MainActivity.VACATION_TOTAL_SAVE_NAME, 0);
        sickTotal = prefs.getInt(MainActivity.SICK_TOTAL_SAVE_NAME, 0);
        otherTotal = prefs.getInt(MainActivity.OTHER_TOTAL_SAVE_NAME, 0);

        switch (category) {
            case VACATION:
                ptoNumber.setText(vacationTotal + "");
                break;
            case SICK:
                ptoNumber.setText(sickTotal + "");
                break;
            case OTHER:
                ptoNumber.setText(otherTotal + "");
                break;
        }

        t.setText(title);
        if (titleColor != 0) {
            t.setTextColor(titleColor);
        }

        d.setText(description);
        if (descColor != 0) {
            d.setTextColor(descColor);
        }

        m.setBackgroundColor(bgColor);

        ptoNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    EditText mView = (EditText) view;
                    if (mView.getText().toString() != null) {

                        int total = Integer.parseInt(mView.getText().toString());

                        switch (category) {
                            case VACATION:
                                prefs.edit().putInt(MainActivity.VACATION_TOTAL_SAVE_NAME, total).commit();
                                break;
                            case SICK:
                                prefs.edit().putInt(MainActivity.SICK_TOTAL_SAVE_NAME, total).commit();
                                break;
                            case OTHER:
                                prefs.edit().putInt(MainActivity.OTHER_TOTAL_SAVE_NAME, total).commit();
                                break;
                        }
                    }

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        return v;
    }

}
