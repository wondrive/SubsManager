package com.example.homecookhelper.recipe

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.homecookhelper.R
import com.example.homecookhelper.database.DatabaseModule
import com.example.homecookhelper.entity.RecipeEntity
import com.google.firebase.auth.FirebaseAuth
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import kotlinx.android.synthetic.main.fragment_recipe_write.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class RecipeWriteFragment : DialogFragment(), BottomSheetImagePicker.OnImagesSelectedListener {

    /* note 객체 생성 및 초기화. */
    private var recipe =
        RecipeEntity(recipeContent = "", recipeTitle = "", recipeImg = null, userId = "")

    //noteDao 참조
    private val dao by lazy { DatabaseModule.getDatabase(requireContext()).recipeDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* 추가/수정 다이얼로그 위한 레이아웃(dialog_note_create.xml) inflate */
        val rootView = inflater.inflate(R.layout.fragment_recipe_write, container, false)

        /* 사진을 누를경우 사진을 선택하게해주고 onImagesSelect로 받게되는 라이브러리를 사용*/
        rootView.img_profile.setOnClickListener {
            //사용한 플러그인
            //https://github.com/kroegerama/bottomsheet-imagepicker
            BottomSheetImagePicker.Builder("빌드")
                .cameraButton(ButtonType.Button)//카메라 버튼 표시
                .galleryButton(ButtonType.Button)//갤러리 버튼 표시
                .singleSelectTitle(R.string.select_title) //제목글
                .requestTag("single")//1개만 선택하게할 경우 single
                .show(childFragmentManager)
        }
        return rootView
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* 만약 Tag값(Note id)이 있으면 생성이 아니라 수정
           - 따라서 수정을 위해 데이터를 불러와야 함
        */
        tag?.toLongOrNull()?.let { recipeId ->
            viewLifecycleOwner.lifecycleScope.launch {
                /* 미리 변수를 선언 */
                var savedRecipe: RecipeEntity? = null
                /* 노트를 쿼리.*/
                withContext(Dispatchers.IO) {
                    savedRecipe = dao.selectRecipe(recipeId)
                }
                /* 노트가 존재한다면 note를 변경*/
                savedRecipe?.let {
                    recipe = it //쿼리한 노트 객체를 note에 저장
                    view.txt_title.setText(it.recipeTitle)
                    view.txt_content.setText(it.recipeContent)
                    it.recipeImg?.let { recipeImg ->
                        view.img_profile.setImageURI(Uri.parse(recipeImg))
                    }
                }

            }
        }//end of let

        //수정하고 저장하기 버튼을 클릭한 경우(DB에 수정사항 저장)
        view.btn_save.setOnClickListener {
            //입력한 noteTitle, noteContent 가져와서 변수에 할당
            recipe.recipeTitle = view.txt_title.text.toString()
            recipe.recipeContent = view.txt_content.text.toString()

            /* 제목과 내용이 다 있는지를 검증 */
            if (recipe.recipeTitle.isBlank() && recipe.recipeContent.isBlank()) {
                Toast.makeText(requireContext(), "제목과 내용을 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                var userId: String? = ""
                FirebaseAuth.getInstance().currentUser?.let {
                    userId = it.email
                }

                /* 자동 스코프에 맞추어 코루틴을 실행*/
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val recipe = RecipeEntity(
                        recipeId = recipe.recipeId,
                        recipeTitle = recipe.recipeTitle,
                        recipeContent = recipe.recipeContent,
                        recipeImg = recipe.recipeImg,
                        userId = userId.toString()
                    )
                    dao.insertRecipes(recipe)//DB에 저장
                    //dismiss()//다이얼로드 종료
                }

                findNavController().popBackStack()
            }
        }//end of view.btn_save.setOnClickListener
    }//end of onViewCreated

    //이미지 피커에서 사진을 선택했을 때
    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        /* 사진이 선택되었을때 uris는 array 길이가 1개인 형태로 들어온다 */
        if (uris.isNotEmpty()) {
            /* 추후 디비저장을 위해 변수를 할당*/
            recipe.recipeImg = uris[0].toString()//path를 db에 저장
            view?.img_profile?.setImageURI(uris[0]);//사진을 뷰에 설정
        }
    }//end of onImagesSelected
}