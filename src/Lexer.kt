package exp

class Lexer(var exp: String) {
    private var last: String? = null

    private var position = 0

    fun peek(): String {
        val l = last
        val start = position

        val s = read()

        position = start
        last = l
        return s
    }

    fun read(): String {
        while (isSpace(peekChar())) {
            getChar()
        }

        if (isOpt(peekChar())) {
            //处理负号问题
            if (peekChar() == '-' && (last == null || last == "(")) {
                getChar()

                last = "-" + read()
                return last!!
            }

            last = getChar().toString()
            return last!!
        }

        if (isDigit(peekChar())) {
            val builder = StringBuilder()
            builder.append(getChar())

            while (isDigit(peekChar()) || isDot(peekChar())) {
                builder.append(getChar())
            }

            last = builder.toString()
            return last ?: ""
        }

        if (peekChar() == null) {
            return ""
        }
        throw RuntimeException("Invalid symbol")
    }

    private fun getChar(): Char? {
        if (position == exp.length) {
            return null
        }
        return exp[position++]
    }

    private fun peekChar(): Char? {
        if (position == exp.length) {
            return null
        }
        return exp[position]
    }

    private fun isDot(c: Char?): Boolean {
        if (c == null) {
            return false;
        }
        return c == '.'
    }

    private fun isDigit(c: Char?): Boolean {
        if (c == null) {
            return false
        }
        return '0' <= c && c <= '9'
    }

    private fun isSpace(c: Char?): Boolean {
        if (c == null) {
            return false
        }
        return c == ' '
    }

    private fun isOpt(c: Char?): Boolean {
        if (c == null) {
            return false
        }
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')'
    }
}