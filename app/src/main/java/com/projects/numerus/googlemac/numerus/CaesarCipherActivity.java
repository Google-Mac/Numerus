package com.projects.numerus.googlemac.numerus;

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
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CaesarCipherActivity extends AppCompatActivity {

    private SharedPreferencesManager prefs;

    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    NumberPicker rotationPicker_caesar;
    EditText inputText_caesar;
    TextView cipherText_caesar;
    RadioGroup radioGroup_caesar;
    Button clearButton_caesar;
    ImageButton sendTextButton_caesar;

    private ClipboardManager myClipboard;
    private ClipData myClip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new SharedPreferencesManager(this); //get SharedPreferencesManager instance
        int t = prefs.retrieveInt("theme", 0); //get stored theme, zero is default
        Utils.setTheme(t);  //Set the stored theme

        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_caesar_cipher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        cipherText_caesar = (TextView)findViewById(R.id.textView2_caesar);

        View.OnLongClickListener listener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                String copyText = cipherText_caesar.getText().toString();
                myClip = ClipData.newPlainText("copyTextCaesar", copyText);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Message Copied",Toast.LENGTH_SHORT).show();
                Log.v("TAG", "button long pressed --> " + copyText);
                return true;
            }
        };

        cipherText_caesar.setOnLongClickListener(listener);

        rotationPicker_caesar = (NumberPicker)findViewById(R.id.numberPicker_caesar);
        rotationPicker_caesar.setMinValue(0);
        rotationPicker_caesar.setMaxValue(25);

        rotationPicker_caesar.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                refreshCipherText();
            }
            });

        radioGroup_caesar = (RadioGroup)findViewById(R.id.radioGroup_caesar);

        radioGroup_caesar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioButton_caesar:
                        refreshCipherText();
                        break;
                    case R.id.radioButton2_caesar:
                        refreshCipherText();
                        break;
                }
                refreshCipherText();
            }
        });


        inputText_caesar = (EditText) findViewById(R.id.editText_caesar);
        inputText_caesar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                try {
                    refreshCipherText();
                } catch (Exception StringIndexOutOfBoundsException) {
                    //prevents app from crashing when codeword is erased and prompts user
                    Log.d("TAG", "No inputText_caesar");
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

        inputText_caesar.setFilters(new InputFilter[]{filter});

        clearButton_caesar = (Button)findViewById(R.id.button_caesar);
        clearButton_caesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText_caesar.setText("");
                cipherText_caesar.setText("");
            }
        });

        sendTextButton_caesar = (ImageButton)findViewById(R.id.send_button_caesar);
        sendTextButton_caesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                intent.putExtra("sms_body", cipherText_caesar.getText().toString());
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

        SharedPreferences settings = getSharedPreferences("cipherPrefsCaesar", 0);
        SharedPreferences.Editor editor = settings.edit();
        saveState(editor);
        editor.commit();
    }

    public void saveState(SharedPreferences.Editor editor) {
        editor.putString("inputTextCaesar", inputText_caesar.getText().toString());
        editor.putInt("rotationCaesar", rotationPicker_caesar.getValue());
        editor.putInt("checkedRadiobuttonCaesar", radioGroup_caesar.getCheckedRadioButtonId());
    }

    public void loadState() {
        SharedPreferences settings = getSharedPreferences("cipherPrefsCaesar", 0);

        inputText_caesar.setText(settings.getString("inputTextCaesar", ""));
        rotationPicker_caesar.setValue(settings.getInt("rotationCaesar", 0));
        radioGroup_caesar.check(settings.getInt("checkedRadiobuttonCaesar", R.id.radioButton_caesar));
        refreshCipherText();
    }

    public void refreshCipherText() {
        String output = computeCipher(inputText_caesar.getText().toString());
        cipherText_caesar.setText(output);
    }

    public String computeCipher(String input) {
        if (radioGroup_caesar.getCheckedRadioButtonId() == R.id.radioButton_caesar) {
            return computeRotationCipher(rotationPicker_caesar.getValue(), input);
        } else if (radioGroup_caesar.getCheckedRadioButtonId() == R.id.radioButton2_caesar) {
            //26 - x reverses the effect of whatever the chosen rotation number is
            return computeRotationCipher(26 - rotationPicker_caesar.getValue(), input);
        }
        return "";
    }

    public String computeRotationCipher(int rotation, String input) {
        // assume input is only a-zA-Z
        int a_num = (int) 'a';
        int A_num = (int) 'A';
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            int cur = (int) input.charAt(i);
            // check if lowercase or uppercase
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
