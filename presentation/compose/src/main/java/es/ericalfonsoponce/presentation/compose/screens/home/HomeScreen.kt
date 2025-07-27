package es.ericalfonsoponce.presentation.compose.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.presentation.compose.R
import es.ericalfonsoponce.presentation.compose.components.debounceClickable
import es.ericalfonsoponce.presentation.compose.components.debounceOnClick
import es.ericalfonsoponce.presentation.compose.theme.aliveColor
import es.ericalfonsoponce.presentation.compose.theme.appBackgroundColor
import es.ericalfonsoponce.presentation.compose.theme.deadColor
import es.ericalfonsoponce.presentation.compose.theme.unknownColor

@Composable
fun HomeScreen(

    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()



}

@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUiState
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = appBackgroundColor),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(1) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (uiState.characters.isNotEmpty()) {
                items(uiState.characters) { character ->
                    ItemCharacter(
                        character = character, onClickItem = {})
                }
            } else {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar() {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.top_bar_character_home_title))
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

@Composable
private fun ItemCharacter(
    character: CharacterShow, onClickItem: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp), border = null, modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true,
                ambientColor = Color.Black,
                spotColor = Color.LightGray
            )
            .debounceClickable { onClickItem() }) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier.size(100.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = when (character.gender) {
                            CharacterGender.MALE -> painterResource(R.drawable.ic_gender_male)
                            CharacterGender.FEMALE -> painterResource(R.drawable.ic_gender_female)
                            CharacterGender.GENDERLESS -> painterResource(R.drawable.ic_gender_undefined)
                            CharacterGender.UNKNOWN -> painterResource(R.drawable.ic_gender_undefined)
                        }, contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = character.name,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = when (character.status) {
                                    CharacterStatus.ALIVE -> aliveColor
                                    CharacterStatus.DEAD -> deadColor
                                    CharacterStatus.UNKNOWN -> unknownColor
                                }
                            )
                        ) {
                            append(character.status.value)
                        }
                        append(" - ${character.location}")
                    }, fontSize = 14.sp, color = Color.Black
                )
            }

            IconButton(
                onClick = debounceOnClick { },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
                    .size(32.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    tint = deadColor
                )
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun HomeScreenPreview() {
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
    val uiState = HomeScreenUiState(
        characters = listOf(
            characterShow, characterShow, characterShow, characterShow, characterShow
        )
    )

    HomeScreenContent(
        uiState = uiState
    )
}