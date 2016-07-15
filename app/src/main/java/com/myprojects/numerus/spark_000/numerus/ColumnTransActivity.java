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

public class ColumnTransActivity extends AppCompatActivity {

    private SharedPreferencesManager prefs;

    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    EditText inputText_colTrans;
    TextView cipherText_colTrans;
    RadioGroup radioGroup_colTrans;
    EditText codewordText_colTrans;
    Button clearButton_colTrans;
    ImageButton sendTextButton_colTrans;

    private ClipboardManager myClipboard;
    private ClipData myClip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new SharedPreferencesManager(this); //get SharedPreferencesManager instance
        int t = prefs.retrieveInt("theme", 0); //get stored theme, zero is default
        Utils.setTheme(t);  //Set the stored theme

        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_column_trans);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        cipherText_colTrans = (TextView)findViewById(R.id.textView2_colTrans);

        View.OnLongClickListener listener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                String copyText = cipherText_colTrans.getText().toString();
                myClip = ClipData.newPlainText("copyTextColTrans", copyText);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Message Copied",Toast.LENGTH_SHORT).show();
                Log.v("TAG", "button long pressed --> " + copyText);
                return true;
            }
        };

        cipherText_colTrans.setOnLongClickListener(listener);

        radioGroup_colTrans = (RadioGroup)findViewById(R.id.radioGroup_colTrans);

        radioGroup_colTrans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioButton_colTrans:
                        refreshCipherText();
                        break;
                    case R.id.radioButton2_colTrans:
                        refreshCipherText();
                        break;
                }
                try {
                    refreshCipherText();
                } catch (Exception StringIndexOutOfBoundsException) {
                    Log.d("TAG", "No cipherText_colTrans");
                }
            }
        });

        codewordText_colTrans = (EditText)findViewById(R.id.editText2_colTrans);
        codewordText_colTrans.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                try {
                    refreshCipherText();
                } catch (Exception StringIndexOutOfBoundsException) {
                    //prevents app from crashing when codeword is erased and prompts user
                    Log.d("TAG", "No cipherText_colTrans");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


        inputText_colTrans = (EditText) findViewById(R.id.editText_colTrans);
        inputText_colTrans.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                try {
                    refreshCipherText();
                } catch (Exception StringIndexOutOfBoundsException) {
                    //prevents app from crashing when codeword is erased and prompts user
                    Log.d("TAG", "No inputText_colTrans");
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

        inputText_colTrans.setFilters(new InputFilter[]{filter});
        codewordText_colTrans.setFilters(new InputFilter[]{filter});

        clearButton_colTrans = (Button)findViewById(R.id.button_colTrans);
        clearButton_colTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText_colTrans.setText("");
                cipherText_colTrans.setText("");
            }
        });

        sendTextButton_colTrans = (ImageButton)findViewById(R.id.send_button_colTrans);
        sendTextButton_colTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                intent.putExtra("sms_body", cipherText_colTrans.getText().toString());
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

        SharedPreferences settings = getSharedPreferences("cipherPrefsColTrans", 0);
        SharedPreferences.Editor editor = settings.edit();
        saveState(editor);
        editor.commit();
    }

    public void saveState(SharedPreferences.Editor editor) {
        editor.putString("inputTextColTrans", inputText_colTrans.getText().toString());
        editor.putString("codewordColTrans", codewordText_colTrans.getText().toString());
        editor.putInt("checkedRadiobuttonColTrans", radioGroup_colTrans.getCheckedRadioButtonId());
    }

    public void loadState() {
        SharedPreferences settings = getSharedPreferences("cipherPrefsColTrans", 0);

        inputText_colTrans.setText(settings.getString("inputTextColTrans", ""));
        codewordText_colTrans.setText(settings.getString("codewordColTrans", ""));
        radioGroup_colTrans.check(settings.getInt("checkedRadiobuttonColTrans", R.id
                .radioButton_colTrans));
        try {
            refreshCipherText();
        } catch (Exception StringIndexOutOfBoundsException) {
            //prevents app from crashing when codeword is erased and prompts user
            Log.d("TAG", "No inputText_colTrans");
        }
    }

    public void refreshCipherText() {
        String output = computeCipher(inputText_colTrans.getText().toString());
        try {
            cipherText_colTrans.setText(output);
        } catch (Exception StringIndexOutOfBoundsException) {
            //prevents app from crashing when codeword is erased and prompts user
            Log.d("TAG", "No cipherText_colTrans");
        }
    }

    public String computeCipher(String input) {
        /** TODO: If getText().toString() doesn't work, try something else */
        if (radioGroup_colTrans.getCheckedRadioButtonId() == R.id.radioButton_colTrans) {
            return encrypt(input, codewordText_colTrans.getText().toString());
        } else if (radioGroup_colTrans.getCheckedRadioButtonId() == R.id.radioButton2_colTrans) {
            return decrypt(input, codewordText_colTrans.getText().toString());
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
