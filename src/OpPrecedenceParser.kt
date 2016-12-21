package exp

import java.util.*

class OpPrecedenceParser(val lexer: Lexer) {
    val operators = HashMap<String, Precedence>()

    class Precedence(val value: Int, val leftAssoc: Boolean) {}

    init {
        operators.put("+", Precedence(2, true))
        operators.put("-", Precedence(2, true))
        operators.put("*", Precedence(3, true))
        operators.put("/", Precedence(3, true))
    }

    fun expresstion(): ASTree {
        var right = factor()
        var next = nextOperator()
        while (next != null) {
            right = doShift(right, next.value)
            next = nextOperator()
        }
        return right
    }

    private fun doShift(left: ASTree, prec: Int): ASTree {
        val op = ASTLeaf(lexer.read())
        var right = factor()
        var next = nextOperator()
        while (next != null && rightIsExpr(prec, next)) {
            right = doShift(right, next.value)
            next = nextOperator()
        }
        return BinaryExpr(listOf(left, op, right))
    }

    private fun rightIsExpr(prec: Int, nextPrec: Precedence): Boolean {
        if (nextPrec.leftAssoc) {
            return prec < nextPrec.value
        } else {
            return prec <= nextPrec.value
        }
    }

    fun factor(): ASTree {
        if (isToken("(")) {
            token("(")
            val e = expresstion()
            token(")")
            return e
        } else {
            val t = lexer.read()
            if (isNumber(t)) {
                val n = NumberLiteral(t)
                return n
            } else {
                throw RuntimeException("Parser exception: token - " + t)
            }
        }
    }

    private fun isNumber(t: String): Boolean {
        try {
            t.toDouble()
            return true
        } catch(e: Exception) {
            return false
        }
    }

    private fun token(name: String) {
        val t = lexer.read()
        if (name != t) {
            throw RuntimeException("Parser exception except: " + name + "actual: " + t)
        }
    }

    private fun isToken(name: String): Boolean {
        val t = lexer.peek()
        return t == name
    }

    private fun nextOperator(): Precedence? {
        val t = lexer.peek()
        return operators[t]
    }
}