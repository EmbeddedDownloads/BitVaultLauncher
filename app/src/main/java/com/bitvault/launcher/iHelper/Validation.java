package com.bitvault.launcher.iHelper;

import android.annotation.SuppressLint;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * This class is used to perform form validation.
 *
 * @author Harish
 */
public class Validation {

    // count variable is used to handle left and right click on the
    // CustomTextvView textViewUserType
    public static int count = 0;

    // Regular Expression
    // you can change the expression based on your need
    // private static final String EMAIL_REGEX =
    // "[A-Z0-9a-z._%+-]{1,}+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String IP_ADDRESS_REGEXC = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String PHONE_REGEX = "\\d{10}";
    private static final String DECIMAL_REGEX = "\\d{0,2}(\\.\\d{1,2})?";
    // Error Messages
    private static final String REQUIRED_MSG = "Required       ";
    private static final String EMAIL_MSG = "Invalid email    ";
    private static final String PHONE_MSG = "Phone Number must be 10 characters long";
    private static final String PWD_MSG = "Password doesn't match       ";
    private static final String PWD_MSG_LENGTH = "Password must be 8-15 characters long";
    private static final String USER_MSG_LENGTH = "Username must be 3-15 characters long";

    private static final String AGE_MSG = "Invalid Age";
    private static final String IP_MSG = "Invalid IP Address";
    private static final String DECIMAL_MSG = "Invalid Rate Value";

    /**
     * call this method when you need to check ip address validation
     *
     * @param editText
     * @return
     */
    public static boolean isIPAdressFormatValid(EditText editText) {

        return isValid(editText, IP_ADDRESS_REGEXC, IP_MSG, true);
    }

    /**
     * isValidDecimalFormat() method is used to allow only two decimal places.
     *
     * @param editText
     * @return
     */
    public static boolean isValidDecimalFormat(EditText editText) {
        return isValid(editText, DECIMAL_REGEX, DECIMAL_MSG, true);
    }

    /**
     * call this method when you need to check email validation
     *
     * @param editText : EmailId editText
     * @param required : Is this field required.
     * @return boolean
     */
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    /**
     * call this method when you need to check phone number validation
     *
     * @param editText : Phone number's EditText
     * @param required : Is this field required
     * @return
     */
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    /**
     * This method is used to check age.
     *
     * @param age
     * @return
     */

    public static boolean isAgeValid(EditText age) {

        String age_value = age.getText().toString().trim();
        age.setError(null);
        if (age_value.equals("00") || age_value.equals("0")) {
            age.setError(null);
            return false;
        } else {
            return true;
        }

    }

    /**
     * call this method when you need to check if password and re-password
     * match.
     *
     * @param password        : Password EditText Field
     * @param confirmPassword : Confirm Password EditText Field.
     * @return boolean.
     */
    public static boolean passwordMatch(EditText password,
                                        EditText confirmPassword) {

        String passwordvalue = password.getText().toString().trim();
        String confirmPasswordValue = confirmPassword.getText().toString()
                .trim();

        if (passwordvalue.equals(confirmPasswordValue)) {
            return true;
        } else {
            confirmPassword.setError(null);
            return false;
        }

    }

    /**
     * To check if phone number has all zeroes
     *
     * @param phoneNumber : Phonenumber EditText
     * @return boolean
     */
    public static boolean checkZeroes(EditText phoneNumber) {

        String number = phoneNumber.getText().toString().trim();

        if (number.equals("0000000000")) {
            phoneNumber.setError(null);
            return false;
        } else {
            return true;
        }
    }

    /**
     * returns true if the input field is valid, based on the parameter passed
     *
     * @param editText : EditText field
     * @param regex    : Match pattern
     * @param errMsg   : Error message to be displayed.
     * @param required : Is this field required
     * @return boolean
     */
    public static boolean isValid(EditText editText, String regex,
                                  String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText))
            return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(null);
            return false;
        }

        return true;
    }

    /**
     *
     */
    public static boolean checkBarcodeLength(EditText editText) {

        String barcode = editText.getText().toString().trim();
        editText.setError(null);
        if (barcode.length() != 15) {
            editText.setError(null);
            return false;
        } else {
            return true;
        }

    }

    /**
     * This method is used to check the height format for example : 1.2,3.3
     *
     * @param editText
     * @return
     */
    public static boolean isValidHeight(EditText editText) {

        String height = editText.getText().toString().trim();

        editText.setError(null);
        if (height.length() != 0) {

            float height2 = Float.parseFloat(height);
            @SuppressWarnings("unused")
            int height3 = (int) height2;

            float heightInt = Float.parseFloat(height);

            if (heightInt > 8 || heightInt < 1) {
                editText.setError(null);

                return false;

            }
            // else if ((height2 - height3) != 0) {
            //
            // if ((height2 - height3) <0.12) {
            // return true;
            //
            // } else {
            // editText.setError("Not a  valid height");
            // return false;
            // }
            //
            // }
            else {
                return true;
            }

        } else
            return true;

    }

    /**
     * check the input field has any text or not return true if it contains text
     * otherwise false
     *
     * @param editText : EditText field.
     * @return boolean
     */
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(null);
            return false;
        }

        return true;
    }

    /**
     * check the input field has any text or not return true if it contains text
     * otherwise false
     *
     * @param textview
     * @return
     */
    public static boolean hasText(TextView textview) {

        String text = textview.getText().toString().trim();
        textview.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            textview.setError(null);
            return false;
        }

        return true;
    }

    /**
     * To check minimum length of password is 4 characters long.
     *
     * @param password : Password EditText field.
     * @return boolean
     */
    public static boolean checkPasswordLength(EditText password) {

        String passwordvalue = password.getText().toString().trim();

        int lenght = passwordvalue.length();
        if (lenght > 5) {
            return true;
        } else {
            password.setError(null);
            return false;
        }

    }

    /**
     * To check minimum length of username is 4 characters long.
     *
     * @param password : Password EditText field.
     * @return boolean
     */
    public static boolean checkUsernameLength(EditText password) {

        String passwordvalue = password.getText().toString().trim();

        int lenght = passwordvalue.length();
        if (lenght > 2) {
            return true;
        } else {
            password.setError(null);
            return false;
        }

    }

    public static boolean checkTime(TextView editTextOldPassword,
                                    TextView editTextNewPassword) {

        String oldPwd = editTextOldPassword.getText().toString().trim();
        String newPwd = editTextNewPassword.getText().toString().trim();
        int lengthOld = oldPwd.length();
        int lengthNew = newPwd.length();

        editTextOldPassword.setError(null);
        editTextNewPassword.setError(null);

        if (lengthOld != 0 || lengthNew != 0) {

            if (lengthOld == 0) {
                editTextOldPassword.setError(null);
                return false;

            } else if (lengthNew == 0) {
                editTextNewPassword.setError(null);
                return false;
            }

        }
        return true;
    }

    public static boolean checkPassword(EditText editTextOldPassword,
                                        EditText editTextNewPassword, EditText editTextConfirmPassword) {
        // TODO Auto-generated method stub
        String oldPwd = editTextOldPassword.getText().toString().trim();
        String newPwd = editTextNewPassword.getText().toString().trim();
        String confirmPwd = editTextConfirmPassword.getText().toString().trim();

        int lengthOld = oldPwd.length();
        int lengthNew = newPwd.length();
        int lengthConfirm = confirmPwd.length();

        editTextOldPassword.setError(null);
        editTextNewPassword.setError(null);
        editTextConfirmPassword.setError(null);

        if (lengthOld != 0 || lengthNew != 0 || lengthConfirm != 0) {

            if (lengthOld == 0) {

                editTextOldPassword.setError(null);
                return false;
            } else if (lengthNew == 0) {
                editTextNewPassword.setError(null);
                return false;
            } else if (lengthConfirm == 0) {
                editTextConfirmPassword.setError(null);
                return false;
            } else if (oldPwd.equalsIgnoreCase(newPwd)) {

                editTextNewPassword.setError(null);
                return false;

            } else if (lengthNew < 8) {
                editTextNewPassword.setError(null);
                return false;

            }
        }

        return true;
    }

    /**
     * This method is used to check whether both name and number are added or
     * not.
     *
     * @return
     */
    public static boolean checkNameNumber(EditText editTextName,
                                          EditText editTextNumber) {

        String name = editTextName.getText().toString().trim();
        String number = editTextNumber.getText().toString().trim();

        editTextName.setError(null);
        editTextNumber.setError(null);

        if (name.length() != 0 || number.length() != 0) {

            if (name.length() == 0) {
                editTextName.setError(null);
                return false;

            } else if (number.length() == 0) {
                editTextNumber.setError(null);
                return false;
            }

        }
        return true;
    }

    /*******************************************************************************
     * Function Name - isEmailValidForCom
     * <p>
     * Description - Check if email is not having .com.com more than one time.
     *
     * @param emailId email id on which validations is to be checked
     * @return true if .com is present only one time, otherwise returns false.
     ******************************************************************************/
    @SuppressLint("DefaultLocale")
    public static boolean isEmailValidForDot(EditText emailId) {
        String emailid = emailId.getText().toString().trim();
        boolean emailValid = false;
        String findStr = ".";
        int lastIndex = emailid.lastIndexOf("@");
        int count = 0;

        emailId.setError(null);

        while (lastIndex != -1) {

            lastIndex = emailid.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        if (count == 1) {

            if ((emailid.toLowerCase().contains("@.".toLowerCase()))) {
                emailId.setError(null);
                emailValid = false;
            } else {
                emailValid = true;
            }

        } else {
            emailId.setError(null);
            emailValid = false;
        }
        return emailValid;
    }

    public static boolean checkUserId(EditText editText) {

        String value = editText.getText().toString().trim();

        editText.setError(null);

        if (value.length() > 0) {
            if (Integer.parseInt(value) > 0) {
                return true;
            } else {
                editText.setError(null);
                return false;
            }
        } else {
            editText.setError(null);
            return false;
        }

    }


}