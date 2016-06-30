package com.myprojects.numerus.spark_000.numerus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class VigenereActivity extends AppCompatActivity {

    private SharedPreferencesManager prefs;

    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    EditText inputText_vigenere;
    TextView cipherText_vigenere;
    EditText codewordText_vigenere;
    Button clearButton_vigenere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new SharedPreferencesManager(this); //get SharedPreferencesManager instance
        int t = prefs.retrieveInt("theme", 0); //get stored theme, zero is default
        Utils.setTheme(t);  //Set the stored theme

        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_vigenere);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cipherText_vigenere = (TextView)findViewById(R.id.textView2_vigenere);


        codewordText_vigenere = (EditText)findViewById(R.id.editText2_vigenere);
        codewordText_vigenere.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                refreshCipherText();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


        inputText_vigenere = (EditText) findViewById(R.id.editText_vigenere);
        inputText_vigenere.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
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

        inputText_vigenere.setFilters(new InputFilter[]{filter});
        codewordText_vigenere.setFilters(new InputFilter[]{filter});

        clearButton_vigenere = (Button)findViewById(R.id.button_vigenere);
        clearButton_vigenere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText_vigenere.setText("");
                cipherText_vigenere.setText("");
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

        SharedPreferences settings = getSharedPreferences("cipherPrefsVigenere", 0);
        SharedPreferences.Editor editor = settings.edit();
        saveState(editor);
        editor.commit();
    }

    public void saveState(SharedPreferences.Editor editor) {
        editor.putString("inputTextVigenere", inputText_vigenere.getText().toString());
        editor.putString("codewordVigenere", codewordText_vigenere.getText().toString());
    }

    public void loadState() {
        SharedPreferences settings = getSharedPreferences("cipherPrefsVigenere", 0);

        inputText_vigenere.setText(settings.getString("inputTextVigenere", ""));
        codewordText_vigenere.setText(settings.getString("codewordVigenere", ""));
        refreshCipherText();
    }

    public void refreshCipherText() {
        String output = computeCipher(inputText_vigenere.getText().toString());
        cipherText_vigenere.setText(output);
    }

    public String computeCipher(String input) {
        return computeCodewordCipher(codewordText_vigenere.getText().toString(), input);
    }

    /** TODO: Change to Vigenere code */
    public String computeCodewordCipher(String codeword, String input) {
        int a_num = (int) 'a';
        int A_num = (int) 'A';
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            int rotation = codeword.length() > 0 && codeword.charAt(i % codeword.length()) != ' ' ? (int)input.charAt(i % codeword.length()) : 0;
            int cur = (int) input.charAt(i);
            if (input.charAt(i) == ' ') {
                output += " ";
            } else if (cur >= a_num && cur < a_num + 26) {
                output += Character.toString((char) ((((cur - a_num) + rotation) % 26) + a_num));
            } else {
                output += Character.toString((char) ((((cur - A_num) + rotation) % 26) + A_num));
            }
        }

        return output;
    }

}
