package com.example.appfinanceiro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appfinanceiro.data.dao.CotacaoDao
import com.example.appfinanceiro.data.dao.LancamentoDao
import com.example.appfinanceiro.data.dao.UsuarioDao
import com.example.appfinanceiro.data.entity.Cotacao
import com.example.appfinanceiro.data.entity.Lancamento
import com.example.appfinanceiro.data.entity.Usuario

@Database(
    entities = [Usuario::class, Lancamento::class, Cotacao::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun lancamentoDao(): LancamentoDao
    abstract fun cotacaoDao(): CotacaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}