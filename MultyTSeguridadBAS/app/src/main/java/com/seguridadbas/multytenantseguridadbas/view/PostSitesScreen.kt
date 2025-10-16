package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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

@Preview(showSystemUi = true)
@Composable
fun PostSitesScreen(
    modifier: Modifier = Modifier,
    onPostSiteClicked: (String) -> Unit = {}

){

    val postSiteList =   listOf(
        PostSite(
            postSiteName = "Cuenca", postSiteAddress = "Puente Roto 1"
        ),

        PostSite(
            postSiteName = "La Prensa", postSiteAddress = "Prensa y Machala"
        ),

        PostSite(
            postSiteName = "La Prensa Edificio", postSiteAddress = "Prensa y Avenida del Maestro"
        ),

        PostSite(
            postSiteName = "BAS Matriz", postSiteAddress = "America y Bartolome de las Casas"
        ),
    )


    //val listState = rememberLazyListState()
    //rememberScrollState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(color = BasBackground)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        items(postSiteList){ site ->
            PostSiteItemList(
                site, modifier, onPostSiteClicked
            )
        }
    }

}


@Composable
fun PostSiteItemList(
    postSite: PostSite,
    modifier: Modifier,
    onPostSiteClicked: (String) -> Unit = {}
    ){

    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(24))
            .border(
                2.dp,
                BasGray,
                shape = RoundedCornerShape(0,24,0,24)
            )
            .shadow(2.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .clickable{
                onPostSiteClicked(postSite.postSiteName)
            }
    ){
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .padding(10.dp),
           horizontalAlignment = Alignment.Start
       ){
           Text(
               text = postSite.postSiteName,
               fontSize = 20.sp,
               textAlign = TextAlign.Start,
               fontWeight = FontWeight.Bold
           )

           Spacer(modifier = Modifier.padding(top = 10.dp))

           Text(
               text = postSite.postSiteAddress,
               fontSize = 16.sp,
               textAlign = TextAlign.Start,
               fontWeight = FontWeight.Bold
           )
       }
    }
}


data class PostSite(
    val postSiteName: String,
    val postSiteAddress: String
)