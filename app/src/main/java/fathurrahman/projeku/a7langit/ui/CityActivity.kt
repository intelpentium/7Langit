package fathurrahman.projeku.a7langit.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import fathurrahman.projeku.a7langit.databinding.ActivityCityBinding
import fathurrahman.projeku.a7langit.db.table.FavoriteTable
import fathurrahman.projeku.a7langit.ext.observe
import fathurrahman.projeku.a7langit.modules.base.BaseActivity
import fathurrahman.projeku.a7langit.ui.adapter.AdapterCity
import fathurrahman.projeku.a7langit.ui.adapter.AdapterMain
import fathurrahman.projeku.a7langit.viewmodel.MainViewModel
import fathurrahman.projeku.a7langit.viewmodel.RepoViewModel
import androidx.core.widget.addTextChangedListener
import fathurrahman.projeku.a7langit.ext.hideKeyboard
import java.util.Locale.filter

class CityActivity : BaseActivity(){
    lateinit var mBinding: ActivityCityBinding
    private val viewModel by viewModels<MainViewModel>()
    private val viewModelRepo by viewModels<RepoViewModel>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE){
        AdapterCity(::onClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
        handleState()
    }

    private fun initView() {

        mBinding.rv.adapter = adapter
    }

    private fun handleState() {

        observe(viewModelRepo.getFavorite()) {
            if (it != null) {
                adapter.clearData()
                adapter.insertData(it)
            }
        }
    }

    private fun onClick(data: FavoriteTable) {
        val intent = Intent()
        intent.putExtra("name", data.name)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}