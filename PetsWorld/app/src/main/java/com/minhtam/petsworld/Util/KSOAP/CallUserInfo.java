package com.minhtam.petsworld.Util.KSOAP;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by st on 5/9/2017.
 */

public class CallUserInfo {
    public  final String OPERATION_NAME_GET = WebserviceAddress.OPERATION_GET_USERINFO;
    public  final String OPERATION_NAME_UPDATE = WebserviceAddress.OPERATION_UPDATE_USERINFO;

    public  final String WSDL_TARGET_NAMESPACE = WebserviceAddress.WSDL_TARGET_NAMESPACE;

    public  final String SOAP_ADDRESS = WebserviceAddress.SOAP_ADDRESS;

    public final String SOAP_ACTION_GET = WSDL_TARGET_NAMESPACE+OPERATION_NAME_GET;
    public final String SOAP_ACTION_UPDATE = WSDL_TARGET_NAMESPACE+OPERATION_NAME_UPDATE;

    public CallUserInfo() {
        super();
    }

    public String CallGet(int id) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_GET);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("id");
        pi.setValue(id);
        pi.setType(Integer.class);
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
            httpTransport.call(SOAP_ACTION_GET, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public int CallUpdate(String jsonUserInfo) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_UPDATE);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("jsonUserInfo");
        pi.setValue(jsonUserInfo);
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
            httpTransport.call(SOAP_ACTION_UPDATE, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return Integer.parseInt(response.toString());
    }
}
