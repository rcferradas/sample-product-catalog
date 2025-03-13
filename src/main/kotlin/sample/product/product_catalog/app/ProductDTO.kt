package sample.product.product_catalog.app

import sample.product.product_catalog.domain.Product

data class ProductDTO (
    val sku: String,
    val description: String,
    val price: Double,
    val discountedPrice: Double?,
    val category : String,
){
    companion object{
        fun fromDomain(product:Product, discountedPrice: Double?=null): ProductDTO = with(product){
            ProductDTO(
                sku = sku,
                description = description,
                price = price,
                discountedPrice = discountedPrice,
                category = category.categoryName
            )
        }
    }
}