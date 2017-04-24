package com.github.imtimturner.translate.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.ui.fragment.TabFavoritesFragment;
import com.github.imtimturner.translate.ui.fragment.TabHistoryFragment;
import com.github.imtimturner.translate.ui.fragment.TabSettingsFragment;
import com.github.imtimturner.translate.ui.fragment.TabTranslateFragment;

/**
 * Класс реализует адаптер для ViewPager на MainActivity
 * Содержит все вкладки приложения
 * */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final SparseArray<Fragment> items = new SparseArray<>();

    public MainPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    public Fragment createItem(int position) {
        switch (position) {
            case 1:
                return new TabFavoritesFragment();
            case 2:
                return new TabHistoryFragment();
            case 3:
                return new TabSettingsFragment();
            default:
                return new TabTranslateFragment();
        }
    }

    private int getPageTitleDrawableResource(int position) {
        switch (position) {
            case 1:
                return R.drawable.favorites_outline_light_24dp;
            case 2:
                return R.drawable.history_light_24dp;
            case 3:
                return R.drawable.settings_light_24dp;
            default:
                return R.drawable.translate_light_24dp;
        }
    }

    public Drawable getPageTitleDrawable(int position) {
        return ContextCompat.getDrawable(context, getPageTitleDrawableResource(position));
    }

    @Override
    public Fragment getItem(int position) {
        Fragment item = items.get(position, null);
        if (item == null) {
            item = createItem(position);
        }
        return item;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment item = (Fragment) super.instantiateItem(container, position);
        items.append(position, item);
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        items.remove(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
