package br.com.developen.sig.widget.validator;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.developen.sig.R;

@ValidateUsing(IndividualNameRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IndividualName {

    boolean trim() default true;

    String message() default "";

    int sequence() default -1;

    int messageResId() default R.string.error_invalid_name;

}