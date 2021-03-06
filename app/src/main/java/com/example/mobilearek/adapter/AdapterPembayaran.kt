package com.example.mobilearek.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilearek.R
import com.example.mobilearek.helper.Helper
import com.example.mobilearek.model.Transaksi


@Suppress("DEPRECATION")
class AdapterPembayaran(var activity: Activity, var data : ArrayList<Transaksi>, var listener: Listeners):RecyclerView.Adapter<AdapterPembayaran.Holder>() {
    class Holder(view: View):RecyclerView.ViewHolder(view){

        val tvExp = view.findViewById<TextView>(R.id.by_expired)
        val tvKode = view.findViewById<TextView>(R.id.by_kode)
        val tvtglPesan = view.findViewById<TextView>(R.id.by_tanggal)
        val tvTotal = view.findViewById<TextView>(R.id.by_total)
        val cdBayar = view.findViewById<CardView>(R.id.pembayaran)
        val tvStatus = view.findViewById<TextView>(R.id.by_status)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_pembayaran,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = data[position]
        if (data.status_bayar == 2) {
        holder.tvStatus.text = "Sukses"
        holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.green))
        }else if (data.status_bayar == 1) {
        holder.tvStatus.text = "Menunggu Admin"
        holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.Kuning))
        }else if (data.status_bayar == 0) {
            holder.tvStatus.text = "Belum Dibayar"
            holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.ColorRed))
        }else if (data.status_bayar == 3) {
            holder.tvStatus.text = "Bukti tidak sesuai"
            holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.ColorRed))
        }
        holder.tvExp.text = data.expired_at+" WIB"
        holder.tvtglPesan.text = data.tgl_order+" WIB"
        holder.tvKode.text = data.kode_tran
        holder.tvTotal.text = "Rp. "+data.total_harga
        holder.cdBayar.setOnClickListener {
            listener.onClicked(data)
        }

    }

    interface Listeners{
        fun onClicked(data:Transaksi)
    }

}