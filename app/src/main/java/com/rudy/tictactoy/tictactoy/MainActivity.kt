import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.util.Random

class MainActivity : AppCompatActivity() {

    // المتغيرات الرئيسية لإدارة حالة اللعبة
    private var activePlayer = 1
    private var player1 = ArrayList<Int>()
    private var player2 = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * هذه الدالة تُستدعى عند الضغط على أي زر من أزرار اللعبة التسعة.
     */
    fun buttonClick(view: View) {
        val buSelected = view as Button
        var cellId = 0

        // تحديد رقم الخانة بناءً على معرّف الزر الذي تم الضغط عليه
        when (buSelected.id) {
            R.id.button1 -> cellId = 1
            R.id.button2 -> cellId = 2
            R.id.button3 -> cellId = 3
            R.id.button4 -> cellId = 4
            R.id.button5 -> cellId = 5
            R.id.button6 -> cellId = 6
            R.id.button7 -> cellId = 7
            R.id.button8 -> cellId = 8
            R.id.button9 -> cellId = 9
        }

        // استدعاء الدالة الرئيسية للعب
        playGame(cellId, buSelected)
    }

    /**
     * الدالة التي تنفذ حركة اللاعب وتحدث واجهة المستخدم.
     */
    private fun playGame(cellId: Int, buSelected: Button) {
        // دور اللاعب الأول
        if (activePlayer == 1) {
            buSelected.text = "X"
            buSelected.setBackgroundColor(Color.parseColor("#0091EA")) // لون أزرق
            player1.add(cellId)
            activePlayer = 2
            // إذا كانت اللعبة ضد الكمبيوتر، يتم استدعاء دور الكمبيوتر هنا
            // autoPlay()
        } 
        // دور اللاعب الثاني (أو الكمبيوتر)
        else {
            buSelected.text = "O"
            buSelected.setBackgroundColor(Color.parseColor("#FF6D00")) // لون برتقالي
            player2.add(cellId)
            activePlayer = 1
        }
        
        // تعطيل الزر بعد الضغط عليه
        buSelected.isEnabled = false
        
        // التحقق من وجود فائز بعد كل حركة
        checkWinner()
    }

    /**
     * تتحقق من جميع حالات الفوز الممكنة.
     * هذه نسخة محسنة وأكثر نظافة من الكود الموجود في Smali.
     */
    private fun checkWinner() {
        var winner = -1

        // قائمة بكل التوليفات الرابحة
        val winningCombos = listOf(
            listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9), // الصفوف
            listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9), // الأعمدة
            listOf(1, 5, 9), listOf(3, 5, 7)                  // الأقطار
        )

        // المرور على كل توليفة للتحقق من الفائز
        for (combo in winningCombos) {
            if (player1.containsAll(combo)) {
                winner = 1
                break
            }
            if (player2.containsAll(combo)) {
                winner = 2
                break
            }
        }

        // إذا تم تحديد فائز، يتم عرض رسالة وإنهاء اللعبة
        if (winner != -1) {
            val message = if (winner == 1) "Player 1 Wins! 🏆" else "Player 2 Wins! 🏆"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            // يمكنك هنا تعطيل جميع الأزرار لمنع المزيد من اللعب
        }
    }

    /**
     * دالة الذكاء الاصطناعي التي تختار حركة عشوائية.
     */
    private fun autoPlay() {
        // إيجاد كل الخانات الفارغة المتاحة
        val emptyCells = ArrayList<Int>()
        for (cellId in 1..9) {
            if (!player1.contains(cellId) && !player2.contains(cellId)) {
                emptyCells.add(cellId)
            }
        }
        
        // إذا لم تكن هناك خانات فارغة، تتوقف الدالة
        if (emptyCells.isEmpty()) return

        // اختيار خانة عشوائية من الخانات المتاحة
        val r = Random()
        val randomIndex = r.nextInt(emptyCells.size)
        val cellId = emptyCells[randomIndex]

        // الحصول على الزر الموافق للخانة المختارة
        val buSelect: Button = when (cellId) {
            1 -> findViewById(R.id.button1)
            2 -> findViewById(R.id.button2)
            3 -> findViewById(R.id.button3)
            4 -> findViewById(R.id.button4)
            5 -> findViewById(R.id.button5)
            6 -> findViewById(R.id.button6)
            7 -> findViewById(R.id.button7)
            8 -> findViewById(R.id.button8)
            9 -> findViewById(R.id.button9)
            else -> findViewById(R.id.button1) // قيمة افتراضية
        }
        
        // تنفيذ حركة الكمبيوتر
        playGame(cellId, buSelect)
    }
