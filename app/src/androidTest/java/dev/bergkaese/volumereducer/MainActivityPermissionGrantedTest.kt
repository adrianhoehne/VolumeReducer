package dev.bergkaese.volumereducer

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityPermissionGrantedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun clickAllowButton(){
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val allowPermissionButton = device.wait(
            Until.findObject(By.res("com.android.permissioncontroller:id/permission_allow_button")),
            2000
        )

        allowPermissionButton?.click()
        Log.d("ClickAllowButton", "Wait for Idle")
        composeTestRule.waitForIdle()
        Log.d("ClickAllowButton", "starting test...")
    }

    @Test
    fun testThatSliderIsVisibleAfterPermissionsAreGranted(){
        composeTestRule.onNodeWithTag("slider_volume").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_close").assertIsDisplayed()
    }
}