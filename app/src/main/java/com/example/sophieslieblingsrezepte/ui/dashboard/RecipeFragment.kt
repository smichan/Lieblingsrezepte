package com.example.sophieslieblingsrezepte.ui.dashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.data.model.Ingredient
import com.example.sophieslieblingsrezepte.data.model.Step
import com.example.sophieslieblingsrezepte.databinding.FragmentShowRecipeBinding
import org.json.JSONArray
import org.json.JSONObject

class RecipeFragment : Fragment() {
    private lateinit var _viewModel: RecipeViewModel
    private lateinit var _binding: FragmentShowRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowRecipeBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        loadRecipe()

        return _binding.root
    }

    private fun loadRecipe() {
        val jsonString = requireActivity().intent.getStringExtra("Json")
        val jsonObject = JSONObject(jsonString)
        _viewModel.addRecipe(jsonObject)

        val li = _binding.root.findViewById<TableLayout>(R.id.tableLayoutShowRecipe)
        val ls = _binding.root.findViewById<TableLayout>(R.id.tableLayoutDirections)
        addIngredientsToGUI(li)
        addDirectionsToGUI(ls)
    }

    private fun addIngredientsToGUI(li: TableLayout)
    {
        for (i in _viewModel.recipe.ingredients)
        {
            addIngredientToGUI(li, i)
        }
    }

    private fun addDirectionsToGUI(ls: TableLayout)
    {
        for (i in _viewModel.recipe.steps)
        {
            addStepToGUI(ls, i)
        }
    }
    private fun addIngredientToGUI(ll: TableLayout, ingredient: Ingredient)
    {
        val row = TableRow(context)

        val lp: TableRow.LayoutParams =
            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
        row.layoutParams = lp

        val newEntry = listOf(TextView(context), TextView(context), TextView(context))

        val headerEntry = listOf<TextView>(
            ll.rootView.findViewById(R.id.textViewAmount),
            ll.rootView.findViewById(R.id.textViewUnit),
            ll.rootView.findViewById(R.id.textViewIngredient)
        )
        newEntry[0].text = ingredient.amount
        newEntry[1].text = ingredient.unit
        newEntry[2].text = ingredient.ingredientName

        for (i: Int in 0..2)
        {
            row.addView(newEntry[i])
            getLayoutParams(newEntry[i]).weight = getLayoutParams(headerEntry[i]).weight
            newEntry[i].textSize = 18f
            newEntry[i].textAlignment = View.TEXT_ALIGNMENT_CENTER
            newEntry[i].isSingleLine = i<2
            newEntry[i].width = headerEntry[i].width
        }
        newEntry[2].textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        getLayoutParams(newEntry[2]).leftMargin = 40

        ll.addView(row)
    }

    private fun addStepToGUI(ll: TableLayout, step: Step)
    {
        val row = TableRow(context)
        val lp: TableRow.LayoutParams =
            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
        row.layoutParams = lp

        val headerEntry = ll.rootView.findViewById<TextView>(R.id.textViewStep)
        val entry = TextView(context)

        row.addView(entry)
        getLayoutParams(entry).weight = getLayoutParams(headerEntry).weight
        entry.text = step.description
        entry.textSize = 18f
        entry.textAlignment = View.TEXT_ALIGNMENT_TEXT_START

        entry.width = headerEntry.width

        ll.addView(row)
    }

    private fun getLayoutParams(view: View): LinearLayout.LayoutParams
    {
        return view.layoutParams as LinearLayout.LayoutParams
    }

    private fun getLayoutParams(ll: TableLayout, id:Int): LinearLayout.LayoutParams {
        return (ll.rootView.findViewById<View>(id).layoutParams) as LinearLayout.LayoutParams
    }

}