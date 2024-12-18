package com.example.nativeandroidaccessibilityforunity;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.unity3d.player.UnityPlayer;

public class MakeButton {
    private Activity activity;
    private Button button;

    public MakeButton(Activity activity) {
        this.activity = activity;
    }

    public void createButton(final String buttonText, final int width, final int height, final int x, final int y, final String buttonID, final int customID) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button = new Button(activity);
                button.setId(View.generateViewId());
                button.setText(buttonText);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                params.leftMargin = x;
                params.topMargin = y;
                button.setLayoutParams(params);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callUnityFunction("Button Pressed!", buttonID);
                    }
                });

                ((FrameLayout) activity.findViewById(android.R.id.content)).addView(button);

                // Register the Button with the AccessibilityViewManager
                AccessibilityViewManager.registerView(customID, button);
                makeButtonAlmostInvisible();
            }
        });
    }

    public void callUnityFunction(String message, String buttonID) {
        UnityPlayer.UnitySendMessage(buttonID, "ButtonPressedFromAndroid", message);
    }




    // Method to make the button completely invisible and not detectable by screen readers





    public void makeButtonAlmostInvisible() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(button != null) {
                    button.setAlpha(0.01f); // Set alpha to 0.1 for almost invisible
                    button.setVisibility(View.VISIBLE);
                    AccessibilityViewManager.updateAllAccessibilityTraversal(); // Update traversal for all buttons
                }
            }
        });
    }

    public void makeButtonInvisible() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(button != null) {
                    button.setVisibility(View.GONE); // Completely hide the button
                    AccessibilityViewManager.updateAllAccessibilityTraversal(); // Update traversal for all buttons
                }
            }
        });
    }


}
