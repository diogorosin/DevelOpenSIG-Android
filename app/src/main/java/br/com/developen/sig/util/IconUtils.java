package br.com.developen.sig.util;


import br.com.developen.sig.R;

public class IconUtils {

    public static int getListIconByType(String type){

        int result = 0;

        switch (type) {

            case "F": result = R.drawable.icon_female_60;
                break;

            case "M": result = R.drawable.icon_male_60;
                break;

        }

        return result;

    }

}