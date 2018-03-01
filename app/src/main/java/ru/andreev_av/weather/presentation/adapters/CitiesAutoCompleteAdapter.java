package ru.andreev_av.weather.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.domain.model.City;

public class CitiesAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private final static String PERFORM_FILTERING = "PerformFiltering";

    private final Context mContext;
    private List<City> mResults;
    private List<City> mCities;

    public CitiesAutoCompleteAdapter(Context context) {
        mContext = context;
        mResults = new ArrayList<>();
        mCities = new ArrayList<>();
    }

    public void setCities(List<City> cities) {
        mCities = cities;
        getFilter().filter(PERFORM_FILTERING);
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public City getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        City city = getItem(position);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(city.toString());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && constraint.toString().equals(PERFORM_FILTERING)) {// необходимо для корректной фильтрации
                    filterResults.values = mCities;
                    filterResults.count = mCities.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<City>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }
}