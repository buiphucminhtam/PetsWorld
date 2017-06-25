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

public class CallPostFindOwner {

    public  final String OPERATION_GET_MAXID    = WebserviceAddress.OPERATION_GET_POST_FINDOWNER_MAXID;
    public  final String OPERATION_GET_OLDER    = WebserviceAddress.OPERATION_GET_POST_FINDOWNER_OLDER;
    public  final String OPERATION_GET_NEWEST   = WebserviceAddress.OPERATION_GET_POST_FINDOWNER_NEWEST;
    public  final String OPERATION_GET_BYUSERID   = WebserviceAddress.OPERATION_GET_POST_FINDOWNER_BYUSERID;
    public  final String OPERATION_GET_BYUSERIDANDPETID   = WebserviceAddress.OPERATION_GET_POST_FINDOWNER_BYUSERIDANDPETID;
    public  final String OPERATION_INSERT       = WebserviceAddress.OPERATION_INSERT_POST_FINDOWNER;
    public  final String OPERATION_UPDATE       = WebserviceAddress.OPERATION_UPDATE_POST_FINDOWNER;
    public  final String OPERATION_DELETE       = WebserviceAddress.OPERATION_DELETE_POST_FINDOWNER;

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

    public CallPostFindOwner() {
        super();
    }

    public String GetPostMaxIdFindOwner() {
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

    public String GetPostFindOwnerNewest(int id) {
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
    public String GetPostFindOwnerOlder(int id) {
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
    public int    UpdatePostFindOwner(String jsonPost) {
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
    public int    InsertPostFindOwner(String jsonPost) {
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
    public int    DeletePostFindOwner(int id) {
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

    public String GetPostFindOwnerByUserId(int userId) {
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

    public String GetPostFindOwnerByAndPetId(int userid, int petid) {
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
