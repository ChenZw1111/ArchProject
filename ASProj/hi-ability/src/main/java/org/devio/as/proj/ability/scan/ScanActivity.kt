package org.devio.`as`.proj.ability.scan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.huawei.hms.ml.scan.HmsScanFrame
import com.huawei.hms.ml.scan.HmsScanFrameOptions

import com.huawei.secure.android.common.sign.HiPkgSignManager
import org.devio.`as`.proj.ability.HiAbility
import org.devio.`as`.proj.ability.R
import org.devio.`as`.proj.ability.databinding.AbilityActivityScanBinding
import org.devio.`as`.proj.ability.util.HiDisplayUtil

internal class ScanActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_SELECT_PHOTO = 10000;
    }
    private val TAG = "ScanActivity"
    private lateinit var remoteView: RemoteView
    private lateinit var binding: AbilityActivityScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AbilityActivityScanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val rect = Rect()
        val screenWidth = HiDisplayUtil.getDisplayWidthInPx(this)
        val screenHeight = HiDisplayUtil.getDisplayHeightInPx(this)
        val screenFrameSize = HiDisplayUtil.dp2px(240f, this)

        rect.left = screenWidth / 2 - screenFrameSize / 2
        rect.right = screenWidth / 2 + screenFrameSize / 2
        rect.top = screenHeight / 2 - screenFrameSize / 2
        rect.bottom = screenHeight / 2 + screenFrameSize / 2

        remoteView = RemoteView.Builder().setContext(this).setBoundingBox(rect)
            .setFormat(HmsScan.ALL_SCAN_TYPE).build()

        remoteView.setOnLightVisibleCallback { visiable ->
            binding.flushBtn.visibility = if (visiable) View.VISIBLE else
                View.GONE
        }
        remoteView.setOnResultCallback{ results ->
            if (results != null && results.isNotEmpty() && results[0] != null && !TextUtils.isEmpty(
                    results[0].originalValue
                )

            ) {
                showResult(results[0])
            }
        }
        remoteView.onCreate(savedInstanceState)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        binding.container.addView(remoteView, params)

        initOperation()
    }

    private fun initOperation() {
        binding.backImg.setOnClickListener {
            finish()
        }

        binding.flushBtn.setOnClickListener {
            if (remoteView.lightStatus) {
                remoteView.switchLight()
                binding.flushBtn.setImageResource(R.drawable.scan_flushlight_on)
            } else {
                remoteView.switchLight()
                binding.flushBtn.setImageResource(R.drawable.scan_flushlight_off)
            }
        }

        binding.selectPhoto.setOnClickListener {
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "Image/*"
            )
            startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SELECT_PHOTO && data != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
//             “QRCODE_SCAN_TYPE”和“DATAMATRIX_SCAN_TYPE”表示只扫描QR和DataMatrix的码
            val options = HmsScanAnalyzerOptions.Creator().
            setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).setPhotoMode(true).create()
            val results = ScanUtil.decodeWithBitmap(
                this,
                bitmap,
                options
            )
            if (results != null && results.isNotEmpty() && results[0] != null && !TextUtils.isEmpty(
                    results[0].originalValue
                )

            ) {
                    showResult(results[0])
            }
        }
    }

    private fun showResult(hmsScan: HmsScan) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(300)
        HiAbility.onScanResult(hmsScan)
        Log.e(TAG,"scan_type: "+ hmsScan.scanType + ",scan_result:" + hmsScan.originalValue)
        finish()
    }

    override fun onStart() {
        super.onStart()
        remoteView.onStart()
    }

    override fun onResume() {
        super.onResume()
        remoteView.onResume()
    }

    override fun onPause() {
        super.onPause()
        remoteView.onPause()
    }

    override fun onStop() {
        super.onStop()
        remoteView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView.onDestroy()
    }
}