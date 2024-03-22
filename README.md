# PageActivity
### 我做了个思考，用View来代替Activity会不会更好呢？

### 性能?未知
### API？覆盖90%...大概
### 动画？调用anim对象，可以玩出花
### 内存回收？✅
### 启动模式？兼容性未知

## 代码示例
### MainActivity
```kotlin

class MainActivity : PageDocker() {

    override fun onGetHomePage(): Class<out PageActivity> {
        return MainPageActivity::class.java
    }

    override fun onPreInjectRootLayout() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //自定义容器布局
    override fun onCustomDockerLayout(): FrameLayout? {
        return super.onCustomDockerLayout()
    }
}

```

### MainPageActivity
```kotlin

class MainPageActivity(context: Context) : PageActivity(context) {

    private val mViewBinding by lazy { MainPageActivityLayoutBinding.bind(this.getPageWindows().pageActivity) }
    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_activity_layout)

        mViewBinding.btNext.setOnClickListener {
            val intent = Intent(context,SubPageActivity::class.java)
            intent.putExtra("title","标题${++num}")
            startActivityForResult(intent,1)
        }

        mViewBinding.btShowDialog.setOnClickListener {
            val dialog = HomeDialog(this)
            dialog.onClick {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

}

```
### SubPageActivity
```kotlin

class SubPageActivity(context: Context) : PageActivity(context) {

    private val mViewBinding by lazy { SubPageActivityLayoutBinding.bind(this.getPageWindows().pageActivity) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sub_page_activity_layout)

        mViewBinding.btBack.setOnClickListener {
            this.setResult(RESULT_OK, Intent().apply {
                this.data = Uri.parse("https://www.hao123.com")
            })
            this.finish()
        }
        mViewBinding.btNewPage.setOnClickListener {
            startActivity(SubPageActivity::class.java)
        }
    }

    override fun onPreEnterStartAnim(animId: Int, function: () -> Unit) {
        scaleX = 0.8f
        scaleY = 0.8f
        translationX = getScreenWidth().toFloat()
        animate().scaleX(1f).scaleY(1f).translationX(0f).onEnd{
            function()
        }
    }

    override fun onPreExitFinishAnim(function: () -> Unit) {
        animate().scaleX(0.8f).scaleY(0.8f).translationX(getScreenWidth().toFloat()).onEnd{
            function()
        }
    }

    private fun ViewPropertyAnimator.onEnd(function:()->Unit): ViewPropertyAnimator {
        this.setListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                function()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        return this
    }
}

```
### HomeDialog
```kotlin

@SuppressLint("ViewConstructor")
class HomeDialog(mPageActivity: BasePageActivity) : PageDialog(mPageActivity) {

    override fun onCreate() {
        setContentView(R.layout.home_dialog_layout)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        listenerClick(R.id.btOk)
        listenerClick(R.id.btCancel)
    }
}

```