package com.example.sophieslieblingsrezepte.ui.newRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sophieslieblingsrezepte.R
import android.widget.*
import androidx.core.view.children
import com.google.android.material.textfield.TextInputEditText


class NewRecipeFragment : Fragment() {

    private lateinit var newRecipeViewModel: NewRecipeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        newRecipeViewModel =
                ViewModelProvider(this).get(NewRecipeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_recipe, container, false)

        val ll = root.findViewById<TableLayout>(R.id.tableLayoutNewRecipe)
        val addButton = root.findViewById<ImageButton>(R.id.buttonAddIngredient)

        addButton.setOnClickListener{
            addRow(ll)
        }
        return root
    }



    private fun addRow(ll: TableLayout)
    {
        val row = TableRow(context)

        val lp: TableRow.LayoutParams =
            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)

        row.layoutParams = lp
        val spaceHeader = getLayoutParams(ll, R.id.spaceHeader)

        val newEntry = listOf(TextView(context), TextView(context), TextView(context))
        val newSpace = Space(context)

        val oldEntry = listOf<EditText>(
            ll.rootView.findViewById(R.id.editTextAmount),
            ll.rootView.findViewById(R.id.editTextUnit),
            ll.rootView.findViewById(R.id.editTextIngredient)
        )

        for (i: Int in 0..2)
        {
            newEntry[i].text = oldEntry[i].text.toString()
            oldEntry[i].text.clear()
            row.addView(newEntry[i])
            getLayoutParams(newEntry[i]).weight = getLayoutParams(oldEntry[i]).weight
            newEntry[i].textSize = 18f
            newEntry[i].textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        newEntry[2].textAlignment = View.TEXT_ALIGNMENT_TEXT_START

        row.addView(newSpace)
        getLayoutParams(newSpace).weight = spaceHeader.weight

        ll.addView(row, ll.childCount-1)

    }

    private fun getLayoutParams(view: View): LinearLayout.LayoutParams
    {
        return view.layoutParams as LinearLayout.LayoutParams
    }

    private fun getLayoutParams(ll: TableLayout, id:Int): LinearLayout.LayoutParams {
        return (ll.rootView.findViewById<View>(id).layoutParams) as LinearLayout.LayoutParams
    }

}