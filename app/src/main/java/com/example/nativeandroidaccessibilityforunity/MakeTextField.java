
package com.example.nativeandroidaccessibilityforunity;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.unity3d.player.UnityPlayer;
import android.view.accessibility.AccessibilityEvent;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

public class MakeTextField {
    private Activity activity;
    private EditText textField;
    private String unityGameObject;
    private String unityMethod;
    public MakeTextField(Activity activity) {
        this.activity = activity;
    }

    public void createTextField(final String hint, final int width, final int height, final int x, final int y, final String unityGameObject, final String unityMethod, final int customID) {
        this.unityGameObject = unityGameObject;
        this.unityMethod = unityMethod;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textField = new EditText(activity);
                textField.setHint(hint);
                textField.setId(View.generateViewId());

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                params.leftMargin = x;
                params.topMargin = y;
                textField.setLayoutParams(params);

                // Set imeOptions to actionDone
                textField.setImeOptions(EditorInfo.IME_ACTION_DONE);

                textField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            // Hide the keyboard when "Done" is pressed
                            hideKeyboard();
                            return true;
                        }
                        return false;
                    }
                });

                // Listen for Enter key press
                textField.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                            // Hide the keyboard when Enter key is pressed
                            hideKeyboard();
                            return true;
                        }
                        return false;
                    }
                });

                textField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Nothing needed here
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Send the text to Unity when it changes
                        sendInputToUnity();

                        // Notify accessibility listeners about the text change
                        textField.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Nothing needed here
                    }
                });

                ((FrameLayout) activity.findViewById(android.R.id.content)).addView(textField);

                // Register the EditText with the AccessibilityViewManager
                AccessibilityViewManager.registerView(customID, textField);
                makeTextFieldAlmostInvisible();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(textField.getWindowToken(), 0);
        }
    }
    public void makeTextFieldAlmostInvisible() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textField != null) {
                    textField.setAlpha(0.01f); // Set alpha to 0.1 for almost invisible
                    textField.setVisibility(View.VISIBLE);
                    AccessibilityViewManager.updateAllAccessibilityTraversal(); // Update traversal for all EditTexts
                }
            }
        });
    }

    public void makeTextFieldInvisible() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textField != null) {
                    textField.setVisibility(View.GONE); // Completely hide the EditText
                    AccessibilityViewManager.updateAllAccessibilityTraversal(); // Update traversal for all EditTexts
                }
            }
        });
    }


    private void sendInputToUnity() {
        if (textField != null) {
            String textInput = textField.getText().toString();
            UnityPlayer.UnitySendMessage(unityGameObject, unityMethod, textInput);
        }
    }
}
