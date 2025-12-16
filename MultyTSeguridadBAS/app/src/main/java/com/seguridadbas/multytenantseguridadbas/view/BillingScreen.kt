package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun BillingScreen(
    modifier: Modifier = Modifier,
    navigateBackToHome: () -> Unit = {}
){

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title={ Text(text = "Nómina") },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBackToHome()  }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "navigate back to business"
                        )
                    }
                },
                scrollBehavior = scrollBehaviour,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BasYellow, titleContentColor = Color.Black
                ),
                actions = {
                    IconButton(
                        onClick = { /** navegar a servicios */  }
                    ){
                        Icon(
                            painter =painterResource( R.drawable.ic_orders ),
                            contentDescription = "pagos de servicios"
                        )
                    }
                }
            )
        }
    ){paddingVals ->
        paddingVals


        LazyColumn(
            modifier = modifier.fillMaxSize().background(BasBackground)
                .padding(paddingVals).statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

        }

    }
}


@Composable
private fun BillingStateItem(
    //dataObject
    modifier: Modifier = Modifier,
    onClick: (billingId: String) -> Unit = {}
){

    Box(
        modifier = modifier.padding(10.dp)
            .clip(RoundedCornerShape(24))
            .border(
                2.dp, BasGray,
                shape = RoundedCornerShape(24,24,24,24)
            )
            .shadow(2.dp)
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White)
            .clickable(  onClick = {  onClick("")  }   )
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                modifier = Modifier.padding(top = 5.dp, start = 5.dp),
                text = "Módulo:", fontSize = 15.sp, textAlign = TextAlign.Start, fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "Servicio ABX", fontSize = 14.sp, textAlign = TextAlign.Start
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ){
                Text(
                    text = "Precio:", fontSize = 14.sp
                )

                Text(
                    text = "$4.5", fontSize = 14.sp
                )
            }


            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ){
                Text(
                    text = "Estado:", fontSize = 14.sp
                )

                Text(
                    text = "activado", fontSize = 14.sp
                )
            }

        }
    }

}