package com.uberprinny.geocoderdemo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Forward Geocoder means [location description] -> [coordinates].
 */
public class ForwardGeocoderFragment extends Fragment {

    public interface OnForwardGeocoderListener {
        void onForwardGeocoderFinished(String description, List<Address> addresses);
    }

    private OnForwardGeocoderListener mListener;

    private static final String ARG_LOCATION_DESCRIPTION = "location_description";

    /**
     * @param locationDescription Description of location (like address, city, state, ZIP, etc.).
     * @return A new instance of fragment ForwardGeocoderFragment.
     */
    public static ForwardGeocoderFragment newInstance(String locationDescription) {
        ForwardGeocoderFragment fragment = new ForwardGeocoderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION_DESCRIPTION, locationDescription);
        fragment.setArguments(args);
        return fragment;
    }

    public ForwardGeocoderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocationDescription = getArguments().getString(ARG_LOCATION_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forward_geocoder, container, false);

        mDescriptionInput = (EditText) v.findViewById(R.id.forward_description_input);
        mDescriptionInput.setText(mLocationDescription);

        Button UiThreadButton = (Button) v.findViewById(R.id.forward_uithread_button);
        UiThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                doForwardGeocoder(mDescriptionInput.getText().toString());
            }
        });

        Button AsyncButton = (Button) v.findViewById(R.id.forward_async_button);
        AsyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Add AsyncTask
            }
        });

        return v;
    }

    private String mLocationDescription;

    private EditText mDescriptionInput;

    private static final int MAX_RESULTS = 5;

    private void doForwardGeocoder(String locationDescription) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationDescription, MAX_RESULTS);

            if (addresses != null && addresses.size() > 0) {
                onForwardGeocoderSuccess(locationDescription, addresses);
            } else {
                Toast.makeText(getContext(), "Couldn\'t find any locations with that address.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {

        }
    }

    public void onForwardGeocoderSuccess(String description, List<Address> addresses) {
        mLocationDescription = description;
        if (mListener != null) {
            mListener.onForwardGeocoderFinished(description, addresses);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnForwardGeocoderListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnForwardGeocoderListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
