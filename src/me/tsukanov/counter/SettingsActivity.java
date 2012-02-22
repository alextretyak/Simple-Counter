package me.tsukanov.counter;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class SettingsActivity extends SherlockPreferenceActivity implements
		OnPreferenceChangeListener {

	CounterApplication app = null;
	Intent starterIntent = null;
	ActionBar actionBar = null;

	ListPreference themePreference = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(CounterApplication.theme);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		app = (CounterApplication) getApplication();
		starterIntent = getIntent();

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		PreferenceScreen prefScreen = getPreferenceScreen();
		themePreference = (ListPreference) prefScreen.findPreference("theme");
		themePreference.setOnPreferenceChangeListener(this);

		String version = (String) getResources().getText(R.string.unknown);
		try {
			version = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Preference versionPref = findPreference("version");
		versionPref.setSummary(version);

		getPreferenceManager().findPreference("clearData")
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						app.counters.clear();
						app.isUpdateNeeded = true;
						Toast.makeText(getBaseContext(),
								getResources().getText(R.string.toast_wipe_success),
								Toast.LENGTH_SHORT).show();
						return true;
					}
				});
		
		Log.v("Activities", "Settings activity created");
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == themePreference) {
			app.isUpdateNeeded = true;
			String value = (String) newValue;
			if (value.equals("dark")) {
				CounterApplication.theme = R.style.Theme_Sherlock;
			} else if (value.equals("light")) {
				CounterApplication.theme = R.style.Theme_Sherlock_Light_DarkActionBar;
			}
			// Restarting activity to activate selected theme in it
			finish();
			startActivity(starterIntent);
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}