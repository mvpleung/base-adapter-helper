/**
 * Copyright 2013 LiangZiChao
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joanzapata.android;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPagerAdapter
 *
 * @author LiangZiChao
 * @Data 2015-9-2
 * @Package com.joanzapata.android
 */
public abstract class BasePagerAdapter<T, H extends BaseAdapterHelper> extends PagerAdapter {

    protected final Context context;

    protected int layoutResId;

    protected final List<T> data;

    protected SparseArray<View> mViews;

    /**
     * Create a QuickAdapter.
     *
     * @param context The context.
     */
    public BasePagerAdapter(Context context) {
        this(context, 0, null);
    }

    /**
     * Create a QuickAdapter.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public BasePagerAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with some
     * initialization data.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BasePagerAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = createConvertView(position, get(position), container);
        put(position, view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * Cache The View
     *
     * @param position
     * @param view
     */
    private void put(int position, View view) {
        if (mViews == null)
            mViews = new SparseArray<View>();
        if (mViews.indexOfValue(view) != -1) {
            mViews.put(position, view);
        }
    }

    /**
     * Get Cache View
     *
     * @return
     */
    private View get(int position) {
        if (mViews == null)
            mViews = new SparseArray<View>();
        return mViews.get(position);
    }

    public View createConvertView(int position, View convertView, ViewGroup parent) {
        final H helper = getAdapterHelper(position, convertView, parent);
        T item = getItem(position);
        helper.setAssociatedObject(item);
        convert(helper, item);
        return helper.getView();
    }

    public T getItem(int position) {
        return data.get(position);
    }

    public List<T> getData() {
        return data;
    }

    public void add(T elem) {
        if (elem != null) {
            data.add(elem);
            notifyDataSetChanged();
        }
    }

    public void add(int index, T elem) {
        if (elem != null) {
            data.add(index, elem);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<T> elem) {
        if (elem != null) {
            data.addAll(elem);
            notifyDataSetChanged();
        }
    }

    public void addAll(int index, List<T> elem) {
        if (elem != null) {
            data.addAll(index, elem);
            notifyDataSetChanged();
        }
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        if (elem != null) {
            data.set(index, elem);
            notifyDataSetChanged();
        }
    }

    public void remove(T elem) {
        if (elem != null) {
            data.remove(elem);
            notifyDataSetChanged();
        }
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        data.clear();
        if (elem != null) {
            data.addAll(elem);
        }
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    /**
     * Clear data list
     */
    public void clear() {
        data.clear();
    }

    /**
     * Clear data list
     */
    public void clearNotify() {
        data.clear();
        notifyDataSetChanged();
    }

    /**
     * Implement this method and use the helper to adapt the view to the given
     * item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(H helper, T item);

    /**
     * You can override this method to use a custom BaseAdapterHelper in order
     * to fit your needs
     *
     * @param position    The position of the item within the adapter's data set of the
     *                    item whose view we want.
     * @param convertView
     * @param parent      The parent that this view will eventually be attached to
     * @return An instance of BaseAdapterHelper
     */
    protected abstract H getAdapterHelper(int position, View convertView, ViewGroup parent);

}
