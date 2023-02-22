package net.sistransito.mobile.ait;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.sistransito.mobile.ait.lister.AitLister;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.fragment.UpdateFragment;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TabAitGenerateFragment extends DebugFragment implements
        OnClickListener, UpdateFragment {
    private View view;
    private Button btnCheckAit, btnGenerationAit, btnCancelAit;
    private ImageView imgViewPhoto1, imgViewPhoto2, imgViewPhoto3, imgViewPhoto4;
    private FrameLayout flAitContainer;
    private AitData aitData;
    private String numberAitRetained, photo1Retained, photo2Retained, photo3Retained, photo4Retained;

    private int REQUEST_CAMERA = 100, SELECT_FILE = 1;
    private String userChoosenTask;
    private int numberPhotos;
    public static String pastaRoot;

    public static TabAitGenerateFragment newInstance() {
        return new TabAitGenerateFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save instance state
        outState.putString("aitNumber", aitData.getAitNumber());
        outState.putString("photo1", aitData.getPhoto1());
        outState.putString("photo2", aitData.getPhoto2());
        outState.putString("photo3", aitData.getPhoto3());
        outState.putString("photo4", aitData.getPhoto4());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ait_genaration_fragment, null, false);

        if (savedInstanceState != null) {
            numberAitRetained = savedInstanceState.getString("aitNumber");
            photo1Retained = savedInstanceState.getString("photo1");
            photo2Retained = savedInstanceState.getString("photo2");
            photo3Retained = savedInstanceState.getString("photo3");
            photo4Retained = savedInstanceState.getString("photo4");

            imgViewPhoto2.setVisibility(View.VISIBLE);
            imgViewPhoto3.setVisibility(View.VISIBLE);
            imgViewPhoto4.setVisibility(View.VISIBLE);

        }

        initializedView();
        getAitObject();
        return view;
    }

    private void addListener() {

        btnCheckAit.setOnClickListener(this);
        btnGenerationAit.setOnClickListener(this);
        btnCancelAit.setOnClickListener(this);
        flAitContainer.setOnClickListener(this);

        imgViewPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPhotos = 1;
                selectImage();
            }
        });
        imgViewPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPhotos = 2;
                selectImage();
            }
        });
        imgViewPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPhotos = 3;
                selectImage();
            }
        });
        imgViewPhoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPhotos = 4;
                selectImage();
            }
        });

    }

    private void getAitObject() {
        aitData = ObjectAit.getAitData();
        if (aitData.isDataisNull()) {
            addListener();
        } else if (aitData.isStoreFullData()) {
            addListener();
        } else {
            addListener();
        }
    }

    private void initializedView() {

        imgViewPhoto1 = (ImageView) view.findViewById(R.id.image1);
        imgViewPhoto2 = (ImageView) view.findViewById(R.id.image2);
        imgViewPhoto3 = (ImageView) view.findViewById(R.id.image3);
        imgViewPhoto4 = (ImageView) view.findViewById(R.id.image4);

        btnCheckAit = (Button) view.findViewById(R.id.btn_check_ait);
        btnGenerationAit = (Button) view.findViewById(R.id.btn_generation_ait);
        flAitContainer = (FrameLayout) view.findViewById(R.id.fragment_container_ait);
        btnCancelAit = (Button) view.findViewById(R.id.btn_discard_ait);

        imgViewPhoto2.setVisibility(View.GONE);
        imgViewPhoto3.setVisibility(View.GONE);
        imgViewPhoto4.setVisibility(View.GONE);

        if(numberAitRetained != null) {

            if(photo1Retained != null) {
                File imageFile1 = new File(photo1Retained);
                imgViewPhoto1.setImageBitmap(BitmapFactory.decodeFile(imageFile1.getAbsolutePath()));
            }

            if(photo2Retained != null) {
                File imageFile2 = new File(photo2Retained);
                imgViewPhoto2.setImageBitmap(BitmapFactory.decodeFile(imageFile2.getAbsolutePath()));
            }

            if(photo3Retained != null) {
                File imageFile3 = new File(photo3Retained);
                imgViewPhoto3.setImageBitmap(BitmapFactory.decodeFile(imageFile3.getAbsolutePath()));
            }

            if(photo4Retained != null) {
                File imageFile4 = new File(photo4Retained);
                imgViewPhoto4.setImageBitmap(BitmapFactory.decodeFile(imageFile4.getAbsolutePath()));
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_ait:
                flAitContainer.setVisibility(view.VISIBLE);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_ait, AitPreviewFragment.newInstance()).commit();
                break;
            case R.id.btn_generation_ait:
                if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataPhotos(aitData))
                    Routine.showAlert(getResources().getString(R.string.update_fotos), getActivity());
                startActivity(new Intent(getActivity(), AitLister.class));
                getActivity().finish();
                break;
            case R.id.btn_discard_ait:
                AnyAlertDialog.dialogView(getActivity(), this.getResources().getString(R.string.titulo_cancelar), "auto");
                break;
            case R.id.fragment_container_ait:
                flAitContainer.setVisibility(view.GONE);
                break;
        }
    }

    @Override
    public void Update() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Câmera"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Galeria"))
                        galleryIntent();
                } else {

                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Câmera", "Galeria",
                "Cancelar" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(),
                android.app.AlertDialog.THEME_HOLO_LIGHT);

        builder.setTitle("Adicionar fotos!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getContext());

                if (items[item].equals("Câmera")) {
                    userChoosenTask ="Câmera";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Galeria")) {
                    userChoosenTask ="Galeria";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityFromFragment(TabAitGenerateFragment.this, Intent.createChooser(intent, "Selecionar arquivo"), SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityFromFragment(TabAitGenerateFragment.this, cameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        if (data != null){
            Bundle bundle = data.getExtras();
            if (bundle != null){
                Bitmap thumbnail = (Bitmap) bundle.get("data");
                namedPhoto(thumbnail, data);
            }
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap thumbnail = null;
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        namedPhoto(thumbnail, data);

    }

    private void namedPhoto(Bitmap thumbnail, Intent data){

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        //Log.i("Photo name : ", String.valueOf(thumbnail));

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + getResources().getString(R.string.folder_app));
        pastaRoot = root + myDir;

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fName = aitData.getAitNumber() + "_photo_" + numberPhotos + ".jpg";
        String fullName = myDir + "/" + fName;
        File destination = new File(myDir, fName);

        //Log.i("Photo", root + "- full way: " + fullName + " - Ait: " + dadosAuto.getNumeroAuto());

        FileOutputStream outputStream;

        try {
            destination.createNewFile();
            outputStream = new FileOutputStream(destination);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(numberPhotos == 1){
            imgViewPhoto1.setImageBitmap(thumbnail);
            aitData.setPhoto1(fullName);
            saveAitImage(aitData.getAitNumber(),"1", fullName);
            imgViewPhoto2.setVisibility(View.VISIBLE);
        } else if(numberPhotos == 2){
            imgViewPhoto2.setImageBitmap(thumbnail);
            aitData.setPhoto2(fullName);
            saveAitImage(aitData.getAitNumber(),"2", fullName);
            imgViewPhoto3.setVisibility(View.VISIBLE);
        } else if(numberPhotos == 3){
            imgViewPhoto3.setImageBitmap(thumbnail);
            aitData.setPhoto3(fullName);
            saveAitImage(aitData.getAitNumber(),"3", fullName);
            imgViewPhoto4.setVisibility(View.VISIBLE);
        } else {
            imgViewPhoto4.setImageBitmap(thumbnail);
            aitData.setPhoto4(fullName);
            saveAitImage(aitData.getAitNumber(),"4", fullName);
        }

        compressBitmapToFile(destination);

      /*  Uri selectedImage = data.getData();
        String filePathColumn = MediaStore.Images.Media.DATA;

        File photo = new File(filePathColumn + "/" + selectedImage);

        Log.d("Foto: 310 ", String.valueOf(photo));
        //photo.delete();*/

    }

    public File compressBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, options1);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;

        } catch (Exception e) {
            return null;
        }

    }

    public void saveAitImage(String ait, String local, String photo){

        if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).insertAitDataPhotos(ait, local, photo))
            Routine.showAlert(getResources().getString(R.string.update_fotos), getActivity());

    }

}
