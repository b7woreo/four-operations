package exp

import java.util.*

abstract class ASTree : Iterable<ASTree> {
    abstract fun child(i: Int): ASTree
    abstract fun numChildren(): Int
    abstract fun children(): Iterator<ASTree>
    abstract fun eval(): Double
    override fun iterator(): Iterator<ASTree> = children()
}

open class ASTLeaf(protected var t: String) : ASTree() {
    override fun eval(): Double {
        throw UnsupportedOperationException()
    }

    companion object {
        private val empty = ArrayList<ASTree>()
    }

    override fun child(i: Int): ASTree {
        throw IndexOutOfBoundsException()
    }

    override fun numChildren(): Int {
        return 0
    }

    override fun children(): Iterator<ASTree> {
        return empty.iterator()
    }

    fun token(): String = t
}

open class ASTList(var children: List<ASTree>) : ASTree() {
    override fun eval(): Double {
        throw UnsupportedOperationException()
    }

    override fun child(i: Int): ASTree {
        return children[i]
    }

    override fun numChildren(): Int {
        return children.size
    }

    override fun children(): Iterator<ASTree> {
        return children().iterator()
    }
}

class BinaryExpr(c: List<ASTree>) : ASTList(c) {
    fun left(): ASTree = child(0)
    fun operator(): String {
        val t = child(1)
        if (t is ASTLeaf) {
            return t.token()
        }
        return ""
    }

    fun right(): ASTree = child(2)

    override fun eval(): Double {
        val op = operator()
        when (op) {
            "+" -> return left().eval() + right().eval()
            "-" -> return left().eval() - right().eval()
            "*" -> return left().eval() * right().eval()
            "/" -> return left().eval() / right().eval()
            else -> {
                return 0.0
            }
        }
    }
}

class NumberLiteral(t: String) : ASTLeaf(t) {
    fun value(): Double = t.toDouble()
    override fun eval(): Double {
        return value()
    }
}