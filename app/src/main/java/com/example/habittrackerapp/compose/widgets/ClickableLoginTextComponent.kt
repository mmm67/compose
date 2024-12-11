package com.example.habittrackerapp.compose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ClickableLoginTextComponent(
    regularText: String,
    clickableText: String,
    onClick: () -> Unit,
    regularTextStyle: SpanStyle = SpanStyle(color = Color.Black),
    clickableTextStyle: SpanStyle = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
    overallTextStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodySmall
) {
    // Create an AnnotatedString with different styles for the regular and clickable text
    val annotatedString = buildAnnotatedString {
        // Append the regular text
        append(regularText)
        addStyle(
            style = regularTextStyle,
            start = 0,
            end = regularText.length
        )

        // Append the clickable text
        val clickableTextStartIndex = length
        append(clickableText)
        addStyle(
            style = clickableTextStyle,
            start = clickableTextStartIndex,
            end = length
        )

        // Add an annotation to make the clickable text actionable
        addStringAnnotation(
            tag = "ClickableText",
            annotation = clickableText,
            start = clickableTextStartIndex,
            end = length
        )
    }

    // Display the text and handle clicks on the clickable part
    Text(
        text = annotatedString,
        style = overallTextStyle,
        modifier = Modifier.clickable {
            annotatedString.getStringAnnotations("ClickableText", 0, annotatedString.length)
                .firstOrNull()?.let {
                    onClick()
                }
        }
    )
}
