package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.dialogs.CountryPickerDialog;
import com.ingic.caristocrat.helpers.CustomTextInputLayout;
import com.ingic.caristocrat.helpers.CustomValidation;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.OnRequestConsultancy;
import com.ingic.caristocrat.models.ContactForm;

import java.util.ArrayList;

public class ContactUsAdapter extends BaseExpandableListAdapter {
    private MainActivity mainActivityContext;
    private ArrayList<ContactForm> arrayList;
    private OnRequestConsultancy requestConsultancyListener;

    public ContactUsAdapter(MainActivity mainActivityContext, OnRequestConsultancy requestConsultancyListener) {
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
        this.requestConsultancyListener = requestConsultancyListener;
    }

    @Override
    public int getGroupCount() {
        return this.arrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.arrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.arrayList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = arrayList.get(groupPosition).getTitle();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_contact_us_header, null);
        }

        TextView tvHeaderTitle = convertView.findViewById(R.id.tvSegmentName);
        tvHeaderTitle.setText(headerTitle);
        ImageView imageView = convertView.findViewById(R.id.view);

        if (isExpanded) {
            imageView.setImageResource(R.drawable.uparrow);
        } else {
            imageView.setImageResource(R.drawable.downarrow1);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_contact_us_content, null);
        }

        TextView tvConsultancyFormDescription = convertView.findViewById(R.id.tvConsultancyFormDescription);
        TextView tvCodePicker = convertView.findViewById(R.id.tvCodePicker);
        TextView tvReadOption = convertView.findViewById(R.id.tvReadOption);
        CustomTextInputLayout ilName = convertView.findViewById(R.id.ilName);
        CustomTextInputLayout ilEmail = convertView.findViewById(R.id.ilEmail);
        CustomTextInputLayout ilPhoneNumber = convertView.findViewById(R.id.ilPhoneNumber);
        EditText etName = convertView.findViewById(R.id.etName);
        EditText etEmail = convertView.findViewById(R.id.etEmail);
        EditText etNumber = convertView.findViewById(R.id.etNumber);
        EditText etAbout = convertView.findViewById(R.id.etAbout);
        Button btnSubmitConsultancyRequest = convertView.findViewById(R.id.btnSubmitConsultancyRequest);
        LinearLayout llContactForm = convertView.findViewById(R.id.llContactForm);

        ilName.setErrorEnabled();
        ilEmail.setErrorEnabled();
        ilPhoneNumber.setErrorEnabled();

        if (arrayList.get(groupPosition).getContent() != null) {
            tvConsultancyFormDescription.setText(Html.fromHtml(arrayList.get(groupPosition).getContent()));
        }

        if (arrayList.get(groupPosition).getUser() != null) {
            etName.setText(arrayList.get(groupPosition).getUser().getName());
            etEmail.setText(arrayList.get(groupPosition).getUser().getEmail());
            if (arrayList.get(groupPosition).getUser().getDetails() != null) {
                tvCodePicker.setText(arrayList.get(groupPosition).getUser().getDetails().getCountryCode());
                etNumber.setText(arrayList.get(groupPosition).getUser().getDetails().getPhone());
            }
        }

        etAbout.setText("");

        tvReadOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvConsultancyFormDescription.getMaxLines() == 10) {
                    llContactForm.setVisibility(View.VISIBLE);
                    tvConsultancyFormDescription.setMaxLines(500);
                    tvReadOption.setText(mainActivityContext.getResources().getString(R.string.read_less));
                } else {
                    llContactForm.setVisibility(View.GONE);
                    tvConsultancyFormDescription.setMaxLines(10);
                    tvReadOption.setText(mainActivityContext.getResources().getString(R.string.read_more));
                }
            }
        });

        tvCodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codePicker(tvCodePicker);
            }
        });

        btnSubmitConsultancyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, code = null, number = null;
                if (!CustomValidation.validateLength(etName, ilName, mainActivityContext.getResources().getString(R.string.err_full_name), "3", "50"))
                    return;

                name = etName.getText().toString().trim();

                if (!CustomValidation.validateEmail(etEmail, ilEmail, mainActivityContext.getResources().getString(R.string.err_email)))
                    return;

                email = etEmail.getText().toString().trim();

                if (etNumber.getText().toString().length() > 0) {
                    if (tvCodePicker.getText().toString().length() == 0) {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.err_code), Toast.LENGTH_SHORT);
                        return;
                    }

                    code = tvCodePicker.getText().toString().trim();

                    if (!CustomValidation.validateLength(etNumber, ilPhoneNumber, mainActivityContext.getResources().getString(R.string.err_phone), "7", "20"))
                        return;

                    number = etNumber.getText().toString().trim();
                }

                btnSubmitConsultancyRequest.setEnabled(false);
                if (requestConsultancyListener != null) {
                    requestConsultancyListener.onRequested(email, name, code, number, arrayList.get(groupPosition).getTypeId(), etAbout.getText().toString());
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void addAll(ArrayList<ContactForm> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    private void codePicker(TextView textView) {
        mainActivityContext.pickCountry(new CountryPickerDialog.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(String name, String code, String dialCode, int flagDrawableResID) {
                textView.setText(dialCode.trim());
            }
        });
    }
}
