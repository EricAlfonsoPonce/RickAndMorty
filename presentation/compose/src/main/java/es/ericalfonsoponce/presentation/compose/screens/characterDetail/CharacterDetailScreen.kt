package es.ericalfonsoponce.presentation.compose.screens.characterDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.presentation.compose.R
import es.ericalfonsoponce.presentation.compose.components.CustomAlertDialog
import es.ericalfonsoponce.presentation.compose.components.CustomOutLinedTextField
import es.ericalfonsoponce.presentation.compose.components.debounceOnClick
import es.ericalfonsoponce.presentation.compose.theme.appBackgroundColor
import es.ericalfonsoponce.presentation.compose.theme.saveButtonColor

@Composable
fun CharacterDetailScreen(
    navigateBack: () -> Unit,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navEventsFlow.collect { event ->
            when (event) {
                is NavEvent.NavigateToHomeScreen -> navigateBack()
            }
        }
    }

    if (uiState.error != null) {
        val message = when(uiState.error){
            is AppError.NoInternet -> stringResource(R.string.error_no_internet)
            is AppError.Failure -> (uiState.error as AppError.Failure).msg
            is AppError.SqlError -> stringResource(R.string.error_sql)
            else -> stringResource(R.string.default_dialog_message)
        }

        CustomAlertDialog(
            title = stringResource(R.string.default_dialog_title),
            message = message,
            buttonText = stringResource(R.string.default_dialog_button),
            onDismiss = viewModel::resetError
        )
    }

    CharacterDetailScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is CharacterDetailScreenActions.SaveChanges -> viewModel::saveChanges
                is CharacterDetailScreenActions.OnSetCharacterGender-> viewModel::setCharacterGender
                is CharacterDetailScreenActions.OnSetCharacterName -> viewModel::setCharacterName
                is CharacterDetailScreenActions.OnSetCharacterSpecie -> viewModel::setCharacterSpecie
                is CharacterDetailScreenActions.OnSetCharacterStatus -> viewModel::setCharacterStatus
            }
        },
        navigateBack = navigateBack,
    )

}

@Composable
private fun CharacterDetailScreenContent(
    uiState: CharacterDetailScreenUiState,
    onAction: (CharacterDetailScreenActions) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CharacterDetailTopBar(
                navigateBack = navigateBack
            )
        },
        bottomBar = {
            CharacterDetailBottomBar(
                saveChanges = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(appBackgroundColor)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AsyncImage(
                model = uiState.character?.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .size(200.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            CustomOutLinedTextField(
                value = uiState.character?.name.orEmpty(),
                label = stringResource(R.string.character_hint_name),
                onValueChange = { onAction(CharacterDetailScreenActions.OnSetCharacterName) }
            )

            GenderDropDown(
                onItemSelected = { onAction(CharacterDetailScreenActions.OnSetCharacterGender) }
            )

            CustomOutLinedTextField(
                value = uiState.character?.origin.orEmpty(),
                label = stringResource(R.string.character_hint_specie),
                onValueChange = { onAction(CharacterDetailScreenActions.OnSetCharacterSpecie) }
            )

            StatusDropDown(
                onItemSelected = { onAction(CharacterDetailScreenActions.OnSetCharacterStatus) }
            )

            CustomOutLinedTextField(
                value = uiState.character?.origin.orEmpty(),
                label = stringResource(R.string.character_hint_origin),
                isEnabled = false,
                onValueChange = {}
            )

            CustomOutLinedTextField(
                value = uiState.character?.location.orEmpty(),
                label = stringResource(R.string.character_hint_location),
                isEnabled = false,
                onValueChange = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailTopBar(
    navigateBack: () -> Unit
) {
    Column {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = debounceOnClick { navigateBack() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            title = {
                Text(text = stringResource(R.string.top_bar_character_detail_title))
            },
            colors = TopAppBarColors(
                containerColor = appBackgroundColor,
                navigationIconContentColor = Color.Transparent,
                titleContentColor = Color.White,
                actionIconContentColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            ),
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropDown(
    onItemSelected: (CharacterGender) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val dropDownOptions = CharacterGender.entries

    val selectedOption = remember {
        mutableStateOf(dropDownOptions[0].value)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .border(
                        width = 1.dp, color = Color.LightGray, shape = if (isDropdownExpanded) {
                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        } else {
                            RoundedCornerShape(4.dp)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = selectedOption.value,
                    fontSize = 16.sp,
                    color = Color.White,
                )

                Image(
                    imageVector = if (isDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            ExposedDropdownMenu(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
                    )
                    .background(appBackgroundColor),
                shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp),
                expanded = isDropdownExpanded,
                onDismissRequest = {
                    isDropdownExpanded = false
                }) {
                dropDownOptions.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row {
                                Text(
                                    text = option.value,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        },
                        onClick = debounceOnClick {
                            selectedOption.value = option.value
                            onItemSelected(option)
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusDropDown(
    onItemSelected: (CharacterStatus) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val dropDownOptions = CharacterStatus.entries

    val selectedOption = remember {
        mutableStateOf(dropDownOptions[0].value)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .border(
                        width = 1.dp, color = Color.LightGray, shape = if (isDropdownExpanded) {
                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        } else {
                            RoundedCornerShape(4.dp)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = selectedOption.value,
                    fontSize = 16.sp,
                    color = Color.White,
                )

                Image(
                    imageVector = if (isDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            ExposedDropdownMenu(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
                    )
                    .background(appBackgroundColor),
                shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp),
                expanded = isDropdownExpanded,
                onDismissRequest = {
                    isDropdownExpanded = false
                }) {
                dropDownOptions.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row {
                                Text(
                                    text = option.value,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        },
                        onClick = debounceOnClick {
                            selectedOption.value = option.value
                            onItemSelected(option)
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailBottomBar(
    saveChanges: () -> Unit
) {
    TextButton(
        modifier = Modifier.background(appBackgroundColor),
        onClick = debounceOnClick { saveChanges() },
        colors = ButtonColors(
            containerColor = saveButtonColor,
            contentColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(
            text = stringResource(R.string.character_save_changes_button),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun CharacterDetailScreenPreview() {
    val characterShow = CharacterShow(
        1,
        "Rick Sanchez",
        CharacterStatus.ALIVE,
        "",
        CharacterGender.MALE,
        "",
        "Citadel of Ricks",
        ""
    )

    CharacterDetailScreenContent(
        uiState = CharacterDetailScreenUiState(character = characterShow),
        onAction = {},
        navigateBack = {}
    )
}