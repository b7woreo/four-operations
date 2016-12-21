/**
 * Created by chren on 2016/5/26.
 */
package calstr

import exp.Lexer


class Parser(val lexer: Lexer) {
    fun expression(): ASTree {
        var left = term()
        while (isToken("+") || isToken("-")) {
            val token = lexer.read()

            val right = term()
            if (token == "+") {
                left = Add(left, right)
            } else {
                left = Sub(left, right)
            }
        }
        return left
    }

    fun term(): ASTree {
        var left = factor()
        while (isToken("*") || isToken("/")) {
            val token = lexer.read()
            val right = factor()

            if (token == "*") {
                left = Mul(left, right)
            } else {
                left = Div(left, right)
            }
        }
        return left
    }

    fun factor(): ASTree {
        if (isToken("(")) {
            lexer.read()
            val e = expression()
            lexer.read()
            return e
        } else {
            val t = lexer.read()
            if (isNum(t)) {
                val n = Num(t.toDouble())
                return n
            } else {
                throw RuntimeException("Invalid num")
            }
        }
    }

    private fun isToken(n: String): Boolean {
        return n == lexer.peek()
    }

    private fun isNum(n: String): Boolean {
        n.toDouble()
        return true
    }
}

abstract class ASTree {
    abstract fun result(): Double
}

class Add(val a: ASTree, val b: ASTree) : ASTree() {
    override fun result(): Double = a.result() + b.result()
}

class Sub(val a: ASTree, val b: ASTree) : ASTree() {
    override fun result(): Double = a.result() - b.result()
}

class Mul(val a: ASTree, val b: ASTree) : ASTree() {
    override fun result(): Double = a.result() * b.result()
}

class Div(val a: ASTree, val b: ASTree) : ASTree() {
    override fun result(): Double = a.result() / b.result()
}

class Num(val n: Double) : ASTree() {
    override fun result() = n
}