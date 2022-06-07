package com.example.studybear.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.studybear.R
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit


class LeaderBoardFragment : Fragment() {
    lateinit var viewKonfetti: KonfettiView
    lateinit var simpleWebView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view= inflater.inflate(R.layout.fragment_leader_board, container, false)
        simpleWebView=view.findViewById(R.id.vwLeaderBoardWeb)
        simpleWebView.visibility=View.GONE
        viewKonfetti=view.findViewById(R.id.konfettiViewLeaderboard)
        val party= Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 20000, TimeUnit.MILLISECONDS).max(1000),
            position = Position.Relative(0.5, 0.0)
        )
        viewKonfetti.start(party)
        simpleWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
               simpleWebView.visibility=View.VISIBLE
                viewKonfetti.stop(party)
                super.onPageFinished(view, url)
            }
        })
        val url = "https://studybear-79c4e.web.app/leaderboard"
        simpleWebView.settings.javaScriptEnabled = true
        simpleWebView.loadUrl(url)
        return view
    }

}