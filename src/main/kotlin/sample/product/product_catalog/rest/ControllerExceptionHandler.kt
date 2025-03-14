package sample.product.product_catalog.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import sample.product.product_catalog.domain.ProductCategoryNotSupportedException

@RestControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class, ProductCategoryNotSupportedException::class)
    fun handleBadRequest(ex: Exception): ResponseEntity<*> {
        return ResponseEntity.badRequest().body("Bad request, reason:${ex.message}")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<*> {
        return ResponseEntity.internalServerError().body("Sorry! Something unexpected went wrong")
    }
}