package com.example.myfirstapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.databinding.ProductItemBinding
import com.example.myfirstapp.models.Product
import com.example.myfirstapp.utils.PriceFormatter
import java.text.NumberFormat
import java.util.Locale

// recive function to active when products is pressed
class HomeAdapter(private val onItemClicked: (Product) -> Unit): RecyclerView.Adapter<HomeViewHolder>() {
    // create products list
    private var products = mutableListOf<Product>()

    // method to update list product and update view state
    fun setProductsList(products: List<Product>) {
        this.products = products.toMutableList()
        notifyDataSetChanged()
    }

    // method equivalent the on create in activity but here is to item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    // pass all args for each item in recycle view
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, onItemClicked)
    }

    // set item count
    override fun getItemCount(): Int {
        return products.size
    }
}

class HomeViewHolder(val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product, onItemClicked: (Product) -> Unit) {
        val formatedPrice = PriceFormatter.formatPrice(product.price)

        binding.productName.text = product.title
        binding.productDescription.text = product.description
        binding.productPrice.text = formatedPrice

        itemView.setOnClickListener {
            onItemClicked(product)
        }
    }
}