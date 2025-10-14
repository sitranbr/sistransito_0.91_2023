package net.sistransito.mobile.setting;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.rey.material.widget.CheckBox;

import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.SettingDatabaseAdapter;
import net.sistransito.mobile.database.SetttingDatabaseHelper;
import net.sistransito.mobile.sync.SyncDataInformation;
import net.sistransito.mobile.network.NetworkConnection;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.R;

public class SettingFragment extends Fragment implements
        OnCheckedChangeListener, OnClickListener {

    private CheckBox checkBoxVibrar, checkBoxRington, checkBoxAutoBackup;
    private SettingDatabaseAdapter database;
    private EditText etSettingPrinter, etSettingState;
    private Button btnSync, btnUpdate, btnFactoryReset;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_main, null, false);
        database = DatabaseCreator.getSettingDatabaseAdapter(getActivity());
        initializedView();
        return view;
    }
    public static SettingFragment newInstance() {
        return new SettingFragment();
    }


    @SuppressLint("Range")
    private void initializedView() {
        etSettingState = (EditText) view.findViewById(R.id.et_setting_uf);
        etSettingPrinter = (EditText) view.findViewById(R.id.et_setting_printer);
        checkBoxAutoBackup = (CheckBox) view.findViewById(R.id.checkBoxAutoBackup);
        checkBoxRington = (CheckBox) view.findViewById(R.id.checkBoxRington);
        checkBoxVibrar = (CheckBox) view.findViewById(R.id.checkBoxVibarte);
        btnSync = (Button) view.findViewById(R.id.btn_sync);
        btnUpdate = (Button) view.findViewById(R.id.btn_update);
        btnFactoryReset = (Button) view.findViewById(R.id.btn_factory_reset);

        Cursor cursor = (DatabaseCreator
                .getSettingDatabaseAdapter(getActivity()))
                .getSettingCursor();

        etSettingState.setText(cursor.getString(cursor
                .getColumnIndex(SetttingDatabaseHelper.SETTING_STATE)));
        etSettingPrinter.setText(cursor.getString(cursor
                .getColumnIndex(SetttingDatabaseHelper.SETTING_PRINTER)));

        btnSync.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnFactoryReset.setOnClickListener(this);

        if (database.getAutoBackup()) {
            checkBoxAutoBackup.setChecked(true);
        } else {
            checkBoxAutoBackup.setChecked(false);
        }
        if (database.getRingtone()) {
            checkBoxRington.setChecked(true);
        } else {
            checkBoxRington.setChecked(false);
        }
        if (database.getVibrator()) {
            checkBoxVibrar.setChecked(true);
            Log.d("Vibrar", database.getVibrator() + " ");

        } else {
            checkBoxVibrar.setChecked(false);
            Log.d("Não vibrar", database.getVibrator() + " ");
        }
        checkBoxVibrar.setOnCheckedChangeListener(this);
        checkBoxRington.setOnCheckedChangeListener(this);
        checkBoxAutoBackup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        int id = view.getId();
        if (id == R.id.checkBoxAutoBackup) {
            if (isChecked) {
                database.setAutoBackup(true);
            } else {
                database.setAutoBackup(false);
            }
        } else if (id == R.id.checkBoxRington) {
            if (isChecked) {
                database.setRingtone(true);
            } else {
                database.setRingtone(false);
            }
        } else if (id == R.id.checkBoxVibarte) {
            Log.d("vibrar", database.getVibrator() + "");
            if (isChecked) {
                database.setVibrator(true);
            } else {
                database.setVibrator(false);
            }
            Log.d("não vibra", database.getVibrator() + "");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_update) {
            etSettingState.setText(etSettingState.getText().toString());
            etSettingPrinter.setText(etSettingPrinter.getText().toString());
            if (database.setUpdatePrinterState(etSettingState.getText().toString(),
                    etSettingPrinter.getText().toString())) {
                Routine.showAlert(getResources().getString(R.string.update_success), getActivity());
            } else {
                Routine.showAlert(getResources().getString(R.string.update_erro), getActivity());
            }
        } else if (id == R.id.btn_sync) {
            if (NetworkConnection.isNetworkAvailable(getActivity())) {
                SyncDataInformation information = new SyncDataInformation(getActivity());
                information.execute("");
            } else {
                Routine.showAlert(getResources().getString(R.string.no_network_connection), getActivity());
            }
        } else if (id == R.id.btn_factory_reset) {
            showFactoryResetConfirmation();
        }

    }

    /**
     * Mostra diálogo de confirmação para reset de fábrica
     */
    private void showFactoryResetConfirmation() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Reset de Fábrica");
        builder.setMessage("ATENÇÃO: Esta ação irá excluir TODOS os dados do sistema:\n\n" +
                "• Todos os AITs\n" +
                "• Todos os RRDs\n" +
                "• Todos os TAVs\n" +
                "• Todos os TCAs\n" +
                "• Todos os dados de PJ\n\n" +
                "Esta ação NÃO PODE ser desfeita!\n\n" +
                "Deseja continuar?");
        
        builder.setPositiveButton("SIM, EXCLUIR TUDO", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                performFactoryReset();
            }
        });
        
        builder.setNegativeButton("Cancelar", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        builder.setCancelable(true);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Executa o reset de fábrica excluindo todos os dados
     */
    private void performFactoryReset() {
        try {
            Log.d("FactoryReset", "Iniciando reset de fábrica...");
            
            // Excluir dados de todas as tabelas
            boolean success = true;
            
            // 1. Excluir dados da tabela auto_pj (banco auto_pj_database.db)
            try {
                net.sistransito.mobile.database.AitPJDatabaseAdapter pjAdapter = 
                    DatabaseCreator.getAitPJDatabaseAdapter(getActivity());
                int pjDeleted = pjAdapter.deleteAllPJData();
                Log.d("FactoryReset", "Excluídos " + pjDeleted + " registros de PJ");
            } catch (Exception e) {
                Log.e("FactoryReset", "Erro ao excluir dados PJ", e);
                success = false;
            }
            
            // 2. Excluir dados da tabela tb_infraction (banco database_ait.db)
            try {
                net.sistransito.mobile.database.InfractionDatabaseAdapter aitAdapter = 
                    DatabaseCreator.getInfractionDatabaseAdapter(getActivity());
                int aitDeleted = aitAdapter.deleteAllInfractions();
                Log.d("FactoryReset", "Excluídos " + aitDeleted + " registros de AIT");
            } catch (Exception e) {
                Log.e("FactoryReset", "Erro ao excluir dados AIT", e);
                success = false;
            }
            
            // 3. Excluir dados da tabela tb_rrd (banco database_rrd.db)
            try {
                net.sistransito.mobile.database.RrdDatabaseAdapter rrdAdapter = 
                    DatabaseCreator.getRrdDatabaseAdapter(getActivity());
                int rrdDeleted = rrdAdapter.deleteAllRrdData();
                Log.d("FactoryReset", "Excluídos " + rrdDeleted + " registros de RRD");
            } catch (Exception e) {
                Log.e("FactoryReset", "Erro ao excluir dados RRD", e);
                success = false;
            }
            
            // 4. Excluir dados da tabela tav (banco database_tav.db)
            try {
                net.sistransito.mobile.database.TavDatabaseAdapter tavAdapter = 
                    DatabaseCreator.getTavDatabaseAdapter(getActivity());
                int tavDeleted = tavAdapter.deleteAllTavData();
                Log.d("FactoryReset", "Excluídos " + tavDeleted + " registros de TAV");
            } catch (Exception e) {
                Log.e("FactoryReset", "Erro ao excluir dados TAV", e);
                success = false;
            }
            
            // 5. Excluir dados da tabela tca (banco database_tca.db)
            try {
                net.sistransito.mobile.database.TcaDatabaseAdapter tcaAdapter = 
                    DatabaseCreator.getTcaDatabaseAdapter(getActivity());
                int tcaDeleted = tcaAdapter.deleteAllTcaData();
                Log.d("FactoryReset", "Excluídos " + tcaDeleted + " registros de TCA");
            } catch (Exception e) {
                Log.e("FactoryReset", "Erro ao excluir dados TCA", e);
                success = false;
            }
            
            if (success) {
                Log.d("FactoryReset", "Reset de fábrica concluído com sucesso");
                Routine.showAlert("Reset de fábrica concluído!\nTodos os dados foram excluídos.", getActivity());
            } else {
                Log.w("FactoryReset", "Reset de fábrica concluído com alguns erros");
                Routine.showAlert("Reset de fábrica concluído com alguns erros.\nVerifique os logs para detalhes.", getActivity());
            }
            
        } catch (Exception e) {
            Log.e("FactoryReset", "Erro geral no reset de fábrica", e);
            Routine.showAlert("Erro ao executar reset de fábrica:\n" + e.getMessage(), getActivity());
        }
    }

}
