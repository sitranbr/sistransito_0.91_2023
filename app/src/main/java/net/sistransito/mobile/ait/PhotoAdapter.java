package net.sistransito.mobile.ait;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.sistransito.R;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    protected List<String> photoPaths;
    private final Context context;
    private final OnPhotoClickListener listener;

    public interface OnPhotoClickListener {
        void onAddPhotoClick();
        void onDeletePhotoClick(int position);
        void onPhotoClick(int position);
    }

    public PhotoAdapter(Context context, OnPhotoClickListener listener) {
        this.context = context;
        this.photoPaths = new ArrayList<>();
        this.listener = listener;
    }

    public void setPhotoPaths(List<String> paths) {
        this.photoPaths = paths != null ? paths : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addPhoto(String path) {
        photoPaths.add(path);
        notifyItemInserted(photoPaths.size() - 1);
    }

    public void removePhoto(int position) {
        if (position >= 0 && position < photoPaths.size()) {
            photoPaths.remove(position);
            if (photoPaths.isEmpty()) {
                notifyDataSetChanged(); // Atualiza tudo se a lista ficar vazia
            } else {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, photoPaths.size()); // Ajusta os índices restantes
            }
        } else {
            Log.e("PhotoAdapter", "Índice inválido ao remover foto: " + position);
        }
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        if (position >= 0 && position < photoPaths.size()) { // Validação adicional
            String photoPath = photoPaths.get(position);
            holder.photoImageView.setImageBitmap(BitmapFactory.decodeFile(photoPath));
            holder.deleteIcon.setOnClickListener(v -> listener.onDeletePhotoClick(position));
            holder.photoImageView.setOnClickListener(v -> listener.onPhotoClick(position)); // Clique para exibir imagem ampliada
        } else {
            Log.e("PhotoAdapter", "Posição inválida no onBindViewHolder: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return photoPaths.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        ImageView deleteIcon;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photo_image_view);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }
}