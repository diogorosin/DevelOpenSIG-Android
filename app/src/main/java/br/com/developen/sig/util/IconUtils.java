package br.com.developen.sig.util;


import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

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


    public static void paintItWhite(MenuItem menuItem){

        menuItem.setIcon( IconUtils.changeColorOfDrawable(menuItem.getIcon(), R.color.colorWhite)  );

    }


    private static Drawable changeColorOfDrawable(Drawable drawable, int color){

        drawable = DrawableCompat.wrap(drawable);

        DrawableCompat.setTint(drawable, ContextCompat.getColor(App.getContext(), color));

        return drawable;

    }


}