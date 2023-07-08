package com.bikkadit.store.Validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid
{
    //Error Message
    String message() default "Invalid Image Name..!!";

    //Represent group of constraints
    Class<?>[] groups() default { };

    //Additional information about annotation
    Class<? extends Payload>[] payload() default { };
}
