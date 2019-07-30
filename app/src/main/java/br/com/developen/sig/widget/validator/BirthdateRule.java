package br.com.developen.sig.widget.validator;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BirthdateRule extends AnnotationRule<Birthdate, String> {


    protected BirthdateRule(final Birthdate birthdate) {

        super(birthdate);

    }


    public boolean isValid(final String date) {

        if (date == null || !date.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))

            return false;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {

            Date newDate = format.parse(date);

            return newDate.before(new Date());

        } catch (ParseException e){

            return false;

        }

    }


}