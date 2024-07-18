package net.sistransito.mobile.ait;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.sistransito.mobile.ait.lister.AitLister;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.fragment.UpdateFragment;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TabAitGenerateFragment extends Fragment implements View.OnClickListener, UpdateFragment {

    private View view;
    private Button btnCheckAit, btnGenerationAit, btnCancelAit;
    private ImageView[] imgViewPhotos = new ImageView[4];
    private FrameLayout frameLayout;
    private AitData aitData;
    private String numberAitRetained, photo1Retained, photo2Retained, photo3Retained, photo4Retained;
    private int photoNumber;
    private String userChoosenTask;
    public static String pastaRoot;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Log.d("TabAitGenerateFragment", "Camera result OK");
                    onCaptureImageResult(data);
                } else {
                    Log.e("TabAitGenerateFragment", "Camera result not OK: " + result.getResultCode());
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Log.d("TabAitGenerateFragment", "Gallery result OK");
                    onSelectFromGalleryResult(data);
                } else {
                    Log.e("TabAitGenerateFragment", "Gallery result not OK: " + result.getResultCode());
                }
            });

    public static TabAitGenerateFragment newInstance() {
        return new TabAitGenerateFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("aitNumber", aitData.getAitNumber());
        outState.putString("photo1", aitData.getPhoto1());
        outState.putString("photo2", aitData.getPhoto2());
        outState.putString("photo3", aitData.getPhoto3());
        outState.putString("photo4", aitData.getPhoto4());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ait_genaration_fragment, container, false);

        if (savedInstanceState != null) {
            numberAitRetained = savedInstanceState.getString("aitNumber");
            photo1Retained = savedInstanceState.getString("photo1");
            photo2Retained = savedInstanceState.getString("photo2");
            photo3Retained = savedInstanceState.getString("photo3");
            photo4Retained = savedInstanceState.getString("photo4");
        }

        initializeViews();
        loadRetainedImages();
        getAitObject();
        addListeners();

        return view;
    }

    private void initializeViews() {
        imgViewPhotos[0] = view.findViewById(R.id.image1);
        imgViewPhotos[1] = view.findViewById(R.id.image2);
        imgViewPhotos[2] = view.findViewById(R.id.image3);
        imgViewPhotos[3] = view.findViewById(R.id.image4);

        btnCheckAit = view.findViewById(R.id.btn_check_ait);
        btnGenerationAit = view.findViewById(R.id.btn_generation_ait);
        frameLayout = view.findViewById(R.id.fragment_container_ait);
        btnCancelAit = view.findViewById(R.id.btn_discard_ait);
    }

    private void loadRetainedImages() {
        String[] photoPaths = {photo1Retained, photo2Retained, photo3Retained, photo4Retained};

        for (int i = 0; i < photoPaths.length; i++) {
            if (photoPaths[i] != null) {
                File imageFile = new File(photoPaths[i]);
                if (imageFile.exists()) {
                    imgViewPhotos[i].setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                }
            }
        }
    }

    private void getAitObject() {
        aitData = AitObject.getAitData();
    }

    private void addListeners() {
        btnCheckAit.setOnClickListener(this);
        btnGenerationAit.setOnClickListener(this);
        btnCancelAit.setOnClickListener(this);
        frameLayout.setOnClickListener(this);

        for (int i = 0; i < imgViewPhotos.length; i++) {
            int index = i;
            imgViewPhotos[i].setOnClickListener(v -> {
                photoNumber = index + 1;
                selectImage();
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_check_ait) {
            showFragment(AitPreviewFragment.newInstance());
        } else if (id == R.id.btn_generation_ait) {
            updateAitData();
        } else if (id == R.id.btn_discard_ait) {
            AnyAlertDialog.dialogView(getActivity(), getResources().getString(R.string.alert_motive), "auto");
        } else if (id == R.id.fragment_container_ait) {
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void showFragment(Fragment fragment) {
        frameLayout.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_ait, fragment).commit();
    }

    private void updateAitData() {
        if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataPhotos(aitData)) {
            Routine.showAlert(getResources().getString(R.string.update_photos), getActivity());
        }
        startActivity(new Intent(getActivity(), AitLister.class));
        getActivity().finish();
    }

    private void selectImage() {
        final CharSequence[] items = {"Câmera", "Galeria", "Cancelar"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), android.app.AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Adicionar fotos!");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Câmera")) {
                userChoosenTask = "Câmera";
                if (hasPermissions()) {
                    cameraIntent();
                } else {
                    requestPermissions();
                }
            } else if (items[item].equals("Galeria")) {
                userChoosenTask = "Galeria";
                if (hasPermissions()) {
                    galleryIntent();
                } else {
                    requestPermissions();
                }
            } else if (items[item].equals("Cancelar")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private boolean hasPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int readImagesPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES);
            return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                    readImagesPermission == PackageManager.PERMISSION_GRANTED;
        } else {
            return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                    readExternalStoragePermission == PackageManager.PERMISSION_GRANTED &&
                    writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES
                }, 100);
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                if ("Câmera".equals(userChoosenTask)) {
                    cameraIntent();
                } else if ("Galeria".equals(userChoosenTask)) {
                    galleryIntent();
                }
            } else {
                Log.e("TabAitGenerateFragment", "Permissão negada");
                Toast.makeText(getContext(), "Permissões necessárias foram negadas", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.d("TabAitGenerateFragment", "Iniciando galeria");
        galleryLauncher.launch(intent);
    }

    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("TabAitGenerateFragment", "Iniciando câmera");
        cameraLauncher.launch(cameraIntent);
    }

    private void onCaptureImageResult(Intent data) {
        if (data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            if (thumbnail != null) {
                Log.d("TabAitGenerateFragment", "Imagem capturada com sucesso");
                saveAndDisplayImage(thumbnail);
            } else {
                Log.e("TabAitGenerateFragment", "Falha ao capturar a imagem");
            }
        } else {
            Log.e("TabAitGenerateFragment", "Dados nulos ao capturar imagem");
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    Log.d("TabAitGenerateFragment", "Imagem selecionada da galeria com sucesso");
                    saveAndDisplayImage(thumbnail);
                } else {
                    Log.e("TabAitGenerateFragment", "URI de imagem nulo ao selecionar da galeria");
                }
            } catch (IOException e) {
                Log.e("TabAitGenerateFragment", "Erro ao selecionar imagem da galeria", e);
            }
        } else {
            Log.e("TabAitGenerateFragment", "Dados nulos ao selecionar imagem da galeria");
        }
    }

    private void saveAndDisplayImage(Bitmap thumbnail) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File imageFile = createImageFile();
        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            outputStream.write(bytes.toByteArray());
            Log.d("TabAitGenerateFragment", "Imagem salva com sucesso: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("TabAitGenerateFragment", "Erro ao salvar imagem", e);
        }

        displayImage(thumbnail, imageFile.getAbsolutePath());
    }

    private File createImageFile() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + getResources().getString(R.string.folder_app));
        pastaRoot = root + myDir;

        if (!myDir.exists()) {
            myDir.mkdirs();
            Log.d("TabAitGenerateFragment", "Diretório criado: " + myDir.getAbsolutePath());
        }

        String photoName = aitData.getAitNumber() + "_photo_" + photoNumber + ".jpg";
        return new File(myDir, photoName);
    }

    private void displayImage(Bitmap thumbnail, String imagePath) {
        imgViewPhotos[photoNumber - 1].setImageBitmap(thumbnail);
        saveImagePath(photoNumber, imagePath);
        if (photoNumber < 4) {
            imgViewPhotos[photoNumber].setVisibility(View.VISIBLE);
        }
    }

    private void saveImagePath(int photoNumber, String imagePath) {
        switch (photoNumber) {
            case 1:
                aitData.setPhoto1(imagePath);
                break;
            case 2:
                aitData.setPhoto2(imagePath);
                break;
            case 3:
                aitData.setPhoto3(imagePath);
                break;
            case 4:
                aitData.setPhoto4(imagePath);
                break;
        }
        saveAitImage(aitData.getAitNumber(), String.valueOf(photoNumber), imagePath);
    }

    public void saveAitImage(String ait, String local, String photo) {
        if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).insertAitDataPhotos(ait, local, photo)) {
            Routine.showAlert(getResources().getString(R.string.update_photos), getActivity());
        }
    }

    @Override
    public void Update() {
        // Implementação do método Update se necessário
    }
}
