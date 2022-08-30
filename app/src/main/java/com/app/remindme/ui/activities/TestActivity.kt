package com.app.remindme.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.remindme.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


/*            logThis("Contact, ID: ${contact.id} Lookup Key: ${contact.lookupKey} Display name: ${contact.displayNamePrimary} Display name alt: ${contact.displayNameAlt} Photo Uri: ${contact.photoUri} Thumbnail Uri: ${contact.photoThumbnailUri} Last updated: ${contact.lastUpdatedTimestamp} Starred?: ${contact.options?.starred} Send to voicemail?: ${contact.options?.sendToVoicemail} Ringtone: ${contact.options?.customRingtone}

        Aggregate data from all RawContacts of the contact
        -----------------------------------
        Addresses: ${contact.addressList()}
        Emails: ${contact.emailList()}
        Events: ${contact.eventList()}
        Group memberships: ${contact.groupMembershipList()}
        IMs: ${contact.imList()}
        Names: ${contact.nameList()}
        Nicknames: ${contact.nicknameList()}
        Notes: ${contact.noteList()}
        Organizations: ${contact.organizationList()}
        Phones: ${contact.phoneList()}
        Relations: ${contact.relationList()}
       + SipAddresses: ${contact.sipAddressList()}+
        Websites: ${contact.websiteList()}".trimIndent()
            // There are also aggregate data functions that return a sequence instead of a list.
        )*/
    }
}