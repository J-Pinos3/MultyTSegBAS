package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.postsitecontrollers.PostSiteController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import com.seguridadbas.multytenantseguridadbas.view.customwidget.EmptyReportsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSitesScreen(
    modifier: Modifier = Modifier,
    onPostSiteClicked: (String) -> Unit = {},
    postSiteController: PostSiteController = hiltViewModel()
){

    var token  by remember { mutableStateOf("") }
    var tenantId  by remember { mutableStateOf("") }
    var postSitesList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    var selectedPostSite by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            token = storedData.token
            token = token.replace("\"", "").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"", "").trim()
            Log.i("post sites", "token: ${token.substring(0,7)} tenant: ${tenantId}")

            loadPostSites(
                "Bearer $token", tenantId, postSiteController,

                onSuccess = { postSitesList = it },
                onError = {
                    Log.e("post sites screeen", "error: $it")
                }
            )

        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title={
                    Text(
                        text="Sitios de Publicación",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BasYellow,
                    titleContentColor = Color.Black
                )
            )
        }

    ){ paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PostSitesDropDown(
                contentList = postSitesList.map { it.first },
                onSelectedPostSite = { postSite ->
                    selectedPostSite = postSitesList.first { it.first == postSite }.second
                    onPostSiteClicked(selectedPostSite)
                },
                modifier = Modifier.fillMaxWidth() // Asegura que ocupe ancho completo
            )

            // Opcional: Mensaje si no hay datos
            if (postSitesList.isEmpty()) {
                EmptyReportsState(Modifier, "Problema al conectar con servidores", true)
            }
        }

    }


}



@Composable
fun PostSitesDropDown(
    contentList: List<String>,
    onSelectedPostSite:(String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Sitios de publicación"
){
    var mExpanded by remember { mutableStateOf(false) }
    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if(mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(   Modifier.padding(20.dp)  ){
        OutlinedTextField(
            value = mSelectedText,
            onValueChange = { mSelectedText = it
            onSelectedPostSite(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding()
                .onGloballyPositioned{ coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text(label) },
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded },
                    tint = Color.Black
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = BasYellow,
                unfocusedIndicatorColor = BasYellow,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.Black
            )

        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
                .background(Color.White)
        ) {
            contentList.forEach { label ->
                DropdownMenuItem(
                    text = { Text(label, color = Color.Black) },
                    onClick = {
                        mSelectedText = label
                        onSelectedPostSite(label)
                        mExpanded = false
                    }
                )
            }
        }

    }

}


private fun loadPostSites(
    bearerToken: String, tenantId: String,
    postSiteController: PostSiteController,
    onSuccess: (List<Pair<String, String>>) -> Unit,
    onError: (String) -> Unit
) {

    CoroutineScope(Dispatchers.IO).launch {
        if (!bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty()) {
            val result = postSiteController.getAllPostSites(bearerToken, tenantId)
            print("Aquii dentro de post sites?")
            withContext(Dispatchers.Main) {
                when (result) {
                    is Resource.Success -> {
                        onSuccess(result.data?.map { Pair(it.companyName, it.id) }!!)
                    }

                    is Resource.Error -> {
                        val msg =result.message.toString()
                        onError(msg)
                        Log.e("post sites screeen", "error: msg")
                    }

                    else -> {
                        val msg =result.message.toString()
                        onError(msg)
                        Log.e("post sites screeen", "error: msg")
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) { onError("token o tenantId inválidos") }
        }
    }
}


