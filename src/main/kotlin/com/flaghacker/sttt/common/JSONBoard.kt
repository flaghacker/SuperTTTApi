package com.flaghacker.sttt.common

import org.json.JSONArray
import org.json.JSONObject

fun Board.toJSON() = JSONBoard.toJSON(this)
class JSONBoard {
	companion object {
		fun fromJSON(json: JSONObject): Board {
			val board = Array(9, { Array(9, { Player.NEUTRAL }) })
			for (i in 0 until 81)
				board[i.toPair().first][i.toPair().second] = fromNiceString(json.getJSONArray("board").getString(i))
			val macroMask = json.getInt("macroMask")
			val lastMove = if (json.length() == 3) json.getInt("lastMove") else null

			return Board(board, macroMask, lastMove?.toByte())
		}
		fun toJSON(board: Board): JSONObject {
			val json = JSONObject()
			val jsonBoard = JSONArray()
			for (i in 0 until 81) jsonBoard.put(board.tile(i.toByte()).niceString)

			json.put("board", jsonBoard)
			json.put("macroMask", board.macroMask())
			json.put("lastMove", board.lastMove()?.toInt())

			return json
		}
	}
}
