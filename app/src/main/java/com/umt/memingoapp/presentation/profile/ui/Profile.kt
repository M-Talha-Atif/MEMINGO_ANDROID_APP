import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umt.memingoapp.R
import com.umt.memingoapp.domain.model.response.UserProfileResponse
import com.umt.memingoapp.presentation.profile.viewmodel.ProfileViewModel
import com.umt.memingoapp.presentation.shared_components.BottomNavigationBar

val LogoColor = Color(0xFF256470)




@Composable
fun UserSettingsScreen(
    navController: NavHostController,
    courseId: String,
    profileViewModel: ProfileViewModel
)
{

    val userProfile = profileViewModel.userProfile.collectAsState().value

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, courseId = courseId)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProfileHeader(userProfile = userProfile)

            SettingsSection(
                title = "ACCOUNT",
                items = listOf(
                    SettingsItem(
                        iconRes = R.drawable.ic_profile,
                        title = "Edit Profile",
                        description = "Update your personal information"
                    ),
                    SettingsItem(
                        iconRes = R.drawable.ic_notification,
                        title = "Notifications",
                        description = "Manage your notification preferences"
                    ),
                    SettingsItem(
                        iconRes = R.drawable.ic_privacy,
                        title = "Privacy & Security",
                        description = "Control your privacy settings"
                    )
                )
            )

            SettingsSection(
                title = "SUPPORT",
                items = listOf(
                    SettingsItem(
                        iconRes = R.drawable.ic_help,
                        title = "Help Center",
                        description = "Get help and support"
                    ),
                    SettingsItem(
                        iconRes = R.drawable.ic_contact,
                        title = "Contact Us",
                        description = "Send us a message"
                    ),
                    SettingsItem(
                        iconRes = R.drawable.ic_rate,
                        title = "Rate App",
                        description = "Share your feedback"
                    )
                )
            )
        }
    }
}
@Composable
fun ProfileHeader(userProfile: UserProfileResponse?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.av5),
            contentDescription = "Profile",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = userProfile?.name ?: "Loading...",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = userProfile?.email ?: "",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun SettingsSection(title: String, items: List<SettingsItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingItemRow(item = item)
                    if (index < items.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(start = 16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingItemRow(item: SettingsItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = item.description,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "Right arrow",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
    }
}

data class SettingsItem(
    val iconRes: Int,
    val title: String,
    val description: String,
)
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewUserSettingsScreenLight() {
//    MaterialTheme {
//        UserSettingsScreen(rememberNavController(), "german-1")
//    }
//}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun PreviewUserSettingsScreenDark() {
//    MaterialTheme {
//        UserSettingsScreen(rememberNavController(), "german-1")
//    }
//}