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



    fun addRow(ll: TableLayout)
    {
        val row = TableRow(context)

        val lp: TableRow.LayoutParams =
            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)

        row.setLayoutParams(lp)

        val amountHeader = (ll.rootView.findViewById<TextView>(R.id.textViewAmount).layoutParams) as LinearLayout.LayoutParams
        val unitHeader = ll.rootView.findViewById<TextView>(R.id.textViewUnit).layoutParams as LinearLayout.LayoutParams
        val ingredientHeader = ll.rootView.findViewById<TextView>(R.id.textViewIngredient).layoutParams as LinearLayout.LayoutParams
        val spaceHeader = ll.rootView.findViewById<Space>(R.id.spaceHeader).layoutParams as LinearLayout.LayoutParams

        val amount = ll.rootView.findViewById<TextView>(R.id.editTextAmount).text
        val unit = ll.rootView.findViewById<TextView>(R.id.editTextUnit).text
        val ingredient = ll.rootView.findViewById<TextView>(R.id.editTextIngredient).text


        ll.rootView.findViewById<TextView>(R.id.editTextAmount).text = ""
        ll.rootView.findViewById<TextView>(R.id.editTextUnit).text = ""
        ll.rootView.findViewById<TextView>(R.id.editTextIngredient).text = ""

        val newAmount = TextView(context)
        val newUnit = TextView(context)
        val newIngredient = TextView(context)
        val newSpace = android.widget.Space(context)

        newAmount.text = amount
        newUnit.text = unit
        newIngredient.text = ingredient


        row.addView(newAmount)
        row.addView(newUnit)
        row.addView(newIngredient)
        row.addView(newSpace)

        (newAmount.layoutParams as LinearLayout.LayoutParams).weight = amountHeader.weight
        (newUnit.layoutParams as LinearLayout.LayoutParams).weight = unitHeader.weight
        (newIngredient.layoutParams as LinearLayout.LayoutParams).weight = ingredientHeader.weight
        (newSpace.layoutParams as LinearLayout.LayoutParams).weight = spaceHeader.weight

        newAmount.textAlignment = View.TEXT_ALIGNMENT_CENTER
        newUnit.textAlignment = View.TEXT_ALIGNMENT_CENTER
        newIngredient.textAlignment = View.TEXT_ALIGNMENT_TEXT_START

        ll.addView(row, ll.childCount-1)

    }




}