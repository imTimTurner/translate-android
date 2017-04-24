package com.github.imtimturner.translate.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.github.imtimturner.translate.R;
import com.github.imtimturner.translate.data.model.Translation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Класс реализует адаптер для списка Переводов
 * */
public class TranslationListAdapter extends BaseAdapter implements Filterable {

    /**
     * Интерфейс слушателя нажатия на кнопку "в избранное"
     * */
    public interface OnFavoritesClickListener {
        void onFavoritesClick(Translation translation);
    }

    /**
     * Содержит все элементы
     * */
    private List<Translation> originItems = new ArrayList<>();

    /**
     * Только те которые отображаются
     * */
    private List<Translation> items = new ArrayList<>();

    private TranslationFilter filter;

    private OnFavoritesClickListener listener;

    public TranslationListAdapter(@NonNull List<Translation> objects) {
        this.originItems = new ArrayList<>(objects);
        this.items = new ArrayList<>(objects);
    }

    public void setOnFavoritesClickListener(OnFavoritesClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TranslationFilter();
        }
        return filter;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void setItems(List<Translation> translations) {
        if (translations == null) {
            translations = new ArrayList<>();
        }
        originItems = new ArrayList<>(translations);
        items = new ArrayList<>(originItems);

        // если фильт установлен то приминяем его
        if (filter != null && filter.constraint != null){
            filter.filter(filter.constraint);
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public Translation getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;
        final ViewHolder holder;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.translation_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Translation translation = getItem(position);
        if (translation != null) {
            holder.inFavorites.setChecked(translation.isInFavorites());
            holder.inFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onFavoritesClick(getItem(position));
                    }
                }
            });

            holder.originText.setText(translation.getOriginText());
            holder.targetText.setText(translation.getTargetText());
            holder.langPair.setText(translation.getOriginLang().getCode().toUpperCase() + " - " +
                    translation.getTargetLang().getCode().toUpperCase());
        }

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.in_favorites)
        CheckBox inFavorites;
        @BindView(R.id.origin_text)
        TextView originText;
        @BindView(R.id.target_text)
        TextView targetText;
        @BindView(R.id.lang_pair)
        TextView langPair;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Реализует фильтр для поиска
     * */
    private class TranslationFilter extends Filter {

        private CharSequence constraint;

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.constraint = constraint;

            items = (List<Translation>) results.values;
            if (results.count > 0) {
                notifyDataSetInvalidated();
            } else {
                notifyDataSetChanged();
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                results.count = originItems.size();
                results.values = new ArrayList<>(originItems);
                return results;
            }

            List<Translation> FilteredArray = new ArrayList<>();

            constraint = constraint.toString().toLowerCase();
            for (Translation translation : originItems) {
                String originText = translation.getOriginText().toLowerCase();
                String targetText = translation.getTargetText().toLowerCase();

                if (originText.startsWith(constraint.toString()) ||
                        targetText.startsWith(constraint.toString())) {
                    FilteredArray.add(translation);
                }
            }

            results.count = FilteredArray.size();
            results.values = FilteredArray;

            return results;
        }
    }
}
