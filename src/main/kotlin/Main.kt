import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    var points by remember { mutableStateOf(mutableListOf<Offset>()) }
    Canvas(modifier = Modifier.fillMaxSize().clickable {}.onPointerEvent(PointerEventType.Press)
    {
        var pnt = it.changes.first().position
        points.add(pnt)
    }) {
        for (pnt in points) {
            drawCircle(
                color = Color.Black,
                radius = 5f,
                center = Offset(pnt.x, pnt.y)
            )
        }

        if (points.size >= 3){
            var minmaxP = jarvis(points)
            for (i in 0 until minmaxP.size-1){
                drawCircle(
                    color = Color.Green,
                    radius = 5f,
                    center = Offset(minmaxP[i].x, minmaxP[i].y)
                )
                drawLine(
                    Color.Black,
                    minmaxP[i],
                    minmaxP[i+1]
                )
            }
        }


    }
}

fun jarvis(inp: MutableList<Offset>) : MutableList<Offset>{
    // список, содержащий точки искомой оболочки
    var total = mutableListOf<Offset>()
    // нижняя точка
    var lowerP = inp[0]
    for (i in inp){
        if (i.y >= lowerP.y) lowerP = i
    }

    // первая тчка списка — нижняя тчка
    total.add(lowerP)

    // след. тчка
    var nextP = Offset(0f,0f)
    // точка лежащая на векторе из последней и предпоследней точек, нужна для выч-ия угла
    var zeroPhiPoint = lowerP.plus(Offset(1f, 0f))

    while (nextP != lowerP){
        var minPhi = PI.toFloat()
        var phi : Float

        for (i in inp){
            if (i == total.last) continue
            phi = acos(csn(i.minus(total.last), zeroPhiPoint.minus(total.last)))
            if (phi < minPhi){
                minPhi = phi
                nextP = i
            }
        }
        total.add(nextP)
        zeroPhiPoint = nextP.minus(total[total.size - 2]).times(2f).plus(total[total.size - 2])
    }

    return total
}

fun csn(a:Offset,b:Offset):Float{
    return (a.x*b.x+a.y*b.y)/sqrt((a.x*a.x+a.y*a.y)*(b.x*b.x+b.y*b.y))
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
