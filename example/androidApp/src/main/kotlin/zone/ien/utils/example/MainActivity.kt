package zone.ien.utils.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import zone.ien.utils.utils.Dlog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dlog.d(TAG, "onCreate")

        setContent {
            enableEdgeToEdge()
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}