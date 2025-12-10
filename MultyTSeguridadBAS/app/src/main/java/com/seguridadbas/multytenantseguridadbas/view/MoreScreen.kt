package com.seguridadbas.multytenantseguridadbas.view

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.tenantcontroller.TenantGuardsController
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground


@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = true)
@Composable

fun MoreScreen(
    modifier: Modifier = Modifier,
    onNavigateToGuardsScreen: () -> Unit,
    onNavigateToGuardShiftsScreen: () -> Unit,
    onNavigateToShiftScreen: () -> Unit
){

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = BasBackground)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Spacer(modifier = modifier.padding(vertical = 8.dp))

        FeatureItems(
            modifier = modifier,
            R.drawable.ic_groups,
            "Guardias de Seguridad",
            onNavigateToGuardsScreen
        )

        Spacer(modifier = modifier.padding(vertical = 8.dp))


        FeatureItems(
            modifier = modifier,
            R.drawable.ic_tracking,
            "Turnos Realizados",
            onNavigateToGuardShiftsScreen
        )

        Spacer(modifier = modifier.padding(vertical = 8.dp))


        FeatureItems(
            modifier = modifier,
            R.drawable.ic_orders,
            "Turnos Asignados",
            onNavigateToShiftScreen
        )

        Spacer(modifier = modifier.padding(vertical = 8.dp))

    }


}

@Composable
fun FeatureItems(
    modifier: Modifier,
    icon: Int,
    featureDescription: String,
    navigateToFeature: () -> Unit
){
    Box(
        modifier = modifier
            .padding(top = 10.dp, start = 5.dp, end =  5.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .background(color = Color.White)
            .height(60.dp)
            .clickable{
                navigateToFeature()
            },
        contentAlignment = Alignment.Center

    ){
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = modifier
                    .height(35.dp)
                    .width(35.dp),
                painter = painterResource(icon),
                contentDescription = featureDescription
            )

            Spacer(modifier =  modifier.width(16.dp))

            Text(
                modifier = modifier.weight(1f),
                text = featureDescription,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
            )

            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                "advance to feature",
                modifier = modifier
                        .height(35.dp)
                    .width(35.dp),
            )
        }
    }
}