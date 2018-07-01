package com.nophonex.ui.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.nophonex.R
import com.nophonex.ui.Contact
import com.nophonex.utils.TimerTaskSync
import kotlinx.android.synthetic.main.activity_call.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.LinearLayout
import com.nophonex.ui.adapter.ContactAdapter
import com.nophonex.utils.Base
import com.nophonex.utils.themeUtils


class CallActivity : Base(), TimerTaskSync.TimerCallback, ContactAdapter.OnRequestPermissionsResultCallback {
    override fun requestPermissionCallback(): Boolean {
        return checkPermissionAndLoadContact()
    }

    private var permissionsRequired = arrayOf<String>(Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS)
    private var mPermsRequestCode = 200
    private lateinit var mRclContact: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeUtils.onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_call)
        tv_make_call.setOnClickListener(callListener)
        mRclContact = findViewById<RecyclerView>(R.id.rcl_contact_list);
        setTimerUpdate()
        checkPermissionAndLoadContact()

        if (checkPermission(Manifest.permission.READ_CONTACTS)) {
            mRclContact.visibility = View.VISIBLE
            tv_loading_contact_msg.visibility = View.GONE
            loadContacts()
        } else {
            mRclContact.visibility = View.GONE
            tv_loading_contact_msg.visibility = View.VISIBLE
        }
    }

    private fun checkPermissionAndLoadContact(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsRequired, mPermsRequestCode)
            return false
        } else {
            return true
        }
    }

    private fun makeCall() {
        try {
            if (checkPermission(Manifest.permission.CALL_PHONE)) {
                val my_callIntent = Intent(Intent.ACTION_CALL)
                my_callIntent.data = Uri.parse("tel:" + et_call.text.toString())
                //here the word 'tel' is important for making a call...
                startActivity(my_callIntent)
            }else{
                ActivityCompat.requestPermissions(this, permissionsRequired, mPermsRequestCode)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Error in your phone call" + e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (permsRequestCode) {
            mPermsRequestCode -> {
                val callPhone = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readContacts = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (callPhone) {
                    makeCall()
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                } else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                if (readContacts) {
                    loadContacts()
                } else {
                    Toast.makeText(this, "Permission Denied for reading contacts", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onTimerListener(date: String) {
        tv_time.text = date
    }

    var callListener = View.OnClickListener { makeCall() }
    private fun setTimerUpdate() {
        val timerTask = TimerTaskSync(this, this);
        timerTask.start()
    }

    private fun loadContacts() {
        val contactList: MutableList<Contact> = mutableListOf()
        var contentResolver: ContentResolver = getContentResolver()
        var cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if(null != cursor) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    var hasNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
                    if (hasNumber > 0) {
                        var contact = Contact()
                        val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        var name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        contact.setContactName(name)
                        var cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                        while (cursor2.moveToNext()) {
                            var phoneNumer = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            if (!TextUtils.isEmpty(phoneNumer))
                                contact.setContactNumber(phoneNumer);
                        }
                        contactList.add(contact);
                        cursor2.close()
                    }
                }
                setAdapter(contactList)
            }
            cursor.close()
        }else{
            tv_loading_contact_msg.visibility = View.VISIBLE
        }
    }

    private fun setAdapter(contactList: MutableList<Contact>) {
        mRclContact.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val contactAdapter = ContactAdapter(this, this,this)
        mRclContact.adapter = contactAdapter
        contactAdapter.setContactList(contactList)
    }
}
