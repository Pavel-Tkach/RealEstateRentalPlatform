package org.example.propertyservice.aspect

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Loggable()
