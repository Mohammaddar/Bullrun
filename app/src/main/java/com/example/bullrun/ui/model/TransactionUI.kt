package com.example.bullrun.ui.model

import com.example.bullrun.data.database.model.Transaction
import com.example.bullrun.data.database.model.TransactionType
import com.example.bullrun.decimalCount
import java.text.SimpleDateFormat
import java.util.*

data class TransactionUI(
    val coinID: String,
    val name: String,
    val symbol: String,
    val price: Double,
    val volume: Double,
    var _type: TransactionType,
    val image: String,
    var walletName: String,
    var _dateMillis: Long,
) {
    var value = (price * volume).decimalCount(2)
    var type = _type.text
    var date = millisToDate(_dateMillis)

    companion object {
        fun transformDataModelToUiModel(transactions: List<Transaction>): List<TransactionUI> {
            return transactions.map {
                TransactionUI(
                    coinID = it.coinId,
                    name = it.coinName,
                    symbol = it.symbol,
                    price = it.price,
                    volume = it.volume,
                    _type = it.type,
                    image = it.image,
                    walletName = it.walletName,
                    _dateMillis = it.dateMillis
                )
            }
        }
    }

    private fun millisToDate(dateInMillis: Long): String {
        return SimpleDateFormat("dd/MM/yyyy").format(Date(dateInMillis))
    }
}