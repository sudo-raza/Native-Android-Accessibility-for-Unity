package com.example.nativeandroidaccessibilityforunity;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.util.TypedValue;
public class MakeText {
    private TextView textView;
    private Activity activity;

    public MakeText(Activity activity) {
        this.activity = activity;
    }

    public void displayText(final String text, final int textSize, final int x, final int y, final int width, final int height, final int customID) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView = new TextView(activity);
                textView.setId(View.generateViewId()); // Generate a unique ID for the text view
                textView.setText(text);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize); // Set text size in pixels
                textView.setTextColor(Color.argb(255, 0, 0, 0)); // Default text color

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                params.leftMargin = x;
                params.topMargin = y;
                textView.setLayoutParams(params);

                ((FrameLayout) activity.findViewById(android.R.id.content)).addView(textView);

                // Register the TextView with the AccessibilityViewManager
                AccessibilityViewManager.registerView(customID, textView);
                makeTextAlmostInvisible();
            }
        });
    }




    public void makeTextAlmostInvisible() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textView != null) {
                    textView.setAlpha(0.01f); // Set alpha to 0.1 for almost invisible
                    textView.setVisibility(View.VISIBLE);
                    AccessibilityViewManager.updateAllAccessibilityTraversal(); // Update traversal for all TextViews
                }
            }
        });
    }

    public void makeTextInvisible() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textView != null) {
                    textView.setVisibility(View.GONE); // Completely hide the TextView
                    AccessibilityViewManager.updateAllAccessibilityTraversal(); // Update traversal for all TextViews
                }
            }
        });
    }


}
