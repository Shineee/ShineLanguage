package com.shine.language.data.db

import com.shine.language.MainApplication
import com.shine.language.util.log.i
import java.io.File
import java.io.FileOutputStream


object DBHelper {
    private const val DB_NAME = "language.db"
    private const val DB_PATH = "data/data/com.shine.language/databases/$DB_NAME"
    fun init() {
        copyDataBase()
    }

    private fun copyDataBase() {
        val file = File(DB_PATH)
        if (file.exists()) {
            "file.exists()".i()
            return
        }
        val context = MainApplication.instance
        val input = context.assets.open(DB_NAME)
        val output = FileOutputStream(DB_PATH)
        //transfer bytes from the inputfile to the outputfile
        //transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length: Int

        while (input.read(buffer).also { length = it } > 0) {
            output.write(buffer, 0, length)
        }
        //Close the streams
        //Close the streams
        output.flush()
        output.close()
        input.close()
    }
}