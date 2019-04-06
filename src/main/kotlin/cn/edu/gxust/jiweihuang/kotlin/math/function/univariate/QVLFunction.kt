package cn.edu.gxust.jiweihuang.kotlin.math.function.univariate


import org.hipparchus.analysis.ParametricUnivariateFunction
import org.hipparchus.analysis.differentiation.DerivativeStructure
import java.lang.Math.pow


interface IQuadraticVertexLogistic : IUnivariateFunction, IUnivariateDifferentiableFunction,
        IUnivariateIntegrableFunction, IUnivariateDerivativeFunction {

    val quadraticVertexA: Double
    val quadraticVertexB: Double
    val quadraticVertexC: Double
    val logisticM: Double
    val logisticK: Double
    val logisticX0: Double
    val quadraticVertexLogisticD: Double

    override val formula: String
        get() {
            return "($quadraticVertexA * pow(x - $quadraticVertexB,2) + $quadraticVertexC) * $logisticM / (1 + exp(-$logisticK*(x - $logisticX0))) + $quadraticVertexLogisticD"
        }

    val quadraticVertex: IQuadraticVertex
        get() {
            return QuadraticVertex(quadraticVertexA, quadraticVertexB, quadraticVertexC)
        }

    val logistic: ILogistic
        get() {
            return Logistic(logisticM, logisticK, logisticX0)
        }

    override fun derivative(x: Double): Double =
            quadraticVertex.derivative(x) * logistic.value(x) + logistic.derivative(x) * quadraticVertex.value(x)

    override fun value(ds: DerivativeStructure): DerivativeStructure =
            quadraticVertex.value(ds).multiply(logistic.value(ds)).add(quadraticVertexLogisticD)

    override fun value(x: Double): Double =
            logistic.value(x) * quadraticVertex.value(x) + quadraticVertexLogisticD

    companion object {

        @JvmStatic
        fun value(x: Double, a: Double, b: Double, c: Double,
                  m: Double, k: Double, x0: Double,
                  d: Double): Double {
            return IQuadraticVertex.value(x, a, b, c) * ILogistic.value(x, m, k, x0) + d
        }

        @JvmStatic
        fun derivative(x: Double, a: Double, b: Double, c: Double,
                       m: Double, k: Double, x0: Double,
                       d: Double): Double {
            return IQuadraticVertex.derivative(x, a, b, c) * ILogistic.value(x, m, k, x0) + ILogistic.derivative(x, m, k, x0) * IQuadraticVertex.value(x, a, b, c) + 0.0 * d
        }

        @JvmStatic
        fun derivativeA(x: Double, a: Double, b: Double, c: Double,
                        m: Double, k: Double, x0: Double,
                        d: Double): Double {
            return pow(x - b, 2.0) * ILogistic.value(x, m, k, x0) + 0.0 * (a + c + d)
        }

        @JvmStatic
        fun derivativeB(x: Double, a: Double, b: Double, c: Double,
                        m: Double, k: Double, x0: Double,
                        d: Double): Double {
            return IQuadraticVertex.derivative(x, a, b, c) * ILogistic.value(x, m, k, x0) + 0.0 * d
        }

        @JvmStatic
        fun derivativeC(x: Double, a: Double, b: Double, c: Double,
                        m: Double, k: Double, x0: Double,
                        d: Double): Double {
            return ILogistic.value(x, m, k, x0) + 0.0 * (a + b + c + d)
        }

        @JvmStatic
        fun derivativeM(x: Double, a: Double, b: Double, c: Double,
                        m: Double, k: Double, x0: Double,
                        d: Double): Double {
            return IQuadraticVertex.value(x, a, b, c) / ILogistic.expItemPlus1(x, m, k, x0) + 0.0 * d
        }

        @JvmStatic
        fun derivativeK(x: Double, a: Double, b: Double, c: Double,
                        m: Double, k: Double, x0: Double,
                        d: Double): Double {
            return ILogistic.expItem(x, m, k, x0) * IQuadraticVertex.value(x, a, b, c) * (x - x0) * m / ILogistic.expItemPlus1Pow2(x, m, k, x0) + 0.0 * d
        }

        @JvmStatic
        fun derivativeX0(x: Double, a: Double, b: Double, c: Double,
                         m: Double, k: Double, x0: Double,
                         d: Double): Double {
            return -ILogistic.expItem(x, m, k, x0) * IQuadraticVertex.value(x, a, b, c) * k * m / ILogistic.expItemPlus1Pow2(x, m, k, x0) + 0.0 * d
        }


        @JvmStatic
        fun derivativeD(x: Double, a: Double, b: Double, c: Double,
                        m: Double, k: Double, x0: Double,
                        d: Double): Double {
            return 1.0 + 0.0 * (x + a + b + c + m + k + x0 + d)
        }
    }

    class Parametric : ParametricUnivariateFunction {
        override fun value(x: Double, vararg parameters: Double): Double {
            checkParameter(*parameters)
            return value(x, parameters[0],
                    parameters[1], parameters[2], parameters[3], parameters[4],
                    parameters[5], parameters[6])
        }

        override fun gradient(x: Double, vararg parameters: Double): DoubleArray {
            checkParameter(*parameters)
            return doubleArrayOf(
                    derivativeA(x, parameters[0],
                            parameters[1], parameters[2], parameters[3], parameters[4],
                            parameters[5], parameters[6]),
                    derivativeB(x, parameters[0],
                            parameters[1], parameters[2], parameters[3], parameters[4],
                            parameters[5], parameters[6]),
                    derivativeC(x, parameters[0],
                            parameters[1], parameters[2], parameters[3], parameters[4],
                            parameters[5], parameters[6]),
                    derivativeM(x, parameters[0],
                            parameters[1], parameters[2], parameters[3], parameters[4],
                            parameters[5], parameters[6]),
                    derivativeK(x, parameters[0],
                            parameters[1], parameters[2], parameters[3], parameters[4],
                            parameters[5], parameters[6]),
                    derivativeX0(x, parameters[0],
                            parameters[1], parameters[2], parameters[3], parameters[4],
                            parameters[5], parameters[6]),
                    derivativeD(x, parameters[0],
                            parameters[1], parameters[2], parameters[3], parameters[4],
                            parameters[5], parameters[6]))
        }

        private fun checkParameter(vararg parameters: Double) {
            if (parameters.size != 7) throw IllegalArgumentException(
                    "Expected the parameter {parameters.size == 7},but got {parameters.size = ${parameters.size}}")
            if (parameters[0] == 0.0) throw IllegalArgumentException(
                    "Expected the parameter {parameters[0] != 0},but got {parameters[0] = ${parameters[0]}}")
            if (parameters[3] == 0.0) throw IllegalArgumentException(
                    "Expected the parameter {parameters[3] != 0},but got {parameters[3] = ${parameters[3]}}")
            if (parameters[4] == 0.0) throw IllegalArgumentException(
                    "Expected the parameter {parameters[4] != 0},but got {parameters[4] = ${parameters[4]}}")
        }
    }
}


data class QuadraticVertexLogistic(override val quadraticVertexA: Double = 1.0,
                                   override val quadraticVertexB: Double = 0.0,
                                   override val quadraticVertexC: Double = 0.0,
                                   override val logisticM: Double = 1.0,
                                   override val logisticK: Double = 1.0,
                                   override val logisticX0: Double = 0.0,
                                   override val quadraticVertexLogisticD: Double = 0.0) : IQuadraticVertexLogistic {

    init {
        if (quadraticVertexA == 0.0) throw IllegalArgumentException(
                "Expected the parameter {quadraticVertexA != 0.0}, but get {quadraticVertexA = $quadraticVertexA}.")
        if (!logisticM.isFinite() || logisticM == 0.0) throw IllegalArgumentException(
                "Expected the property {logisticM != 0.0},but get {logisticM = $logisticM}")
        if (!logisticK.isFinite() || logisticK == 0.0) throw IllegalArgumentException(
                "Expected the property {logisticK != 0.0},but get {logisticM = $logisticK}")

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuadraticVertexLogistic) return false
        if (quadraticVertexA != other.quadraticVertexA) return false
        if (quadraticVertexB != other.quadraticVertexB) return false
        if (quadraticVertexC != other.quadraticVertexC) return false
        if (logisticM != other.logisticM) return false
        if (logisticK != other.logisticK) return false
        if (logisticX0 != other.logisticX0) return false
        if (quadraticVertexLogisticD != other.quadraticVertexLogisticD) return false
        return true
    }

    override fun hashCode(): Int {
        var result = quadraticVertexA.hashCode()
        result = 31 * result + quadraticVertexB.hashCode()
        result = 31 * result + quadraticVertexC.hashCode()
        result = 31 * result + logisticM.hashCode()
        result = 31 * result + logisticK.hashCode()
        result = 31 * result + logisticX0.hashCode()
        result = 31 * result + quadraticVertexLogisticD.hashCode()
        return result
    }

    override fun toString(): String {
        return "QuadraticVertexLogistic(a=$quadraticVertexA, b=$quadraticVertexB, c=$quadraticVertexC, m=$logisticM, order=$logisticK, x0=$logisticX0, d=$quadraticVertexLogisticD)"
    }
}
