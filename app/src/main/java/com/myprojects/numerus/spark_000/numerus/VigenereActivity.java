package com.myprojects.numerus.spark_000.numerus;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class VigenereActivity extends AppCompatActivity {

    private SharedPreferencesManager prefs;

    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    EditText inputText_vigenere;
    TextView cipherText_vigenere;
    RadioGroup radioGroup_vigenere;
    EditText codewordText_vigenere;
    Button clearButton_vigenere;
    ImageButton sendTextButton_vigenere;

    private ClipboardManager myClipboard;
    private ClipData myClip;


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

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        cipherText_vigenere = (TextView)findViewById(R.id.textView2_vigenere);

        View.OnLongClickListener listener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                String copyText = cipherText_vigenere.getText().toString();
                myClip = ClipData.newPlainText("copyTextVigenere", copyText);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Message Copied",Toast.LENGTH_SHORT).show();
                Log.v("TAG", "button long pressed --> " + copyText);
                return true;
            }
        };

        cipherText_vigenere.setOnLongClickListener(listener);

        radioGroup_vigenere = (RadioGroup)findViewById(R.id.radioGroup_vigenere);

        radioGroup_vigenere.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioButton_vigenere:
                        refreshCipherText();
                        break;
                    case R.id.radioButton2_vigenere:
                        refreshCipherText();
                        break;
                }
                try {
                    refreshCipherText();
                } catch (Exception StringIndexOutOfBoundsException) {
                    Log.d("TAG", "No cipherText_vigenere");
                }
            }
        });

        codewordText_vigenere = (EditText)findViewById(R.id.editText2_vigenere);
        codewordText_vigenere.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                try {
                    refreshCipherText();
                } catch (Exception StringIndexOutOfBoundsException) {
                    //prevents app from crashing when codeword is erased and prompts user
                    Log.d("TAG", "No cipherText_vigenere");
                }
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
                try {
                    refreshCipherText();
                } catch (Exception StringIndexOutOfBoundsException) {
                    //prevents app from crashing when codeword is erased and prompts user
                    Log.d("TAG", "No inputText_vigenere");
                }
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

        sendTextButton_vigenere = (ImageButton)findViewById(R.id.send_button_vigenere);
        sendTextButton_vigenere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                intent.putExtra("sms_body", cipherText_vigenere.getText().toString());
                startActivity(intent);
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
        editor.putInt("checkedRadiobuttonVigenere", radioGroup_vigenere.getCheckedRadioButtonId());
    }

    public void loadState() {
        SharedPreferences settings = getSharedPreferences("cipherPrefsVigenere", 0);

        inputText_vigenere.setText(settings.getString("inputTextVigenere", ""));
        codewordText_vigenere.setText(settings.getString("codewordVigenere", ""));
        radioGroup_vigenere.check(settings.getInt("checkedRadiobuttonVigenere", R.id
                .radioButton_vigenere));
        try {
            refreshCipherText();
        } catch (Exception StringIndexOutOfBoundsException) {
            //prevents app from crashing when codeword is erased and prompts user
            Log.d("TAG", "No inputText_vigenere");
        }
    }

    public void refreshCipherText() {
        String output = computeCipher(inputText_vigenere.getText().toString());
        try {
            cipherText_vigenere.setText(output);
        } catch (Exception StringIndexOutOfBoundsException) {
            //prevents app from crashing when codeword is erased and prompts user
            Log.d("TAG", "No cipherText_vigenere");
        }
    }

    public String computeCipher(String input) {
        /** TODO: If getText().toString() doesn't work, try something else */
        if (radioGroup_vigenere.getCheckedRadioButtonId() == R.id.radioButton_vigenere) {
            return encrypt(input, codewordText_vigenere.getText().toString());
        } else if (radioGroup_vigenere.getCheckedRadioButtonId() == R.id.radioButton2_vigenere) {
            return decrypt(input, codewordText_vigenere.getText().toString());
        }
        return "";
    }

    public String encrypt(String text, final String key) {
        int a_num = (int) 'a';
        int A_num = (int) 'A';
        String output = "";
        for (int i = 0, j = 0; i < text.length(); i++) {
            int cur = (int) text.charAt(i);
            //check if lowercase or uppercase
            if (text.charAt(i) == ' ') {
                output += " ";
            } else if (cur >= 'a' && cur < 'z' + 26) {
                output += Character.toString((char) ((cur + key.charAt(j) - 2 * 'a') % 26 + 'a'));
                j = ++j % key.length();
            } else {
                output += Character.toString((char) ((cur + key.charAt(j) - 2 * 'A') % 26 + 'A'));
                j = ++j % key.length();
            }
        }

        return output;
    }

    public String decrypt(String text, final String key) {
        int a_num = (int) 'a';
        int A_num = (int) 'A';
        String output = "";
        for (int i = 0, j = 0; i < text.length(); i++) {
            int cur = text.charAt(i);
            //check if lowercase or uppercase
            if (text.charAt(i) == ' ') {
                output += " ";
            } else if (cur < 'a' || cur > 'z') {
                output += Character.toString((char) ((cur - key.charAt(j) + 26) % 26 + 'a'));
                j = ++j % key.length();
            } else {
                output += Character.toString((char) ((cur - key.charAt(j) + 26) % 26 + 'A'));
                j = ++j % key.length();
            }
        }

        return output;
    }

}
