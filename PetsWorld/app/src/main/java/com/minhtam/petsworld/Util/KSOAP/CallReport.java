package com.minhtam.petsworld.Util.KSOAP;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by st on 6/21/2017.
 */

public class CallReport {
    private final String OPERATION_NAME_GET = WebserviceAddress.OPERATION_GET_REPORT;
    private final String OPERATION_NAME_GET_BYDATE = WebserviceAddress.OPERATION_GET_REPORT_BYDATE;
    private final String OPERATION_NAME_INSERT = WebserviceAddress.OPERATION_INSERT_REPORT;
    private final String OPERATION_NAME_DELETE = WebserviceAddress.OPERATION_DELETE_REPORT;

    private final String WSDL_TARGET_NAMESPACE = WebserviceAddress.WSDL_TARGET_NAMESPACE;

    private final String SOAP_ADDRESS = WebserviceAddress.SOAP_ADDRESS;

    private final String SOAP_ACTION_GET = WSDL_TARGET_NAMESPACE+OPERATION_NAME_GET;
    private final String SOAP_ACTION_GET_BYDATE = WSDL_TARGET_NAMESPACE+OPERATION_NAME_GET_BYDATE;
    private final String SOAP_ACTION_INSERT = WSDL_TARGET_NAMESPACE+OPERATION_NAME_INSERT;
    private final String SOAP_ACTION_DELETE = WSDL_TARGET_NAMESPACE+OPERATION_NAME_DELETE;


    public CallReport() {
        super();
    }

    public String Get() {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_GET);

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

    public int Insert(String jsonReport) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_INSERT);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("jsonReport");
        pi.setValue(jsonReport);
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

    public String GetReportByDate(String date, int type) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_GET_BYDATE);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("date");
        pi.setValue(date);
        pi.setType(String.class);
        request.addProperty(pi);

        pi= new PropertyInfo();
        pi.setName("type");
        pi.setValue(type);
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
            httpTransport.call(SOAP_ACTION_GET_BYDATE, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
}
