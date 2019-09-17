package com.ingic.caristocrat.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ingic.caristocrat.R;

public class NationalityDialog extends DialogFragment {
    String[] nationality = new String[]{
            "Afghan",

            "Albanian",

            "Algerian",

            "American",

            "Andorran",

            "Angolan",

            "Antiguans",

            "Argentinean",

            "Armenian",

            "Australian",

            "Austrian",

            "Azerbaijani",

            "Bahamian",

            "Bahraini",

            "Bangladeshi",

            "Barbadian",

            "Barbudans",

            "Batswana",

            "Belarusian",

            "Belgian",

            "Belizean",

            "Beninese",

            "Bhutanese",

            "Bolivian",

            "Bosnian",

            "Brazilian",

            "British",

            "Bruneian",

            "Bulgarian",

            "Burkinabe",

            "Burmese",

            "Burundian",

            "Cambodian",

            "Cameroonian",

            "Canadian",

            "Cape Verdean",

            "Central African",

            "Chadian",

            "Chilean",

            "Chinese",

            "Colombian",

            "Comoran",

            "Congolese",

            "Costa Rican",

            "Croatian",

            "Cuban",

            "Cypriot",

            "Czech",

            "Danish",

            "Djibouti",

            "Dominican",

            "Dutch",

            "East Timorese",

            "Ecuadorean",

            "Egyptian",

            "Emirian",

            "Equatorial Guinean",

            "Eritrean",

            "Estonian",

            "Ethiopian",

            "Fijian",

            "Filipino",

            "Finnish",

            "French",

            "Gabonese",

            "Gambian",

            "Georgian",

            "German",

            "Ghanaian",

            "Greek",

            "Grenadian",

            "Guatemalan",

            "Guinea-Bissauan",

            "Guinean",

            "Guyanese",

            "Haitian",

            "Herzegovinian",

            "Honduran",

            "Hungarian",

            "I-Kiribati",

            "Icelander",

            "Indian",

            "Indonesian",

            "Iranian",

            "Iraqi",

            "Irish",

            "Israeli",

            "Italian",

            "Ivorian",

            "Jamaican",

            "Japanese",

            "Jordanian",

            "Kazakhstani",

            "Kenyan",

            "Kittian and Nevisian",

            "Kuwaiti",

            "Kyrgyz",

            "Laotian",

            "Latvian",

            "Lebanese",

            "Liberian",

            "Libyan",

            "Liechtensteiner",

            "Lithuanian",

            "Luxembourger",

            "Macedonian",

            "Malagasy",

            "Malawian",

            "Malaysian",

            "Maldivan",

            "Malian",

            "Maltese",

            "Marshallese",

            "Mauritanian",

            "Mauritian",

            "Mexican",

            "Micronesian",

            "Moldovan",

            "Monacan",

            "Mongolian",

            "Moroccan",

            "Mosotho",

            "Motswana",

            "Mozambican",

            "Namibian",

            "Nauruan",

            "Nepalese",

            "New Zealander",

            "Nicaraguan",

            "Nigerian",

            "Nigerien",

            "North Korean",

            "Northern Irish",

            "Norwegian",

            "Omani",

            "Pakistani",

            "Palauan",

            "Panamanian",

            "Papua New Guinean",

            "Paraguayan",

            "Peruvian",

            "Polish",

            "Portuguese",

            "Qatari",

            "Romanian",

            "Russian",

            "Rwandan",

            "Saint Lucian",

            "Salvadoran",

            "Samoan",

            "San Marinese",

            "Sao Tomean",

            "Saudi",

            "Scottish",

            "Senegalese",

            "Serbian",

            "Seychellois",

            "Sierra Leonean",

            "Singaporean",

            "Slovakian",

            "Slovenian",

            "Solomon Islander",

            "Somali",

            "South African",

            "South Korean",

            "Spanish",

            "Sri Lankan",

            "Sudanese",

            "Surinamer",

            "Swazi",

            "Swedish",

            "Swiss",

            "Syrian",
            "Taiwanese",
            "Tajik",
            "Tanzanian", "Thai", "Togolese",

            "Tongan", "Trinidadian/Tobagonian", "Tunisian",

            "Turkish",            "Tuvaluan",

            "Ugandan",            "Ukrainian",

            "Uruguayan",

            "Uzbekistani",

            "Venezuelan",

            "Vietnamese",

            "Welsh",

            "Yemenite",

            "Zambian",

            "Zimbabwean"
    };

    TextView txt_Heading;
    ListView listView;
    LinearLayout layout;

    private buttonClicked buttonClicked;

    public static NationalityDialog newInstance(String title) {
        NationalityDialog frag = new NationalityDialog();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.dialog_country, container,
                false);

        txt_Heading = parentView.findViewById(R.id.txt_Heading);
        listView = parentView.findViewById(R.id.listView);
        layout = parentView.findViewById(R.id.layout);
        return parentView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_Heading.setText(getActivity().getResources().getString(R.string.select_nationality));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_country_dialog, nationality);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String value = (String) parent.getItemAtPosition(position);
                buttonClicked.onClicked(value);

                dismiss();
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setOKButtonClicked(buttonClicked buttonClicked) {
        this.buttonClicked = buttonClicked;
    }


    public interface buttonClicked {
        public void onClicked(String value);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() != null)
            getActivity().setTheme(R.style.AppTheme);
    }

}