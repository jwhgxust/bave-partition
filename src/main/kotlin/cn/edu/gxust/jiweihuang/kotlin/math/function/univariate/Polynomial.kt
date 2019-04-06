package cn.edu.gxust.jiweihuang.kotlin.math.function.univariate

import org.hipparchus.analysis.ParametricUnivariateFunction
import org.hipparchus.analysis.differentiation.DerivativeStructure
import java.lang.Math.pow

interface IPolynomial : IUnivariateFunction, IUnivariateDerivativeFunction,
        IUnivariateDifferentiableFunction, IUnivariateIntegralFunction,
        IUnivariateIntegrableFunction {

    val coefficients: List<Double>

    val maxDegree: Int
        get() {
            return maxDegreeCount()
        }

    fun maxDegreeCount(): Int = coefficients.count() - 1

    override val formula: String
        get() {
            val makeFormula = StringBuilder("")
            var degree = maxDegree
            while (degree > 1) {
                makeFormula.append("${coefficients[maxDegree - degree]} * x^$degree")
                makeFormula.append(" + ")
                degree--
            }
            if (degree == 1) {
                makeFormula.append("${coefficients[maxDegree - 1]} * x + ")
                degree--
            }
            if (degree == 0) {
                makeFormula.append("${coefficients[maxDegree]}")
            }
            return makeFormula.toString()
        }

    override fun value(x: Double): Double {
        var makeSum = 0.0
        var degree = maxDegree
        while (degree >= 0) {
            makeSum += coefficients[maxDegree - degree] * pow(x, degree.toDouble())
            degree--
        }
        return makeSum
    }

    override fun value(t: DerivativeStructure?): DerivativeStructure {
        var makeSum: DerivativeStructure = t?.factory?.constant(coefficients[maxDegree])!!
        var degree = maxDegree
        while (degree >= 1) {
            makeSum = makeSum.add(t.pow(degree)?.multiply(coefficients[maxDegree - degree])!!)
            degree--
        }
        return makeSum
    }

    override fun derivative(x: Double): Double {
        var makeSum = 0.0
        var degree = maxDegree
        while (degree > 1) {
            makeSum += coefficients[maxDegree - degree] * degree * pow(x, (degree - 1).toDouble())
            degree--
        }
        if (degree == 1) {
            makeSum += coefficients[maxDegree - 1]
        }
        return makeSum
    }

    override fun integral(x: Double): Double {
        var makeSum = 0.0
        for (index in 0..maxDegree) {
            makeSum += pow(x, (maxDegree - index + 1).toDouble()) * (coefficients[index] / (maxDegree - index + 1))
        }
        return makeSum
    }

    class Parametric : ParametricUnivariateFunction {
        override fun value(x: Double, vararg parameters: Double): Double {
            var makeSum = 0.0
            for (index in 0 until parameters.size) {
                makeSum += parameters[index] * pow(x, (parameters.size - index - 1).toDouble())
            }
            return makeSum
        }

        override fun gradient(x: Double, vararg parameters: Double): DoubleArray {
            val makeArray = DoubleArray(parameters.size)
            for (index in 0 until parameters.size) {
                makeArray[index] = pow(x, (parameters.size - index - 1).toDouble())
            }
            return makeArray
        }

        private fun checkParameter(vararg parameters: Double) {
            if (parameters.isEmpty()) throw IllegalArgumentException(
                    "Expected the parameter {parameters.size > 0},but get {parameters.size = ${parameters.size}}")
        }
    }
}

data class Polynomial(override val coefficients: List<Double>) : IPolynomial {
    constructor(vararg coefficient: Double) : this(coefficient.toList())

    init {
        if (coefficients.isEmpty()) throw IllegalArgumentException(
                "Expected the parameter {coefficients.size > 0},but get {coefficients.size = ${coefficients.size}}")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Polynomial) return false

        if (coefficients != other.coefficients) return false

        return true
    }

    override fun hashCode(): Int {
        return coefficients.hashCode()
    }

    override fun toString(): String {
        return "Polynomial(coefs=$coefficients)"
    }

}