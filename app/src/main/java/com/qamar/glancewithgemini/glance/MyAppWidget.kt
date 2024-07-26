package com.qamar.glancewithgemini.glance

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.Visibility
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.visibility
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.qamar.glancewithgemini.BuildConfig
import com.qamar.glancewithgemini.R
import com.qamar.glancewithgemini.ui.theme.Blue

val images = arrayOf(
    // Image generated using Gemini from the prompt "cupcake image"
    R.drawable.breakfast_1,
    // Image generated using Gemini from the prompt "cookies images"
    R.drawable.breakfast_2,
    // Image generated using Gemini from the prompt "cake images"
    R.drawable.breakfast_3,
    // Image generated using Gemini from the prompt "cake images"
    R.drawable.breakfast_4,
)
private val result = stringPreferencesKey("result")
private val randomImage = intPreferencesKey("randomImage")
private val isLoading = booleanPreferencesKey("isLoading")

class MyAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val result = currentState(result) ?: ""
            val isLoading = currentState(isLoading) ?: false
            val randomImage = currentState(randomImage) ?: R.drawable.breakfast_2
            Column(
                GlanceModifier.fillMaxWidth()
                    .padding(10.dp)
                    .wrapContentHeight()
                    .background(Blue),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(GlanceModifier.height(10.dp))
                Text(
                    text = "Breakfast with Breakfasty!", style = TextStyle(
                        color = ColorProvider(Color.White)
                    )
                )
                Spacer(GlanceModifier.height(10.dp))
                Image(
                    provider = ImageProvider(randomImage),
                    contentDescription = "",
                    modifier = GlanceModifier.fillMaxWidth()
                        .height(100.dp)
                        .cornerRadius(25.dp)
                        .clickable(actionRunCallback<GetRandomImage>()),
                    contentScale = ContentScale.Crop
                )

                Spacer(GlanceModifier.height(10.dp))

                androidx.glance.appwidget.lazy.LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    item {
                        if (isLoading.not()) {
                            Text(
                                text = result, style = TextStyle(
                                    color = ColorProvider(Color.White)
                                )
                            )
                        }
                    }
                    item {
                        Spacer(GlanceModifier.height(20.dp))

                        CircularProgressIndicator(
                            color = ColorProvider(Color.White),
                            modifier = GlanceModifier.visibility(
                                if (isLoading)
                                    Visibility.Visible
                                else Visibility.Gone
                            ).size(30.dp)
                        )
                        Button(
                            text = "get recipe", onClick = actionRunCallback<GetRecipe>(
                                parameters = actionParametersOf(
                                    actionWidgetKey to randomImage
                                )
                            ),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = ColorProvider(
                                    Color.White
                                ),
                                contentColor = ColorProvider(Blue)
                            ),
                            modifier = GlanceModifier.visibility(
                                if (isLoading.not())
                                    Visibility.Visible
                                else Visibility.Gone
                            )
                        )
                    }
                }
            }
        }
    }
}

val actionWidgetKey = ActionParameters.Key<Int>("action-widget-key")

class GetRecipe : ActionCallback {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val drawableRes = parameters[actionWidgetKey] ?: R.drawable.baked_goods_2
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[isLoading] = true
        }
        MyAppWidget().update(context, glanceId)
        updateAppWidgetState(context, glanceId) { prefs ->
            val bitmap = BitmapFactory.decodeResource(
                context.resources,
                drawableRes
            )
            val response = generativeModel.generateContent(
                content {
                    image(bitmap)
                    text("Provide a recipe for the breakfast goods in the image")
                }
            )
            response.text?.let { outputContent ->
                prefs[isLoading] = false
                prefs[result] = outputContent
            }
        }
        MyAppWidget().update(context, glanceId)
    }
}

class GetRandomImage : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[randomImage] = images.random()
        }
        MyAppWidget().update(context, glanceId)
    }
}