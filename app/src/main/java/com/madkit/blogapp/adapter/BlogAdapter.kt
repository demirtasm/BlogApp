package com.madkit.blogapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madkit.blogapp.databinding.BlogItemBinding
import com.madkit.blogapp.model.BlogItemModel

class BlogAdapter(private val items: List<BlogItemModel>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BlogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BlogItemBinding.inflate(inflater, parent, false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BlogViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


    inner class BlogViewHolder(private val binding: BlogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BlogItemModel) {
            binding.tvHeading.text = model.heading
            Glide.with(binding.imgProfile.context).load(model.imageUrl).into(
                binding.imgProfile
            )
            binding.tvUserName.text = model.userName
            binding.tvDate.text = model.date
            binding.tvPost.text = model.post
            binding.tvLikeCount.text = model.likeCount.toString()

        }


    }
}