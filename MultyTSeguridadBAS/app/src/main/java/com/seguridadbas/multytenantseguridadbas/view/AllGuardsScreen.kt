package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantcontroller.TenantGuardsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.Guard
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllGuardsScreen(
    modifier: Modifier = Modifier,
    navigateBackToMore: () -> Unit,
    onGuardClicked: (guardId: String) -> Unit = {},
    tenantGuardsController: TenantGuardsController
){


    var token by remember { mutableStateOf("") }
    var tenantId by remember { mutableStateOf("") }
    var allGuardsList by remember { mutableStateOf<List<Guard>>( emptyList() ) }
    var filteredGuards by remember { mutableStateOf<List<Guard>>( emptyList() ) }

    var searchQuery by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf(SearchType.BOTH) }


    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            token = storedData.token
            token = token.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"","").trim()

            if( !token.isNullOrEmpty() || !tenantId.isNullOrEmpty() ){
                val result = tenantGuardsController.getSecGuards("Bearer $token", tenantId)

                when(result){
                    is Resource.Success -> {
                        allGuardsList = result.data?.toList()!!
                    }

                    is Resource.Error -> {
                        allGuardsList = emptyList()
                        Log.e("AllGuards","No se pudo traer los guardias ${result.message.toString()}")
                    }

                    else -> {
                        Log.e("AllGuards","No se pudo traer los guardias")
                    }
                }
            }
        }
    }

    LaunchedEffect(searchQuery, searchType, allGuardsList    ) {
        filteredGuards = when{
            searchQuery.isBlank() -> allGuardsList
            searchType == SearchType.NAME -> allGuardsList.filter { it.firstName.contains(searchQuery, ignoreCase = true) }
            searchType == SearchType.GOVERNMENT_ID -> allGuardsList.filter { it.id.contains(searchQuery, ignoreCase = true) }
            else -> {
                allGuardsList.filter{ guard ->
                    guard.firstName.contains(searchQuery, ignoreCase = true) ||
                            guard.id.contains(searchQuery)
                }
            }
        }
    }

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()


    Scaffold (
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            Column{
                CenterAlignedTopAppBar(
                    title = { Text(text = "Guardias de Seguridad", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(
                            onClick = { navigateBackToMore() }
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "navigate back to business"
                            )
                        }
                    },
                    actions = {
                        var expanded by remember { mutableStateOf(false) }
                        Box{
                            IconButton( onClick = {expanded = true} ){
                                Icon( painterResource(R.drawable.ic_filter), contentDescription = "filter" )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {expanded = false}
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Buscar en ambos") },
                                    onClick = {
                                        searchType = SearchType.BOTH
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Solo por nombre") },
                                    onClick = {
                                        searchType = SearchType.NAME
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Solo por cédula") },
                                    onClick = {
                                        searchType = SearchType.GOVERNMENT_ID
                                        expanded = false
                                    }
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehaviour,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BasYellow, titleContentColor = Color.Black
                    )
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = {
                        Text(
                            when(searchType){
                                SearchType.NAME -> "Buscar por nombre..."
                                SearchType.GOVERNMENT_ID -> "Buscar por cédula..."
                                else -> "Buscar por ambos"
                            }
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,  contentDescription = "Buscar"
                        )
                    },
                    trailingIcon = {
                        if(searchQuery.isNotBlank()){
                            IconButton(
                                onClick = {searchQuery = ""}
                            ) {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                                    contentDescription = "Limpiar búsqueda"
                                )
                            }
                        }
                    },
                    singleLine = true, shape = RoundedCornerShape(8.dp)
                )
            }
        }
    ){ paddingVals ->

        if( !allGuardsList.isNullOrEmpty() ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BasBackground)
                    .padding(paddingVals)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(filteredGuards){ item ->
                    GuardItem(item, modifier)
                }
            }
        }else{
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "NO hay guardias de seguridad",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textDecoration = TextDecoration.Underline,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }

}

enum class SearchType { BOTH, NAME, GOVERNMENT_ID }

@Composable
private fun GuardItem(
    guard: Guard,
    modifier: Modifier = Modifier,
    onClick: (id: String) -> Unit = {}
){
    Box(
        modifier=modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(24))
            .border(
             2.dp, BasGray,
                shape = RoundedCornerShape(24,24,24,24)
            )
            .shadow(2.dp)
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White)
            .clickable(
                onClick = { onClick(guard.id) }
            )
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = guard.firstName + " " + guard.lastName,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = guard.governmentId,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}