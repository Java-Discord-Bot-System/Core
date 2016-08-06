package com.almightyalpaca.adbs4j.events.manager;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EventHandler {

}
