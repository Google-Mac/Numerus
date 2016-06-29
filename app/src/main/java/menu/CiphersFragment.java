package menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myprojects.numerus.spark_000.numerus.CaesarCipherActivity;
import com.myprojects.numerus.spark_000.numerus.ColumnTransActivity;
import com.myprojects.numerus.spark_000.numerus.R;
import com.myprojects.numerus.spark_000.numerus.BinaryActivity;
import com.myprojects.numerus.spark_000.numerus.SharedPreferencesManager;
import com.myprojects.numerus.spark_000.numerus.Utils;
import com.myprojects.numerus.spark_000.numerus.VigenereActivity;

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
    Button binaryButton;
    Button vigenereButton;
    Button columnTransButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ciphers, container, false);

        prefs = new SharedPreferencesManager(getActivity()); //get SharedPreferencesManager instance
        int t = prefs.retrieveInt("theme", 0); //get stored theme, zero is default
        Utils.setTheme(t);  //Set the stored theme

        Utils.onActivityCreateSetTheme(getActivity());

        caesarButton = (Button) rootView.findViewById(R.id.caesar_button);
        caesarButton.setOnClickListener(this);

        binaryButton = (Button) rootView.findViewById(R.id.binary_button);
        binaryButton.setOnClickListener(this);

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
            case R.id.binary_button:
                Intent intentRot13 = new Intent(getActivity(), BinaryActivity.class);
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
