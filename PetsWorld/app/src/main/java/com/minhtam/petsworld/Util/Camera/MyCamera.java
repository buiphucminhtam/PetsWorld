package com.minhtam.petsworld.Util.Camera;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by st on 2/22/2017.
 */

public class MyCamera {

    public MyCamera() {
    }

    public byte[] ImageView_To_Byte(ImageView imgv) {

        //get bitmap from image view
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        int heigh = drawable.getIntrinsicHeight();
        int width = drawable.getIntrinsicWidth();

        if (heigh > 3000) {
            resizeBitmap(drawable.getBitmap(), width, 3000);
        }
        if (width > 3000) {
            resizeBitmap(drawable.getBitmap(),3000,heigh);
        }
        Bitmap bmp = drawable.getBitmap();

        //Cover it to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public Bitmap resizeBitmap(Bitmap yourBitmap, int targetW, int targetH) {
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, targetW, targetH, true);
        return resized;
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK){
//            if(data == null ) return;
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imageButtonChupHinh.setImageBitmap(bitmap);
//            Toast.makeText(getContext(), "camera", Toast.LENGTH_SHORT).show();
//        }
//        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK) {
//            if(data == null ) return;
//            Uri uri = data.getData();
//
//            imageButtonChupHinh.setImageURI(uri);
//        }
//    }


    /*Chon hinh
      Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      startActivityForResult(cameraIntent, CAMERA_REQUEST);
      */

//    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//    getIntent.setType("image/*");
//
//    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//    pickIntent.setType("image/*");
//
//    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
//    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
//
//    startActivityForResult(chooserIntent, PICK_IMAGE);


    //Get all file path image

//    public ArrayList<String> getFilePaths()
//    {
//
//
//        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Images.ImageColumns.DATA};
//        Cursor c = null;
//        SortedSet<String> dirList = new TreeSet<String>();
//        ArrayList<String> resultIAV = new ArrayList<String>();
//
//        String[] directories = null;
//        if (u != null)
//        {
//            c = managedQuery(u, projection, null, null, null);
//        }
//
//        if ((c != null) && (c.moveToFirst()))
//        {
//            do
//            {
//                String tempDir = c.getString(0);
//                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
//                try{
//                    dirList.add(tempDir);
//                }
//                catch(Exception e)
//                {
//
//                }
//            }
//            while (c.moveToNext());
//            directories = new String[dirList.size()];
//            dirList.toArray(directories);
//
//        }
//
//        for(int i=0;i<dirList.size();i++)
//        {
//            File imageDir = new File(directories[i]);
//            File[] imageList = imageDir.listFiles();
//            if(imageList == null)
//                continue;
//            for (File imagePath : imageList) {
//                try {
//
//                    if(imagePath.isDirectory())
//                    {
//                        imageList = imagePath.listFiles();
//
//                    }
//                    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
//                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
//                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
//                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
//                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
//                            )
//                    {
//
//
//
//                        String path= imagePath.getAbsolutePath();
//                        resultIAV.add(path);
//
//                    }
//                }
//                //  }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return resultIAV;
//
//
//    }

}
