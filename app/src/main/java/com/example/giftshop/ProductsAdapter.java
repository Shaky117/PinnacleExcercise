package com.example.giftshop;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Product products);
    }

    private ViewGroup parent;
    private ArrayList<Product> data;
    private OnItemClickListener listener;
    private ArrayList<Product> cart = new ArrayList<>();


    public ProductsAdapter(ArrayList<Product> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }
    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        return new ProductsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false));
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, int position) {
        final Product products = data.get(position);

        int price = products.getPrice();

        holder.tvPrice.setText("$" + String.valueOf(price));
        holder.tvName.setText(products.getName());

        if(parent.getId() == R.id.rvProducts) {
            final MainScreen mainScreen = (MainScreen) parent.getContext();
            holder.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cart.add(new Product(products.getId(),
                            products.getName(),
                            products.getPrice(),
                            products.getDescription(),
                            products.getCategory()));
                    mainScreen.onUpdateList();
                    Toast.makeText(v.getContext().getApplicationContext(), "Added to cart", Toast.LENGTH_LONG).show();
                }
            });
        }
        holder.bind(data.get(position), listener);
    }

    public ArrayList<Product> getCart(){
        return cart;
    }

    public int getCartCount(){
        return cart.size();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvPrice;
        Button btnAddCart;

        public ProductsViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.txtName);
            tvPrice = itemView.findViewById(R.id.txtPrice);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);

        }

        public void bind(final Product products, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(products);
                }
            });
        }
    }
}