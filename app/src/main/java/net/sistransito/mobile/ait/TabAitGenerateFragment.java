package net.sistransito.mobile.ait;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sistransito.mobile.ait.lister.AitLister;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.fragment.UpdateFragment;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TabAitGenerateFragment extends Fragment implements View.OnClickListener, UpdateFragment, PhotoAdapter.OnPhotoClickListener {

    private static final int MAX_PHOTOS = 6;

    private View view;
    private Button btnCheckAit, btnGenerationAit, btnCancelAit, btnAddPhoto;
    private RecyclerView photoRecyclerView;
    private PhotoAdapter photoAdapter;
    private AitData aitData;
    private String userChoosenTask;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ait_genaration_fragment, container, false);
        initializeViews();
        getAitObject();
        setupRecyclerView();
        addListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadExistingPhotos();
    }

    private void initializeViews() {
        btnAddPhoto = view.findViewById(R.id.btn_add_photo);
        photoRecyclerView = view.findViewById(R.id.photo_recycler_view);
        btnCheckAit = view.findViewById(R.id.btn_check_ait);
        btnGenerationAit = view.findViewById(R.id.btn_generation_ait);
        btnCancelAit = view.findViewById(R.id.btn_discard_ait);
    }

    private void setupRecyclerView() {
        photoAdapter = new PhotoAdapter(getContext(), this);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        photoRecyclerView.setAdapter(photoAdapter);
    }

    private void loadExistingPhotos() {
        if (aitData != null && aitData.getAitNumber() != null) {
            List<String> photoPaths = DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).getAitPhotos(aitData.getAitNumber());
            photoAdapter.setPhotoPaths(photoPaths);
            Log.d("TabAitGenerateFragment", "Fotos carregadas para aitNumber: " + aitData.getAitNumber());
        } else {
            Log.w("TabAitGenerateFragment", "aitData ou aitNumber é null, definindo lista vazia");
            photoAdapter.setPhotoPaths(new ArrayList<>());
        }
    }

    private void getAitObject() {
        aitData = AitObject.getAitData();
        if (aitData == null) {
            aitData = new AitData();
            AitObject.setAitData(aitData);
            Log.w("TabAitGenerateFragment", "aitData era null, inicializado como novo");
        }
    }

    private void addListeners() {
        btnAddPhoto.setOnClickListener(this);
        btnCheckAit.setOnClickListener(this);
        btnGenerationAit.setOnClickListener(this);
        btnCancelAit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add_photo) {
            selectImage();
        } else if (id == R.id.btn_check_ait) {
            showPreviewDialog();
        } else if (id == R.id.btn_generation_ait) {
            updateAitData();
        } else if (id == R.id.btn_discard_ait) {
            AnyAlertDialog.dialogView(getActivity(), getResources().getString(R.string.alert_motive), "auto");
        }
    }

    private void showPreviewDialog() {
        AitPreviewDialogFragment dialogFragment = AitPreviewDialogFragment.newInstance();
        dialogFragment.show(getParentFragmentManager(), "AitPreviewDialog");
    }

    private void updateAitData() {
        if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitData(aitData)) {
            Routine.showAlert(getResources().getString(R.string.update_photos), getActivity());
        }
        startActivity(new Intent(getActivity(), AitLister.class));
        getActivity().finish();
    }

    @Override
    public void onAddPhotoClick() {
        selectImage();
    }

    @Override
    public void onDeletePhotoClick(int position) {
        if (position >= 0 && position < photoAdapter.photoPaths.size()) {
            String photoPath = photoAdapter.photoPaths.get(position);
            File file = new File(photoPath);
            if (file.exists() && file.delete()) {
                Log.d("TabAitGenerateFragment", "Arquivo deletado: " + photoPath);
            } else {
                Log.e("TabAitGenerateFragment", "Falha ao deletar arquivo: " + photoPath);
            }
            DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).deleteAitPhoto(aitData.getAitNumber(), photoPath);
            photoAdapter.removePhoto(position);
        } else {
            Log.e("TabAitGenerateFragment", "Índice inválido ao tentar deletar foto: " + position);
        }
    }

    @Override
    public void onPhotoClick(int position) {
        if (position >= 0 && position < photoAdapter.photoPaths.size()) {
            String photoPath = photoAdapter.photoPaths.get(position);
            showFullSizeImage(photoPath);
        } else {
            Log.e("TabAitGenerateFragment", "Índice inválido ao tentar exibir foto: " + position);
        }
    }

    private void showFullSizeImage(String photoPath) {
        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_full_image);

        ImageView fullImageView = dialog.findViewById(R.id.full_image_view);
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (bitmap != null) {
            fullImageView.setImageBitmap(bitmap);
        } else {
            Log.e("TabAitGenerateFragment", "Falha ao carregar bitmap da imagem: " + photoPath);
            Toast.makeText(getContext(), "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }

        fullImageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private boolean canAddMorePhotos() {
        int currentPhotoCount = photoAdapter.getItemCount();
        if (currentPhotoCount >= MAX_PHOTOS) {
            Toast.makeText(getContext(), "Limite de " + MAX_PHOTOS + " fotos atingido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void selectImage() {
        if (!canAddMorePhotos()) {
            return;
        }

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
        if (data != null && data.getExtras() != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            if (thumbnail != null) {
                Log.d("TabAitGenerateFragment", "Imagem capturada com sucesso");
                saveAndDisplayImage(thumbnail);
            } else {
                Log.e("TabAitGenerateFragment", "Falha ao capturar a imagem: thumbnail é null");
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
        if (aitData != null && aitData.getAitNumber() != null) {
            File imageFile = createImageFile(photoAdapter.getItemCount() + 1);
            try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                String photoPath = imageFile.getAbsolutePath();
                photoAdapter.addPhoto(photoPath);
                saveAitImage(aitData.getAitNumber(), photoPath);
                Log.d("TabAitGenerateFragment", "Imagem salva e exibida: " + photoPath);
            } catch (IOException e) {
                Log.e("TabAitGenerateFragment", "Erro ao salvar imagem: " + e.getMessage(), e);
            }
        } else {
            Log.e("TabAitGenerateFragment", "aitData ou aitNumber é null, não salvando imagem");
            Toast.makeText(getContext(), "Erro: Número do AIT não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile(int photoIndex) {
        File myDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (myDir != null && !myDir.exists()) {
            if (myDir.mkdirs()) {
                Log.d("TabAitGenerateFragment", "Diretório criado: " + myDir.getAbsolutePath());
            } else {
                Log.e("TabAitGenerateFragment", "Falha ao criar diretório: " + myDir.getAbsolutePath());
            }
        }
        String photoName = aitData.getAitNumber() + "_photo_" + photoIndex + ".jpg";
        return new File(myDir, photoName);
    }

    private void saveAitImage(String aitNumber, String photoPath) {
        if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).insertAitPhoto(aitNumber, photoPath)) {
            Routine.showAlert(getResources().getString(R.string.update_photos), getActivity());
            Log.e("TabAitGenerateFragment", "Falha ao salvar imagem no banco de dados");
        } else {
            Log.d("TabAitGenerateFragment", "Imagem salva no banco de dados: " + photoPath);
        }
    }

    @Override
    public void Update() {
        // Implementação do método Update se necessário
    }
}