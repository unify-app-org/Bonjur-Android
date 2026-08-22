package com.bonjur.designSystem.components.alert

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * An alert action that carried a handler used to run the handler and leave the
 * alert on screen — "send reminder" fired but the popup stayed up.
 */
@RunWith(AndroidJUnit4::class)
class AppAlertDismissTest {

    @get:Rule
    val compose = createComposeRule()

    @After
    fun tearDown() {
        AppAlertPresenter.dismiss()
    }

    @Test
    fun actionWithHandlerRunsItAndClosesTheAlert() {
        var handlerRan = false
        compose.setContent { AppAlertOverlay() }

        compose.runOnUiThread {
            AppAlertPresenter.present(
                AppAlert(
                    config = AppAlert.Config(title = "Send reminder?"),
                    actions = listOf(
                        AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.SECONDARY),
                        AppAlert.Action(title = "Send", style = AppAlert.Action.Style.PRIMARY) {
                            handlerRan = true
                        }
                    )
                )
            )
        }
        compose.waitForIdle()
        compose.onNodeWithText("Send reminder?").assertIsDisplayed()

        compose.onNodeWithText("Send").performClick()
        compose.waitForIdle()

        assertTrue("action handler must still run", handlerRan)
        assertNull("alert must be cleared", AppAlertPresenter.currentAlert.value)
        compose.waitUntil(3_000) {
            compose.onAllNodesWithTextOrEmpty("Send reminder?")
        }
    }

    @Test
    fun actionWithoutHandlerStillCloses() {
        compose.setContent { AppAlertOverlay() }

        compose.runOnUiThread {
            AppAlertPresenter.present(
                AppAlert(
                    config = AppAlert.Config(title = "Leave event?"),
                    actions = listOf(
                        AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.SECONDARY)
                    )
                )
            )
        }
        compose.waitForIdle()
        compose.onNodeWithText("Cancel").performClick()
        compose.waitForIdle()

        assertNull(AppAlertPresenter.currentAlert.value)
    }

    /** The exit animation keeps the node briefly, so poll until it's gone. */
    private fun androidx.compose.ui.test.junit4.ComposeContentTestRule.onAllNodesWithTextOrEmpty(
        text: String
    ): Boolean = onAllNodesWithText(text).fetchSemanticsNodes().isEmpty()
}
