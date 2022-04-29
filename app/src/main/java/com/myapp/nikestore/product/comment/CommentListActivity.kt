package com.myapp.nikestore.product.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.nikestore.NikeActivity
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.common.EXTRA_KEY_ID
import com.myapp.nikestore.data.Comment
import com.myapp.nikestore.product.CommentAdapter
import com.myapp.nikestore.view.NikeToolbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.ArrayList

class CommentListActivity : NikeActivity() {
    val commentListViewModel:CommentListViewModel by viewModel { parametersOf(intent.extras!!.getInt(EXTRA_KEY_ID)) }
    lateinit var commentsListRv:RecyclerView
    lateinit var commentAdapter: CommentAdapter
    lateinit var toolbar:NikeToolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)
        commentsListRv=findViewById(R.id.commentListRv)
        toolbar=findViewById(R.id.toolbar_comment_list)
        commentListViewModel.commentLiveData.observe(this){
            commentsListRv.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            commentAdapter= CommentAdapter(true)
            commentAdapter.comments= it as ArrayList<Comment>
            commentsListRv.adapter=commentAdapter
        }
        commentListViewModel.prograssLiveData.observe(this){
            setPrograssBarIndicator(it)
        }
        toolbar.onBackButtonClickListener= View.OnClickListener {
            finish()
        }

    }
}