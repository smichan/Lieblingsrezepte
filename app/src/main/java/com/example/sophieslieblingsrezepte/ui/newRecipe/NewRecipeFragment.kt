package com.example.sophieslieblingsrezepte.ui.newRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sophieslieblingsrezepte.R
import android.widget.*
import com.example.sophieslieblingsrezepte.data.Result
import com.example.sophieslieblingsrezepte.data.model.Ingredient
import com.example.sophieslieblingsrezepte.data.model.Step
import com.example.sophieslieblingsrezepte.ui.serverConnection.ServerConnector


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

        val li = root.findViewById<TableLayout>(R.id.tableLayoutNewRecipe)
        val ls = root.findViewById<TableLayout>(R.id.tableLayoutInstructions)
        val addIngredientButton = root.findViewById<ImageButton>(R.id.buttonAddIngredient)
        val addStepButton = root.findViewById<ImageButton>(R.id.buttonAddStep)
        val addNewRecipeButton = root.findViewById<Button>(R.id.buttonAddNewRecipe)
        val recipeNameEditText = root.findViewById<EditText>(R.id.textRecipeTitle)

        addIngredientButton.setOnClickListener{
            val ingredient = getNewIngredient(li)
            newRecipeViewModel.addIngredient(ingredient)

        }

        addStepButton.setOnClickListener{
            val step = getNewStep(ls)
            newRecipeViewModel.addStep(step)
        }

        addNewRecipeButton.setOnClickListener{
            var recipeName = recipeNameEditText.text.toString()
            newRecipeViewModel.setName(recipeName)

            val token = requireActivity().intent.getStringExtra("Token")

            val serverConnector = ServerConnector(token!!)
            var resultSuccess = serverConnector.saveNewRecipe(newRecipeViewModel)

            if (resultSuccess is Result.Success)
            {
                println("Recipe created successful")
                Toast.makeText(
                    context,
                    recipeName + " has been created successful!",
                    Toast.LENGTH_LONG
                ).show()
            }
            else
            {
                Toast.makeText(
                    context,
                    "Something went wrong. Please check your input and try again.",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        newRecipeViewModel.ingredients.observe(viewLifecycleOwner, {
            modelChangedIngredients(li, it)
        })

        newRecipeViewModel.steps.observe(viewLifecycleOwner, {
            modelChangedSteps(ls, it)
        })

        newRecipeViewModel.name.observe(viewLifecycleOwner, {
            recipeNameEditText.setText(it)
        })

        return root
    }

    private fun modelChangedIngredients(ingredientTable: TableLayout, ingredients : List<Ingredient>)
    {
        // -2: do not remove the editable fields for the next ingredient
        ingredientTable.removeViews(1, ingredientTable.childCount - 2)
        ingredients.forEach{ x -> addIngredientToGUI(ingredientTable, x)}
    }

    private fun modelChangedSteps(stepTable: TableLayout, steps : List<Step>)
    {
        stepTable.removeViews(1, stepTable.childCount - 2)
        steps.forEach{ x -> addStepToGUI(stepTable, x)}
    }

    private fun getNewIngredient(ll: TableLayout) : Ingredient
    {
        val entry = listOf<EditText>(
        ll.rootView.findViewById(R.id.editTextAmount),
        ll.rootView.findViewById(R.id.editTextUnit),
        ll.rootView.findViewById(R.id.editTextIngredient)
    )
        val newIngredient = Ingredient(entry[0].text.toString(), entry[1].text.toString(), entry[2].text.toString())

        for (i: Int in 0..2)
        {
            entry[i].text.clear()
        }
        return newIngredient
    }

    private fun getNewStep(ll: TableLayout) : Step
    {
        val entry = ll.rootView.findViewById<EditText>(R.id.editTextStep)
        val newStep = Step(entry.text.toString(), 0)
        entry.text.clear()
        return newStep
    }

    private fun addIngredientToGUI(ll: TableLayout, ingredient: Ingredient)
    {
        val row = TableRow(context)

        val lp: TableRow.LayoutParams =
            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
        row.layoutParams = lp

        val spaceHeader = getLayoutParams(ll, R.id.spaceHeader)

        val newEntry = listOf(TextView(context), TextView(context), TextView(context))

        val oldEntry = listOf<EditText>(
            ll.rootView.findViewById(R.id.editTextAmount),
            ll.rootView.findViewById(R.id.editTextUnit),
            ll.rootView.findViewById(R.id.editTextIngredient)
        )
        newEntry[0].text = ingredient.amount
        newEntry[1].text = ingredient.unit
        newEntry[2].text = ingredient.ingredientName

        for (i: Int in 0..2)
        {
            row.addView(newEntry[i])
            getLayoutParams(newEntry[i]).weight = getLayoutParams(oldEntry[i]).weight
            newEntry[i].textSize = 18f
            newEntry[i].textAlignment = View.TEXT_ALIGNMENT_CENTER
            newEntry[i].isSingleLine = i<2
            newEntry[i].width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        newEntry[2].textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        getLayoutParams(newEntry[2]).leftMargin = 40
        getLayoutParams(newEntry[2]).weight += spaceHeader.weight

        ll.addView(row, ll.childCount-1)
    }

    private fun addStepToGUI(ll: TableLayout, step: Step)
    {
        val row = TableRow(context)
        val lp: TableRow.LayoutParams =
            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)
        row.layoutParams = lp

        val entry = TextView(context)
        entry.text = step.description

        row.addView(entry)
        entry.textSize = 18f
        entry.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        entry.width = ll.rootView.findViewById<TextView>(R.id.textViewStep).width

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