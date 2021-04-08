package com.purchase.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.purchase.avertimed.FavDatabaseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class CreateOrderRequest extends StringRequest {

    private Map<String, String> parameters;

    public CreateOrderRequest(String ShippingAddressID,
                              String CouponID,
                              String OrderAmount,
                              String Tax,
                              String Discount,
                              String ShippingCharge,
                              String TotalAmount,
                              List<FavDatabaseModel> databaseModels, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"create-order", listener, null);
        parameters = new HashMap<>();


        parameters.put("ShippingAddressID", ShippingAddressID);
        parameters.put("CouponID", CouponID);
        parameters.put("OrderAmount", OrderAmount);
        parameters.put("Tax", Tax);
        parameters.put("Discount", Discount);
        parameters.put("ShippingCharge", ShippingCharge);
        parameters.put("TotalAmount", TotalAmount);

        for(int i =0 ; i<databaseModels.size();i++){
            parameters.put("ProductID[]", databaseModels.get(i).getId());
            parameters.put("Qty[]", databaseModels.get(i).getQT());
        }

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
