package rationals

import java.lang.NumberFormatException
import java.lang.UnsupportedOperationException
import java.math.BigInteger


class Rational(val numerator: BigInteger, val denominator: BigInteger): Comparable<Rational> {

    private val normalised: Rational by lazy {
        val gcd = numerator.gcd(denominator)
        val numeratorSign = if (
            (numerator < BigInteger.ZERO) xor (denominator < BigInteger.ZERO)
        ) BigInteger.valueOf(-1) else BigInteger.ONE
        Rational(numeratorSign * (numerator / gcd).abs(), (denominator/gcd).abs())
    }

    operator fun plus(other: Rational): Rational {
        val gcd = denominator.gcd(other.denominator)
        return Rational(
            (numerator * other.denominator / gcd) + (other.numerator * denominator / gcd),
            (denominator * other.denominator / gcd)
        )
    }

    operator fun minus(other: Rational): Rational = this + Rational(- other.numerator, other.denominator)

    operator fun times(other: Rational): Rational {
        val gcd = denominator.gcd(other.denominator)
        return Rational(
            numerator * other.numerator / gcd,
            (denominator * other.denominator / gcd)
        )
    }

    operator fun div(other: Rational) = this * Rational(other.denominator, other.numerator)

    operator fun unaryMinus() = Rational(-numerator, denominator)

    override fun toString(): String {
        if (normalised.denominator == BigInteger.ONE) {
            return normalised.numerator.toString()
        }
        return "${normalised.numerator}/${normalised.denominator}"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Rational) {
            return false
        }
        return normalised.numerator == other.normalised.numerator &&
                normalised.denominator == other.normalised.denominator
    }

    override fun hashCode(): Int {
        return normalised.numerator.hashCode() * normalised.denominator.hashCode()
    }

    override fun compareTo(other: Rational): Int {
        val difference = this - other
        if (difference.numerator == BigInteger.ZERO) {
            return 0
        }
        return if ((difference.numerator < BigInteger.ZERO) xor ((difference.denominator < BigInteger.ZERO))) {
            -1
        } else {
            1
        }
    }
}

fun toBigInt(t: Any?): BigInteger = when(t) {
    is BigInteger -> t
    is Long -> t.toBigInteger()
    is Int -> t.toBigInteger()
    else -> throw UnsupportedOperationException("$t cannot be converted to BigInteger")
}

infix fun <T, S> T.divBy(denominator: S): Rational = Rational(toBigInt(this), toBigInt(denominator))

fun String.toRational(): Rational {
    val parts = this.split("/")
    if (parts.size > 2) {
        throw NumberFormatException("'$this' has too many / symbols")
    }
    return Rational(
        BigInteger(parts[0]),
        if (parts.size > 1) BigInteger(parts[1]) else BigInteger.ONE
    )
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}