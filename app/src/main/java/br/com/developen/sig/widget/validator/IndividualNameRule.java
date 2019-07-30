package br.com.developen.sig.widget.validator;

import com.mobsandgeeks.saripaar.AnnotationRule;

public class IndividualNameRule extends AnnotationRule<IndividualName, String> {

    protected IndividualNameRule(final IndividualName individualName) {

        super(individualName);

    }

    public boolean isValid(final String data) {

        boolean isValid = true;

        if (data != null && (mRuleAnnotation.trim() ? data.trim().length() > 0 : data.length() > 0))

            isValid = (mRuleAnnotation.trim() ? data.trim().split(" ").length >= 2 : data.split(" ").length >= 2);

        return isValid;

    }

}