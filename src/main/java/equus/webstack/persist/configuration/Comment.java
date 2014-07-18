package equus.webstack.persist.configuration;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ TYPE, FIELD })
@Retention(RUNTIME)
public @interface Comment {

  String value() default "";
}
