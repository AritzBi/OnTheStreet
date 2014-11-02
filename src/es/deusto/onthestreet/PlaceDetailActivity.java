package es.deusto.onthestreet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class PlaceDetailActivity extends Activity{
	public static final String ITEM_DESCRIPTION = "item_description";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// If item description provided, fill the EditText widget 
		EditText edtDescription = ((EditText) findViewById(R.id.edtItemDescription));
		String value = getIntent().getStringExtra(ITEM_DESCRIPTION);
		if(value != null)
			edtDescription.setText(value);
	}



}
