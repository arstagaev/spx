package com.revolve44.solarpanelx.ui.fragments.features

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.revolve44.solarpanelx.R
import com.revolve44.solarpanelx.datasource.local.PreferenceMaestro
import com.revolve44.solarpanelx.domain.base.materialdialog.BaseMaterialDialogFragment
import com.revolve44.solarpanelx.domain.core.mainTerminalGate
import com.revolve44.solarpanelx.domain.core.setLocale
import com.revolve44.solarpanelx.domain.enums.LanguagesOfApp
import com.revolve44.solarpanelx.feature_modules.terminal.terminalRouter
import timber.log.Timber
import kotlin.collections.ArrayList


class FragmentTerminal : Fragment(R.layout.dialog_fragment_terminal_for_commands) {

    private lateinit var mListView             : ListView
    private lateinit var editCommands          : EditText
    private lateinit var okay_confirm_language : Button
    private lateinit var terminal_body_of_text : TextView
    private var textBodyOfTerminal : String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // access the listView from xml file
        okay_confirm_language = view.findViewById(R.id.terminal_send_command)
        editCommands = view.findViewById(R.id.input_commands_stroke)
        terminal_body_of_text = view.findViewById(R.id.terminal_body_of_text)

        editCommands.addTextChangedListener( object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.i("input to Command ${(editCommands.text)} ")
                //terminal_body_of_text.text = "$s"
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        okay_confirm_language.setOnClickListener {
            textBodyOfTerminal += "\n ${editCommands.text}"

            terminal_body_of_text.text = terminalRouter(textBodyOfTerminal,editCommands.text.toString())
            //terminal_body_of_text.setTextColor()
            editCommands.setText("")

            //Toast.makeText(requireActivity(), mainTerminalGate((editCommands.text).toString()),Toast.LENGTH_LONG).show()

        }
    }

}