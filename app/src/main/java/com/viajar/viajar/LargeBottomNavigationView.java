package com.viajar.viajar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenuView;

/**
 * This code allows to show a custom amount of options in a Bottom Navigation View (max is 5 by default)
 * This is obtained by overriding methods and classes from Android
 * Most code was kept intact
 */

public class LargeBottomNavigationView extends BottomNavigationView {
    // Allows to override max item count (default is 5)
    static int MAX_ITEM_COUNT = 100;

    public LargeBottomNavigationView(@NonNull Context context) {
        super(context);
    }

    public LargeBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LargeBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getMaxItemCount() {
        return MAX_ITEM_COUNT;
    }

    static int getStaticMaxItemCount() {
        return MAX_ITEM_COUNT;
    }

    @SuppressLint("RestrictedApi")
    public void setItemHorizontalTranslationEnabled(boolean itemHorizontalTranslationEnabled) {
        LargeBottomNavigationMenuView menuView = (LargeBottomNavigationMenuView) getMenuView();
        if (menuView.isItemHorizontalTranslationEnabled() != itemHorizontalTranslationEnabled) {
            menuView.setItemHorizontalTranslationEnabled(itemHorizontalTranslationEnabled);
            getPresenter().updateMenuView(false);
        }
    }

    @NonNull
    @SuppressLint("RestrictedApi")
    protected NavigationBarMenuView createNavigationBarMenuView(@NonNull Context context) {
        return new LargeBottomNavigationMenuView(context);
    }

    @SuppressLint("RestrictedApi")
    public boolean isItemHorizontalTranslationEnabled() {
        return ((LargeBottomNavigationMenuView) getMenuView()).isItemHorizontalTranslationEnabled();
    }
}

@SuppressLint("RestrictedApi")
class LargeBottomNavigationMenuView extends BottomNavigationMenuView {
    private final int inactiveItemMaxWidth;
    private final int inactiveItemMinWidth;
    private final int activeItemMaxWidth;
    private final int activeItemMinWidth;
    private final int itemHeight;

    private boolean itemHorizontalTranslationEnabled;
    private int[] tempChildWidths;

    public LargeBottomNavigationMenuView(@NonNull Context context) {
        super(context);

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);

        final Resources res = getResources();
        inactiveItemMaxWidth =
                res.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
        inactiveItemMinWidth =
                res.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
        activeItemMaxWidth =
                res.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
        activeItemMinWidth =
                res.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_min_width);
        itemHeight = res.getDimensionPixelSize(R.dimen.design_bottom_navigation_height);

        tempChildWidths = new int[LargeBottomNavigationView.getStaticMaxItemCount()];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final MenuBuilder menu = getMenu();
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        // Use visible item count to calculate widths
        final int visibleCount = menu.getVisibleItems().size();
        // Use total item counts to measure children
        final int totalCount = getChildCount();

        final int heightSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY);

        if (isShifting(getLabelVisibilityMode(), visibleCount)
                && isItemHorizontalTranslationEnabled()) {
            final View activeChild = getChildAt(getSelectedItemPosition());
            int activeItemWidth = activeItemMinWidth;
            if (activeChild.getVisibility() != View.GONE) {
                // Do an AT_MOST measure pass on the active child to get its desired width, and resize the
                // active child view based on that width
                activeChild.measure(
                        MeasureSpec.makeMeasureSpec(activeItemMaxWidth, MeasureSpec.AT_MOST), heightSpec);
                activeItemWidth = Math.max(activeItemWidth, activeChild.getMeasuredWidth());
            }
            final int inactiveCount = visibleCount - (activeChild.getVisibility() != View.GONE ? 1 : 0);
            final int activeMaxAvailable = width - inactiveCount * inactiveItemMinWidth;
            final int activeWidth =
                    Math.min(activeMaxAvailable, Math.min(activeItemWidth, activeItemMaxWidth));
            final int inactiveMaxAvailable =
                    (width - activeWidth) / (inactiveCount == 0 ? 1 : inactiveCount);
            final int inactiveWidth = Math.min(inactiveMaxAvailable, inactiveItemMaxWidth);
            int extra = width - activeWidth - inactiveWidth * inactiveCount;

            for (int i = 0; i < totalCount; i++) {
                if (getChildAt(i).getVisibility() != View.GONE) {
                    tempChildWidths[i] = (i == getSelectedItemPosition()) ? activeWidth : inactiveWidth;
                    // Account for integer division which sometimes leaves some extra pixel spaces.
                    // e.g. If the nav was 10px wide, and 3 children were measured to be 3px-3px-3px, there
                    // would be a 1px gap somewhere, which this fills in.
                    if (extra > 0) {
                        tempChildWidths[i]++;
                        extra--;
                    }
                } else {
                    tempChildWidths[i] = 0;
                }
            }
        } else {
            final int maxAvailable = width / (visibleCount == 0 ? 1 : visibleCount);
            final int childWidth = Math.min(maxAvailable, activeItemMaxWidth);
            int extra = width - childWidth * visibleCount;
            for (int i = 0; i < totalCount; i++) {
                if (getChildAt(i).getVisibility() != View.GONE) {
                    tempChildWidths[i] = childWidth;
                    if (extra > 0) {
                        tempChildWidths[i]++;
                        extra--;
                    }
                } else {
                    tempChildWidths[i] = 0;
                }
            }
        }

        int totalWidth = 0;
        for (int i = 0; i < totalCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.measure(
                    MeasureSpec.makeMeasureSpec(tempChildWidths[i], MeasureSpec.EXACTLY), heightSpec);
            ViewGroup.LayoutParams params = child.getLayoutParams();
            params.width = child.getMeasuredWidth();
            totalWidth += child.getMeasuredWidth();
        }
        setMeasuredDimension(
                View.resolveSizeAndState(
                        totalWidth, MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY), 0),
                View.resolveSizeAndState(itemHeight, heightSpec, 0));
    }
}
