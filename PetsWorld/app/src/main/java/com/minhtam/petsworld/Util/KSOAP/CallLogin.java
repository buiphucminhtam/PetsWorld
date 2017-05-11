package com.minhtam.petsworld.Util.KSOAP;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by st on 5/6/2017.
 */

public class CallLogin {


    public  final String OPERATION_NAME = WebserviceAddress.OPERATION_LOGIN;

    public  final String WSDL_TARGET_NAMESPACE = WebserviceAddress.WSDL_TARGET_NAMESPACE;

    public  final String SOAP_ADDRESS = WebserviceAddress.SOAP_ADDRESS;

    public final String SOAP_ACTION = WSDL_TARGET_NAMESPACE+OPERATION_NAME;

    public CallLogin() {
        super();
    }

    public String Call(String username,String password) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("userName");
        pi.setValue(username);
        pi.setType(String.class);
        request.addProperty(pi);
        pi=new PropertyInfo();
        pi.setName("passWord");
        pi.setValue(password);
        pi.setType(String.class);
        request.addProperty(pi);

        Log.d("request",request.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
}
