package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class UpdateProfile extends StringRequest {

    private Map<String, String> parameters;

    public UpdateProfile(String FirstName, String LastName, String Email, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"update-profile", listener, null);
        parameters = new HashMap<>();
       // parameters.put("OldPassword ", OldPassword );
        parameters.put("FirstName",FirstName);
        parameters.put("LastName",LastName);
        parameters.put("Email",Email);
        parameters.put("Country", "1");
        parameters.put("Longitude", "1");
        parameters.put("State", "1");
        parameters.put("City", "1");
        parameters.put("Address", "1");
        parameters.put("MobileNo", "1");
        parameters.put("Latitude", "1");
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
