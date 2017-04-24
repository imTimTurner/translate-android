package com.github.imtimturner.translate.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.data.model.Lang;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Класс реализует адаптер для Списка языков
 */
public class SelectLangListAdapter extends ArrayAdapter<Lang> {

    /**
     * Хранит выбранный язык.
     * Делаем это сдесь т.к. ListView хранит выбранный по позиции а не по значению
     * и нас это не устраивает т.к. это не работает не правильно при поиске
     * */
    private Lang checked;

    public SelectLangListAdapter(@NonNull Context context,
                                 @NonNull List<Lang> objects) {
        super(context, 0, objects);
    }

    public void setChecked(Lang checked) {
        this.checked = checked;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;
        final ViewHolder holder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.simple_checkable_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Lang lang = getItem(position);
        if (lang != null) {
            holder.text.setText(lang.getName());
            holder.text.setChecked(lang.equals(checked));
        }

        return view;
    }

    static class ViewHolder {
        @BindView(android.R.id.text1)
        CheckedTextView text;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
