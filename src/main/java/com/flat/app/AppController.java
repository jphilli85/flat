package com.flat.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.flat.localization.Controller;
import com.flat.localization.Model;
import com.flat.localization.coordinatesystem.LocationAlgorithm;
import com.flat.localization.signal.Signal;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;

	private static AppController sInstance;
    public static synchronized AppController getInstance() {
        return sInstance;
    }

    // Main power switch in AppServiceFragment
    private boolean enabled;

    private SharedPreferences prefs;

    private Controller controller;
    private Model model;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        controller = Controller.getInstance(this); // will populate the model.
        model = Model.getInstance();
    }

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            for (Signal s : model.getSignals()) {
                if (prefs.getBoolean(s.getName(), false)) {
                    s.enable(this);
                }
            }
            for (LocationAlgorithm la : model.getAlgorithms()) {
                if (prefs.getBoolean(la.getName(), false)) {
                    la.setEnabled(true);
                }
            }
        } else {
            for (Signal s : model.getSignals()) {
                s.disable(this);
            }
            for (LocationAlgorithm la : model.getAlgorithms()) {
                la.setEnabled(false);
            }
        }
    }
}
