package menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.myprojects.numerus.spark_000.numerus.CaesarCipherActivity;
import com.myprojects.numerus.spark_000.numerus.ColumnTransActivity;
import com.myprojects.numerus.spark_000.numerus.NavigationActivity;
import com.myprojects.numerus.spark_000.numerus.R;
import com.myprojects.numerus.spark_000.numerus.Rot13Activity;
import com.myprojects.numerus.spark_000.numerus.SharedPreferencesManager;
import com.myprojects.numerus.spark_000.numerus.Utils;
import com.myprojects.numerus.spark_000.numerus.VigenereActivity;

import javax.crypto.Cipher;

/**
 * A simple {@link Fragment} subclass.
 */
public class CiphersFragment extends Fragment implements View.OnClickListener {

    private SharedPreferencesManager prefs;

    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    public CiphersFragment() {
        // Required empty public constructor
    }

    Button caesarButton;
    Button rot13Button;
    Button vigenereButton;
    Button columnTransButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ciphers, container, false);

        /** getActivity() could cause an issue.. May not be able to use themes like this */
        prefs = new SharedPreferencesManager(getActivity()); //get SharedPreferencesManager instance
        int t = prefs.retrieveInt("theme", 0); //get stored theme, zero is default
        Utils.setTheme(t);  //Set the stored theme

        Utils.onActivityCreateSetTheme(getActivity());

        caesarButton = (Button) rootView.findViewById(R.id.caesar_button);
        caesarButton.setOnClickListener(this);

        rot13Button = (Button) rootView.findViewById(R.id.rot_13_button);
        rot13Button.setOnClickListener(this);

        vigenereButton = (Button) rootView.findViewById(R.id.vigenere_button);
        vigenereButton.setOnClickListener(this);

        columnTransButton = (Button) rootView.findViewById(R.id.column_trans_button);
        columnTransButton.setOnClickListener(this);

        return rootView;
    } //end onCreateView


    @Override
    public void onClick(View v) {
        //what to do when button is clicked
        switch (v.getId()) {
            case R.id.caesar_button:
                Intent intentCaesar = new Intent(getActivity(), CaesarCipherActivity.class);
                startActivity(intentCaesar);
                break;
            case R.id.rot_13_button:
                Intent intentRot13 = new Intent(getActivity(), Rot13Activity.class);
                startActivity(intentRot13);
                break;
            case R.id.vigenere_button:
                Intent intentVigenere = new Intent(getActivity(), VigenereActivity.class);
                startActivity(intentVigenere);
                break;
            case R.id.column_trans_button:
                Intent intentColTrans = new Intent(getActivity(), ColumnTransActivity.class);
                startActivity(intentColTrans);
                break;
        }
    } //end onClick

}
