package com.example.ancos_binoxxo;

import com.example.ancos_binoxxo.databinding.ActivityMainBinding;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    List<String> selection = Arrays.asList("X","O"," ");

    Binoxxo binoxxo;
    int[][] binoxxo_matrix;

    // TODO fix these strings?
    public static final String CHECK_TEXT = "com.example.anita.binoxxo.check_text";
    public static final String CHECK_COLOR = "com.example.anita.binoxxo.check_color";
    public static final String VALID_ERROR = "com.example.anita.binoxxo.error_msg";
    public static final String BACK_COLOR = "#d8d8d8";

    String check_text = "";
    String check_color = "#000000";
    String error_msg = "";
    static String hint_color = "#000000";
    static String edit_color = "#000000";
    int stroke_width = 220;

    public int current_rule = 0;

    public static final String mainPreferences = "MyPrefs" ;
    SharedPreferences sharedPreferences;

    private void create_binoxxo()
    {
        int it = 0;
        boolean valid = false;
        //TODO: 1000 durch konstante ersetzen
        while (!valid && it < 1000)
        {
            this.binoxxo = new Binoxxo(8,8);
            this.binoxxo.create();
            valid = this.binoxxo.binoxxo_is_valid();
            it += 1;
        }
        this.binoxxo_matrix = this.binoxxo.matrix.get_matrix();
        this.binoxxo.solution.print_matrix();
    }

    private void load_binoxxo(Bundle savedInstanceState)
    {
        int num_rows = savedInstanceState.getInt("num_rows");
        int num_cols = savedInstanceState.getInt("num_cols");
        int[][] matrix = new int[num_rows][num_cols];
        int[][] original = new int[num_rows][num_cols];
        int[][] solution = new int[num_rows][num_cols];
        for(int row = 0; row < num_rows; row ++) {
            matrix[row] = savedInstanceState.getIntArray(String.format("matrix_row_%i",row));
            original[row] = savedInstanceState.getIntArray(String.format("original_row_%i",row));
            solution[row] = savedInstanceState.getIntArray(String.format("solution_row_%i",row));
        }
        this.binoxxo = new Binoxxo(num_rows,num_cols,matrix,original,solution);
        this.binoxxo_matrix = this.binoxxo.matrix.get_matrix();
        this.binoxxo.solution.print_matrix();
    }

    private void load_binoxxo(SharedPreferences preferences) {
        int num_rows = preferences.getInt("num_rows",8);
        int num_cols = preferences.getInt("num_cols",8);
        int[][] matrix = new int[num_rows][num_cols];
        int[][] original = new int[num_rows][num_cols];
        int[][] solution = new int[num_rows][num_cols];

        this.create_binoxxo();
        int[][] default_matrix = this.binoxxo.matrix.matrix;
        int[][] default_solution = this.binoxxo.original.matrix;
        int[][] default_original = this.binoxxo.solution.matrix;

        for(int row = 0; row < num_rows; row ++) {
            for(int col= 0; col < num_cols; col++) {
                matrix[row][col] = preferences.getInt(String.format("matrix_row_%o_%o", row,col),default_matrix[row][col]);
                original[row][col] = preferences.getInt(String.format("original_row_%o_%o", row,col),default_original[row][col]);
                solution[row][col] = preferences.getInt(String.format("solution_row_%o_%o", row,col),default_solution[row][col]);
            }
        }
        this.binoxxo = new Binoxxo(num_rows,num_cols,matrix,original,solution);
        this.binoxxo_matrix = this.binoxxo.matrix.get_matrix();
        this.binoxxo.matrix.print_matrix();
    }


    protected void initializeButtonColor(ImageButton button, String color) {
        /* Initialize background colors of the buttons for setting font color */
        StateListDrawable listDrawable = (StateListDrawable) button.getBackground();
        for(int i = 0; i < 2; i++) {
            GradientDrawable shapeDrawable = (GradientDrawable) listDrawable.getStateDrawable(i);
            // This didn't work with setColor(R.color.red) seems that somethings goes wrong
            // when not parsing color first
            shapeDrawable.setColor(Color.parseColor(color));
        }
    }

    private void _initialize()
    {
        this.color_default();

        this.set_spinner_selection();
        ImageButton black_button = findViewById(R.id.button_black);
        black_button.performClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton red_button = findViewById(R.id.button_red);
        ImageButton blue_button = findViewById(R.id.button_blue);
        ImageButton black_button = findViewById(R.id.button_black);
        this.initializeButtonColor(red_button,getResources().getString(R.color.red));
        this.initializeButtonColor(blue_button,getResources().getString(R.color.blue));
        this.initializeButtonColor(black_button,getResources().getString(R.color.black));
        red_button.setOnClickListener(view -> {
            red_button.setSelected(true);
            blue_button.setSelected(false);
            black_button.setSelected(false);
            edit_color = getResources().getString(R.color.red);
        });
        blue_button.setOnClickListener(view -> {
            blue_button.setSelected(true);
            red_button.setSelected(false);
            black_button.setSelected(false);
            edit_color = getResources().getString(R.color.blue);
        });
        black_button.setOnClickListener(view -> {
            black_button.setSelected(true);
            red_button.setSelected(false);
            blue_button.setSelected(false);
            edit_color = getResources().getString(R.color.black);
        });

        Resources r = getResources();
        String pack = getPackageName();

        if (savedInstanceState != null && savedInstanceState.getBoolean("saved_binoxxo")) {
            this.load_binoxxo(savedInstanceState);
        }
        else {
            Log.w("OnCreate","Loading shared preference");
            sharedPreferences = getSharedPreferences(mainPreferences,Context.MODE_PRIVATE);
            this.load_binoxxo(sharedPreferences);
            Log.w("OnCreate","Loaded shared preference");
        }

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                int id = r.getIdentifier("Entry" + Integer.toString(i*8 + j), "id", pack);
                SpinnerPlus field = (SpinnerPlus) findViewById(id);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, selection);
                field.setAdapter(adapter);

                field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        TextView tv =  (TextView) parent.getChildAt(0);
                        tv.setTextColor(Color.parseColor(edit_color));

                        if (tv.getText() != " ")
                        {
                            tv.setBackgroundColor(Color.parseColor(BACK_COLOR));
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });
            }
        }

        this._initialize();

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set listener for navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void set_spinner_selection()
    {
        Resources r = getResources();
        String pack = getPackageName();

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                int id = r.getIdentifier("Entry" + Integer.toString(i*8 + j), "id", pack);

                SpinnerPlus field = (SpinnerPlus) findViewById(id);

                //set default values and en/disabled
                if (this.binoxxo.original.matrix[i][j] == 9)
                {
                    field.setSelection(2);
                    field.setEnabled(true);
                    if (this.binoxxo_matrix[i][j] == 1) {
                        Log.i("Spinner","Matrix is 1 where Original is 9");
                        field.setSelection(0, false);
                    }
                    else if (this.binoxxo_matrix[i][j] == 0)
                    {
                        field.setSelection(1, false);
                    }
                }
                else
                {
                    field.setSelection(2, false);

                    if (this.binoxxo_matrix[i][j] == 1)
                    {
                        field.setSelection(0, false);
                    }
                    else if (this.binoxxo_matrix[i][j] == 0)
                    {
                        field.setSelection(1, false);
                    }
                    field.setEnabled(false);
                }
            }
        }
    }

    public void load_new(View view)
    {
        this.create_binoxxo();

        this.color_default();
        this.binoxxo.reload_grid();

        this.set_spinner_selection();
        ImageButton black_button = findViewById(R.id.button_black);
        black_button.performClick();
    }

    private void _reload()
    {
        this.color_default();
        this.binoxxo.reload_grid();

        this.set_spinner_selection();
        ImageButton black_button = findViewById(R.id.button_black);
        black_button.performClick();
    }

    public void reload(View view)
    {
        this._reload();
    }

    public Binoxxo_Matrix get_current_matrix() {
        int rows = this.binoxxo.rows;
        int cols = this.binoxxo.cols;
        int init = this.binoxxo.matrix.init;
        Binoxxo_Matrix current = new Binoxxo_Matrix(rows,cols,init);
        Resources r = getResources();
        String pack = getPackageName();
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                int id = r.getIdentifier("Entry" + Integer.toString(i*cols + j), "id", pack);
                Spinner field = (Spinner) findViewById(id);
                int val = field.getSelectedItemPosition();
                if (val == 0)
                {
                    current.matrix[i][j] = 1;
                }
                else if (val == 1)
                {
                    current.matrix[i][j] = 0;
                }
                else
                {
                    current.matrix[i][j] = init;
                }
            }
        }
        return current;
    }

    public void check(View view)
    {
        Binoxxo_Matrix actual = this.get_current_matrix();

        if (actual.is_valid())
        {
            this.check_text = "Correct!!";
            this.check_color = "#14cc63";
        }
        else if (!actual.is_valid())
        {
            this.check_text = "Wrong..";
            this.check_color = "#aa1717";
        }

        error_msg = actual.valid_error;

        Intent intent = new Intent(MainActivity.this,CheckActivity.class);
        intent.putExtra(CHECK_TEXT, check_text);
        intent.putExtra(CHECK_COLOR,check_color);
        intent.putExtra(VALID_ERROR,error_msg);
        startActivity(intent);
    }

    private void color_default()
    {
        edit_color = "#000000";
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_rules: {
                // Inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                // Create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // Show the popup window
                popupWindow.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);

                // Dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                int[] rules = {R.string.rule_1, R.string.rule_2, R.string.rule_3};

                // Set new text when button "Continue" is clicked
                final Button button_continue = (Button) popupView.findViewById(R.id.continue_button);
                button_continue.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (current_rule < rules.length - 1) {
                            current_rule = current_rule + 1;
                            TextView counter_text = (TextView) popupWindow.getContentView().findViewById(R.id.rule_counter_text);
                            counter_text.setText(String.format("%d/%d",current_rule+1,rules.length));
                        }
                        else {
                            return;
                        }
                        TextView editable = (TextView) popupWindow.getContentView().findViewById(R.id.rule_text);
                        editable.setText(rules[current_rule]);
                    }
                });

                // Set previous text when button "Back" is clicked
                final Button button_back = (Button) popupView.findViewById(R.id.back_button);
                button_back.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (current_rule > 0) {
                            current_rule = current_rule - 1;
                            TextView counter_text = (TextView) popupWindow.getContentView().findViewById(R.id.rule_counter_text);
                            counter_text.setText(String.format("%d/%d",current_rule+1,rules.length));
                        }
                        else {
                            return;
                        }
                        TextView editable = (TextView) popupWindow.getContentView().findViewById(R.id.rule_text);
                        editable.setText(rules[current_rule]);
                    }
                });
                break;
            }
            case R.id.nav_feedback: {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setData(Uri.parse("mailto:"));
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@anco-design.de"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Binoxxo App Feedback");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;
            }
            case R.id.nav_about: {
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }
        }
        // Close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("edit_color",edit_color);
        outState.putInt("num_cols",this.binoxxo.cols);
        outState.putInt("num_rows",this.binoxxo.rows);
        outState.putBoolean("saved_binoxxo",true);
        Binoxxo_Matrix current_matrix = this.get_current_matrix();
        for(int row = 0; row < this.binoxxo.rows; row++) {
            outState.putIntArray(String.format("matrix_row_%o",row),current_matrix.matrix[row]);
            outState.putIntArray(String.format("original_row_%o",row),this.binoxxo.original.matrix[row]);
            outState.putIntArray(String.format("solution_row_%o",row),this.binoxxo.solution.matrix[row]);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        edit_color=savedInstanceState.getString("edit_color");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences = getSharedPreferences(mainPreferences,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("edit_color",edit_color);
        editor.putInt("num_cols",this.binoxxo.cols);
        editor.putInt("num_rows",this.binoxxo.rows);
        editor.putBoolean("saved_binoxxo",true);
        Binoxxo_Matrix current_matrix = this.get_current_matrix();
        for(int row = 0; row < this.binoxxo.rows; row++) {
            for(int col = 0; col < this.binoxxo.cols; col++) {
                editor.putInt(String.format("matrix_row_%o_%o", row,col), current_matrix.matrix[row][col]);
                editor.putInt(String.format("original_row_%o_%o", row,col), this.binoxxo.original.matrix[row][col]);
                editor.putInt(String.format("solution_row_%o_%o", row,col), this.binoxxo.solution.matrix[row][col]);
            }
        }
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.onPause();
    }
}
