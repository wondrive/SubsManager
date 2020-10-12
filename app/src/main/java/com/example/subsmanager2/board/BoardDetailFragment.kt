//package com.example.subsmanager2.board
//
//import android.net.Uri
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.lifecycle.Observer
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.subsmanager2.R
//import com.example.subsmanager2.database.DatabaseModule
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.fragment_board_detail.view.*
//import kotlinx.android.synthetic.main.fragment_board_list.view.*
//import kotlinx.android.synthetic.main.fragment_board_list.view.fab_add_note
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.util.*
//
//
//class BoardDetailFragment : Fragment() {
//
//    //boardDao 참조
//    private val boardDao by lazy { DatabaseModule.getDatabase(requireContext()).boardDao() }
//
//    //fragment_detail 뷰를 생성(인플레이션)하여 반환
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        return inflater.inflate(R.layout.fragment_board_detail, container, false)
//    }//end of onCreateView
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // arguments에서 BOARD_ID 추출
//        val boardId = arguments?.getLong("BOARD_ID") ?: kotlin.run { throw Error("BOARD_ID가 없습니다.") }
//        val userId = arguments?.getString("USER_ID") ?: kotlin.run { throw Error("USER_ID가 없습니다.") }
//
//        // 본인 게시글일때만 수정, 삭제 버튼 보이게
//        FirebaseAuth.getInstance().currentUser?.let {
//            if(it.email.equals(userId)) {
//                view.btn_detail_edit.visibility = View.VISIBLE
//                view.btn_detail_delete.visibility = View.VISIBLE
//            } else {
//                view.btn_detail_edit.visibility = View.GONE
//                view.btn_detail_delete.visibility = View.GONE
//            }
//        }
//
//        /* DetailFragment에서 LiveData observe
//           - DB에서 트랜잭션(Transaction)이 발생하면 UI를 갱신
//        */
//        boardDao.selectLiveBoard(boardId).observe(viewLifecycleOwner, Observer {
//           view.txt_board_title.setText(it.boardTitle);
//           view.txt_board_content.setText(it.boardContent);
//           view.txt_user_id.setText(it.userId);
//           it.boardImg?.let { uri -> view.img_board.setImageURI(Uri.parse(uri)) };
//        });
//
//        /* 수정버튼 */
//        view.btn_detail_edit.setOnClickListener {
//            //childFragmentManager:  Fragment에서 다른 Fragment를 만들 때(DetailFragment에서 DialFragment 만들 때)
//            //BoardWriteFragment().show(childFragmentManager, recipeId.toString())
//
//
//            findNavController().navigate(R.id.action_boardDetailFragment_to_writeBoard,
//                Bundle().apply {
//                    putLong("BOARD_ID", boardId!!)
//                    putString("UPDATE_YN", "Y")
//                })
//        }
//
//
//        /* Note 삭제버튼 */
//        view.btn_detail_delete.setOnClickListener {
//            /* 삭제*/
//            lifecycleScope.launch (Dispatchers.IO){
//                boardDao.deleteBoard(boardId)
//            }
//            Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
//            //뒤로가기
//            findNavController().popBackStack()
//        }
//    }//end of onViewCreated
//
//}
//
