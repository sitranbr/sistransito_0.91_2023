package net.sistransito.mobile.ait;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.DialogFragment;
import net.sistransito.R;

public class AitPreviewDialogFragment extends DialogFragment {

    public static AitPreviewDialogFragment newInstance() {
        return new AitPreviewDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_preview_container, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.dialog_frame_container);

        // Configura o listener para fechar o diálogo ao clicar fora
        frameLayout.setOnClickListener(v -> dismiss());

        // Adiciona o AitPreviewFragment ao FrameLayout
        getChildFragmentManager().beginTransaction()
                .replace(R.id.dialog_frame_container, AitPreviewFragment.newInstance())
                .commit();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}