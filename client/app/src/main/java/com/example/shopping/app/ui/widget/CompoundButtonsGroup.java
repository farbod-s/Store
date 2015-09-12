package com.example.shopping.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/**
 * This class is used to create a multiple-exclusion scope for a set of compound
 * buttons. Checking one compound button that belongs to a group unchecks any
 * previously checked compound button within the same group. Intially, all of
 * the compound buttons are unchecked. While it is not possible to uncheck a
 * particular compound button, the group can be cleared to remove the checked
 * state. Basically, this class extends functionality of
 * {@link android.widget.RadioGroup} because it doesn't require that compound
 * buttons are direct childs of the group. This means you can wrap compound
 * buttons with other views. <br>
 * <br>
 *
 * <b>IMPORTATNT! Follow these instruction when using this class:</b><br>
 * 1. Each direct child of this group must contain one compound button or be
 * compound button itself.<br>
 * 2. Do not set any "on click" or "on checked changed" listeners for the childs
 * of this group.
 */


public class CompoundButtonsGroup extends LinearLayout {

    private View checkedView;
    private OnCheckedChangeListener listener;
    private OnHierarchyChangeListener onHierarchyChangeListener;

    private OnHierarchyChangeListener onHierarchyChangeListenerInternal = new OnHierarchyChangeListener() {

        @Override
        public final void onChildViewAdded(View parent, View child) {
            notifyHierarchyChanged(null);
            if (CompoundButtonsGroup.this.onHierarchyChangeListener != null) {
                CompoundButtonsGroup.this.onHierarchyChangeListener.onChildViewAdded(
                        parent, child);
            }
        }

        @Override
        public final void onChildViewRemoved(View parent, View child) {
            notifyHierarchyChanged(child);
            if (CompoundButtonsGroup.this.onHierarchyChangeListener != null) {
                CompoundButtonsGroup.this.onHierarchyChangeListener.onChildViewRemoved(
                        parent, child);
            }
        }
    };

    public CompoundButtonsGroup(Context context) {
        super(context);
        init();
    }

    public CompoundButtonsGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CompoundButtonsGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        super.setOnHierarchyChangeListener(this.onHierarchyChangeListenerInternal);
    }

    @Override
    public final void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        this.onHierarchyChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked view changes in this
     * group.
     *
     * @param listener
     *            the callback to call on checked state change.
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Returns currently selected view in this group. Upon empty selection, the
     * returned value is null.
     */
    public View getCheckedView() {
        return this.checkedView;
    }

    /**
     * Returns index of currently selected view in this group. Upon empty
     * selection, the returned value is -1.
     */
    public int getCheckedViewIndex() {
        return (this.checkedView != null) ? indexOfChild(this.checkedView) : -1;
    }

    /**
     * Sets the selection to the view whose index in group is passed in
     * parameter.
     *
     * @param index
     *            the index of the view to select in this group.
     */
    public void check(int index) {
        check(getChildAt(index));
    }

    /**
     * Clears the selection. When the selection is cleared, no view in this
     * group is selected and {@link #getCheckedView()} returns null.
     */
    public void clearCheck() {
        if (this.checkedView != null) {
            findCompoundButton(this.checkedView).setChecked(false);
            this.checkedView = null;
            onCheckedChanged();
        }
    }

    private void onCheckedChanged() {
        if (this.listener != null) {
            this.listener.onCheckedChanged(this.checkedView);
        }
    }

    private void check(View child) {
        if (this.checkedView == null || !this.checkedView.equals(child)) {
            if (this.checkedView != null) {
                findCompoundButton(this.checkedView).setChecked(false);
            }

            CompoundButton comBtn = findCompoundButton(child);
            comBtn.setChecked(true);

            this.checkedView = child;
            onCheckedChanged();
        }
    }

    private void notifyHierarchyChanged(View removedView) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    check(v);
                }
            });
            CompoundButton comBtn = findCompoundButton(child);
            comBtn.setClickable(comBtn.equals(child));
        }

        if (this.checkedView != null && removedView != null
                && this.checkedView.equals(removedView)) {
            clearCheck();
        }
    }

    private CompoundButton findCompoundButton(View view) {
        if (view instanceof CompoundButton) {
            return (CompoundButton) view;
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                CompoundButton compoundBtn = findCompoundButton(((ViewGroup) view)
                        .getChildAt(i));
                if (compoundBtn != null) {
                    return compoundBtn;
                }
            }
        }

        return null;
    }

    /**
     * Interface definition for a callback to be invoked when the checked view
     * changed in this group.
     */
    public interface OnCheckedChangeListener {

        /**
         * Called when the checked view has changed.
         *
         * @param checkedView
         *            newly checked view or null if selection was cleared in the
         *            group.
         */
        public void onCheckedChanged(View checkedView);
    }
}