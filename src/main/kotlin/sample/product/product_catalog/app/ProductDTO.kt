package sample.product.product_catalog.app

import sample.product.product_catalog.domain.Product

data class ProductDTO (
    val sku: String,
    val description: String,
    val price: Double,
    val category : String,
    val discountedPrice: Double?=null,
){
    companion object{
        fun fromDomain(product:Product, discountedPrice: Double?=null): ProductDTO = with(product){
            ProductDTO(
                sku = sku,
                description = description,
                price = price,
                category = category.categoryName,
                discountedPrice = discountedPrice,
            )
        }
    }
}