package fixture

import androidx.compose.runtime.Composable
import com.star.forge.kit.primitives.ForgeButton
import com.star.forge.kit.primitives.ForgeText
import com.star.forge.kit.theme.ForgeKitTheme
import com.star.forge.kit.theme.ForgeTokenSets

@Composable
fun ForgeConsumer() {
    ForgeKitTheme(tokenSet = ForgeTokenSets.default()) {
        ForgeButton(onClick = {}) { ForgeText("External package consumer") }
    }
}
