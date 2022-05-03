package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> selection = Arrays.asList("X","O"," ");

    Binoxxo binoxxo;
    int[][] binoxxo_matrix;

    public static final String CHECK_TEXT = "com.example.anita.binoxxo.check_text";
    public static final String CHECK_COLOR = "com.example.anita.binoxxo.check_color";
    public static final String VALID_ERROR = "com.example.anita.binoxxo.error_msg";
    public static final String BACK_COLOR = "#d8d8d8";

    String check_text = "";
    String check_color = "#000000";
    String error_msg = "";
    static String edit_color = "#000000";

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources r = getResources();
        String pack = getPackageName();

        this.create_binoxxo();

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                int id = r.getIdentifier("Entry" + Integer.toString(i*8 + j), "id", pack);
                SpinnerPlus field = (SpinnerPlus) findViewById(id);
                //Spinner field = (Spinner) findViewById(id);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, selection);
                field.setAdapter(adapter);

                //make color changing possible
                //field.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener()
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

        this._reload();
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
                if (this.binoxxo_matrix[i][j] == 9)
                {
                    field.setSelection(2);
                    field.setEnabled(true);
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
    }

    private void _reload()
    {
        this.color_default();
        this.binoxxo.reload_grid();

        this.set_spinner_selection();
    }

    public void reload(View view)
    {
        this._reload();
    }

    public void check(View view)
    {
        Binoxxo_Matrix actual = new Binoxxo_Matrix(8,8,9);

        Resources r = getResources();
        String pack = getPackageName();
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                int id = r.getIdentifier("Entry" + Integer.toString(i*8 + j), "id", pack);
                Spinner field = (Spinner) findViewById(id);
                int val = field.getSelectedItemPosition();
                if (val == 0)
                {
                    actual.matrix[i][j] = 1;
                }
                else if (val == 1)
                {
                    actual.matrix[i][j] = 0;
                }
                else
                {
                    actual.matrix[i][j] = 9;
                }
            }
        }

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

    public void color_black(View view)
    {
        edit_color = "#000000";
    }

    public void color_blue(View view)
    {
        edit_color = "#0000ff";
    }

    public void color_red(View view)
    {
        edit_color = "#ff0000";
    }

    private void color_default()
    {
        edit_color = "#000000";
    }
}
