package br.com.developen.sig.util;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;

import br.com.developen.sig.database.AgencyModel;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.StateModel;

public class StringUtils {


    private static final DecimalFormatSymbols symbols;

    private static final DecimalFormat decimalFormatWithSymbol;

    private static final DecimalFormat decimalFormatOfQuantity;

    private static final DecimalFormat decimalFormat;

    private static final DateFormat dateFormat;


    static{

        symbols = new DecimalFormatSymbols();

        symbols.setDecimalSeparator(',');

        symbols.setGroupingSeparator('.');

        symbols.setCurrencySymbol("R$");

        decimalFormat = new DecimalFormat("###,###,###,###.##", symbols);

        decimalFormat.setMaximumFractionDigits(2);

        decimalFormat.setMinimumFractionDigits(2);

        decimalFormatWithSymbol = new DecimalFormat("R$ ###,###,###,###.##", symbols);

        decimalFormatWithSymbol.setMaximumFractionDigits(2);

        decimalFormatWithSymbol.setMinimumFractionDigits(2);

        decimalFormatOfQuantity = new DecimalFormat("###,###,###,###.###", symbols);

        dateFormat = new DateFormat();

    }


    public static String formatCurrencyWithSymbol(Double currency){

        return decimalFormatWithSymbol.format(currency);

    }


    public static String formatCurrency(Double currency){

        return decimalFormat.format(currency);

    }


    public static String formatQuantity(Double currency){

        decimalFormatOfQuantity.setMinimumFractionDigits(0);

        decimalFormatOfQuantity.setMaximumFractionDigits(3);

        return decimalFormatOfQuantity.format(currency);

    }


    public static String formatQuantity(Integer integer){

        decimalFormatOfQuantity.setMinimumFractionDigits(0);

        decimalFormatOfQuantity.setMaximumFractionDigits(0);

        return decimalFormatOfQuantity.format(integer);

    }


    public static String formatQuantityWithMinimumFractionDigit(Double currency){

        decimalFormatOfQuantity.setMinimumFractionDigits(3);

        decimalFormatOfQuantity.setMaximumFractionDigits(3);

        return decimalFormatOfQuantity.format(currency);

    }


    public static String formatDate(Date dateTime){

        if (dateTime == null)

            return null;

        return dateFormat.format("dd/MM/yyyy", dateTime).toString();

    }


    public static String formatDateTime(Date dateTime){

        return dateFormat.format("dd/MM/yyyy HH:mm:ss", dateTime).toString();

    }


    public static String formatShortDateTime(Date dateTime){

        return dateFormat.format("dd/MM HH:mm", dateTime).toString();

    }


    public static String formatShortState(StateModel stateModel){

        return stateModel.getAcronym();

    }


    public static String formatShortAgency(AgencyModel agencyModel){

        return agencyModel.getAcronym();

    }


    public static String formatRgWithPrefix(Long number, AgencyModel agency, StateModel state){

        if (number == null | agency == null || state == null)

            return null;

        return "RG: " + number + " " + formatShortAgency(agency) + "/" + formatShortState(state);

    }

    public static String formatCpfWithPrefix(Long cpf){

        if (cpf == null)

            return null;

        String string = padCpf(cpf);

        return "CPF: " + string.substring(0,3) + "." + string.substring(3,6) + "." + string.substring(6,9) + "-" + string.substring(9,11);

    }


    public static String formatCityWithState(CityModel cityModel){

        if (cityModel == null)

            return null;

        return cityModel.getDenomination() + " - " + cityModel.getState().getAcronym();

    }


    public static String leftPad(String string, Integer size, Character character){

        String result = string;

        if (string.length() > size)

            result = string.substring(0, size - 1);

        else

            while (result.length() < size)

                result = character + result;

        return result;

    }


    public static String rightPad(String string, Integer size, Character character){

        String result = string;

        if (string.length() > size)

            result = string.substring(0, size - 1);

        else

            while (result.length() < size)

                result = result + character;

        return result;

    }


    public static Long parseCpf(String string){

        if (string == null || string.isEmpty())

            return null;

        return Long.valueOf(string.replaceAll("\\D+",""));

    }


    public static Long parseRgNumber(String string){

        if (string == null || string.isEmpty())

            return null;

        return Long.valueOf(string.replaceAll("\\D+",""));

    }


    public static Integer parsePostalCode(String string ){

        if (string == null || string.isEmpty())

            return null;

        return Integer.valueOf(string.replaceAll("\\D+",""));

    }


    public static boolean isValidCpf(String data){

        String cpf = data.replaceAll("\\D+","");

        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") ||
                cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555")
                || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888")
                || cpf.equals("99999999999") || (cpf.length() != 11))

            return(false);

        char dig10, dig11;

        int sm, i, r, num, peso;

        try {

            sm = 0; peso = 10;

            for (i=0; i<9; i++) {

                num = (cpf.charAt(i) - 48);

                sm = sm + (num * peso);

                peso = peso - 1;

            }

            r = 11 - (sm % 11);

            if ((r == 10) || (r == 11))

                dig10 = '0';

            else

                dig10 = (char)(r + 48);

            sm = 0;

            peso = 11;

            for(i=0; i<10; i++) {

                num = (cpf.charAt(i) - 48);

                sm = sm + (num * peso);

                peso = peso - 1;

            }

            r = 11 - (sm % 11);

            if ((r == 10) || (r == 11))

                dig11 = '0';

            else

                dig11 = (char)(r + 48);

            if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10)))

                return(true);

            else

                return(false);

        } catch (InputMismatchException error) {

            return(false);

        }

    }


    @SuppressLint("SimpleDateFormat")
    private static Date internalStringToDate(String string) throws ParseException {

        return new SimpleDateFormat("dd/MM/yyyy").parse(string);

    }


    public static Date stringToDate(String string) {

        if (string == null || string.isEmpty() || !string.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))

            return null;

        try{

            return internalStringToDate(string);

        } catch (Exception e){

            return null;

        }

    }


    public static boolean stringCanBeAnBirthDate(String string){

        if (string == null || string.isEmpty())

            return true;

        if (!string.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))

            return false;

        try {

            Date date = internalStringToDate(string);

            return date.before(new Date());

        } catch (ParseException e) {

            return false;

        }

    }


    public static String padPostalCode(Integer postalCode){

        return postalCode == null ? null : leftPad(String.valueOf(postalCode), 8, '0');

    }


    public static String padCpf(Long cpf){

        return cpf == null ? null : leftPad(String.valueOf(cpf), 11, '0');

    }


    /** TODO: Implementar a formatação do numero do RG conforme o estado */
    public static String formatRgNumberOfState(Long rgNumber, StateModel state){

        return rgNumber == null ? null : String.valueOf(rgNumber);

    }


}