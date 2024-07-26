package com.qamar.glancewithgemini

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.Text
import androidx.lifecycle.viewmodel.compose.viewModel

val images = arrayOf(
    // Image generated using Gemini from the prompt "cupcake image"
    R.drawable.baked_goods_1,
    // Image generated using Gemini from the prompt "cookies images"
    R.drawable.baked_goods_2,
    // Image generated using Gemini from the prompt "cake images"
    R.drawable.baked_goods_3,
)
val imageDescriptions = arrayOf(
    R.string.image1_description,
    R.string.image2_description,
    R.string.image3_description,
)

@Composable
fun BakingScreen(
    bakingViewModel: BakingViewModel = viewModel()
) {
    val selectedImage = remember { mutableIntStateOf(0) }
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    val placeholderResult = stringResource(R.string.results_placeholder)
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by bakingViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        GlanceModifier.fillMaxWidth()
            .padding(10.dp)
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Baking with Gemini",
            modifier = GlanceModifier.padding(16.dp)
        )
//
//        Row(
//            modifier = GlanceModifier.fillMaxWidth()
//        ) {
//            androidx.glance.Image(provider = ImageProvider(R.drawable.baked_goods_1),
//                contentDescription = "",
//                modifier = GlanceModifier.size(40.dp))
//        }
//
//        Row(
//            modifier = GlanceModifier.padding(all = 16.dp)
//        ) {
//            Button(
//                text = stringResource(R.string.action_go),
//                onClick = {
//                    val bitmap = BitmapFactory.decodeResource(
//                        context.resources,
//                        images[selectedImage.intValue]
//                    )
//                    bakingViewModel.sendPrompt(bitmap, prompt)
//                },
//                enabled = prompt.isNotEmpty(),
//            )
//        }
//
//        if (uiState is UiState.Loading) {
//            CircularProgressIndicator()
//        } else {
//            if (uiState is UiState.Error) {
//                result = (uiState as UiState.Error).errorMessage
//            } else if (uiState is UiState.Success) {
//                result = (uiState as UiState.Success).outputText
//            }
//            Text(
//                text = result,
//                modifier = GlanceModifier
//                    .padding(16.dp)
//                    .fillMaxSize()
//            )
//        }
    }
}