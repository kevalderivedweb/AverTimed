package com.purchase.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class SetShippingAddressRequest extends StringRequest {

    private Map<String, String> parameters;

    public SetShippingAddressRequest(String name, String last, String ad1, String ad2, String state, String contry, String pin,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"update-shipping-address", listener, null);

        parameters = new HashMap<>();
        parameters.put("FirstName", name );
        parameters.put("LastName", last);
        parameters.put("ShippingAddress", ad1);
        parameters.put("ShippingCity", ad2);
        parameters.put("ShippingState", state);
        parameters.put("ShippingCountry", contry);
        parameters.put("ShippingPinCode", pin);
        parameters.put("ShippingLatitude", "5.66");
        parameters.put("ShippingLongitude", "7.66");
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
