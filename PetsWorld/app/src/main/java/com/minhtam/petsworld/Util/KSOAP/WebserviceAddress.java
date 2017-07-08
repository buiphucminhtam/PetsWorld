package com.minhtam.petsworld.Util.KSOAP;

/**
 * Created by st on 5/6/2017.
 */

public class WebserviceAddress
{
    public  static final String WSDL_TARGET_NAMESPACE = "http://buiphucminhtam.com/";

    public  static final String SOAP_ADDRESS = "http://192.168.1.8/C/Service1.asmx";
//    public  static final String SOAP_ADDRESS = "http://172.16.8.241/C/Service1.asmx";
    public  static final String WEB_ADDRESS = "http://192.168.1.8/C/";
//    public  static final String WEB_ADDRESS = "http://172.16.8.241/C/";


    /**OPERATION**/
    public  static final String OPERATION_LOGIN = "Login";
    public  static final String OPERATION_REGISTER = "Register";
    //USER INFO
    public  static final String OPERATION_GET_USERINFO = "GetUserInfo";
    public  static final String OPERATION_UPDATE_USERINFO  = "UpdateUserInfo";
    public  static final String OPERATION_UPDATE_USERIMAGE = "UploadUserImage";
    public  static final String OPERATION_CHANGE_PASSWORD = "ChangePassword";
    public  static final String OPERATION_LOCK_USER = "LockUser";
    public  static final String OPERATION_UNLOCK_USER = "UnlockUser";
    //PET INFO
    public  static final String OPERATION_GET_PETINFO = "GetPetInfo";
    public  static final String OPERATION_INSERT_PETINFO = "InsertPetInfo";
    public  static final String OPERATION_UPDATE_PETINFO  = "UpdatePetInfo";
    public  static final String OPERATION_DELETE_PETINFO  = "DeletePetInfo";
    //PET TYPE
    public  static final String OPERATION_GET_PETTYPE = "GetPetType";
    public  static final String OPERATION_INSERT_PETTYPE = "InsertPetType";
    public  static final String OPERATION_UPDATE_PETTYPE  = "UpdatePetType";
    public  static final String OPERATION_DELETE_PETTYPE  = "DeletePetType";
    //POST FIND OWNER
    public  static final String OPERATION_GET_POST_FINDOWNER_MAXID = "GetPostMaxIdFindOwner";
    public  static final String OPERATION_GET_POST_FINDOWNER_NEWEST = "GetPostFindOwnerNewest";
    public  static final String OPERATION_GET_POST_FINDOWNER_OLDER = "GetPostFindOwnerOlder";
    public  static final String OPERATION_GET_POST_FINDOWNER_BYUSERID = "GetPostFindOwnerByUserId";
    public  static final String OPERATION_GET_POST_FINDOWNER_BYUSERIDANDPETID = "GetPostFindOwnerByAndPetId";
    public  static final String OPERATION_UPDATE_POST_FINDOWNER = "UpdatePostFindOwner";
    public  static final String OPERATION_DELETE_POST_FINDOWNER = "DeletePostFindOwner";
    public  static final String OPERATION_INSERT_POST_FINDOWNER = "InsertPostFindOwner";

    //POST FIND PET
    public  static final String OPERATION_GET_POST_FINDPET_MAXID = "GetPostMaxIdFindPet";
    public  static final String OPERATION_GET_POST_FINDPET_NEWEST = "GetPostFindPetNewest";
    public  static final String OPERATION_GET_POST_FINDPET_OLDER = "GetPostFindPetOlder";
    public  static final String OPERATION_GET_POST_FINDPET_BYUSERID = "GetPostFindPetByUserId";
    public  static final String OPERATION_GET_POST_FINDPET_BYUSERIDANDPETID = "GetPostFindPetByUserIdAndPetId";
    public  static final String OPERATION_UPDATE_POST_FINDPET = "UpdatePostFindPet";
    public  static final String OPERATION_DELETE_POST_FINDPET = "DeletePostFindPet";
    public  static final String OPERATION_INSERT_POST_FINDPET = "InsertPostFindPet";

    //REPORT
    public  static final String OPERATION_GET_REPORT = "GetReport";
    public  static final String OPERATION_GET_REPORT_BYDATE = "GetReportByDate";
    public  static final String OPERATION_DELETE_REPORT = "DeleteReport";
    public  static final String OPERATION_INSERT_REPORT = "InsertReport";


    //PHOTO
    public  static final String OPERATION_INSERT_PETPHOTO = "UploadPetImage";
    public  static final String OPERATION_GET_PETPHOTO = "GetPhotoById";

}
