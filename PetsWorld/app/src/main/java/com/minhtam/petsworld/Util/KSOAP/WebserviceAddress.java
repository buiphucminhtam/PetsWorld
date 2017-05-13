package com.minhtam.petsworld.Util.KSOAP;

/**
 * Created by st on 5/6/2017.
 */

public class WebserviceAddress
{
    public  static final String WSDL_TARGET_NAMESPACE = "http://buiphucminhtam.com/";

    public  static final String SOAP_ADDRESS = "http://192.168.1.2/C/Service1.asmx";



    /**OPERATION**/
    public  static final String OPERATION_LOGIN = "Login";
    public  static final String OPERATION_REGISTER = "Register";
    //USER INFO
    public  static final String OPERATION_GET_USERINFO = "GetUserInfo";
    public  static final String OPERATION_UPDATE_USERINFO  = "UpdateUserInfo";
    public  static final String OPERATION_UPDATE_USERIMAGE = "UploadUserImage";
    //PET INFO
    public  static final String OPERATION_GET_PETINFO = "GetPetInfo";
    public  static final String OPERATION_INSERT_PETINFO = "InsertPetInfo";
    public  static final String OPERATION_UPDATE_PETINFO  = "UpdatePetInfo";
    public  static final String OPERATION_DELETE_PETINFO  = "DeletePetInfo";
    //POST FIND OWNER
    public  static final String OPERATION_GET_POST_FINDOWNER_MAXID = "GetPostMaxIdFindOwner";
    public  static final String OPERATION_GET_POST_FINDOWNER_NEWEST = "GetPostFindOwnerNewest";
    public  static final String OPERATION_GET_POST_FINDOWNER_OLDER = "GetPostFindOwnerOlder";
    public  static final String OPERATION_UPDATE_POST_FINDOWNER = "UpdatePostFindOwner";
    public  static final String OPERATION_DELETE_POST_FINDOWNER = "DeletePostFindOwner";
    public  static final String OPERATION_INSERT_POST_FINDOWNER = "InsertPostFindOwner";
}
