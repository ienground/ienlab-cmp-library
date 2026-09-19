package zone.ien.utils.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertNotNull
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.primitives.IenAssetFrameShape
import zone.ien.utils.ui.primitives.IenAssetFrameSize

class EmptyTest {
    @Test
    fun `Empty icon configures IenAssetFrame parameters with receiver scope`() {
        assertNotNull(emptyWithAssetFrameParameters())
    }

    private fun emptyWithAssetFrameParameters(): @Composable () -> Unit = {
        Empty(
            icon = {
                this.modifier = Modifier.padding(4.dp)
                this.size = IenAssetFrameSize.ExtraLarge
                this.tone = IenSemanticTone.Brand
                this.shape = IenAssetFrameShape.Circle
                this.bordered = true
                this.contentDescription = "empty state icon"
                this.contentAlignment = Alignment.TopCenter
                content { modifier ->
                    assertNotNull(modifier)
                }
            },
            title = {},
            modifier = Modifier.padding(4.dp),
        )
    }
}
