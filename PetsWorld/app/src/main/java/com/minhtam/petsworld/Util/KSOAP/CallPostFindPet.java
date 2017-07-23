package com.minhtam.petsworld.Util.KSOAP;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by st on 6/14/2017.
 */

public class CallPostFindPet {
    public  final String OPERATION_GET_MAXID    	= WebserviceAddress.OPERATION_GET_POST_FINDPET_MAXID;
    public  final String OPERATION_GET_OLDER    = WebserviceAddress.OPERATION_GET_POST_FINDPET_OLDER;
    public  final String OPERATION_GET_NEWEST   = WebserviceAddress.OPERATION_GET_POST_FINDPET_NEWEST;
    public  final String OPERATION_GET_BYUSERID = WebserviceAddress.OPERATION_GET_POST_FINDPET_BYUSERID;
    public  final String OPERATION_GET_BYUSERIDANDPETID = WebserviceAddress.OPERATION_GET_POST_FINDPET_BYUSERIDANDPETID;
    public  final String OPERATION_INSERT       = WebserviceAddress.OPERATION_INSERT_POST_FINDPET;
    public  final String OPERATION_UPDATE       = WebserviceAddress.OPERATION_UPDATE_POST_FINDPET;
    public  final String OPERATION_DELETE       = WebserviceAddress.OPERATION_DELETE_POST_FINDPET;

    public  final String WSDL_TARGET_NAMESPACE  = WebserviceAddress.WSDL_TARGET_NAMESPACE;

    public  final String SOAP_ADDRESS           = WebserviceAddress.SOAP_ADDRESS;

    public  final String SOAP_ACTION_GETMAXID    = WSDL_TARGET_NAMESPACE +   OPERATION_GET_MAXID;
    public  final String SOAP_ACTION_GETOLDER    = WSDL_TARGET_NAMESPACE +   OPERATION_GET_OLDER;
    public  final String SOAP_ACTION_GETNEWEST   = WSDL_TARGET_NAMESPACE +   OPERATION_GET_NEWEST;
    public  final String SOAP_ACTION_GETBYUSERID   = WSDL_TARGET_NAMESPACE +   OPERATION_GET_BYUSERID;
    public  final String SOAP_ACTION_GETBYUSERIDANDPETID   = WSDL_TARGET_NAMESPACE +   OPERATION_GET_BYUSERIDANDPETID;
    public  final String SOAP_ACTION_INSERT      = WSDL_TARGET_NAMESPACE +   OPERATION_INSERT;
    public  final String SOAP_ACTION_UPDATE      = WSDL_TARGET_NAMESPACE +   OPERATION_UPDATE;
    public  final String SOAP_ACTION_DELETE      = WSDL_TARGET_NAMESPACE +   OPERATION_DELETE;

    public CallPostFindPet() {
        super();
    }

    public String GetPostMaxIdFindPet() {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_GET_MAXID);

        Log.d("request",request.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.skipNullProperties = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION_GETMAXID, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String GetPostFindPetNewest(int id) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_GET_NEWEST);
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
            httpTransport.call(SOAP_ACTION_GETNEWEST, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
    public String GetPostFindPetOlder(int id) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_GET_OLDER);
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
            httpTransport.call(SOAP_ACTION_GETOLDER, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
    public int UpdatePostFindPet(String jsonPost) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_UPDATE);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("jsonPost");
        pi.setValue(jsonPost);
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
    public int    InsertPostFindPet(String jsonPost) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_INSERT);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("jsonPost");
        pi.setValue(jsonPost);
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
    public int DeletePostFindPet(int id) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_DELETE);
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

    public String GetPostFindPetByUserId(int userId) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_GET_BYUSERID);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("userId");
        pi.setValue(userId);
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
            httpTransport.call(SOAP_ACTION_GETBYUSERID, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String GetPostFindPetByAndPetId(int userid, int petid) {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_GET_BYUSERIDANDPETID);
        PropertyInfo pi=new PropertyInfo();
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
            httpTransport.call(SOAP_ACTION_GETBYUSERIDANDPETID, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
}
