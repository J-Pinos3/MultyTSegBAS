package com.seguridadbas.multytenantseguridadbas.view

import android.graphics.drawable.shapes.OvalShape
import android.provider.CalendarContract
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@Preview(showSystemUi = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = BasBackground)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 6.dp)
                .align(Alignment.Start),
            text = "Bienvenido Cliente, ",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasYellow)
                .padding(top = 30.dp, bottom = 30.dp)
        ){
            Image(
                painter = painterResource(R.drawable.baslogo),
                contentDescription = "Logo de bas"
            )
        }

        Spacer(modifier = Modifier.padding(top = 10.dp))

        titleTexts(  modifier = Modifier.align(Alignment.Start), "Nuestros Servicios:"  )

        Spacer(modifier = Modifier.padding(top = 10.dp))

        LazyRow {
            /** TODO ADD ITEMS*/
        }

        Spacer(modifier = Modifier.padding(top = 30.dp))

        titleTexts(  modifier = Modifier.align(Alignment.Start), "Facturación:"  )

        Spacer(modifier = Modifier.padding(top = 10.dp))

        BillingComposable(
            modifier,
            "$ 12312.00",
            "20/01/23"
        )

        Spacer(modifier = Modifier.padding(top = 30.dp))

        titleTexts(  modifier = Modifier.align(Alignment.Start), "Certificaciones y Permisos:"  )

        LazyRow {

        }

    }

}


@Composable
fun BillingComposable(modifier: Modifier, amount: String, lastPayment: String){

    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd" )
    val date = LocalDate.parse(LocalDate.now().toString(), apiFormat)


    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BasGray),
        color = Color.Transparent
    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = BasYellow)
                    .height(50.dp)
                    .width(10.dp)
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Siguiente Pago",
                        fontSize = 14.sp
                    )

                    Text(
                        modifier = Modifier,
                        text = "${date.month.toString().uppercase()} ${date.year}",
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.padding(top = 10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = amount,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.padding(top = 10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Último Pago: $lastPayment",
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun titleTexts(modifier: Modifier, text: String){
    Text(
        modifier = modifier
            .padding(start = 10.dp),
        text = text,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        textAlign = TextAlign.Start
    )
}