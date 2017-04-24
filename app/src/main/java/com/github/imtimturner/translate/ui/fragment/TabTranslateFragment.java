package com.github.imtimturner.translate.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.data.model.Lang;
import com.github.imtimturner.translate.di.module.TranslateModule;
import com.github.imtimturner.translate.presenter.TranslatePresenter;
import com.github.imtimturner.translate.ui.activity.SelectLangActivity;
import com.github.imtimturner.translate.util.TRAnimationUtil;
import com.github.imtimturner.translate.view.TranslateView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

import static com.github.imtimturner.translate.R.id.origin_text;

/**
 * Фрагмент реализующий основную вкладку Перевод
 */
public class TabTranslateFragment extends BaseFragment implements TranslateView {

    private static final int SELECT_ORIGIN_LANG_REQUEST_CODE = 0;
    private static final int SELECT_TARGET_LANG_REQUEST_CODE = 1;

    @BindView(R.id.origin_lang)
    TextView originLang;
    @BindView(R.id.target_lang)
    TextView targetLang;

    @BindView(origin_text)
    EditText originText;
    @BindView(R.id.target_text)
    TextView targetText;
    @BindView(R.id.in_favorites)
    CheckBox inFavorites;

    @BindView(R.id.clear_button)
    ImageButton clearButton;

    @BindView(R.id.loading_layout)
    View loadingLayout;
    @BindView(R.id.content_layout)
    View contentLayout;
    @BindView(R.id.error_layout)
    View errorLayout;

    @Inject
    TranslatePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjections();
    }

    private void initializeInjections() {
        getApplicationComponent().getTranslateComponent(new TranslateModule(this)).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        originText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        targetText.setMovementMethod(new ScrollingMovementMethod());
        presenter.onViewCreated();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void setOriginLang(Lang originLang) {
        this.originLang.setText(originLang.getName());
    }

    @OnClick(R.id.origin_lang)
    public void onOriginLangClick(){
        presenter.onOriginLangClick();
    }

    @Override
    public void setTargetLang(Lang targetLang) {
        this.targetLang.setText(targetLang.getName());
    }

    @OnClick(R.id.target_lang)
    public void onTargetLangClick(){
        presenter.onTargetLangClick();
    }

    @Override
    public void setOriginText(String originText) {
        this.originText.setText(originText);
    }

    @OnTextChanged(origin_text)
    public void onOriginTextChanged(CharSequence newText){
        presenter.onOriginTextChanged(newText.toString());

        if(newText.length() > 0){
            clearButton.setVisibility(View.VISIBLE);
        } else {
            clearButton.setVisibility(View.GONE);
        }
    }

    @OnEditorAction(origin_text)
    public boolean onOriginTextEditorAction(KeyEvent event) {
        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            // Убираем фокус с поля ввода и закрываем клавиатуру
            originText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(originText.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            presenter.onEnterAction();

            return true;
        }

        return false;
    }

    @OnClick(R.id.clear_button)
    public void onClearButtonClick(){
        originText.setText(null);
    }

    @Override
    public void setTargetText(String targetText) {
        this.targetText.setText(targetText);
    }

    @OnTextChanged(R.id.target_text)
    public void onTargetTextChanged(CharSequence newText){
        if(newText.length() > 0){
            inFavorites.setVisibility(View.VISIBLE);
        } else {
            inFavorites.setVisibility(View.GONE);
        }
    }

    @Override
    public void setInFavorites(boolean inFavorites) {
        this.inFavorites.setChecked(inFavorites);
    }

    @OnClick(R.id.in_favorites)
    public void onInFavoritesClick(){
        presenter.onInFavoritesClick();
    }

    @Override
    public void showError(boolean animate){
        TRAnimationUtil.defaultFadeIn(errorLayout, animate);
        TRAnimationUtil.defaultFadeOut(contentLayout, animate);
        TRAnimationUtil.defaultFadeOut(loadingLayout, animate);
    }

    @Override
    public void showContent(boolean animate) {
        TRAnimationUtil.defaultFadeOut(errorLayout, animate);
        TRAnimationUtil.defaultFadeIn(contentLayout, animate);
        TRAnimationUtil.defaultFadeOut(loadingLayout, animate);
    }

    @Override
    public void showLoading(boolean animate) {
        TRAnimationUtil.defaultFadeOut(errorLayout, animate);
        TRAnimationUtil.defaultFadeOut(contentLayout, animate);
        TRAnimationUtil.defaultFadeIn(loadingLayout, animate);
    }

    @Override
    public void showSelectOriginLangView(Lang checked) {
        showSelectLang(SELECT_ORIGIN_LANG_REQUEST_CODE, checked);
    }

    @Override
    public void showSelectTargetLangView(Lang checked) {
        showSelectLang(SELECT_TARGET_LANG_REQUEST_CODE, checked);
    }

    public void showSelectLang(int requestCode, Lang checked) {
        startActivityForResult(new Intent(getContext(), SelectLangActivity.class)
                .putExtra(SelectLangActivity.EXTRA_CHECKED_LANG_CODE, checked.getCode()), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK &&
                data.hasExtra(SelectLangActivity.EXTRA_SELECTED_LANG_CODE)) {
            // Обрабатываем результат выбора языка

            Lang selected = new Lang(data.getStringExtra(SelectLangActivity.EXTRA_SELECTED_LANG_CODE));

            if (requestCode == SELECT_ORIGIN_LANG_REQUEST_CODE) {
                presenter.onOriginLangSelected(selected);
            } else if (requestCode == SELECT_TARGET_LANG_REQUEST_CODE) {
                presenter.onTargetLangSelected(selected);
            }
        }
    }

    @OnClick(R.id.swap_button)
    public void onSwapButtonClick(){
        presenter.onSwapButtonClick();
    }
}
