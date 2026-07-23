package com.example.tas_mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tas_mobile.R
import com.example.tas_mobile.models.TouristSpot
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookmarkFragment : Fragment() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var tvEmpty: TextView
    private val favoriteList = mutableListOf<TouristSpot>()
    private lateinit var adapter: FavoriteAdapter

    private val db = FirebaseDatabase.getInstance().getReference("bookmarks")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmark, container, false)

        rvFavorites = view.findViewById(R.id.rv_favorites)
        tvEmpty = view.findViewById(R.id.tv_empty)

        rvFavorites.layoutManager = LinearLayoutManager(context)
        adapter = FavoriteAdapter(favoriteList) { spot ->
            removeFromBookmark(spot)
        }
        rvFavorites.adapter = adapter

        fetchBookmarks()

        return view
    }

    private fun fetchBookmarks() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteList.clear()
                for (child in snapshot.children) {
                    val spot = child.getValue(TouristSpot::class.java)
                    spot?.let { favoriteList.add(it) }
                }
                adapter.notifyDataSetChanged()
                tvEmpty.visibility = if (favoriteList.isEmpty()) View.VISIBLE else View.GONE
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun removeFromBookmark(spot: TouristSpot) {
        db.child(spot.idWisata.ifEmpty { spot.hashCode().toString() }).removeValue()
    }

    inner class FavoriteAdapter(
        private val list: List<TouristSpot>,
        private val onRemoveClick: (TouristSpot) -> Unit
    ) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tv_fav_name)
            val tvCategory: TextView = view.findViewById(R.id.tv_fav_category)
            val tvAddress: TextView = view.findViewById(R.id.tv_fav_address)
            val btnRemove: Button = view.findViewById(R.id.btn_remove_fav)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val spot = list[position]
            holder.tvName.text = spot.nama
            holder.tvCategory.text = "Kategori: ${spot.kategori} (${if (spot.isOfficial) "Dari API Resmi" else "Spot Warga"})"
            holder.tvAddress.text = spot.alamat
            holder.btnRemove.setOnClickListener { onRemoveClick(spot) }
        }

        override fun getItemCount(): Int = list.size
    }
}