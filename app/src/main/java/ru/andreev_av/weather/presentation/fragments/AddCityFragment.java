package ru.andreev_av.weather.presentation.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.arellomobile.mvp.MvpDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import javax.inject.Inject;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.presentation.App;
import ru.andreev_av.weather.presentation.adapters.CitiesAutoCompleteAdapter;
import ru.andreev_av.weather.presentation.presenters.CitiesPresenter;
import ru.andreev_av.weather.presentation.views.ICitiesView;


public class AddCityFragment extends MvpDialogFragment implements ICitiesView {

    @Inject
    @InjectPresenter
    CitiesPresenter mCitiesPresenter;

    private FrameLayout mAddCityLayout;
    private AutoCompleteTextView mAddCityAutoCompleteTextView;

    private CitiesAutoCompleteAdapter mCityAdapter;

    private City mSelectedCity;
    private boolean mIsSelectedCity;

    private OnAddCityFragmentInteractionListener mListener;

    public AddCityFragment() {
        // Required empty public constructor
    }

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        App.getInstance().getAppComponent().plusCitiesComponent().inject(this);
        super.onCreate(savedInstanceState);
        mCitiesPresenter.initPublishSubjectForFindCities();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        findComponents();

        initAdapter();

        initComponents();

        initListeners();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_add_city_title_label);
        builder.setView(mAddCityLayout)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mSelectedCity = null;
                    }
                });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button buttonPositive = (dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (mSelectedCity != null) {
                            mCitiesPresenter.loadCityToWatch(mSelectedCity);
                        }
                    }
                });
            }
        });


        mAddCityAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    @ProvidePresenter
    CitiesPresenter provideCitiesPresenter() {
        return mCitiesPresenter;
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

    @Override
    public void showCities(List<City> cities) {
        mCityAdapter.setCities(cities);
    }

    @Override
    public void processAddedCity(City city) {
        mListener.onAddCityFragmentInteraction(city);
        getDialog().dismiss();
    }

    private void findComponents() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mAddCityLayout = (FrameLayout) inflater.inflate(R.layout.fragment_add_city, null);
        mAddCityAutoCompleteTextView = (AutoCompleteTextView) mAddCityLayout.findViewById(R.id.actv_add_city);
    }

    private void initAdapter() {
        mCityAdapter = new CitiesAutoCompleteAdapter(getActivity());
    }

    private void initComponents() {
        mAddCityAutoCompleteTextView.setAdapter(mCityAdapter);
        mAddCityAutoCompleteTextView.setThreshold(CitiesPresenter.CITY_NAME_LETTERS_MIN_FOR_SEARCH);
    }

    protected void initListeners() {
        mAddCityAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // необходимо, чтобы поиск не отрабатывал сразу же после выбора города
                if (!mIsSelectedCity) {
                    mSelectedCity = null;
                    final String cityNameFirstLetters = mAddCityAutoCompleteTextView.getText().toString();
                    mCitiesPresenter.findCities(cityNameFirstLetters);
                }
                mIsSelectedCity = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mAddCityAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = (City) parent.getItemAtPosition(position);
                if (city != null) {
                    mIsSelectedCity = true;
                    mSelectedCity = city;
                    mAddCityAutoCompleteTextView.setText(mSelectedCity.toString());
                }
            }
        });
    }

    public interface OnAddCityFragmentInteractionListener {
        void onAddCityFragmentInteraction(City city);
    }
}
