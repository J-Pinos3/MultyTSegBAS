package com.seguridadbas.multytenantseguridadbas.view.customwidget



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data class para items booleanos
data class BooleanItem(
    val label: String,
    val value: Boolean?,
    val serialNumber: String? = null,
    val type: String? = null
)

// Componente reutilizable para items booleanos
@Composable
fun BooleanItemRow(
    item: BooleanItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Indicador circular de estado
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = if (item.value == true) Color.Green else Color.Red,
                            shape = CircleShape
                        )
                )
            }

            item.serialNumber?.let {
                Text(
                    text = "N°: $it",
                    fontSize = 14.sp
                )
            }
        }

        item.type?.let {
            Text(
                modifier = Modifier.padding(start = 24.dp, top = 4.dp),
                text = "Tipo: $it",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

// Componente para secciones
@Composable
fun InventorySection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            thickness = 1.dp,
            color = Color(0f, 0f, 0f, 0.14f)
        )

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        content()
    }
}