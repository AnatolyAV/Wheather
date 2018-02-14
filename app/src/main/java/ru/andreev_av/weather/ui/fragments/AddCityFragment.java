package ru.andreev_av.weather.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.data.model.Coordinate;
import ru.andreev_av.weather.db.entry.CityEntry;
import ru.andreev_av.weather.processors.Processor;


public class AddCityFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static int CITY_LETTERS_THRESHOLD = 4;
    private final static int CITY_LIST_LIMIT = 10;

    private final static String CITY_FIRST_LETTERS = "cityFirstLetters";
    private final static int CITY_LOADER = 1;
    private FrameLayout flAddCity;
    private AutoCompleteTextView actvAddCity;
    private SimpleCursorAdapter cityAdapter;
    private City selectedCity;
    private boolean isSelectedCity;
    private OnAddCityFragmentInteractionListener mListener;

    public AddCityFragment() {
        // Required empty public constructor
    }

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(CITY_LOADER, null, this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        findComponents();

        initAdapter();

        initComponents();

        initListeners();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_add_city_title_label);
        builder.setView(flAddCity)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onAddCityFragmentInteraction(selectedCity);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        selectedCity = null;
                    }
                });

        final AlertDialog dialog = builder.create();

        actvAddCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (dialog.getWindow() != null)
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddCityFragmentInteractionListener) {
            mListener = (OnAddCityFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddCityFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void findComponents() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        flAddCity = (FrameLayout) inflater.inflate(R.layout.fragment_add_city, null);
        actvAddCity = (AutoCompleteTextView) flAddCity.findViewById(R.id.actv_add_city);
    }

    private void initAdapter() {
        String[] from = new String[]{CityEntry.COLUMN_NAME, CityEntry.COLUMN_COUNTRY_CODE};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        cityAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.two_line_list_item, null, from, to, 0);
    }

    private void initComponents() {
        actvAddCity.setAdapter(cityAdapter);
        actvAddCity.setThreshold(CITY_LETTERS_THRESHOLD);
    }

    protected void initListeners() {
        actvAddCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // необходимо, чтобы поиск не отрабатывал сразу же после выбора города
                if (!isSelectedCity) {
                    selectedCity = null;
                    final String content = actvAddCity.getText().toString();
                    Bundle args = new Bundle();
                    args.putString(CITY_FIRST_LETTERS, content);
                    getActivity().getSupportLoaderManager().restartLoader(CITY_LOADER, args, AddCityFragment.this);
                }
                isSelectedCity = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        actvAddCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    loadCityFromCursor(cursor);
                    cursor.close();
                    actvAddCity.setText(selectedCity.toString());
                }
            }
        });
    }

    private void loadCityFromCursor(Cursor cursor) {
        int cityId = cursor.getInt(cursor.getColumnIndex(CityEntry.COLUMN_CITY_ID));
        String cityName = cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_NAME));
        float lat = cursor.getInt(cursor.getColumnIndex(CityEntry.COLUMN_LATITUDE));
        float lon = cursor.getInt(cursor.getColumnIndex(CityEntry.COLUMN_LONGITUDE));
        String countryCode = cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_COUNTRY_CODE));
        isSelectedCity = true;
        selectedCity = new City(cityId, cityName, new Coordinate(lat, lon), countryCode);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        if (args != null && !args.isEmpty()) {
            String cityFirstLetters = args.getString(CITY_FIRST_LETTERS);
            if (cityFirstLetters != null && cityFirstLetters.length() >= CITY_LETTERS_THRESHOLD) {
                switch (id) {
                    case CITY_LOADER:
                        String selection = CityEntry.COLUMN_NAME + " like " + String.format("'%s%%'", cityFirstLetters) + " and " + CityEntry.COLUMN_WATCHED + " != " + Processor.CITY_WATCH;
                        String sortOrder = CityEntry.COLUMN_NAME + " ASC " + " LIMIT " + AddCityFragment.CITY_LIST_LIMIT;
                        cursorLoader = new CursorLoader(getActivity(), CityEntry.CONTENT_URI, null, selection, null, sortOrder);
                        break;
                }
            }
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cityAdapter != null) {
            if (loader != null && !isSelectedCity) {
                cityAdapter.swapCursor(cursor);
            } else {
                cityAdapter.swapCursor(null);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // у нового loader отрабатывает onLoadFinished раньше чем у старого onLoaderReset,
        // что может сбросить результаты нового loader если не проверять loader.isAbandoned() (судя по lifeCycle)
        if (!loader.isAbandoned()) {
            cityAdapter.swapCursor(null);
        }
    }

    public interface OnAddCityFragmentInteractionListener {
        void onAddCityFragmentInteraction(City city);
    }
}
