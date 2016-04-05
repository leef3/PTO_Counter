package com.redleefstudios.ptocounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<PTOItem> mData;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Context context;

    ListRecyclerAdapter mAdapter;

    //Dialog Change Global
    PTOItem toChange;

    //UI Globals
    TextView totalVacation;
    TextView totalSick;
    TextView totalOther;

    //Data Storage
    public static final String MASTER_SAVE_NAME = "MASTER_SAVE_DATA_PTOPLANNER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("PTO Planner");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog(-1);
            }
        });

        totalVacation = (TextView) findViewById(R.id.vacationcount);
        totalSick = (TextView) findViewById(R.id.sickcount);
        totalOther = (TextView) findViewById(R.id.othercount);

        mData = new ArrayList<PTOItem>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        mAdapter = new ListRecyclerAdapter(mData, this);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecalculateStatistics();
    }

    public void onItemClicked(int position)
    {
        ShowDialog(position);
        return;
    }

    public void onItemLongClicked(final int position)
    {

        View.OnClickListener snackClick = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mData.remove(position);
            }
        };

        Snackbar.make(this.findViewById(R.id.mRootView), "Delete this event?", Snackbar.LENGTH_LONG)
                .setAction("Confirm", snackClick).show();
        return;
    }

    public void ShowDialog(final int position)
    {
        PTOItem toEdit;
        if(position > 0)
        {
            toEdit = mData.get(position);
        }
        else
        {
            toEdit = new PTOItem("", Category.VACATION, 0);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View customView = inflater.inflate(R.layout.add_dialog, null);
        builder.setView(customView);

        //Set Global Variable
        toChange = toEdit;

        //Title
        TextView dialogTitle = (TextView)customView.findViewById(R.id.titleText);
        if(toEdit.GetEvent().length() > 0)
        {
            dialogTitle.setText("Edit Event");
        }

        //Days Edit Text
        EditText daysEdit = (EditText)customView.findViewById(R.id.dayEntered);
        daysEdit.setText(toEdit.GetDays() + "");
        //Name Edit Text
        EditText nameEdit = (EditText)customView.findViewById(R.id.nameEntered);
        nameEdit.setText(toEdit.GetEvent() + "");

        //Set Plus Minus Buttons
        Button plusButton = (Button)customView.findViewById(R.id.plusSign);
        Button minusButton = (Button)customView.findViewById(R.id.minusSign);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText daysChange = (EditText)customView.findViewById(R.id.dayEntered);
                int days = Integer.parseInt(daysChange.getText().toString());
                days++;
                toChange.SetDays(days);
                daysChange.setText(days + "");
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText daysChange = (EditText)customView.findViewById(R.id.dayEntered);
                int days = Integer.parseInt(daysChange.getText().toString());
                days--;
                toChange.SetDays(days);
                daysChange.setText(days + "");
            }
        });

        //Category Buttons
        Button vacationButton = (Button)customView.findViewById(R.id.vacationButton);
        Button sickButton = (Button)customView.findViewById(R.id.sickButton);
        Button otherButton = (Button)customView.findViewById(R.id.otherButton);

        Button.OnClickListener categoryToggle = new Button.OnClickListener(){
          @Override
          public void onClick(View v)
          {
              //Reset Buttons
              Button tempVacationButton = (Button)customView.findViewById(R.id.vacationButton);
              tempVacationButton.setBackgroundResource(R.drawable.button_wire_empty);
              tempVacationButton.setTextColor(Color.parseColor("#42A5F5"));
              Button tempSickButton = (Button)customView.findViewById(R.id.sickButton);
              tempSickButton.setBackgroundResource(R.drawable.button_wire_empty);
              tempSickButton.setTextColor(Color.parseColor("#42A5F5"));
              Button tempOtherButton = (Button)customView.findViewById(R.id.otherButton);
              tempOtherButton.setBackgroundResource(R.drawable.button_wire_empty);
              tempOtherButton.setTextColor(Color.parseColor("#42A5F5"));

              //Set the selected one
              Button test = (Button)v;
              test.setBackgroundColor(Color.parseColor("#42A5F5"));
              test.setTextColor(Color.parseColor("#EEEEEE"));
              toChange.SetType(Category.valueOf(test.getText().toString()));
          }
        };

        vacationButton.setOnClickListener(categoryToggle);
        sickButton.setOnClickListener(categoryToggle);
        otherButton.setOnClickListener(categoryToggle);

        //Set initial value
        switch(toEdit.GetType())
        {
            case VACATION:
                vacationButton.callOnClick();
                break;
            case SICK:
                sickButton.callOnClick();
                break;
            case OTHER:
                otherButton.callOnClick();
                break;
        }

        // Add the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                //Create the new PTOItem
                EditText nameEdit = (EditText)customView.findViewById(R.id.nameEntered);
                toChange.SetEvent(nameEdit.getText().toString());
                if(position > 0) {mData.remove(position);}
                mData.add(toChange);
                mAdapter.notifyDataSetChanged();
                RecalculateStatistics();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.create().show();
    }

    public void RecalculateStatistics()
    {
        int vacationCount, sickCount, otherCount;
        vacationCount = sickCount = otherCount = 15;
        for(int x = 0; x < mData.size(); x++)
        {
            switch(mData.get(x).GetType())
            {
                case VACATION:
                    vacationCount = vacationCount - mData.get(x).GetDays();
                    break;
                case SICK:
                    sickCount = sickCount - mData.get(x).GetDays();
                    break;
                case OTHER:
                    otherCount = otherCount - mData.get(x).GetDays();
            }
        }

        totalVacation.setText(vacationCount + "");
        totalSick.setText(sickCount + "");
        totalOther.setText(otherCount + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //========================================================SAVE FUNCS=========================
    private void loadData()
    {
        mData.clear();
        SharedPreferences settings = context.getSharedPreferences("pref", 0);
        settings = context.getSharedPreferences("pref", 0);
        String objectData = settings.getString(MASTER_SAVE_NAME, "");
        if (!objectData.equals("")) {
            System.out.println("Object Data: " + objectData);
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<PTOItem>>() {
            }.getType();
            JsonArray jArray = new JsonParser().parse(objectData).getAsJsonArray();
            for (JsonElement e : jArray) {
                PTOItem c = gson.fromJson(e, PTOItem.class);
                mData.add(c);
            }
        }
    }

    private void saveData()
    {
        SharedPreferences.Editor settings = context.getSharedPreferences("pref",0).edit();
        String data = new Gson().toJson(mData);
        System.out.println("Data!: " + data);
        settings.putString(MASTER_SAVE_NAME, data);
        settings.commit();
    }
}
