package hr.uniri.szsur.util

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.Nullable


class AppActivityResult<Input, Result> private constructor(
    caller: ActivityResultCaller,
    contract: ActivityResultContract<Input, Result>,
    @field:Nullable @param:Nullable private var onActivityResult: OnActivityResult<Result>?
) {
    /**
     * Callback interface
     */
    fun interface OnActivityResult<O> {
        /**
         * Called after receiving a result from the target activity
         */
        fun onActivityResult(result: O)
    }

    private val launcher: ActivityResultLauncher<Input>
    fun setOnActivityResult(@Nullable onActivityResult: OnActivityResult<Result>?) {
        this.onActivityResult = onActivityResult
    }
    /**
     * Launch activity, same as [ActivityResultLauncher.launch] except that it allows a callback
     * executed after receiving a result from the target activity.
     */
    /**
     * Same as [.launch] with last parameter set to `null`.
     */
    @JvmOverloads
    fun launch(
        input: Input,
        @Nullable onActivityResult: OnActivityResult<Result>? = this.onActivityResult
    ) {
        if (onActivityResult != null) {
            this.onActivityResult = onActivityResult
        }
        launcher.launch(input)
    }

    private fun callOnActivityResult(result: Result) {
        if (onActivityResult != null) onActivityResult!!.onActivityResult(result)
    }

    companion object {
        /**
         * Register activity result using a [ActivityResultContract] and an in-place activity result callback like
         * the default approach. You can still customise callback using [.launch].
         */
        fun <Input, Result> registerForActivityResult(
            caller: ActivityResultCaller,
            contract: ActivityResultContract<Input, Result>,
            @Nullable onActivityResult: OnActivityResult<Result>?
        ): AppActivityResult<Input, Result> {
            return AppActivityResult(caller, contract, onActivityResult)
        }

        /**
         * Same as [.registerForActivityResult] except
         * the last argument is set to `null`.
         */
        fun <Input, Result> registerForActivityResult(
            caller: ActivityResultCaller,
            contract: ActivityResultContract<Input, Result>
        ): AppActivityResult<Input, Result> {
            return registerForActivityResult(caller, contract, null)
        }

        /**
         * Specialised method for launching new activities.
         */
        fun registerActivityForResult(
            caller: ActivityResultCaller
        ): AppActivityResult<Intent, ActivityResult> {
            return registerForActivityResult(caller, StartActivityForResult())
        }
    }

    init {
        launcher = caller.registerForActivityResult(
            contract
        ) { result: Result -> callOnActivityResult(result) }
    }
}
