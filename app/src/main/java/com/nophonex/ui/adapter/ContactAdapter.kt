package com.nophonex.ui.adapter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nophonex.R
import com.nophonex.ui.Contact
import kotlinx.android.synthetic.main.contact_list_item.view.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast


class ContactAdapter(private val mContext: Context, private val mActivity: Activity, private val requestListener: OnRequestPermissionsResultCallback) :
        RecyclerView.Adapter<ContactAdapter.ContactHolder>() {
    private var contactList: List<Contact>? = null
    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
//        mListTodoTasks = ArrayList<String>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = mLayoutInflater.inflate(R.layout.contact_list_item, parent, false)
        return ContactHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList?.size!!
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.tvContactName.setText(contactList?.get(position)?.getContactName())
        holder.tvPhoneNumber.setText(contactList?.get(position)?.getContactNumber())
        holder.llContact.setOnClickListener(View.OnClickListener {
            if (requestListener.requestPermissionCallback()) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + contactList?.get(position)?.getContactNumber())
                mContext.startActivity(callIntent)
            }
        })
    }

    class ContactHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var tvContactName = view.tv_contact_name
        var tvPhoneNumber = view.tv_phone_number
        var llContact = view.ll_contact
    }

    fun setContactList(contactList: List<Contact>) {
        this.contactList = contactList
        notifyDataSetChanged()
    }

    interface OnRequestPermissionsResultCallback {
        fun requestPermissionCallback(): Boolean
    }
}