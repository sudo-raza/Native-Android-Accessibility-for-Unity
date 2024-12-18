package com.example.texturehelper;

import android.view.View;
import java.util.HashMap;
import java.util.Map;

public class AccessibilityViewManager {
    private static final Map<Integer, View> allViews = new HashMap<>();

    public static void registerView(int customID, View view) {
        allViews.put(customID, view);
    }

    public static void updateAllAccessibilityTraversal() {
        for (Map.Entry<Integer, View> entry : allViews.entrySet()) {
            int customID = entry.getKey();
            updateAccessibilityTraversal(customID);
        }
    }

    private static void updateAccessibilityTraversal(int customID) {
        View currentView = allViews.get(customID);
        if (currentView == null || currentView.getVisibility() != View.VISIBLE) {
            return;
        }

        View nextView = findNextVisibleView(customID);
        View previousView = findPreviousVisibleView(customID);

        if (nextView != null) {
            currentView.setAccessibilityTraversalAfter(nextView.getId());
        }

        if (previousView != null) {
            currentView.setAccessibilityTraversalBefore(previousView.getId());
        }
    }

    private static View findNextVisibleView(int currentID) {
        // Iterate in forward order to find the next view
        for (int i = currentID - 1; i >= 0; i--) {
            if (allViews.containsKey(i)) {
                View nextView = allViews.get(i);
                if (nextView != null && nextView.getVisibility() == View.VISIBLE) {
                    return nextView;
                }
            }
        }
        return null;
    }

    private static View findPreviousVisibleView(int currentID) {
        // Iterate in reverse order to find the previous view
        for (int i = currentID + 1; i <= allViews.size(); i++) {
            if (allViews.containsKey(i)) {
                View prevView = allViews.get(i);
                if (prevView != null && prevView.getVisibility() == View.VISIBLE) {
                    return prevView;
                }
            }
        }
        return null;
    }
}
