package cn.edu.gxust.jiweihuang.kotlin.math.function.univariate

import org.hipparchus.analysis.ParametricUnivariateFunction
import org.hipparchus.analysis.differentiation.DerivativeStructure
import java.lang.Math.pow
import java.lang.Math.sqrt

/**
 * The Interface [IQuadratic] is used for representing quadratic function.
 *
 * Reference: [quadratic function](https://en.wikipedia.org/wiki/Quadratic_function).
 *
 * @author Jiwei Huang
 * @since 1.0.0
 * @see IUnivariateFunction
 * @see IUnivariateInverseFunction
 * @see IUnivariateDerivativeFunction
 * @see IUnivariateDifferentiableFunction
 * @see IUnivariateIntegrableFunction
 * @see IUnivariateIntegralFunction
 */
interface IQuadratic : IUnivariateFunction, IUnivariateInverseFunction,
        IUnivariateDerivativeFunction, IUnivariateDifferentiableFunction,
        IUnivariateIntegralFunction, IUnivariateIntegrableFunction {

    /**
     * The vertex coordinates (x,y) of quadratic function
     */
    val vertex: DoubleArray

    /**
     * Whether quadratic function is invert.
     *
     * If invert (i.e. open side down) return true else return false.
     */
    val isInvert: Boolean

    /**
     * the x coordinate of intersection with x axis of function
     */
    val xIntersection: DoubleArray
        get() = inverse(0.0)

    /**
     * the y coordinate of intersection with y axis of function
     */
    val yIntersection: Double
        get() = value(0.0)

    /**
     * The number of intersection with x axis of function
     */
    val xIntersectionNum: Int
        get() = xIntersection.size
}

/**
 * The vertex form of quadratic function.
 *
 * The formula:`q(x) = a * pow(x-b, 2.0) + c`.
 *
 * @property quadraticVertexA the parameter {a} of the vertex form of quadratic function.
 * @property quadraticVertexB the parameter {b} of the vertex form of quadratic function.
 * @property quadraticVertexC the parameter {c} of the vertex form of quadratic function.
 *
 * @author JiweiHuang
 * @since 1.0.0
 * @see IQuadratic
 */
interface IQuadraticVertex : IQuadratic {

    val quadraticVertexA: Double
    val quadraticVertexB: Double
    val quadraticVertexC: Double

    /**
     * {@inheritDoc}
     */
    override val vertex: DoubleArray
        get() = doubleArrayOf(quadraticVertexB, quadraticVertexC)

    /**
     * {@inheritDoc}
     */
    override val isInvert: Boolean
        get() = quadraticVertexA < 0

    /**
     * {@inheritDoc}
     */
    override val formula: String
        get() {
            return "$quadraticVertexA * pow(x - $quadraticVertexB, 2.0) + $quadraticVertexC"
        }

    /**
     * {@inheritDoc}
     */
    override fun value(x: Double): Double {
        return value(x, quadraticVertexA, quadraticVertexB, quadraticVertexC)
    }

    /**
     * {@inheritDoc}
     */
    override fun value(x: DerivativeStructure): DerivativeStructure {
        return x.subtract(quadraticVertexB).pow(2).multiply(quadraticVertexA).add(quadraticVertexC)
    }

    /**
     * {@inheritDoc}
     */
    override fun inverse(y: Double): DoubleArray {
        return inverse(y, quadraticVertexA, quadraticVertexB, quadraticVertexC)
    }

    /**
     * {@inheritDoc}
     */
    override fun integral(x: Double): Double {
        return integral(x, quadraticVertexA, quadraticVertexB, quadraticVertexC)
    }

    /**
     * {@inheritDoc}
     */
    override fun derivative(x: Double): Double {
        return derivative(x, quadraticVertexA, quadraticVertexB, quadraticVertexC)
    }

    companion object {
        /**
         * q(x) = a * pow(x-b,2) + c
         */
        @JvmStatic
        fun value(x: Double, a: Double = 1.0, b: Double = 0.0, c: Double = 0.0): Double {
            return a * pow(x - b, 2.0) + c
        }

        /**
         * qi1(x) = b - sqrt(a*(y-c))/a
         * qi2(x) = b + sqrt(a*(y-c))/a
         */
        @JvmStatic
        fun inverse(y: Double, a: Double = 1.0, b: Double = 0.0, c: Double = 0.0): DoubleArray {
            val tem = a * (y - c)
            return when {
                tem < 0.0 -> doubleArrayOf()
                tem == 0.0 -> doubleArrayOf(b)
                else -> doubleArrayOf(b - sqrt(tem) / a, b + sqrt(tem) / a)
            }
        }

        /**
         * dq(x) = 2 * a * (x-b)
         */
        @JvmStatic
        fun derivative(x: Double, a: Double = 1.0, b: Double = 0.0, c: Double = 0.0): Double {
            return 2.0 * a * (x - b) + 0.0 * c
        }

        /**
         * iq(x) = a * pow(b,2) * x + c * x - a * b * pow(x,2)  +  (a * pow(x,3)) / 3
         */
        @JvmStatic
        fun integral(x: Double, a: Double = 1.0, b: Double = 0.0, c: Double = 0.0): Double {
            return a * pow(b, 2.0) * x + c * x - a * b * pow(x, 2.0) + (a * pow(x, 3.0)) / 3.0
        }

        /**
         * dqa(x) = pow(x-b,2)
         */
        @JvmStatic
        fun derivativeA(x: Double, a: Double = 1.0, b: Double = 0.0, c: Double = 0.0): Double {
            return pow(x - b, 2.0) + 0.0 * (a + c)
        }

        /**
         * dqb(x) = -2 * a * (x-b) = 2 * a * (b-x)
         */
        @JvmStatic
        fun derivativeB(x: Double, a: Double = 1.0, b: Double = 0.0, c: Double = 0.0): Double {
            return 2.0 * a * (b - x) + 0.0 * c
        }

        /**
         * dqc(x) = 1
         */
        @JvmStatic
        fun derivativeC(x: Double, a: Double = 1.0,
                        b: Double = 0.0,
                        c: Double = 0.0): Double {
            return 1.0 + 0.0 * (x + a + b + c)
        }
    }

    /**
     * Parameterized vertex form of quadratic function for
     * regression algorithm.
     */
    class Parametric : ParametricUnivariateFunction {
        override fun value(x: Double, vararg parameters: Double): Double {
            checkParameter(*parameters)
            return value(x, parameters[0], parameters[1], parameters[2])
        }

        override fun gradient(x: Double, vararg parameters: Double): DoubleArray {
            checkParameter(*parameters)
            return doubleArrayOf(
                    derivativeA(x, parameters[0], parameters[1], parameters[2]),
                    derivativeB(x, parameters[0], parameters[1], parameters[2]),
                    derivativeC(x, parameters[0], parameters[1], parameters[2]))
        }

        private fun checkParameter(vararg parameters: Double) {
            if (parameters.size != 3) throw IllegalArgumentException(
                    "Expected the parameter {parameters.size == 3}, but get {parameters.size = ${parameters.size}}.")
            if (!parameters[0].isFinite() || parameters[0] == 0.0) throw IllegalArgumentException(
                    "Expected the parameter {parameters[0] != 0.0}, but get {parameters[0] = ${parameters[0]}}.")
            if (!parameters[0].isFinite() || !parameters[2].isFinite()) throw IllegalArgumentException(
                    "Expected the parameter {parameters[1] and parameters[1]} is finite, but get {parameters[1] = ${parameters[1]},parameters[2] = ${parameters[2]}}.")
        }
    }
}

/**
 * required that {@code quadraticVertexA != 0.0}
 */
data class QuadraticVertex(override val quadraticVertexA: Double = 1.0,
                           override val quadraticVertexB: Double = 0.0,
                           override val quadraticVertexC: Double = 0.0) : IQuadraticVertex {
    init {
        /**
         * Ensure the parameters {quadraticVertexA != 0.0}
         */
        if (!quadraticVertexA.isFinite() || quadraticVertexA == 0.0) throw IllegalArgumentException(
                "Expected the parameter {quadraticVertexA != 0.0}, but get {quadraticVertexA = $quadraticVertexA}.")
        if (!quadraticVertexB.isFinite() || !quadraticVertexC.isFinite()) throw IllegalArgumentException(
                "Expected the parameter {quadraticVertexB and quadraticVertexC} is finite, but get {quadraticVertexB = $quadraticVertexB,quadraticVertexC = $quadraticVertexC}.")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuadraticVertex) return false
        if (quadraticVertexA != other.quadraticVertexA) return false
        if (quadraticVertexB != other.quadraticVertexB) return false
        if (quadraticVertexC != other.quadraticVertexC) return false
        return true
    }

    override fun hashCode(): Int {
        var result = quadraticVertexA.hashCode()
        result = 31 * result + quadraticVertexB.hashCode()
        result = 31 * result + quadraticVertexC.hashCode()
        return result
    }

    override fun toString(): String {
        return "QuadraticVertex(a=$quadraticVertexA, b=$quadraticVertexB, c=$quadraticVertexC)"
    }

}



