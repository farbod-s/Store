package com.example.shopping.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Badgeable<T> {
    public T withBadge(String badge);

    public String getBadge();

    public void setBadge(String badge);
}
