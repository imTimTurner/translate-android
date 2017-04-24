package com.github.imtimturner.translate.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.ui.adapter.MainPagerAdapter;
import com.github.imtimturner.translate.ui.widget.TRViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Основное активити приложения
 * */
public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_POSITION_KEY = "current_position";

    @BindView(R.id.view_pager)
    TRViewPager view_pager;
    @BindView(R.id.bottom_bar)
    View bottom_bar;
    @BindView(R.id.navigation_tabs)
    TabLayout navigation_tabs;

    // BugFix 5497
    // Проблема при открытии клавиатуры и изменение размера layout активити
    // все что находится за ViewPager остается на своих местах
    // данная проблема похожа на проблему 5497.
    // Честно я очень долго пытался разобраться как решить данную проблему, но я очень устал
    // поэтому вставляю костыль.
    // При изменение размеров мы просто передвигаем нижнюю часть на величину изменения размеров
    private final ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();

                    View rootView = findViewById(android.R.id.content);
                    rootView.getWindowVisibleDisplayFrame(r);

                    int height = rootView.getContext().getResources()
                            .getDisplayMetrics().heightPixels;
                    int heightDiff = height - r.bottom;
                    if (heightDiff != 0) {
                        bottom_bar.setTranslationY(-heightDiff);
                    } else {
                        bottom_bar.setTranslationY(0);
                    }
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainPagerAdapter adapter = new MainPagerAdapter(this, getSupportFragmentManager());
        view_pager.setAdapter(adapter);

        for (int i = 0; i < adapter.getCount(); i++) {
            navigation_tabs.addTab(navigation_tabs.newTab()
                    .setIcon(adapter.getPageTitleDrawable(i)), i);
        }

        navigation_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());

                // При изменении вкладки закрываем клавиатуру
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view_pager.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // BugFix 5497
        findViewById(android.R.id.content).getViewTreeObserver()
                .addOnGlobalLayoutListener(globalLayoutListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Востанавливаем сохраненое положение индикатора TabLayout
        TabLayout.Tab currentTab = navigation_tabs.getTabAt(
                savedInstanceState.getInt(CURRENT_POSITION_KEY, 0));
        if (currentTab != null) {
            currentTab.select();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION_KEY, view_pager.getCurrentItem());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // BugFix 5497
        findViewById(android.R.id.content).getViewTreeObserver()
                .removeOnGlobalLayoutListener(globalLayoutListener);
    }
}
