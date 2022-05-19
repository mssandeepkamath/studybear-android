package com.example.studybear.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.studybear.R


class DiscussFragment : Fragment() {
    lateinit var webDisqus: WebView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_discuss, container, false)

        webDisqus = view.findViewById(R.id.vwDisqus)
        webDisqus.settings.javaScriptEnabled = true
        webDisqus.webViewClient = WebViewClient()


        return view
    }







}