package com.ingic.caristocrat.helpers;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ingic.caristocrat.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CustomValidation {

    public static boolean validateEmail(EditText editText, CustomTextInputLayout textInputLayout, String error) {
        String email = editText.getText().toString().trim();
        textInputLayout.errorEnable(false);

        if (email.isEmpty() || !isValidEmail(email)) {
            textInputLayout.setError(error);
            textInputLayout.requestFocus();
//            btnLogin.setAlpha(0.5f);
            editText.requestFocus();
            return false;
        } else {

            textInputLayout.setError("");

        }

        return true;
    }

    public static boolean validatePassword(EditText txtPassword, final CustomTextInputLayout textInputLayout, String error) {
        if (txtPassword.getText().toString().isEmpty() && txtPassword.getText().toString().length() < 8) {
            textInputLayout.setError(error);
            textInputLayout.requestFocus();
//            btnLogin.setAlpha(0.5f);
            txtPassword.requestFocus();
            return false;
        } else {
            textInputLayout.setError("");
        }

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return true;
    }

    public static boolean isValidWebsite(EditText text, final CustomTextInputLayout textInputLayout, String error) {
        String emailPattern = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[\u200C\u200Ba-z]{2}\\.([a-z]+)?$";
        text.requestFocus();
        CharSequence inputStr = text.getText().toString();
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr.toString().trim());
        if (matcher.matches()) {
            textInputLayout.setError("");
            return true;
        }
        //  textInputLayout.requestFocus();
        textInputLayout.setError(error);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return false;
    }

    public static boolean isValidEditText(EditText text, final CustomTextInputLayout textInputLayout, String error) {
        String emailPattern = "^(?=\\s*\\S).*$";
        text.requestFocus();
        CharSequence inputStr = text.getText().toString();
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr.toString().trim());
        if (matcher.matches()) {
            textInputLayout.setError("");
            return true;
        }
        textInputLayout.requestFocus();
        textInputLayout.setError(error);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return false;
    }

    public static boolean isValidNumericField(EditText text, CustomTextInputLayout textInputLayout, String error) {
        String emailPattern = "[0-9]{0,100}$";
        CharSequence inputStr = text.getText().toString();
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()&&text.getText().toString().length()>0) {
            textInputLayout.errorEnable(false);
            return true;
        }
        textInputLayout.requestFocus();
        textInputLayout.setErrorEnabled();
        textInputLayout.setError(error);
        text.requestFocus();
        return false;
    }

    public static boolean isValidPassword(String text, String conformpass, CustomTextInputLayout textInputLayout, String error) {
        if (text.equals(conformpass)) {
            textInputLayout.setError("");
            return true;
        }
        textInputLayout.setError(error);
        return false;
    }

    public static boolean validateLength(EditText editText, final CustomTextInputLayout textInputLayout, String error, String min, String max) {
        String emailPattern = "^.{" + min + "," + max + "}$";
        CharSequence inputStr = editText.getText().toString().trim();
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            textInputLayout.setError("");
            return true;
        }
        textInputLayout.requestFocus();
        textInputLayout.setError(error);
        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return false;
    }

    public static boolean validateNewConfirmPassword(EditText newPassword, EditText confirmPassword, CustomTextInputLayout textInputLayout, Activity activity){
        boolean status = false;
        if(newPassword.getText().toString().equals(confirmPassword.getText().toString())){
            textInputLayout.setError("");
            status = true;
        }else{
            textInputLayout.setError(activity.getResources().getString(R.string.err_passwords_match));
        }
        return status;
    }

    private static boolean isValidEmail(String email){
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
//        else
//            Utils.showSnackBar(ctx,view,"Email Not Valid",ctx.getResources().getColor(R.color.grayColor));

          /*  editText.setError("Email Not Valid");
        editText.requestFocus();
*/
        return false;
    }
}
