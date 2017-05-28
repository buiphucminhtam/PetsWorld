package com.minhtam.petsworld.Util.KSOAP;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by st on 5/22/2017.
 */

public class CallPhoto {
    private final String OPERATION_NAME_INSERT = WebserviceAddress.OPERATION_INSERT_PETPHOTO;
    private final String OPERATION_NAME_GET = WebserviceAddress.OPERATION_GET_PETPHOTO;

    private final String WSDL_TARGET_NAMESPACE = WebserviceAddress.WSDL_TARGET_NAMESPACE;

    private final String SOAP_ADDRESS = WebserviceAddress.SOAP_ADDRESS;

    private final String SOAP_ACTION_INSERT = WSDL_TARGET_NAMESPACE+OPERATION_NAME_INSERT;
    private final String SOAP_ACTION_GET = WSDL_TARGET_NAMESPACE+OPERATION_NAME_GET;

    public CallPhoto() {
        super();
    }

    public String InsertPhoto(String byteArray,int userid,int petid) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_INSERT);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("byteArray");
        pi.setValue(byteArray);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("userid");
        pi.setValue(userid);
        pi.setType(Integer.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("petid");
        pi.setValue(petid);
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
            httpTransport.call(SOAP_ACTION_INSERT, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String GetPhotoById(int petId) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_GET);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("petId");
        pi.setValue(petId);
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
}
