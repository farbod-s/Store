package com.example.shopping.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Checkable<T> {
    public T withCheckable(boolean checkable);

    public boolean isCheckable();

    public void setCheckable(boolean checkable);
}
