package com.digiecsoft.myappvb.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.digiecsoft.myappvb.R
import com.google.firebase.auth.FirebaseAuth

class VisitorFragment : Fragment() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth.signOut()
        return inflater.inflate(R.layout.fragment_visitor, container, false)
    }

}