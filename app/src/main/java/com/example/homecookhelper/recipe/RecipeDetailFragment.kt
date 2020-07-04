package com.example.homecookhelper.recipe

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.homecookhelper.R
import com.example.homecookhelper.database.DatabaseModule
import kotlinx.android.synthetic.main.fragment_recipe_detail.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment() {

    //recipeDao 참조
    private val recipeDao by lazy { DatabaseModule.getDatabase(requireContext()).recipeDao() }

    //fragment_detail 뷰를 생성(인플레이션)하여 반환
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // arguments에서 RECIPE_ID 추출
        val recipeId = arguments?.getLong("RECIPE_ID") ?: kotlin.run { throw Error("RECIPE_ID가 없습니다.") }

        /* DetailFragment에서 LiveData observe
           - DB에서 트랜잭션(Transaction)이 발생하면 UI를 갱신
        */
        recipeDao.selectLiveRecipe(recipeId).observe(viewLifecycleOwner, Observer {
            view.txt_recipe_title.setText(it.recipeTitle)
            view.txt_recipe_content.setText(it.recipeContent)
            it.recipeImg?.let { uri -> view.img_recipe.setImageURI(Uri.parse(uri)) }
        })

        /* 수정버튼 */
        view.btn_detail_edit.setOnClickListener {
            //childFragmentManager:  Fragment에서 다른 Fragment를 만들 때(DetailFragment에서 DialFragment 만들 때)
            //RecipeWriteFragment().show(childFragmentManager, recipeId.toString())
            findNavController().navigate(R.id.action_recipeListFragment_to_writeRecipe)
        }

        /* Note 삭제버튼 */
        view.btn_detail_delete.setOnClickListener {
            /* 삭제*/
            lifecycleScope.launch(Dispatchers.IO){
                recipeDao.deleteRecipe(recipeId)
            }
            //viewModel.deleteNote(noteId, requireContext())
            /*뒤로가기
            * popBackStack(): 뒤로가기
            * */
            findNavController().popBackStack()
        }
    }//end of onViewCreated
}
