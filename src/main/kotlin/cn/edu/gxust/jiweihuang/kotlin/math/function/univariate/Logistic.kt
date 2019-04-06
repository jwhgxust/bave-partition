package cn.edu.gxust.jiweihuang.kotlin.math.function.univariate


import org.hipparchus.analysis.ParametricUnivariateFunction
import org.hipparchus.analysis.differentiation.DerivativeStructure
import java.lang.Math.*

/**
 * Reference:[Logistic function](https://en.wikipedia.org/wiki/Logistic_function).
 *
 * The formula: `l(x) = m / (1+exp(-order*(x-x0)))`.
 *
 * @property logisticM The parameter {m} of ILogistic function.
 * @property logisticK The parameter {k} of ILogistic function.
 * @property logisticX0 The parameter {x0} of ILogistic function.
 *
 * @author Jiwei Huang
 * @since 1.0.0
 * @see IUnivariateFunction
 * @see IUnivariateDifferentiableFunction
 * @see IUnivariateDerivativeFunction
 * @see IUnivariateIntegrableFunction
 * @see IUnivariateIntegralFunction
 */
interface ILogistic : IUnivariateFunction, IUnivariateDifferentiableFunction,
        IUnivariateDerivativeFunction, IUnivariateIntegrableFunction,
        IUnivariateIntegralFunction {

    val logisticM: Double
    val logisticK: Double
    val logisticX0: Double

    /**
     * The string form of analysis formula of univariate function.
     */
    override val formula: String
        get() {
            return "$logisticM / (1 + exp(-$logisticK * (x - $logisticX0)))"
        }

    override fun derivative(x: Double): Double = derivative(x, logisticM, logisticK, logisticX0)

    override fun integral(x: Double): Double = integral(x, logisticM, logisticK, logisticX0)

    override fun value(t: DerivativeStructure): DerivativeStructure {
        return t.subtract(logisticX0).multiply(-logisticK).exp().add(1.0).pow(-1).multiply(logisticM)
    }

    override fun value(x: Double): Double {
        return value(x, logisticM, logisticK, logisticX0)
    }

    companion object {
        /**
         * lexp(x) = exp(-order*(x-x0))
         */
        @JvmStatic
        fun expItem(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return exp(-k * (x - x0)) + 0.0 * m
        }


        /**
         * lexpp1(x) = 1 + exp(-order*(x-x0))
         */
        @JvmStatic
        fun expItemPlus1(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return 1 + expItem(x, m, k, x0)
        }


        /**
         * lexpp1p2(x) = pow(1 + exp(-order*(x-x0)),2)
         */
        @JvmStatic
        fun expItemPlus1Pow2(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return pow(expItemPlus1(x, m, k, x0), 2.0)
        }


        /**
         * lexpp1pn1(x) = pow(1 + exp(-order*(x-x0)),-1) = 1/(1 + exp(-order*(x-x0)))
         */
        @JvmStatic
        fun expItemPlus1PowN1(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return 1.0 / expItemPlus1(x, m, k, x0)
        }

        /**
         * l(x) = m / (1 + exp(-order*(x-x0)))
         */
        @JvmStatic
        fun value(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return m / expItemPlus1(x, m, k, x0)
        }


        /**
         * il(x) = m * (x+log(1 + exp(-order*(x-x0)))/order)
         */
        @JvmStatic
        fun integral(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return m * (x + log(expItemPlus1(x, m, k, x0)) / k)
        }

        /**
         * dl(x)
         */
        @JvmStatic
        fun derivative(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return (m * k * expItem(x, m, k, x0)) / expItemPlus1Pow2(x, m, k, x0)
        }


        /**
         * dlm(x)
         */
        @JvmStatic
        fun derivativeM(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return expItemPlus1PowN1(x, m, k, x0)
        }

        /**
         * dlk(x)
         */
        @JvmStatic
        fun derivativeK(x: Double, m: Double = 1.0,
                        k: Double = 1.0,
                        x0: Double = 0.0): Double {
            return -(m * (-x + x0) * expItem(x, m, k, k)) / expItemPlus1Pow2(x, m, k, x0)
        }

        /**
         * dlx0(x)
         */
        @JvmStatic
        fun derivativeX0(x: Double, m: Double = 1.0, k: Double = 1.0, x0: Double = 0.0): Double {
            return -(k * m * expItem(x, m, k, x0)) / expItemPlus1Pow2(x, m, k, x0)
        }

    }

    class Parametric : ParametricUnivariateFunction {
        override fun value(x: Double, vararg parameters: Double): Double {
            checkParameter(*parameters)
            return value(x, parameters[0], parameters[1], parameters[2])
        }

        override fun gradient(x: Double, vararg parameters: Double): DoubleArray {
            checkParameter(*parameters)
            return doubleArrayOf(derivativeM(x, parameters[0], parameters[1], parameters[2]),
                    derivativeK(x, parameters[0], parameters[1], parameters[2]),
                    derivativeX0(x, parameters[0], parameters[1], parameters[2]))
        }

        private fun checkParameter(vararg parameters: Double) {
            if (parameters.size != 3) throw IllegalArgumentException(
                    "Expected the parameter {parameters.size == 3},but get {parameters.size = ${parameters.size}}")
            if (!parameters[0].isFinite() || parameters[0] == 0.0) throw IllegalArgumentException(
                    "Expected the parameter {parameters[0] != 0.0} and finite,but get {parameters[0] = ${parameters[0]}}")
            if (!parameters[1].isFinite() || parameters[1] == 0.0) throw IllegalArgumentException(
                    "Expected the parameter {parameters[1] != 0.0} and finite,but get {parameters[1] = ${parameters[1]}}")
            if (!parameters[2].isFinite()) throw IllegalArgumentException(
                    "Expected the parameter {parameters[2]} and finite,but get {parameters[2] = ${parameters[2]}}")
        }
    }
}

/**
 * required that {@code logisticM != 0}
 * required that {@code logisticK != 0}
 */
data class Logistic(override val logisticM: Double = 1.0,
                    override val logisticK: Double = 1.0,
                    override val logisticX0: Double = 0.0) : ILogistic {
    init {
        if (!logisticM.isFinite() || logisticM == 0.0) throw IllegalArgumentException(
                "Expected the property {logisticM != 0.0},but get {logisticM = $logisticM}")
        if (!logisticK.isFinite() || logisticK == 0.0) throw IllegalArgumentException(
                "Expected the property {logisticK != 0.0},but get {logisticM = $logisticK}")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Logistic) return false
        if (logisticM != other.logisticM) return false
        if (logisticK != other.logisticK) return false
        if (logisticX0 != other.logisticX0) return false
        return true
    }

    override fun hashCode(): Int {
        var result = logisticM.hashCode()
        result = 31 * result + logisticK.hashCode()
        result = 31 * result + logisticX0.hashCode()
        return result
    }

    override fun toString(): String {
        return "Logistic(m=$logisticM, order=$logisticK, x0=$logisticX0)"
    }

}
