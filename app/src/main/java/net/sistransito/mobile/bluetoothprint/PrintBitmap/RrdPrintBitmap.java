package net.sistransito.mobile.bluetoothprint.PrintBitmap;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;

import net.sistransito.R;
import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.bluetoothprint.PrintBitmap.base.BasePrintBitmap;
import net.sistransito.mobile.bluetoothprint.PrintBitmap.base.PrintBitmapFormat;
import net.sistransito.mobile.rrd.RrdData;

/**
 * Created on 8/15/2016.
 */

public class RrdPrintBitmap extends BasePrintBitmap {
    private AitData aitData;
    private RrdData rrdData;
    public RrdPrintBitmap(Context context, RrdData rrdData, AitData aitData) {
        super(context);
        this.aitData = aitData;
        this.rrdData = rrdData;
    }
    @Override
    public Bitmap getBitmap() {
        PrintBitmapFormat bitmapFormat = new PrintBitmapFormat(context);

        String title = "NOME DO ESTADO\n" +
                "NOME DA SECRETARIA\n" +
                "DEPARTAMENTO DE TRÂNSITO";
        String subTitle = context.getString(R.string.rrd_receipt);

        bitmapFormat.createTitle(title, subTitle, "ic_left_title.png", "ic_right_title.png", PrintBitmapFormat.TITLE_FONT_SIZE, PrintBitmapFormat.TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("IDENTIFICAÇÃO DO RECIBO", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);

        if(rrdData.getRrdType().equals("avulso")) {
            bitmapFormat.createNameTable("NÚMERO DO RRD", rrdData.getRrdNumber(), "NÚMERO DO AIT", "",
                    true, PrintBitmapFormat.TableCellAlign.MIDDLE,
                    PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.MIDDLE);
        }else{
            bitmapFormat.createNameTable("NÚMERO DO RRD", rrdData.getRrdNumber(), "NÚMERO DO AIT", aitData.getAitNumber(),
                    true, PrintBitmapFormat.TableCellAlign.MIDDLE,
                    PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.MIDDLE);
        }

        bitmapFormat.createQuotes("RECEBEMOS DE:", rrdData.getDriverName(), true, false, PrintBitmapFormat.NORMAL_FONT);

        bitmapFormat.createQuotes("O DOCUMENTO ABAIXO ESPECIFICADO", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);

        if (rrdData.getDocumentType().equals("CRLV")) {
            bitmapFormat.createTable("CRLV", rrdData.getCrlvNumber(), "PLACA", rrdData.getPlate(), "UF", rrdData.getPlateState(), true,
                    PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);
        } else {
            bitmapFormat.createTable("REGISTRO CNH/PPD/ACC", rrdData.getRegistrationNumber(),
                    "VALIDADE", rrdData.getValidity(), "UF", rrdData.getRegistrationState(), true,
                    PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);
        }

        bitmapFormat.createQuotes("MOTIVO PARA O RECOLHIMENTO", rrdData.getReasonCollected().toUpperCase(), true, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("DATA DO RECOLHIMENTO", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createNameTable("DATA", rrdData.getDateCollected(), "HORA", rrdData.getTimeCollected(),
        true, PrintBitmapFormat.TableCellAlign.MIDDLE, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.MIDDLE);
        bitmapFormat.createNameTable("MUNICÍPIO", rrdData.getCityCollected(), "UF", rrdData.getStateCollected(),
                true, PrintBitmapFormat.TableCellAlign.MIDDLE, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.MIDDLE);

        bitmapFormat.createQuotes("RESPONSÁVEL PELO RECOLHIMENTO", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createTwoColumns("NOME", context.getString(R.string.rrd_name), "MATRÍCULA", user.getRegistration(),
                false, PrintBitmapFormat.TableCellAlign.LEFT,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.LEFT);
        bitmapFormat.createSignatureQuotes("ASSINATURA", "\n\n\n", true, PrintBitmapFormat.NORMAL_FONT);

        bitmapFormat.setNewLine(2);

        bitmapFormat.createQuotes("ORIENTAÇÕES AO USUÁRIO:", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes(context.getString(R.string.rrd_observation_fist) + " " + rrdData.getDaysForRegularization() + " " + context.getString(R.string.rrd_observation_second),
                PrintBitmapFormat.MEDIO_FONT);

        bitmapFormat.setNewLine(5);

        bitmapFormat.createQuotes("---------------------------------------------------------------------------", Paint.Align.CENTER, false, false, PrintBitmapFormat.MAIOR_FONT);
        bitmapFormat.createQuotes("Departamento de Trânsito", Paint.Align.CENTER, false, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("Endereço", Paint.Align.CENTER, false, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("CEP: 00000-000, Estado-UF", Paint.Align.CENTER, false, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);

        bitmapFormat.setNewLine(25);
        bitmapFormat.printDocumentClose();
        bitmapFormat.saveBitmap();
        return bitmapFormat.getPrintBitmap();
    }
}

