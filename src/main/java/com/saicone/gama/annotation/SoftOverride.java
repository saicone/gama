package com.saicone.gama.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method declaration is intended to override another method declaration that may exist or not in a supertype.<br>
 * Similar as {@link Override} but not intended to be verified on compilation.
 *
 * @author Rubenicos
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface SoftOverride {
}
