package com.myprojects.numerus.spark_000.numerus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.myprojects.numerus.spark_000.numerus.R.id.radioGroup;

public class BinaryActivity extends AppCompatActivity {

    private SharedPreferencesManager prefs;

    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    EditText inputText_binary;
    TextView cipherText_binary;
    RadioGroup radioGroup_binary;
    Button clearButton_binary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new SharedPreferencesManager(this); //get SharedPreferencesManager instance
        int t = prefs.retrieveInt("theme", 0); //get stored theme, zero is default
        Utils.setTheme(t);  //Set the stored theme

        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_binary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cipherText_binary = (TextView)findViewById(R.id.textView2_binary);

        radioGroup_binary = (RadioGroup)findViewById(R.id.radioGroup_binary);

        radioGroup_binary.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioButton:
                        computeBinaryCipher(inputText_binary.getText().toString());
                        break;
                    case R.id.radioButton2:
                        /** TODO: Create a binary-to-text function */
                        break;
                }
                refreshCipherText();
            }
        });


        inputText_binary = (EditText) findViewById(R.id.editText_binary);
        inputText_binary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
            int
                    after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int
                    after) {
                refreshCipherText();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for ( int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i)) && source.charAt(i) != ' ') {
                        return "";
                    }
                }
                return null;
            }
        };

        inputText_binary.setFilters(new InputFilter[]{filter});

        clearButton_binary = (Button)findViewById(R.id.button_binary);
        clearButton_binary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText_binary.setText("");
                cipherText_binary.setText("");
            }
        });

        loadState();

    } //end onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences settings = getSharedPreferences("cipherPrefsBinary", 0);
        SharedPreferences.Editor editor = settings.edit();
        saveState(editor);
        editor.commit();
    }

    public void saveState(SharedPreferences.Editor editor) {
        editor.putString("inputTextBinary", inputText_binary.getText().toString());
        editor.putInt("checkedRadiobutton", radioGroup_binary.getCheckedRadioButtonId());
    }

    public void loadState() {
        SharedPreferences settings = getSharedPreferences("cipherPrefsBinary", 0);

        inputText_binary.setText(settings.getString("inputTextBinary", ""));
        radioGroup_binary.check(settings.getInt("checkedRadiobutton", R.id.radioButton_binary));
        refreshCipherText();
    }

    public void refreshCipherText() {
        StringBuilder binary = computeBinaryCipher(inputText_binary.getText().toString());
        cipherText_binary.setText(binary);
    }

    /*public String computeCipher(String input) {
        if (radioGroup_binary.getCheckedRadioButtonId() == R.id.radioButton_binary) {
            return computeBinaryCipher(rotationPicker.getValue(), input);
        } else if (radioGroup_binary.getCheckedRadioButtonId() == R.id.radioButton2_binary) {
            return computeTextCipher(codewordText.getText().toString(), input);
        }
        return "";
    }*/

    public StringBuilder computeBinaryCipher(String s) {
        /*String s = "foo";*/
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary;
    }

    /*public StringBuilder computeTextCipher(String s) {
        *//*String s = "foo";*//*
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary;
    }*/


}
