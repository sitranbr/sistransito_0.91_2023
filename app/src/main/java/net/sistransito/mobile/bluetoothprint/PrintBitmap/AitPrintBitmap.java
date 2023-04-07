package net.sistransito.mobile.bluetoothprint.PrintBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;

import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.bluetoothprint.PrintBitmap.base.BasePrintBitmap;
import net.sistransito.mobile.bluetoothprint.PrintBitmap.base.PrintBitmapFormat;
import net.sistransito.R;

public class AitPrintBitmap extends BasePrintBitmap {
    private AitData aitData;
    private net.sistransito.mobile.aitcompany.pjData pjData;
    private String copy;

    public AitPrintBitmap(Context context, AitData aitData) {
        super(context);
        this.aitData = aitData;
    }

    public AitPrintBitmap(Context context, AitData autoData, String copy) {
        super(context);
        this.aitData = autoData;
        this.copy = copy;
    }

    public AitPrintBitmap(Context context, net.sistransito.mobile.aitcompany.pjData autoData, String copy) {
        super(context);
        this.pjData = autoData;
        this.copy = copy;
    }

    @Override
    public Bitmap getBitmap() {

        PrintBitmapFormat bitmapFormat = new PrintBitmapFormat(context);
        String title = context.getString(R.string.capital_government_title) + "\n" +
                context.getString(R.string.capital_security_department) + "\n" +
                context.getString(R.string.capital_transit_department);
        String subTitle = context.getString(R.string.capital_infraction_title);

        bitmapFormat.createTitle(title, subTitle, "ic_left_title.png", "ic_right_title.png", PrintBitmapFormat.TITLE_FONT_SIZE, PrintBitmapFormat.TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("1-IDENTIFICAÇÃO DA AUTUAÇÃO", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);

        /*bitmapFormat.createTitle("ÓRGÃO AUTUADOR", user.getCodigoOrgao(), "NÚMERO DO AIT", aData.getNumeroAuto(), PrintBitmapFormat.NORMAL_FONT,
               PrintBitmapFormat.MAIOR_FONT);*/

        bitmapFormat.createNameTable("ÓRGÃO AUTUADOR", "114100", "NÚMERO DO AIT", aitData.getAitNumber(),
                true, PrintBitmapFormat.TableCellAlign.MIDDLE,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.MIDDLE);

        bitmapFormat.createQuotes("2-IDENTIFICAÇÃO DO VEÍCULO", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);

        /*if (aitData.getCountry() == null) {
            bitmapFormat.createTable("PLACA", aitData.getPlate(), "UF",
                    aitData.getStateVehicle(), "CHASSI", aitData.getChassi(), false,
                    PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);
        }else{
            bitmapFormat.createTable("PLACA", aitData.getPlate(), "UF",
                    aitData.getStateVehicle(), "PAIS", "BR",
                    "CHASSI", aitData.getChassi(), false,
                    PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);
        }*/

        final String LICENSE_PLATE_COLUMN = "PLACA";
        final String STATE_COLUMN = "UF";
        final String COUNTRY_COLUMN = "PAIS";
        final String CHASSI_COLUMN = "CHASSI";
        final String BRAZIL_COUNTRY = "BR";

        String country = aitData.getCountry() == null ? BRAZIL_COUNTRY : aitData.getCountry();
        String licensePlate = aitData.getPlate();
        String state = aitData.getStateVehicle();
        String chassi = aitData.getChassi();

        bitmapFormat.createTable(LICENSE_PLATE_COLUMN, licensePlate, STATE_COLUMN, state,
                COUNTRY_COLUMN, country, CHASSI_COLUMN, chassi, false,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);


        bitmapFormat.createTable("MARCA/MODELO", aitData.getVehicleModel(),
                "ESPÉCIE", aitData.getVehicleSpecies(),
                true, PrintBitmapFormat.TableCellAlign.MIDDLE,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        bitmapFormat.createQuotes("3-IDENTIFICAÇÃO DO CONDUTOR", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("NOME", aitData.getConductorName(), true, false, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        String documentType = aitData.getDocumentType();
        String documentTitleDescription;
        String titleStateIdentity = null;

        switch (documentType) {
            case "CPF":
            case "RG":
            case "OUTROS":
                documentTitleDescription = documentType;
                break;
            default:
                documentTitleDescription = "RG/CPF/OUTROS";
                titleStateIdentity = aitData.getCnhState();
                break;
        }

        bitmapFormat.createTable("REGISTRO CNH/PPD/ACC", aitData.getCnhPpd(), "UF", aitData.getCnhState(), documentTitleDescription, aitData.getDocumentNumber(), true,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        bitmapFormat.createQuotes("4-IDENTIFICAÇÃO DO LOCAL, DATA E HORA", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("LOCAL", aitData.getAddress(), true, false, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);
        bitmapFormat.createNameTable("CÓD. MUNICÍPIO", aitData.getCityCode(), "MUNICÍPIO/UF", aitData.getCity() + "/" + aitData.getState(), true, PrintBitmapFormat.TableCellAlign.MIDDLE, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.MIDDLE);
        bitmapFormat.createNameTable("DATA", aitData.getAitDate(), "HORA", aitData.getAitTime(), true, PrintBitmapFormat.TableCellAlign.MIDDLE, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.MIDDLE);
        bitmapFormat.createQuotes("5-TIPIFICAÇÃO DA INFRAÇÃO", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createTable("CÓD. INFRAÇÃO", aitData.getFramingCode(),
                "DESDOB.", aitData.getUnfolding(),
                "AMPARO LEGAL", "Art." + aitData.getArticle() + " da Lei 9.503/97", true, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);
        bitmapFormat.createQuotes("DESCRIÇÃO DA INFRAÇÃO", aitData.getInfraction(), true, false, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        if (aitData.getDescription().isEmpty()) {
            createTcaNumberQuotes(bitmapFormat, aitData.getTcaNumber());
        } else {
            createEquipmentTable(bitmapFormat, aitData);
        }

        StringBuilder proceduresBuilder = new StringBuilder();
        if (TextUtils.isEmpty(aitData.getProcedures())) {
            proceduresBuilder.append(aitData.getRetreat());
        } else if (TextUtils.isEmpty(aitData.getRetreat())) {
            proceduresBuilder.append(aitData.getProcedures());
        } else {
            proceduresBuilder.append(aitData.getProcedures()).append(", ").append(aitData.getRetreat());
        }
        String resultProcedure = proceduresBuilder.toString();

        bitmapFormat.createQuotes("PROCEDIMENTOS", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("MEDIDAS ADMINISTRATIVAS", resultProcedure, true, false, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        String approach = aitData.getApproach().equals("0") ? context.getString(R.string.driver_no_approached) : context.getString(R.string.driver_approach);

        bitmapFormat.createObservationQuotes("OBSERVAÇÕES", aitData.getObservation(), true, PrintBitmapFormat.NORMAL_FONT);

        bitmapFormat.createQuotes("ABORDAGEM", approach, true, false, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        bitmapFormat.createQuotes("6-IDENTIFICAÇÃO DA AUTORIDADE OU AGENTE AUTUADOR", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createNameTable("NOME", user.getEmployeeName().toUpperCase(), "MATRÍCULA", user.getRegistration(), false, PrintBitmapFormat.TableCellAlign.RIGHT,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.LEFT);

        bitmapFormat.createSignatureQuotes("ASSINATURA", "\n\n\n", true, PrintBitmapFormat.NORMAL_FONT);

        String cpfOrCnpjShipperTitle = aitData.getCpfShipper() == null ? "CNPJ" : "CPF";
        String cpfOrCnpjShipperText = aitData.getCpfShipper() == null ? aitData.getCnpShipper() : aitData.getCpfShipper();

        bitmapFormat.createQuotes("7-IDENTIFICAÇÃO DO EMBARCADOR OU EXPEDIDOR", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createNameTable("NOME", aitData.getShipperIdentification(), cpfOrCnpjShipperTitle, cpfOrCnpjShipperText, true, PrintBitmapFormat.TableCellAlign.RIGHT,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.LEFT);

        String cpfOrCnpjCarrierTitle = aitData.getCpfCarrier() == null ? "CNPJ" : "CPF";
        String cpfOrCnpjCarrierText = aitData.getCpfCarrier() == null ? aitData.getCnpjCarrier() : aitData.getCpfCarrier();

        bitmapFormat.createQuotes("8-IDENTIFICAÇÃO DO TRANSPORTADOR", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createNameTable("NOME", aitData.getCarrierIdentification(), cpfOrCnpjCarrierTitle, cpfOrCnpjCarrierText, true, PrintBitmapFormat.TableCellAlign.RIGHT,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT, PrintBitmapFormat.TableCellAlign.LEFT);

        bitmapFormat.createQuotes("9-ASSINATURA DO INFRATOR OU CONDUTOR", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createSignatureQuotes("\n\n\n", "\n\n\n", true, PrintBitmapFormat.NORMAL_FONT);

        bitmapFormat.setNewLine(1);
        bitmapFormat.createQuotes("VIA DO " + copy, Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.setNewLine(2);

        /*bitmapFormat.createQuotes("ORIENTAÇÕES PARA APRESENTAÇÃO", Paint.Align.CENTER, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("DE DEFESA DE AUTUAÇÃO", Paint.Align.CENTER, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.setNewLine(2);
        bitmapFormat.createQuotes("O Auto de Infração de Trânsito - AIT valerá como NOTIFICAÇÃO DE AUTUAÇÃO quando assinado pelo condutor quando:", Paint.Align.LEFT, true, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("I- A infração for de responsabilidade do condutor;", Paint.Align.LEFT, true, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("II- A infração for de responsabilidade do proprietário e este estiver conduzindo o veículo.", Paint.Align.LEFT, true, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("a) Nos casos acima o prazo para a apresentação da DEFESA DE AUTUAÇÃO é de 15 (quinze) dias, " +
                "contados a partir da data da assinatura do auto de infração;).", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.setNewLine(3);
        bitmapFormat.createQuotes("b) Nos demais casos, o prazo para a apresentação da DEFESA DE AUTUAÇÃO é " +
                "de 15 (quinze) dias contados da data da Notificação da Autuação. O ÓRGÃO disponibizará, em seus postos " +
                "de atendimento e através da internet no site: www.orgao.transito.com.br, modelo_veiculo padrão de REQUERIMENTO para apresentação " +
                "de DEFESA DE AUTUAÇÃO;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.setNewLine(3);
        bitmapFormat.createQuotes("c) Elebore sua DEFESA DE AUTUAÇÃO expondo os motivos, alegações e argumentos permitidos em lei;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.setNewLine(3);
        bitmapFormat.createQuotes("d) Documentação (cópia) a ser anexada na DEFESA DE AUTUAÇÃO:", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.setNewLine(2);
        bitmapFormat.createQuotes("- Auto de Infração de Transito - AIT ou Notificação de Autuação - NA;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("- Certificado de Registro e Licenciamento de Veículo - CRLV;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("- Documento de Identificação, com foto e assinatura;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("- CPF, caso não conste no documento de identificação;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("- Procuração, com firma reconhecida, quando se fizer representar por terceiros;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("- Se possoa Jurídica: Contrato Social e Procuração com firma reconhecida quando o resposável legal se fizer representar por terceiros;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.createQuotes("- Provas documentais previstas em lei que comprovem as alegações apresentadas.", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);

        bitmapFormat.setNewLine(3);
        bitmapFormat.createQuotes("e) O requerimento com a DEFESA DE AUTUAÇÃO poderá ser entregue no protocolo da sede do " +
                "Orgão, Postos de Atendimento ou remetido via postal para:", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.setNewLine(3);
        bitmapFormat.createQuotes("Departamento de Trânsito do Estado", Paint.Align.CENTER, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("Endereço compleot", Paint.Align.CENTER, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);
        bitmapFormat.createQuotes("CEP: 00000-000, Cidade-UF", Paint.Align.CENTER, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);

        bitmapFormat.setNewLine(3);

        bitmapFormat.createQuotes("f) Apresentada a DEFESA DE AUTUAÇÃO, no prezo previsto e desde que preenchidos " +
                "os requisitos legais, o ÓRGÃO apreciará e caso considere procedente, cancelará o Auto de Infração de Transito - AIT " +
                "e comunicará o fato ao proprietário do veículo;", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.setNewLine(3);
        bitmapFormat.createQuotes("g) Em caso de não apresentação de DEFESA DE AUTUAÇÃO, do seu não conhecimento, ou ainda, " +
                "de indeferimento, o ÓRGÃO aplicará a penalidade expedindo a respectiva NOTIFICAÇÃO DE PENALIDADE; " +
                "pena de invalidade da penalidade aplicada.", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
        bitmapFormat.setNewLine(3);
        bitmapFormat.createQuotes("h) É obrigatório a presença do código RENAINF ou INFRAEST na " +
                "notificação de penalidade, nos casos de infrações vinculadas ao veículo, sob " +
                "pena de invalidade da penalidade aplicada.", Paint.Align.LEFT, false, false, PrintBitmapFormat.NORMAL_FONT);
*/

        bitmapFormat.setNewLine(25);
        bitmapFormat.printDocumentClose();
        bitmapFormat.saveBitmap();

        return bitmapFormat.getPrintBitmap();

    }

    private void createTcaNumberQuotes(PrintBitmapFormat bitmapFormat, String tcaNumber) {
        bitmapFormat.createQuotes("NÚMERO DO TCA", tcaNumber, true, false, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);
    }

    private void createEquipmentTable(PrintBitmapFormat bitmapFormat, AitData aData) {
        bitmapFormat.createQuotes("EQUIPAMENTOS/INSTRUMENTOS DE AFERIÇÃO UTILIZADO", Paint.Align.LEFT, true, false, PrintBitmapFormat.SUB_TITLE_FONT_SIZE);

        bitmapFormat.createTable("DESCRIÇÃO", aData.getDescription(), "MARCA", aData.getEquipmentBrand(), true, PrintBitmapFormat.TableCellAlign.MIDDLE, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        bitmapFormat.createTable("MODELO", aData.getEquipmentModel(), "NÚMERO DE SÉRIE", aData.getSerialNumber(), true, PrintBitmapFormat.TableCellAlign.MIDDLE, PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        bitmapFormat.createTable("MEDIÇÃO REALIZADA", aData.getMeasurementPerformed(), "LIMITE REGULAMENTADO", aData.getRegulatedValue(),
                true, PrintBitmapFormat.TableCellAlign.MIDDLE,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);

        bitmapFormat.createTable("VALOR CONSIDERADO", aData.getValueConsidered(), "NÚMERO DA AMOSTRA", aData.getAlcoholTestNumber(),
                true, PrintBitmapFormat.TableCellAlign.MIDDLE,
                PrintBitmapFormat.NORMAL_FONT, PrintBitmapFormat.MEDIO_FONT);
    }

}

