package com.minhtam.petsworld.Util.KSOAP;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by st on 5/20/2017.
 */

public class CallPetType {
    private final String OPERATION_NAME_GET = WebserviceAddress.OPERATION_GET_PETTYPE;
    private final String OPERATION_NAME_INSERT = WebserviceAddress.OPERATION_INSERT_PETTYPE;
    private final String OPERATION_NAME_UPDATE = WebserviceAddress.OPERATION_UPDATE_PETTYPE;
    private final String OPERATION_NAME_DELETE = WebserviceAddress.OPERATION_DELETE_PETTYPE;

    private final String WSDL_TARGET_NAMESPACE = WebserviceAddress.WSDL_TARGET_NAMESPACE;

    private final String SOAP_ADDRESS = WebserviceAddress.SOAP_ADDRESS;

    private final String SOAP_ACTION_GET = WSDL_TARGET_NAMESPACE+OPERATION_NAME_GET;
    private final String SOAP_ACTION_INSERT = WSDL_TARGET_NAMESPACE+OPERATION_NAME_INSERT;
    private final String SOAP_ACTION_UPDATE = WSDL_TARGET_NAMESPACE+OPERATION_NAME_UPDATE;
    private final String SOAP_ACTION_DELETE = WSDL_TARGET_NAMESPACE+OPERATION_NAME_DELETE;


    public CallPetType() {
        super();
    }

    public String Get(int id) {
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

    public int Insert(String jsonPetType) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_INSERT);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("jsonPetType");
        pi.setValue(jsonPetType);
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
            httpTransport.call(SOAP_ACTION_INSERT, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return Integer.parseInt(response.toString());
    }

    public int Update(String jsonPetType) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_UPDATE);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("jsonPetType");
        pi.setValue(jsonPetType);
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

    public int Delete(int id) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_DELETE);
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
            httpTransport.call(SOAP_ACTION_DELETE, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return Integer.parseInt(response.toString());
    }
}
